package com.cloud.network.rules;

import java.util.List;

import com.cloud.network.rules.BandwidthClassRule.BandwidthType;


public class BandwidthRule{
	private BandwidthClassRule bandwidthClassRule;
	
	private List<BandwidthFilterRules> bandwidthFilterRules;
	
    private Boolean classRuleRevoked;
  
    private Boolean classRuleKeepState;
  
    private Boolean classRuleAlreadyAdded;
	
//	private int deviceId;
	
	public BandwidthRule(BandwidthClassRule bandwidthClassRule, List<BandwidthFilterRules> bandwidthFilterRules){
		this.bandwidthClassRule = bandwidthClassRule;
		this.bandwidthFilterRules = bandwidthFilterRules;
	}
	
	public BandwidthRule(BandwidthClassRule bandwidthClassRule){
		this.bandwidthClassRule = bandwidthClassRule;
	}
	
	public BandwidthClassRule getBandwidthClassRule() {
		return bandwidthClassRule;
	}

	public void setBandwidthClassRule(BandwidthClassRule bandwidthClassRule) {
		this.bandwidthClassRule = bandwidthClassRule;
	}

	public List<BandwidthFilterRules> getBandwidthFilterRules() {
		return bandwidthFilterRules;
	}

	public void setBandwidthFilterRules(
			List<BandwidthFilterRules> bandwidthFilterRules) {
		this.bandwidthFilterRules = bandwidthFilterRules;
	}

//	public int getDeviceId() {
//		return deviceId;
//	}
//
//	public void setDeviceId(int deviceId) {
//		this.deviceId = deviceId;
//	}

	public Long getBandwidthId(){
		return bandwidthClassRule.getBandwidthId();
	}
	
	public Long getNetworksId(){
		return bandwidthClassRule.getNetworksId();
	}
	
	public Long getBandwidthOfferingId(){
		return bandwidthClassRule.getBandwidthOfferingId();
	}
	
	public Integer getTrafficRuleId(){
		return bandwidthClassRule.getTrafficRuleId();
	}
	
	public BandwidthType getType(){
		return bandwidthClassRule.getType();
	}
	
	public Integer getPrio(){
		return bandwidthClassRule.getPrio();
	}
	
	public Integer getRate(){
		return bandwidthClassRule.getRate();
	}
	
	public Integer getCeil(){
		return bandwidthClassRule.getCeil();
	}
	
	public Boolean isClassRuleRevoked(){
		return classRuleRevoked;
	}
	
	public Boolean isClassRuleKeepState() {
		return classRuleKeepState;
	}
	
	public Boolean isClassRuleAlreadyAdded(){
		return classRuleAlreadyAdded;
	}
	
	public void setClassRuleRevoked(Boolean classRuleRevoked) {
		this.classRuleRevoked = classRuleRevoked;
	}

	public void setClassRuleKeepState(Boolean classRuleKeepState) {
		this.classRuleKeepState = classRuleKeepState;
	}

	public void setClassRuleAlreadyAdded(Boolean classRuleAlreadyAdded) {
		this.classRuleAlreadyAdded = classRuleAlreadyAdded;
	}



	public interface BandwidthFilter {
		String getIpAddress();
		int getStartPort();
		int getEndPort();
		boolean isRevoke();
		boolean isAlreadyAdded();
	}
	
	public static class BandwidthFilterRules implements BandwidthFilter {
		private String ip;
		private int startPort;
		private int endPort;
		private boolean revoke;
		private boolean alreadyAdded;
		
		public BandwidthFilterRules(String ip, int startPort, int endPort, boolean revoke, boolean alreadyAdded){
			this.ip = ip;
			this.startPort = startPort;
			this.endPort = endPort;
			this.revoke = revoke;
			this.alreadyAdded = alreadyAdded;
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

		public boolean isAlreadyAdded() {
			return alreadyAdded;
		}

		public void setAlreadyAdded(boolean alreadyAdded) {
			this.alreadyAdded = alreadyAdded;
		}

	}
	
}
