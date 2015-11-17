package com.cloud.agent.api.to;

import java.util.ArrayList;
import java.util.List;

import com.cloud.network.rules.BandwidthClassRule.BandwidthType;
import com.cloud.network.rules.BandwidthRule.BandwidthFilterRules;

public class BandwidthRuleTO {
    private int deviceId;
    private BandwidthType type;
    private int rate;
    private int ceil;
    private int trafficRuleId;
    private int prio;
    boolean revoked;
	boolean alreadyAdded;
	boolean keepState;
    private BandwidthFilterTO[] bandwidthFilters;
    
    protected BandwidthRuleTO() {
    	
    }
    public BandwidthRuleTO(int deviceId, int rate, int ceil, int trafficRuleId, boolean revoked, boolean alreadyAdded, List<BandwidthFilterRules> bandwidthFilters){
    	if(bandwidthFilters == null){
    		bandwidthFilters = new ArrayList<BandwidthFilterRules>();
    	}
    	this.deviceId = deviceId;
    	this.rate = rate;
    	this.ceil = ceil;
    	this.revoked = revoked;
    	this.alreadyAdded = alreadyAdded;
    	this.trafficRuleId = trafficRuleId;
    	this.bandwidthFilters = new BandwidthFilterTO[bandwidthFilters.size()];
    	int i = 0;
    	for(BandwidthFilterRules bandwidthFilter : bandwidthFilters){
    		this.bandwidthFilters[i++] = new BandwidthFilterTO(bandwidthFilter.getIpAddress(), bandwidthFilter.getStartPort(), bandwidthFilter.getEndPort(), bandwidthFilter.isRevoke(), false);
    	}
    }
    
    public BandwidthRuleTO(int deviceId, BandwidthType type ,int rate, int ceil, int trafficRuleId, int prio, boolean revoked, boolean alreadyAdded,boolean keepState, List<BandwidthFilterRules> bandwidthFilters){
    	if(bandwidthFilters == null){
    		bandwidthFilters = new ArrayList<BandwidthFilterRules>();
    	}
    	this.deviceId = deviceId;
    	this.type = type;
    	this.rate = rate;
    	this.ceil = ceil;
    	this.revoked = revoked;
    	this.alreadyAdded = alreadyAdded;
    	this.keepState = keepState;
    	this.trafficRuleId = trafficRuleId;
    	this.bandwidthFilters = new BandwidthFilterTO[bandwidthFilters.size()];
    	int i = 0;
    	for(BandwidthFilterRules bandwidthFilter : bandwidthFilters){
    		this.bandwidthFilters[i++] = new BandwidthFilterTO(bandwidthFilter.getIpAddress(), bandwidthFilter.getStartPort(), bandwidthFilter.getEndPort(), bandwidthFilter.isRevoke(), false);
    	}
    }
    
    public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getCeil() {
		return ceil;
	}

	public void setCeil(int ceil) {
		this.ceil = ceil;
	}

	public int getTrafficRuleId() {
		return trafficRuleId;
	}

	public void setTrafficRuleId(int trafficRuleId) {
		this.trafficRuleId = trafficRuleId;
	}

	public int getPrio() {
		return prio;
	}

	public void setPrio(int prio) {
		this.prio = prio;
	}

	public BandwidthFilterTO[] getBandwidthFilters() {
		return bandwidthFilters;
	}

	public void setBandwidthFilters(BandwidthFilterTO[] bandwidthFilters) {
		this.bandwidthFilters = bandwidthFilters;
	}

	public boolean isRevoked() {
		return revoked;
	}
	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}
	public boolean isAlreadyAdded() {
		return alreadyAdded;
	}
	public void setAlreadyAdded(boolean alreadyAdded) {
		this.alreadyAdded = alreadyAdded;
	}

	public boolean isKeepState() {
		return keepState;
	}
	public void setKeepState(boolean keepState) {
		this.keepState = keepState;
	}

	public BandwidthType getType() {
		return type;
	}
	public void setType(BandwidthType type) {
		this.type = type;
	}

	public static class BandwidthFilterTO{
    	private String ip;
    	private int startPort;
    	private int endPort;
    	boolean revoke;
    	boolean alreadyAdded;
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
		public boolean isAlreadyAdded() {
			return alreadyAdded;
		}
		public void setAlreadyAdded(boolean alreadyAdded) {
			this.alreadyAdded = alreadyAdded;
		}
		public BandwidthFilterTO(String ip, int startPort, int endPort, boolean revoke, boolean alreadyAdded){
    		this.ip = ip;
    		this.startPort = startPort;
    		this.endPort = endPort;
    		this.revoke = revoke;
    		this.alreadyAdded = alreadyAdded;
    	}
    }
}
