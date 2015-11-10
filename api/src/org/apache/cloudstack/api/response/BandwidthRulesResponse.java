package org.apache.cloudstack.api.response;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.cloudstack.api.BaseResponse;
import org.apache.cloudstack.api.EntityReference;

import com.cloud.network.rules.BandwidthClassRule;
import com.cloud.serializer.Param;
import com.google.gson.annotations.SerializedName;

@EntityReference(value=BandwidthClassRule.class)
public class BandwidthRulesResponse  extends BaseResponse {
	//id,bandwidth_id, networks_id, bandwidth_offering_id, domain_id, account_id, type, prio, ceil, rate, traffic_rule_id
	//ip_address, start_port, end_port
	@SerializedName("id") @Param(description="the id of the bandwidth rule")
    private String id;
	
	@SerializedName("bandwidthId") @Param(description="the bandwidth id of the bandwidth rule")
    private String bandwidthId;
	
	@SerializedName("networkId") @Param(description="the network id of the bandwidth rule")
    private String networkId;
	
	@SerializedName("bandwidthOfferingId") @Param(description="the bandwidth offering id of the bandwidth rule")
    private String bandwidthOfferingId;
	
	@SerializedName("domainId") @Param(description="the domain id of the bandwidth rule")
    private String domainId;
	
	@SerializedName("type") @Param(description="the type of the bandwidth rule")
    private String type;
	
	@SerializedName("rate") @Param(description="the rate of the bandwidth offering in Kbit")
    private Integer rate;
    
    @SerializedName("ceil") @Param(description="the ceil of the bandwidth offering in Kbit")
    private Integer ceil;
    
    @SerializedName("prio") @Param(description="the prio of the bandwidth rule")
    private Integer prio;
    
    @SerializedName("trafficRuleId") @Param(description="the trafficRuleId of the bandwidth rule")
    private Integer trafficRuleId;
    
	@SerializedName("filterRule")  @Param(description="the list of filter rules associated with bandwidth class rule", responseObject = BandwidthFilterRuleResponse.class)
	private Set<BandwidthFilterRuleResponse> filterRules;

	public BandwidthRulesResponse(){
		filterRules = new LinkedHashSet<BandwidthFilterRuleResponse>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getBandwidthOfferingId() {
		return bandwidthOfferingId;
	}

	public void setBandwidthOfferingId(String bandwidthOfferingId) {
		this.bandwidthOfferingId = bandwidthOfferingId;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public Integer getCeil() {
		return ceil;
	}

	public void setCeil(Integer ceil) {
		this.ceil = ceil;
	}

	public Integer getPrio() {
		return prio;
	}

	public void setPrio(Integer prio) {
		this.prio = prio;
	}

	public Integer getTrafficRuleId() {
		return trafficRuleId;
	}

	public void setTrafficRuleId(Integer trafficRuleId) {
		this.trafficRuleId = trafficRuleId;
	}

	public Set<BandwidthFilterRuleResponse> getFilterRules() {
		return filterRules;
	}

	public void setFilterRules(Set<BandwidthFilterRuleResponse> filterRules) {
		this.filterRules = filterRules;
	}

	public void addFilterRule(BandwidthFilterRuleResponse filter) {
		this.filterRules.add(filter);
	}

	public String getBandwidthId() {
		return bandwidthId;
	}

	public void setBandwidthId(String bandwidthId) {
		this.bandwidthId = bandwidthId;
	}
}
