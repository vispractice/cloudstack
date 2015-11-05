package com.cloud.network.rules;

import org.apache.cloudstack.acl.ControlledEntity;
import org.apache.cloudstack.api.Identity;
import org.apache.cloudstack.api.InternalIdentity;

public interface BandwidthRule extends ControlledEntity, Identity, InternalIdentity{
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
	
	Boolean isRevoked();
	
	public interface BandwidthFilter {
		String getIpAddress();
		int getStartPort();
		int getEndPort();
		boolean isRevoke();
	}
	
	public static class BandwidthFilterRules implements BandwidthFilter {
		private String ip;
		private int startPort;
		private int endPort;
		private boolean revoke;
		
		public BandwidthFilterRules(String ip, int startPort, int endPort, boolean revoke){
			this.ip = ip;
			this.startPort = startPort;
			this.endPort = endPort;
			this.revoke = revoke;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public int getStartPort() {
			return startPort;
		}

		public void setStartPort(int startPort) {
			this.startPort = startPort;
		}

		public int getEndPort() {
			return endPort;
		}

		public void setEndPort(int endPort) {
			this.endPort = endPort;
		}

		public boolean isRevoke() {
			return revoke;
		}

		public void setRevoke(boolean revoke) {
			this.revoke = revoke;
		}

		@Override
		public String getIpAddress() {
			return ip;
		}

		
	}
	
}
