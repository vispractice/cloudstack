package com.cloud.network.bandwidth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.inject.Inject;
import javax.naming.ConfigurationException;

import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.command.user.bandwidth.AssignToBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.CreateBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.DeleteBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.ListBandwidthRulesCmd;
import org.apache.cloudstack.api.command.user.bandwidth.RemoveFromBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.UpdateBandwidthRuleCmd;
import org.apache.cloudstack.api.response.BandwidthFilterRuleResponse;
import org.apache.cloudstack.api.response.BandwidthRulesResponse;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.cloudstack.context.CallContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.Network;
import com.cloud.network.NetworkModel;
import com.cloud.network.dao.BandwidthDao;
import com.cloud.network.dao.BandwidthIPPortMapDao;
import com.cloud.network.dao.BandwidthIPPortMapVO;
import com.cloud.network.dao.BandwidthOfferingDao;
import com.cloud.network.dao.BandwidthOfferingVO;
import com.cloud.network.dao.BandwidthRulesDao;
import com.cloud.network.dao.BandwidthRulesVO;
import com.cloud.network.dao.BandwidthVO;
import com.cloud.network.dao.IPAddressDao;
import com.cloud.network.dao.IPAddressVO;
import com.cloud.network.dao.MultilineDao;
import com.cloud.network.dao.MultilineVO;
import com.cloud.network.dao.NetworkDao;
import com.cloud.network.element.BandwidthServiceProvider;
import com.cloud.network.rules.BandwidthClassRule.BandwidthType;
import com.cloud.network.rules.BandwidthRule.BandwidthFilterRules;
import com.cloud.network.rules.BandwidthManager;
import com.cloud.network.rules.BandwidthRule;
import com.cloud.offering.BandwidthOffering;
import com.cloud.utils.component.ManagerBase;
import com.cloud.utils.net.NetUtils;
import com.cloud.vm.VMInstanceVO;
import com.cloud.vm.dao.VMInstanceDao;

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
	@Inject
	BandwidthIPPortMapDao _bandwidthIPPortMapDao;
	@Inject
    IPAddressDao _ipAddressDao;
	@Inject
	VMInstanceDao _vMInstanceDao;
	
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
		// 根据networkId，找到VR所在的hostId，然后把参数发送到VR中执行相关的命令。
		// find the network id and get the network
		List<BandwidthRule> rules = new ArrayList<BandwidthRule>();
		BandwidthRulesVO rule = _bandwidthRulesDao.findById(ruleId);
		rule.setRevoked(false);
		rule.setKeepState(false);
		rule.setAlreadyAdded(false);

		Network network = _networksDao.findById(rule.getNetworksId());
		BandwidthRule bandwidthRule = new BandwidthRule(rule);
		
		rules.add(bandwidthRule);
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
		return _bandwidthRulesDao.remove(ruleId);
	}

	@Override
	public boolean deleteBandwidthRule(DeleteBandwidthRuleCmd cmd) throws ResourceUnavailableException{
		List<BandwidthRule> rules = new ArrayList<BandwidthRule>();
		BandwidthRulesVO rule = _bandwidthRulesDao.findById(cmd.getId());
		rule.setRevoked(true);
		rule.setKeepState(false);
		rule.setAlreadyAdded(true);
		Network network = _networksDao.findById(rule.getNetworksId());
		//update the filter rules
//		List<BandwidthFilterRules> bandwidthFilterRules = new ArrayList<BandwidthFilterRules>();
//		List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(rule.getId());
//		for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
//			String ip = bandwidthIPPortMap.getIpAddress();
//			int startPort = bandwidthIPPortMap.getBandwidthPortStart();
//			int endPort = bandwidthIPPortMap.getBandwidthPortEnd();
//			boolean revoke = true;
//			boolean alreadyAdded = true;
//			BandwidthFilterRules bandwidthFilterRule = new BandwidthFilterRules(ip, startPort, endPort, revoke, alreadyAdded);
//			bandwidthFilterRules.add(bandwidthFilterRule);
//		}
//		
//		BandwidthRule bandwidthRule = new BandwidthRule(rule, bandwidthFilterRules);
		BandwidthRule bandwidthRule = new BandwidthRule(rule);
		rules.add(bandwidthRule);
		return applyBandwidthRules(network, rules);
	}

	@Override
	public boolean updateBandwidthRule(UpdateBandwidthRuleCmd cmd)  throws ResourceUnavailableException{
		//check the parameters are changed or not, get the old from the DB.
		BandwidthRulesVO rule = _bandwidthRulesDao.findById(cmd.getBandwidthRuleId());
		int oldRate = rule.getRate();
		int oldCeil = rule.getCeil();
		int oldPrio = rule.getPrio();
		if(oldRate == cmd.getRate() && oldCeil == cmd.getCeil() && oldPrio == cmd.getPrio()){
			throw new InvalidParameterValueException("The update bandwidth rule parameters is not different between the old, not execute the update operation.");
		}
		//check the parameter rate is right or not.
		if(oldRate < cmd.getRate()){
			BandwidthVO bandwidthVO = _bandwidthDao.findById(rule.getBandwidthId());
			if(!checkBandwidthCapacity(bandwidthVO, rule.getType(), cmd.getRate(), oldRate)){
				throw new InvalidParameterValueException("The bandwidth rule parameter: rate is not right, The bandwidth Capacity is not enough.");
			}
		}
		
		//first delete the old
		List<BandwidthRule> oldRules = new ArrayList<BandwidthRule>();
		rule.setRevoked(true);
		rule.setKeepState(false);
		rule.setAlreadyAdded(true);
		Network network = _networksDao.findById(rule.getNetworksId());
		BandwidthRule oldBandwidthRule = new BandwidthRule(rule);
		oldRules.add(oldBandwidthRule);
		if(!applyBandwidthRules(network, oldRules)){
			s_logger.error("Update the bandwidth rules, when delete the old rules, it get wrong.");
			throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to update bandwidth rule");
		}
		//then create the new and store to DB.
		List<BandwidthRule> newRules = new ArrayList<BandwidthRule>();
		//reload the bandwidth class rule.
		rule.setRevoked(false);
		rule.setKeepState(false);
		rule.setAlreadyAdded(false);
		rule.setRate(cmd.getRate());
		rule.setCeil(cmd.getCeil());
		rule.setPrio(cmd.getPrio());
		
		//reload the filter rules
		List<BandwidthFilterRules> bandwidthFilterRules = new ArrayList<BandwidthFilterRules>();
		List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(rule.getId());
		for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
			String ip = bandwidthIPPortMap.getIpAddress();
			int startPort = bandwidthIPPortMap.getBandwidthPortStart();
			int endPort = bandwidthIPPortMap.getBandwidthPortEnd();
			boolean revoke = false;
			boolean alreadyAdded = false;
			BandwidthFilterRules bandwidthFilterRule = new BandwidthFilterRules(ip, startPort, endPort, revoke, alreadyAdded);
			bandwidthFilterRules.add(bandwidthFilterRule);
		}
		
		BandwidthRule newBandwidthRule = new BandwidthRule(rule, bandwidthFilterRules);
		newRules.add(newBandwidthRule);
		if(!applyBandwidthRules(network, newRules)){
			s_logger.error("Update the bandwidth rules, when create the new rules, it get wrong.");
			throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to update bandwidth rule");
		}
		//store to DB, if this need to delete the rule ,when can not update the DB?
		CallContext.current().setEventDetails("bandwidth rule id=" + rule.getId());
		BandwidthRulesVO bandwidthRules = _bandwidthRulesDao.persist(rule);
        if (bandwidthRules != null) {
            CallContext.current().setEventDetails("Bandwidth rule id=" + rule.getId());
            return true;
        } else {
            return false;
        }
	}


	private boolean validateBandwidthFilterRule(Long bandwidthRuleId, BandwidthType type, String ip, Integer portStart, Integer portEnd){
		
		if (portStart != null && !NetUtils.isValidPort(portStart)) {
            throw new InvalidParameterValueException("publicPort is an invalid value: " + portStart);
        }
        if (portEnd != null && !NetUtils.isValidPort(portEnd)) {
            throw new InvalidParameterValueException("Public port range is an invalid value: " + portEnd);
        }

        // start port can't be bigger than end port
        if (portStart != null && portEnd != null && portStart > portEnd) {
            throw new InvalidParameterValueException("Start port can't be bigger than end port");
        }
        //check the ip form
        if(!NetUtils.isValidIp(ip)){
        	throw new InvalidParameterValueException("The ip address is not right.");
        }
        
		if(type.equals(BandwidthType.InTraffic)){
			//TODO vm ip address, if it want to check the private ip address in the network?
			VMInstanceVO vMInstanceVO = _vMInstanceDao.findVMByIpAddress(ip);
			if(vMInstanceVO == null){
				throw new InvalidParameterValueException("Unable to create bandwidth filter rule ; The private ip is not right.");
			}
		} else if(type.equals(BandwidthType.OutTraffic)){
			//public ip address
			IPAddressVO ipAddress = null;
			ipAddress = _ipAddressDao.findByIp(ip);
			if(ipAddress == null){
				throw new InvalidParameterValueException("The ip address in the bandwidth filter rule is not right.");
			} else {
				BandwidthRulesVO rule = _bandwidthRulesDao.findById(bandwidthRuleId);
				if (ipAddress.getAssociatedWithNetworkId() == null || ipAddress.getAssociatedWithNetworkId() != rule.getNetworksId()) {
                    throw new InvalidParameterValueException("Unable to create bandwidth filter rule ; The public ip is not right.");
				}
			}
		} else {
			s_logger.error("The bandwidth rule parameter: type is not right, Only support InTraffic and OutTraffic.");
			throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Get the wrong bandwidth type");
		}
		return true;
	}
	
	@Override
	public boolean assignToBandwidthRule(AssignToBandwidthRuleCmd cmd) throws ResourceUnavailableException {
		//check the parameters, get the bandwidth rule by the id, and add the filter rule to the bandwidth rule
		Long bandwidthRuleId = cmd.getBandwidthRuleId();
		String ip = cmd.getIp();
		Integer newStartPort = cmd.getStartPort();
		Integer newEndPort = cmd.getEndPort();
		BandwidthRulesVO bandwidthClassRule = _bandwidthRulesDao.findById(bandwidthRuleId);
		BandwidthType type = bandwidthClassRule.getType();
		
		if(!validateBandwidthFilterRule(bandwidthRuleId, type, ip, newStartPort, newEndPort)){
			s_logger.error("The input parameters is not right, please reconfirm the parameters:ip,start port, end port.");
			throw new InvalidParameterValueException("The input parameters is not right, please reconfirm the parameters:ip,start port, end port");
		}
		
		List<BandwidthRule> rules = new ArrayList<BandwidthRule>();
		bandwidthClassRule.setRevoked(false);
		bandwidthClassRule.setKeepState(true);
		bandwidthClassRule.setAlreadyAdded(true);
		Network network = _networksDao.findById(bandwidthClassRule.getNetworksId());
		//update the filter rules
		List<BandwidthFilterRules> bandwidthFilterRules = new ArrayList<BandwidthFilterRules>();
		List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(bandwidthClassRule.getId());
		if(bandwidthIPPortMapList != null){
			for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
				String ipAddress = bandwidthIPPortMap.getIpAddress();
				int startPort = bandwidthIPPortMap.getBandwidthPortStart();
				int endPort = bandwidthIPPortMap.getBandwidthPortEnd();
				boolean revoke = false;
				boolean alreadyAdded = true;
				BandwidthFilterRules bandwidthFilterRule = new BandwidthFilterRules(ipAddress, startPort, endPort, revoke, alreadyAdded);
				bandwidthFilterRules.add(bandwidthFilterRule);
			}
		}
		
		BandwidthFilterRules newBandwidthFilterRule = new BandwidthFilterRules(ip, newStartPort, newEndPort, false, false);
		bandwidthFilterRules.add(newBandwidthFilterRule);
		
		BandwidthRule bandwidthRule = new BandwidthRule(bandwidthClassRule, bandwidthFilterRules);
		rules.add(bandwidthRule);
		if(applyBandwidthRules(network, rules)){
			//stort to db
			BandwidthIPPortMapVO newBandwidthIPPortMapVO = new BandwidthIPPortMapVO(bandwidthRuleId, ip, newStartPort, newEndPort);
	        CallContext.current().setEventDetails("bandwidth filter rule id=" + newBandwidthIPPortMapVO.getId());
	        BandwidthIPPortMapVO bandwidthIPPortMap = _bandwidthIPPortMapDao.persist(newBandwidthIPPortMapVO);
	        if (bandwidthIPPortMap != null) {
	            CallContext.current().setEventDetails("Bandwidth filter rule id=" + newBandwidthIPPortMapVO.getId());
	            return true;
	        } else {
	            return false;
	        }
		} else{
			return false;
		}
	}

	@Override
	public boolean removeFromBandwidthRule(RemoveFromBandwidthRuleCmd cmd) throws ResourceUnavailableException {
		//check the parameters, then get the bandwidth rule by the id, and add the filter rule to the bandwidth rule
		Long bandwidthRuleId = cmd.getId();
		String removedIp = cmd.getIp();
		Integer removedStartPort = cmd.getStartPort();
		Integer removedEndPort = cmd.getEndPort();
		Long bandwidthFilterRuleId = null;
		BandwidthRulesVO bandwidthClassRule = _bandwidthRulesDao.findById(bandwidthRuleId);
		BandwidthType type = bandwidthClassRule.getType();
		
		if(!validateBandwidthFilterRule(bandwidthRuleId, type, removedIp, removedStartPort, removedEndPort)){
			s_logger.error("The input parameters is not right, please reconfirm the parameters:ip,start port, end port.");
			throw new InvalidParameterValueException("The input parameters is not right, please reconfirm the parameters:ip,start port, end port");
		}
		
		List<BandwidthRule> rules = new ArrayList<BandwidthRule>();
		bandwidthClassRule.setRevoked(false);
		bandwidthClassRule.setKeepState(true);
		bandwidthClassRule.setAlreadyAdded(true);
		Network network = _networksDao.findById(bandwidthClassRule.getNetworksId());
		//update the filter rules
		List<BandwidthFilterRules> bandwidthFilterRules = new ArrayList<BandwidthFilterRules>();
		List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(bandwidthClassRule.getId());
		if(bandwidthIPPortMapList != null){
			for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
				String ipAddress = bandwidthIPPortMap.getIpAddress();
				int startPort = bandwidthIPPortMap.getBandwidthPortStart();
				int endPort = bandwidthIPPortMap.getBandwidthPortEnd();
				boolean revoke = false;
				boolean alreadyAdded = true;
				if(ipAddress.equalsIgnoreCase(removedIp) && startPort == removedStartPort && endPort == removedEndPort){
					revoke = true;
					alreadyAdded = true;
					bandwidthFilterRuleId = bandwidthIPPortMap.getId();
				}
				BandwidthFilterRules bandwidthFilterRule = new BandwidthFilterRules(ipAddress, startPort, endPort, revoke, alreadyAdded);
				bandwidthFilterRules.add(bandwidthFilterRule);
			}
		}
		
		BandwidthFilterRules newBandwidthFilterRule = new BandwidthFilterRules(removedIp, removedStartPort, removedEndPort, false, false);
		bandwidthFilterRules.add(newBandwidthFilterRule);
		
		BandwidthRule bandwidthRule = new BandwidthRule(bandwidthClassRule, bandwidthFilterRules);
		rules.add(bandwidthRule);
		if(applyBandwidthRules(network, rules)){
			//remove from the DB
	        CallContext.current().setEventDetails("bandwidth filter rule id=" + bandwidthFilterRuleId);
	        return _bandwidthIPPortMapDao.remove(bandwidthFilterRuleId);
		} else{
			return false;
		}
	}

	@Override
	public boolean checkBandwidthCapacity(BandwidthVO bandwidthVO, BandwidthType type, Integer newRate, Integer oldRate) {
		// 查询数据库表
		int bandwidthCapacity = 0;
		//检查rate参数是否合格，要查询数据库，所有的rate相加，不能大于总带宽数。
		if(BandwidthType.InTraffic.equals(type)){
			bandwidthCapacity = bandwidthVO.getInTraffic();
		} else if(BandwidthType.OutTraffic.equals(type)){
			bandwidthCapacity = bandwidthVO.getOutTraffic();
		} else {
			throw new InvalidParameterValueException("The bandwidth rule parameter: type is not right, Only support InTraffic and OutTraffic.");
		}
		List<BandwidthRulesVO> BandwidthRulesList = _bandwidthRulesDao.listByBandwidthIdAndType(bandwidthVO.getId(), type);
		int sumOfRuleUsed = 0;
		for(BandwidthRulesVO vo : BandwidthRulesList){
			sumOfRuleUsed += vo.getRate();
		}
		int nowSumOfRuleUsed = 0;
		
		nowSumOfRuleUsed = sumOfRuleUsed + newRate - oldRate;
		if(bandwidthCapacity < nowSumOfRuleUsed){
			return false;
		}
		return true;
	}

	@Override
	public boolean updateOfferingRefreshRules(int updateRate, int updateCeil, BandwidthOffering oldOffering) throws ResourceUnavailableException {
		// find all the rules which used the bandwidth offering
		List<BandwidthRulesVO> bandwidthRulesList = _bandwidthRulesDao.listByBandwidthOfferingId(oldOffering.getId());
		Set<Long> networksSet = new HashSet<Long>();
		for(BandwidthRulesVO BandwidthRulesVO : bandwidthRulesList){
			networksSet.add(BandwidthRulesVO.getNetworksId());
		}
		for(Long networkId : networksSet){
			List<BandwidthRule> oldRulesList = new ArrayList<BandwidthRule>();
			for(BandwidthRulesVO bandwidthClassRule : bandwidthRulesList){
				if(networkId.equals(bandwidthClassRule.getNetworksId())){
					bandwidthClassRule.setRevoked(true);
					bandwidthClassRule.setKeepState(false);
					bandwidthClassRule.setAlreadyAdded(true);
					BandwidthRule oldBandwidthRule = new BandwidthRule(bandwidthClassRule);
					oldRulesList.add(oldBandwidthRule);
				}
			}
			Network network = _networksDao.findById(networkId);
			if(!applyBandwidthRules(network, oldRulesList)){
				s_logger.error("Update the bandwidth rules, when delete the old rules, it get wrong.");
				throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to update bandwidth rule");
			}
		}
		
		for(Long networkId : networksSet){
			List<BandwidthRule> updateRules = new ArrayList<BandwidthRule>();
			for(BandwidthRulesVO bandwidthClassRule : bandwidthRulesList){
				if(networkId.equals(bandwidthClassRule.getNetworksId())){
					bandwidthClassRule.setRevoked(false);
					bandwidthClassRule.setKeepState(false);
					bandwidthClassRule.setAlreadyAdded(false);
					bandwidthClassRule.setRate(updateRate);
					bandwidthClassRule.setCeil(updateCeil);
					
					//reload the filter rules
					List<BandwidthFilterRules> bandwidthFilterRules = new ArrayList<BandwidthFilterRules>();
					List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(bandwidthClassRule.getId());
					for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
						String ip = bandwidthIPPortMap.getIpAddress();
						int startPort = bandwidthIPPortMap.getBandwidthPortStart();
						int endPort = bandwidthIPPortMap.getBandwidthPortEnd();
						boolean revoke = false;
						boolean alreadyAdded = false;
						BandwidthFilterRules bandwidthFilterRule = new BandwidthFilterRules(ip, startPort, endPort, revoke, alreadyAdded);
						bandwidthFilterRules.add(bandwidthFilterRule);
					}
					
					BandwidthRule updateBandwidthRule = new BandwidthRule(bandwidthClassRule, bandwidthFilterRules);
					updateRules.add(updateBandwidthRule);
				}
			}
			Network network = _networksDao.findById(networkId);
			if(!applyBandwidthRules(network, updateRules)){
				s_logger.error("Update the bandwidth rules, when create the new rules, it get wrong.");
				throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to update bandwidth rule");
			}
		}
		
		return true;
	}

	@Override
	public ListResponse<BandwidthRulesResponse> searchForBandwidthRules(ListBandwidthRulesCmd cmd) {
		
		ListResponse<BandwidthRulesResponse> response = new ListResponse<BandwidthRulesResponse>();
		List<BandwidthRulesResponse> respList = new ArrayList<BandwidthRulesResponse>();
		int count = 0;
		//get the result by the search condition.
		if(cmd.listAll()){
			//get all the bandwidth rule from DB.
			List<BandwidthRulesVO> bandwidthList = _bandwidthRulesDao.listAll();
			//id,bandwidth_id, networks_id, bandwidth_offering_id, domain_id, account_id, type, prio, ceil, rate, traffic_rule_id
			//ip_address, start_port, end_port
			count = bandwidthList.size();
			for(BandwidthRulesVO vo : bandwidthList){
				BandwidthRulesResponse bandwidthRulesResponse = new BandwidthRulesResponse();
				bandwidthRulesResponse.setId(vo.getUuid());
				BandwidthVO bandwidth = _bandwidthDao.findById(vo.getBandwidthId());
				bandwidthRulesResponse.setBandwidthId(bandwidth.getUuid());
				Network network = _networksDao.findById(vo.getNetworksId());
				bandwidthRulesResponse.setNetworkId(network.getUuid());
				BandwidthOfferingVO bandwidthOffering = _bandwidthOfferingDao.findById(vo.getBandwidthOfferingId());
				bandwidthRulesResponse.setBandwidthOfferingId(bandwidthOffering.getUuid());
				bandwidthRulesResponse.setType(vo.getType().toString());
				bandwidthRulesResponse.setPrio(vo.getPrio());
				bandwidthRulesResponse.setRate(vo.getRate());
				bandwidthRulesResponse.setCeil(vo.getCeil());
				bandwidthRulesResponse.setTrafficRuleId(vo.getTrafficRuleId());
				List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(vo.getId());
				if(bandwidthIPPortMapList != null){
					for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
						BandwidthFilterRuleResponse bandwidthFilterRuleResponse = new BandwidthFilterRuleResponse();
						bandwidthFilterRuleResponse.setIpAddress(bandwidthIPPortMap.getIpAddress());
						bandwidthFilterRuleResponse.setStartPort(bandwidthIPPortMap.getBandwidthPortStart());
						bandwidthFilterRuleResponse.setEndPort(bandwidthIPPortMap.getBandwidthPortEnd());
						bandwidthFilterRuleResponse.setObjectName("bandwidthfilter");
						bandwidthRulesResponse.addFilterRule(bandwidthFilterRuleResponse);
					}
				}
				bandwidthRulesResponse.setObjectName("bandwidthrule");
				respList.add(bandwidthRulesResponse);
			}
			
		} else if (cmd.getNetworkId() != null){
			//get the bandwidth rule from DB.
			List<BandwidthRulesVO> bandwidthList = _bandwidthRulesDao.listByNetworksId(cmd.getNetworkId());
			//id,bandwidth_id, networks_id, bandwidth_offering_id, domain_id, account_id, type, prio, ceil, rate, traffic_rule_id
			//ip_address, start_port, end_port
			count = bandwidthList.size();
			for(BandwidthRulesVO vo : bandwidthList){
				BandwidthRulesResponse bandwidthRulesResponse = new BandwidthRulesResponse();
				bandwidthRulesResponse.setId(vo.getUuid());
				BandwidthVO bandwidth = _bandwidthDao.findById(vo.getBandwidthId());
				bandwidthRulesResponse.setBandwidthId(bandwidth.getUuid());
				Network network = _networksDao.findById(vo.getNetworksId());
				bandwidthRulesResponse.setNetworkId(network.getUuid());
				BandwidthOfferingVO bandwidthOffering = _bandwidthOfferingDao.findById(vo.getBandwidthOfferingId());
				bandwidthRulesResponse.setBandwidthOfferingId(bandwidthOffering.getUuid());
				bandwidthRulesResponse.setType(vo.getType().toString());
				bandwidthRulesResponse.setPrio(vo.getPrio());
				bandwidthRulesResponse.setRate(vo.getRate());
				bandwidthRulesResponse.setCeil(vo.getCeil());
				bandwidthRulesResponse.setTrafficRuleId(vo.getTrafficRuleId());
				List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(vo.getId());
				if(bandwidthIPPortMapList != null){
					for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
						BandwidthFilterRuleResponse bandwidthFilterRuleResponse = new BandwidthFilterRuleResponse();
						bandwidthFilterRuleResponse.setIpAddress(bandwidthIPPortMap.getIpAddress());
						bandwidthFilterRuleResponse.setStartPort(bandwidthIPPortMap.getBandwidthPortStart());
						bandwidthFilterRuleResponse.setEndPort(bandwidthIPPortMap.getBandwidthPortEnd());
						bandwidthFilterRuleResponse.setObjectName("bandwidthfilter");
						bandwidthRulesResponse.addFilterRule(bandwidthFilterRuleResponse);
					}
				}
				bandwidthRulesResponse.setObjectName("bandwidthrule");
				respList.add(bandwidthRulesResponse);
			}
		} else if (cmd.getId() != null){
			
			//get the bandwidth rule from DB.
			BandwidthRulesVO vo = _bandwidthRulesDao.findById(cmd.getId());
			//id,bandwidth_id, networks_id, bandwidth_offering_id, domain_id, account_id, type, prio, ceil, rate, traffic_rule_id
			//ip_address, start_port, end_port
			count = 1;
			BandwidthRulesResponse bandwidthRulesResponse = new BandwidthRulesResponse();
			bandwidthRulesResponse.setId(vo.getUuid());
			BandwidthVO bandwidth = _bandwidthDao.findById(vo.getBandwidthId());
			bandwidthRulesResponse.setBandwidthId(bandwidth.getUuid());
			Network network = _networksDao.findById(vo.getNetworksId());
			bandwidthRulesResponse.setNetworkId(network.getUuid());
			BandwidthOfferingVO bandwidthOffering = _bandwidthOfferingDao.findById(vo.getBandwidthOfferingId());
			bandwidthRulesResponse.setBandwidthOfferingId(bandwidthOffering.getUuid());
			bandwidthRulesResponse.setType(vo.getType().toString());
			bandwidthRulesResponse.setPrio(vo.getPrio());
			bandwidthRulesResponse.setRate(vo.getRate());
			bandwidthRulesResponse.setCeil(vo.getCeil());
			bandwidthRulesResponse.setTrafficRuleId(vo.getTrafficRuleId());
			List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(vo.getId());
			if(bandwidthIPPortMapList != null){
				for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
					BandwidthFilterRuleResponse bandwidthFilterRuleResponse = new BandwidthFilterRuleResponse();
					bandwidthFilterRuleResponse.setIpAddress(bandwidthIPPortMap.getIpAddress());
					bandwidthFilterRuleResponse.setStartPort(bandwidthIPPortMap.getBandwidthPortStart());
					bandwidthFilterRuleResponse.setEndPort(bandwidthIPPortMap.getBandwidthPortEnd());
					bandwidthFilterRuleResponse.setObjectName("bandwidthfilter");
					bandwidthRulesResponse.addFilterRule(bandwidthFilterRuleResponse);
				}
			}
			bandwidthRulesResponse.setObjectName("bandwidthrule");
			respList.add(bandwidthRulesResponse);
		} else {
			s_logger.error("The search criteria is insufficient, when list the bandwidth rules.");
			throw new InvalidParameterValueException("The search criteria is insufficient.");
		}
		
        response.setResponses(respList, count);
        return response;
	}

}
