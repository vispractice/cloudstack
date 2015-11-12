package com.cloud.network.rules;

import org.apache.cloudstack.acl.ControlledEntity;
import org.apache.cloudstack.api.Identity;
import org.apache.cloudstack.api.InternalIdentity;

public interface BandwidthClassRule extends ControlledEntity, Identity, InternalIdentity{
	//bandwidth_rules(uuid, id, bandwidth_id, networks_id, bandwidth_offering_id, traffic_rule_id, type, prio, ceil, rate)
		public enum BandwidthType {
			InTraffic, OutTraffic
		};
	
	String getUuid();
	
	Long getBandwidthId();
	
	Long getNetworksId();
	
	Long getBandwidthOfferingId();
	
	Integer getTrafficRuleId();
	
	BandwidthType getType();
	
	Integer getPrio();
	
	Integer getRate();
	
	Integer getCeil();
	
//	Boolean isRevoked();
//	
//	Boolean isKeepState();
//	
//	Boolean isAlreadyAdded();
}
