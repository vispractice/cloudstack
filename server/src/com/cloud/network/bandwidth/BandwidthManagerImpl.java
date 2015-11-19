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
import org.apache.cloudstack.api.command.user.bandwidth.AddBandwidthCmd;
import org.apache.cloudstack.api.command.user.bandwidth.AssignToBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.CreateBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.DeleteBandwidthCmd;
import org.apache.cloudstack.api.command.user.bandwidth.DeleteBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.ListBandwidthRulesCmd;
import org.apache.cloudstack.api.command.user.bandwidth.ListBandwidthsCmd;
import org.apache.cloudstack.api.command.user.bandwidth.RemoveFromBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.UpdateBandwidthCmd;
import org.apache.cloudstack.api.command.user.bandwidth.UpdateBandwidthRuleCmd;
import org.apache.cloudstack.api.response.BandwidthFilterRuleResponse;
import org.apache.cloudstack.api.response.BandwidthResponse;
import org.apache.cloudstack.api.response.BandwidthRulesResponse;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.cloudstack.context.CallContext;
import org.apache.cloudstack.engine.cloud.entity.api.db.dao.VMNetworkMapDao;
import org.apache.cloudstack.framework.config.dao.ConfigurationDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cloud.configuration.Config;
import com.cloud.dc.DataCenterVO;
import com.cloud.dc.dao.DataCenterDao;
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
	@Inject
	DataCenterDao _dataCenterDao;
	@Inject
    ConfigurationDao _configDao;
	@Inject
	VMNetworkMapDao _vmNetworkMapDao;
	
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
		// the number 9999 must be write in the global setting.
		Integer maxTrafficRuleId = Integer.parseInt(_configDao.getValue(Config.NetworkRouterBandwidthTrafficIdRange.key()));
		for(Integer i = 2; i <= maxTrafficRuleId; i++){
			if(!trafficRuleIdList.contains(i)){
				trafficRuleId = i;
				break;
			}
		}
		if(trafficRuleId == null){
			s_logger.error("Failed to create bandwidth rule, because there is not enough traffic id to be used. Only support range is:2~9999");
			throw new ServerApiException(ApiErrorCode.INSUFFICIENT_CAPACITY_ERROR, "Failed to create bandwidth rule.");
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
		Network network = _networksDao.findById(rule.getNetworksId());
		BandwidthRule bandwidthRule = new BandwidthRule(rule);
		bandwidthRule.setClassRuleRevoked(false);
		bandwidthRule.setClassRuleKeepState(false);
		bandwidthRule.setClassRuleAlreadyAdded(false);
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
		Network network = _networksDao.findById(rule.getNetworksId());
		BandwidthRule bandwidthRule = new BandwidthRule(rule);
		bandwidthRule.setClassRuleRevoked(true);
		bandwidthRule.setClassRuleKeepState(false);
		bandwidthRule.setClassRuleAlreadyAdded(true);
		rules.add(bandwidthRule);
		if(!applyBandwidthRules(network, rules)){
			s_logger.error("when delete the bandwidth rules it get wrong.");
			throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to delete bandwidth rule");
		}
		return revokeRelatedBandwidthRule(cmd.getId());
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
		Network network = _networksDao.findById(rule.getNetworksId());
		BandwidthRule oldBandwidthRule = new BandwidthRule(rule);
		oldBandwidthRule.setClassRuleRevoked(true);
		oldBandwidthRule.setClassRuleKeepState(false);
		oldBandwidthRule.setClassRuleAlreadyAdded(true);
		oldRules.add(oldBandwidthRule);
		if(!applyBandwidthRules(network, oldRules)){
			s_logger.error("Update the bandwidth rules, when delete the old rules, it get wrong.");
			throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to update bandwidth rule");
		}
		//then create the new and store to DB.
		List<BandwidthRule> newRules = new ArrayList<BandwidthRule>();
		//reload the bandwidth class rule.
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
		newBandwidthRule.setClassRuleRevoked(false);
		newBandwidthRule.setClassRuleKeepState(false);
		newBandwidthRule.setClassRuleAlreadyAdded(false);
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


	private boolean validateBandwidthFilterRule(boolean isAdd, Long bandwidthRuleId, BandwidthType type, String ip, Integer portStart, Integer portEnd){
		
		if (portStart != null && !NetUtils.isValidPort(portStart)) {
            throw new InvalidParameterValueException("Port is an invalid value: " + portStart);
        }
        if (portEnd != null && !NetUtils.isValidPort(portEnd)) {
            throw new InvalidParameterValueException("Port range is an invalid value: " + portEnd);
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
			// vm ip address, it want to check the private ip address in the network.
			VMInstanceVO vMInstanceVO = _vMInstanceDao.findVMByIpAddress(ip);
			if(vMInstanceVO == null){
				throw new InvalidParameterValueException("Unable to create or delete bandwidth filter rule ; The private ip is not right.");
			}
			List<Long> networksIds = _vmNetworkMapDao.getNetworks(vMInstanceVO.getId());
			boolean isVmUsedNetwork = false;
			BandwidthRulesVO rule = _bandwidthRulesDao.findById(bandwidthRuleId);
			for(Long networksId : networksIds){
				if(networksId.longValue() == rule.getNetworksId().longValue()){
					isVmUsedNetwork = true;
				}
			}
			if(!isVmUsedNetwork){
				throw new InvalidParameterValueException("Unable to create or delete bandwidth filter rule ; The private ip is not right.");
			}
		} else if(type.equals(BandwidthType.OutTraffic)){
			//public ip address
			IPAddressVO ipAddress = null;
			ipAddress = _ipAddressDao.findByIp(ip);
			if(ipAddress == null){
				throw new InvalidParameterValueException("The ip address in the bandwidth filter rule is not right.");
			} else {
				BandwidthRulesVO rule = _bandwidthRulesDao.findById(bandwidthRuleId);
				//get the public ip multiline label and compare with the bandwidth rule multiline label,if it is same, it is ok.
				BandwidthVO bandwidth = _bandwidthDao.findById(rule.getBandwidthId());
				MultilineVO multiline = _multilineDao.findById(bandwidth.getMultilineId());
				if (ipAddress.getAssociatedWithNetworkId() == null || ipAddress.getAssociatedWithNetworkId().longValue() != rule.getNetworksId().longValue() || !multiline.getLabel().equalsIgnoreCase(ipAddress.getMultilineLabel())) {
                    throw new InvalidParameterValueException("Unable to create or delete bandwidth filter rule ; The public ip is not right.");
				}
			}
		} else {
			s_logger.error("The bandwidth rule parameter: type is not right, Only support InTraffic and OutTraffic.");
			throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Get the wrong bandwidth type");
		}
		if(isAdd){
			//when the operation is add the filter rule to the bandwidth class rule, 
			//it want to compare the port with the DB which be used in the same bandwidth_rule_id and ip.
			List<BandwidthIPPortMapVO> listVOs = _bandwidthIPPortMapDao.listByBWClassIdIp(bandwidthRuleId, ip);
			for(BandwidthIPPortMapVO vo : listVOs){
				if(portStart <= vo.getBandwidthPortStart() && portEnd >= vo.getBandwidthPortStart()){
					throw new InvalidParameterValueException("Port range is an invalid value: " + portEnd + ", Conflict with the old rules");
				}
				if(portStart >= vo.getBandwidthPortStart() && portEnd <= vo.getBandwidthPortEnd()){
					throw new InvalidParameterValueException("Port range is an invalid value: "+ portStart +":"+ portEnd+ ", Conflict with the old rules");
				}
				if(portStart <= vo.getBandwidthPortEnd() && portEnd >= vo.getBandwidthPortEnd()){
					throw new InvalidParameterValueException("Port range is an invalid value: " + portStart+ ", Conflict with the old rules");
				}
			}
		} else {
			//when the operation is delete the filter rule to the bandwidth class rule, 
			//it must be a old filter rule in the DB.
			BandwidthIPPortMapVO BandwidthIPPortMapVO = _bandwidthIPPortMapDao.findOneByBWClassIdIpPorts(bandwidthRuleId, ip, portStart, portEnd);
			if(BandwidthIPPortMapVO == null){
				throw new InvalidParameterValueException("Unable to delete bandwidth filter rule ; Can not find the filter rule in the DB.");
			}
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
		
		if(!validateBandwidthFilterRule(true, bandwidthRuleId, type, ip, newStartPort, newEndPort)){
			s_logger.error("The input parameters is not right, please reconfirm the parameters:ip,start port, end port.");
			throw new InvalidParameterValueException("The input parameters is not right, please reconfirm the parameters:ip,start port, end port");
		}
		
		List<BandwidthRule> rules = new ArrayList<BandwidthRule>();
		Network network = _networksDao.findById(bandwidthClassRule.getNetworksId());
		//update the filter rules
		List<BandwidthFilterRules> bandwidthFilterRules = new ArrayList<BandwidthFilterRules>();
		List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(bandwidthClassRule.getId());
		if(bandwidthIPPortMapList != null && !bandwidthIPPortMapList.isEmpty()){
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
		bandwidthRule.setClassRuleRevoked(false);
		bandwidthRule.setClassRuleKeepState(true);
		bandwidthRule.setClassRuleAlreadyAdded(true);
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
		BandwidthIPPortMapVO oldBandwidthIPPortMap = _bandwidthIPPortMapDao.findByUuid(cmd.getFilterRuleId());
		if(oldBandwidthIPPortMap == null){
			throw new InvalidParameterValueException("The input parameters is not right, can not find the rule, please reconfirm the parameters.");
		}
		String removedIp = oldBandwidthIPPortMap.getIpAddress();
		Integer removedStartPort = oldBandwidthIPPortMap.getBandwidthPortStart();
		Integer removedEndPort = oldBandwidthIPPortMap.getBandwidthPortEnd();
		Long bandwidthFilterRuleId = oldBandwidthIPPortMap.getId();
		BandwidthRulesVO bandwidthClassRule = _bandwidthRulesDao.findById(bandwidthRuleId);
		BandwidthType type = bandwidthClassRule.getType();
		
		if(!validateBandwidthFilterRule(false, bandwidthRuleId, type, removedIp, removedStartPort, removedEndPort)){
			s_logger.error("The input parameters is not right, please reconfirm the parameters:ip,start port, end port.");
			throw new InvalidParameterValueException("The input parameters is not right, please reconfirm the parameters:ip,start port, end port");
		}
		
		List<BandwidthRule> rules = new ArrayList<BandwidthRule>();
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
				}
				BandwidthFilterRules bandwidthFilterRule = new BandwidthFilterRules(ipAddress, startPort, endPort, revoke, alreadyAdded);
				bandwidthFilterRules.add(bandwidthFilterRule);
			}
		}
		
		BandwidthRule bandwidthRule = new BandwidthRule(bandwidthClassRule, bandwidthFilterRules);
		bandwidthRule.setClassRuleRevoked(false);
		bandwidthRule.setClassRuleKeepState(true);
		bandwidthRule.setClassRuleAlreadyAdded(true);
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
					BandwidthRule oldBandwidthRule = new BandwidthRule(bandwidthClassRule);
					oldBandwidthRule.setClassRuleRevoked(true);
					oldBandwidthRule.setClassRuleKeepState(false);
					oldBandwidthRule.setClassRuleAlreadyAdded(true);
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
					updateBandwidthRule.setClassRuleRevoked(false);
					updateBandwidthRule.setClassRuleKeepState(false);
					updateBandwidthRule.setClassRuleAlreadyAdded(false);
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
				if(vo.getBandwidthOfferingId() != null){
					BandwidthOfferingVO bandwidthOffering = _bandwidthOfferingDao.findById(vo.getBandwidthOfferingId());
					bandwidthRulesResponse.setBandwidthOfferingId(bandwidthOffering.getUuid());
				}
				bandwidthRulesResponse.setType(vo.getType().toString());
				bandwidthRulesResponse.setPrio(vo.getPrio());
				bandwidthRulesResponse.setRate(vo.getRate());
				bandwidthRulesResponse.setCeil(vo.getCeil());
				bandwidthRulesResponse.setTrafficRuleId(vo.getTrafficRuleId());
				List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(vo.getId());
				if(bandwidthIPPortMapList != null){
					for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
						BandwidthFilterRuleResponse bandwidthFilterRuleResponse = new BandwidthFilterRuleResponse();
						bandwidthFilterRuleResponse.setFilterRuleId(bandwidthIPPortMap.getUuid());
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
				if(vo.getBandwidthOfferingId() != null){
					BandwidthOfferingVO bandwidthOffering = _bandwidthOfferingDao.findById(vo.getBandwidthOfferingId());
					bandwidthRulesResponse.setBandwidthOfferingId(bandwidthOffering.getUuid());
				}
				bandwidthRulesResponse.setType(vo.getType().toString());
				bandwidthRulesResponse.setPrio(vo.getPrio());
				bandwidthRulesResponse.setRate(vo.getRate());
				bandwidthRulesResponse.setCeil(vo.getCeil());
				bandwidthRulesResponse.setTrafficRuleId(vo.getTrafficRuleId());
				List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(vo.getId());
				if(bandwidthIPPortMapList != null){
					for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
						BandwidthFilterRuleResponse bandwidthFilterRuleResponse = new BandwidthFilterRuleResponse();
						bandwidthFilterRuleResponse.setFilterRuleId(bandwidthIPPortMap.getUuid());
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
			if(vo.getBandwidthOfferingId() != null){
				BandwidthOfferingVO bandwidthOffering = _bandwidthOfferingDao.findById(vo.getBandwidthOfferingId());
				bandwidthRulesResponse.setBandwidthOfferingId(bandwidthOffering.getUuid());
			}
			bandwidthRulesResponse.setType(vo.getType().toString());
			bandwidthRulesResponse.setPrio(vo.getPrio());
			bandwidthRulesResponse.setRate(vo.getRate());
			bandwidthRulesResponse.setCeil(vo.getCeil());
			bandwidthRulesResponse.setTrafficRuleId(vo.getTrafficRuleId());
			List<BandwidthIPPortMapVO> bandwidthIPPortMapList = _bandwidthIPPortMapDao.listByBandwidthRulesId(vo.getId());
			if(bandwidthIPPortMapList != null){
				for(BandwidthIPPortMapVO bandwidthIPPortMap : bandwidthIPPortMapList){
					BandwidthFilterRuleResponse bandwidthFilterRuleResponse = new BandwidthFilterRuleResponse();
					bandwidthFilterRuleResponse.setFilterRuleId(bandwidthIPPortMap.getUuid());
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

	@Override
	public boolean addBandwidth(AddBandwidthCmd cmd) {
		//check the parameters
		String multilineId = cmd.getMultilineId();
		Long zoneId = cmd.getZoneId();
		int inTraffic = cmd.getInTraffic();
		int outTraffic = cmd.getOutTraffic();
		MultilineVO multiline = _multilineDao.findByUuid(multilineId);
		if(multiline == null){
			throw new InvalidParameterValueException("The multiline id is wrong in this zone.");
		}
		DataCenterVO dataCenter = _dataCenterDao.findById(zoneId);
		if(dataCenter == null){
			throw new InvalidParameterValueException("The zone id is wrong.");
		}
		if(inTraffic <= 0 || outTraffic <= 0 ){
			throw new InvalidParameterValueException("The in traffic and out traffic must be more than zero.");
		}
		BandwidthVO oldBandwidth = _bandwidthDao.getBandwidthByMultilineId(multiline.getId());
		if(oldBandwidth != null){
			throw new InvalidParameterValueException("The multiline and the bandwidth are one-to-one relationship.");
		}
		//persist to DB
		BandwidthVO bandwidth = new BandwidthVO(multiline.getId(), zoneId, inTraffic, outTraffic);
		CallContext.current().setEventDetails("bandwidth rule id=" + bandwidth.getId());
		BandwidthVO bandwidthVO = _bandwidthDao.persist(bandwidth);
        if (bandwidthVO != null) {
            CallContext.current().setEventDetails("Bandwidth rule id=" + bandwidthVO.getId());
            return true;
        } else {
            return false;
        }
	}

	@Override
	public boolean deleteBandwidth(DeleteBandwidthCmd cmd) {
		Long bandwidthId = cmd.getId();
		BandwidthVO bandwidth = _bandwidthDao.findById(bandwidthId);
		if(bandwidth == null){
			throw new InvalidParameterValueException("The bandwidth id is wrong.");
		}
		List<BandwidthRulesVO> bandwidthList = _bandwidthRulesDao.listByBandwidthId(bandwidthId);
		if(!bandwidthList.isEmpty()){
			throw new InvalidParameterValueException("The bandwidth is in use now.");
		}
		//remove from the DB
        CallContext.current().setEventDetails("bandwidth id=" + bandwidthId);
		return _bandwidthDao.remove(bandwidthId);
	}
	
	@Override
	public ListResponse<BandwidthResponse> searchForBandwidths(ListBandwidthsCmd cmd) {
		ListResponse<BandwidthResponse> response = new ListResponse<BandwidthResponse>();
		List<BandwidthResponse> respList = new ArrayList<BandwidthResponse>();
		int count = 0;
		//get the result by the search condition.
		if(cmd.getZoneId() != null){
			List<BandwidthVO> bandwidthList = _bandwidthDao.listByZoneId(cmd.getZoneId());
			count = bandwidthList.size();
			for(BandwidthVO vo : bandwidthList){
				BandwidthResponse bandwidthResponse = new BandwidthResponse();
				bandwidthResponse.setId(vo.getUuid());
				MultilineVO multilineVO = _multilineDao.findById(vo.getMultilineId());
				if(multilineVO != null){
					bandwidthResponse.setMultilineId(multilineVO.getUuid());
					bandwidthResponse.setMultilineLabel(multilineVO.getLabel());
				}
				bandwidthResponse.setInTraffic(vo.getInTraffic());
				bandwidthResponse.setOutTraffic(vo.getOutTraffic());
				bandwidthResponse.setObjectName("bandwidth");
				respList.add(bandwidthResponse);
			}
		} else if(cmd.getId() != null){
			BandwidthVO bandwidth = _bandwidthDao.findById(cmd.getId());
			BandwidthResponse bandwidthResponse = new BandwidthResponse();
			bandwidthResponse.setId(bandwidth.getUuid());
			MultilineVO multilineVO = _multilineDao.findById(bandwidth.getMultilineId());
			if(multilineVO != null){
				bandwidthResponse.setMultilineId(multilineVO.getUuid());
				bandwidthResponse.setMultilineLabel(multilineVO.getLabel());
			}
			bandwidthResponse.setInTraffic(bandwidth.getInTraffic());
			bandwidthResponse.setOutTraffic(bandwidth.getOutTraffic());
			bandwidthResponse.setObjectName("bandwidth");
			respList.add(bandwidthResponse);
		}
		
		response.setResponses(respList, count);
        return response;
	}

	@Override
	public boolean updateBandwidth(UpdateBandwidthCmd cmd) {
		// only support to expand the bandwidth capacity.
		BandwidthVO bandwidth = _bandwidthDao.findById(cmd.getBandwidthId());
		if(bandwidth.getInTraffic() > cmd.getInTraffic() || bandwidth.getOutTraffic() > cmd.getOutTraffic()){
			throw new InvalidParameterValueException("The bandwidth now only support to expand the in traffic and out traffic.");
		}
		CallContext.current().setEventDetails("bandwidth id=" + bandwidth.getId());
		bandwidth.setInTraffic(cmd.getInTraffic());
		bandwidth.setOutTraffic(cmd.getOutTraffic());
		BandwidthVO bandwidthVO = _bandwidthDao.persist(bandwidth);
        if (bandwidthVO != null) {
            CallContext.current().setEventDetails("Bandwidth id=" + bandwidthVO.getId());
            return true;
        } else {
            return false;
        }
	}

}
