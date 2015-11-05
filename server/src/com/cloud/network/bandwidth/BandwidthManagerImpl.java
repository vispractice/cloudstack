package com.cloud.network.bandwidth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.inject.Inject;
import javax.naming.ConfigurationException;

import org.apache.cloudstack.api.command.user.bandwidth.AssignToBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.CreateBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.DeleteBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.ListBandwidthRulesCmd;
import org.apache.cloudstack.api.command.user.bandwidth.RemoveFromBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.UpdateBandwidthRuleCmd;
import org.apache.cloudstack.api.response.BandwidthRulesResponse;
import org.apache.cloudstack.context.CallContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.Network;
import com.cloud.network.NetworkModel;
import com.cloud.network.dao.BandwidthDao;
import com.cloud.network.dao.BandwidthOfferingDao;
import com.cloud.network.dao.BandwidthOfferingVO;
import com.cloud.network.dao.BandwidthRulesDao;
import com.cloud.network.dao.BandwidthRulesVO;
import com.cloud.network.dao.BandwidthVO;
import com.cloud.network.dao.MultilineDao;
import com.cloud.network.dao.MultilineVO;
import com.cloud.network.dao.NetworkDao;
import com.cloud.network.element.BandwidthServiceProvider;
import com.cloud.network.rules.BandwidthManager;
import com.cloud.network.rules.BandwidthRule;
import com.cloud.network.rules.BandwidthRule.BandwidthType;
import com.cloud.offering.BandwidthOffering;
import com.cloud.utils.component.ManagerBase;

@Component
@Local(value = { BandwidthService.class, BandwidthManager.class})
public class BandwidthManagerImpl extends ManagerBase implements BandwidthService, BandwidthManager {
	private static final Logger s_logger = Logger.getLogger(BandwidthManagerImpl.class);
	
	@Inject
	MultilineDao _multilineDao;
	@Inject
	BandwidthRulesDao _bandwidthRulesDao;
	@Inject
	BandwidthDao _bandwidthDao;
	@Inject
	BandwidthOfferingDao _bandwidthOfferingDao;
	@Inject
	NetworkDao _networksDao;
	@Inject
	NetworkModel _networkModel;
	@Inject
	BandwidthServiceProvider _bandwidthServiceProvider;
	
	@Override
    public boolean configure(String name, Map<String, Object> params) throws ConfigurationException {
		return true;
	}
	
	@Override
    public boolean start() {
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }

	@Override
	public Long createBandwidthRule(CreateBandwidthRuleCmd cmd){
		Long networkId = cmd.getNetworkId();
		Long bandwidthOfferingId = cmd.getBandwidthOfferingId();
		Integer rate = cmd.getRate();
		Integer ceil = cmd.getCeil();
		Integer prio = cmd.getPrio();
		String type = cmd.getType();
		String multilineLable = cmd.getMultilineLabel();
		Integer trafficRuleId = null;
		
		MultilineVO multilineVO = _multilineDao.getMultilineByLabel(multilineLable);
		if(multilineVO == null){
			throw new InvalidParameterValueException("The bandwidth rule parameter: multiline lable is not right.");
		}
		
		BandwidthVO bandwidthVO = _bandwidthDao.getBandwidthByMultilineId(multilineVO.getId());
		int bandwidthCapacity = 0;
		BandwidthType bandwidthType = null;
		//检查rate参数是否合格，要查询数据库，所有的rate相加，不能大于总带宽数。
		if(BandwidthType.InTraffic.toString().equalsIgnoreCase(type)){
			bandwidthType = BandwidthType.InTraffic;
			bandwidthCapacity = bandwidthVO.getInTraffic();
		} else if(BandwidthType.OutTraffic.toString().equalsIgnoreCase(type)){
			bandwidthType = BandwidthType.OutTraffic;
			bandwidthCapacity = bandwidthVO.getOutTraffic();
		} else {
			throw new InvalidParameterValueException("The bandwidth rule parameter: type is not right, Only support InTraffic and OutTraffic.");
		}
		List<BandwidthRulesVO> BandwidthRulesList = _bandwidthRulesDao.listByBandwidthIdAndType(bandwidthVO.getId(), bandwidthType);
		int sumOfRuleUsed = 0;
		for(BandwidthRulesVO vo : BandwidthRulesList){
			sumOfRuleUsed += vo.getRate();
		}
		//判断是使用方案，还是直接输入。
		int nowSumOfRuleUsed = 0;
		if(bandwidthOfferingId != null){
			BandwidthOfferingVO bandwidthOfferingVO = _bandwidthOfferingDao.findById(bandwidthOfferingId);
			rate = bandwidthOfferingVO.getRate();
			ceil = bandwidthOfferingVO.getCeil();
		}
		nowSumOfRuleUsed = sumOfRuleUsed + rate;
		if(bandwidthCapacity < nowSumOfRuleUsed){
			throw new InvalidParameterValueException("The bandwidth rule parameter: rate is not right, The bandwidth Capacity is not enough.");
		}
		//traffic rule id要选一个合适的值。0~9999在一个network里面。
		List<BandwidthRulesVO> BandwidthRulesByNetwork = _bandwidthRulesDao.listByNetworksId(networkId);
		List<Integer> trafficRuleIdList = new ArrayList<Integer>();
		for(BandwidthRulesVO vo: BandwidthRulesByNetwork){
			trafficRuleIdList.add(vo.getTrafficRuleId());
		}
		//TODO the number 9999 must be write in the global setting.
		for(Integer i = 2; i < 9999; i++){
			if(!trafficRuleIdList.contains(i)){
				trafficRuleId = i;
			}
		}
		//参数合格之后，保存到DB中
		BandwidthRulesVO newBandwidthRulesVO = new BandwidthRulesVO(bandwidthVO.getId(), networkId, trafficRuleId, bandwidthType, prio, rate, ceil );
		if(bandwidthOfferingId != null){
			newBandwidthRulesVO.setBandwidthOfferingId(bandwidthOfferingId);
		}
		Network network = _networkModel.getNetwork(networkId);
        Long accountId = network.getAccountId();
        Long domainId = network.getDomainId();
		newBandwidthRulesVO.setAccountId(accountId);
		newBandwidthRulesVO.setDomainId(domainId);
		CallContext.current().setEventDetails("bandwidth rule id=" + newBandwidthRulesVO.getId());
		BandwidthRulesVO bandwidthRules = _bandwidthRulesDao.persist(newBandwidthRulesVO);
        if (bandwidthRules != null) {
            CallContext.current().setEventDetails("Bandwidth rule id=" + newBandwidthRulesVO.getId());
            return bandwidthRules.getId();
        } else {
            return null;
        }
	}

	@Override
	public boolean applyBandwidthRule(Long ruleId) throws ResourceUnavailableException{
		// TODO  根据networkId，找到VR所在的hostId，然后把参数发送到VR中执行相关的命令。
		// find the network id and get the network
		List<BandwidthRule> rules = new ArrayList<BandwidthRule>();
		BandwidthRulesVO rule = _bandwidthRulesDao.findById(ruleId);
		rule.setRevoked(false);
		//TODO need to find device id by the IP when the type is out traffic
		Network network = _networksDao.findById(rule.getNetworksId());
		rules.add(rule);
		return applyBandwidthRules(network, rules);
	}
	
	@Override
	public boolean applyBandwidthRules(Network network, List<BandwidthRule> rules) throws ResourceUnavailableException {
		return _bandwidthServiceProvider.applyBandwidthRules(network, rules);
	}
	@Override
	public boolean revokeRelatedBandwidthRule(Long ruleId) {
		// delete the rule from the DB.
		CallContext.current().setEventDetails("bandwidth rule id=" + ruleId);
		_bandwidthRulesDao.remove(ruleId);
		return true;
	}

	@Override
	public boolean deleteBandwidthRule(DeleteBandwidthRuleCmd cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBandwidthRule(UpdateBandwidthRuleCmd cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BandwidthRulesResponse listBandwidthRules(ListBandwidthRulesCmd cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean assignToBandwidthRule(AssignToBandwidthRuleCmd cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeFromBandwidthRule(RemoveFromBandwidthRuleCmd cmd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkBandwidthCapacity(Integer newRate) {
		// 查询数据库表
		return false;
	}

	@Override
	public boolean updateOfferingRefreshRules(BandwidthOffering newOffering,
			BandwidthOffering oldOffering) {
		// TODO Auto-generated method stub
		return false;
	}

}
