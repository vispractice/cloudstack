package com.cloud.network.dao;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.cloudstack.api.InternalIdentity;

import com.cloud.network.rules.BandwidthRule;

@Entity
@Table(name="bandwidth_rules")
public class BandwidthRulesVO implements BandwidthRule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
	
	@Column(name="uuid")
    private String uuid;
	
	@Column(name="bandwidth_id")
    private Long bandwidthId;
	
	@Column(name="networks_id")
    private Long networksId;
	
	@Column(name="bandwidth_offering_id")
    private Long bandwidthOfferingId;
	
	@Column(name="domain_id")
    private Long domainId;
	
	@Column(name="account_id")
    private Long accountId;
	
	@Column(name="traffic_rule_id")
    private Integer trafficRuleId;
	
	@Column(name = "type")
	BandwidthType type;
	
	@Column(name="prio")
    private Integer prio;
	
	@Column(name="rate")
    private Integer rate;

    @Column(name="ceil")
    private Integer ceil;
    
    private Boolean revoked;
    
    private Boolean keepState;
    
    public BandwidthRulesVO(){
    	uuid = UUID.randomUUID().toString();
    }
    
    public BandwidthRulesVO(Long bandwidthId, Long networksId, Long bandwidthOfferingId, Integer trafficRuleId, BandwidthType type, Integer prio, Integer rate, Integer ceil ){
    	uuid = UUID.randomUUID().toString();
    	this.bandwidthId = bandwidthId;
    	this.networksId = networksId;
    	this.bandwidthOfferingId = bandwidthOfferingId;
    	this.trafficRuleId = trafficRuleId;
    	this.type = type;
    	this.prio = prio;
    	this.rate = rate;
    	this.ceil = ceil;
    }
    
    public BandwidthRulesVO(Long bandwidthId, Long networksId, Integer trafficRuleId, BandwidthType type, Integer prio, Integer rate, Integer ceil ){
    	uuid = UUID.randomUUID().toString();
    	this.bandwidthId = bandwidthId;
    	this.networksId = networksId;
    	this.trafficRuleId = trafficRuleId;
    	this.type = type;
    	this.prio = prio;
    	this.rate = rate;
    	this.ceil = ceil;
    }
    
	@Override
	public long getId() {
		return id;
	}
    
    public void setId(long id) {
		this.id = id;
	}
    
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getBandwidthId() {
		return bandwidthId;
	}

	public void setBandwidthId(Long bandwidthId) {
		this.bandwidthId = bandwidthId;
	}

	public Long getNetworksId() {
		return networksId;
	}

	public void setNetworksId(Long networksId) {
		this.networksId = networksId;
	}

	public Long getBandwidthOfferingId() {
		return bandwidthOfferingId;
	}

	public void setBandwidthOfferingId(Long bandwidthOfferingId) {
		this.bandwidthOfferingId = bandwidthOfferingId;
	}

	public Integer getTrafficRuleId() {
		return trafficRuleId;
	}

	public void setTrafficRuleId(Integer trafficRuleId) {
		this.trafficRuleId = trafficRuleId;
	}

	public BandwidthType getType() {
		return type;
	}

	public void setType(BandwidthType type) {
		this.type = type;
	}

	public Integer getPrio() {
		return prio;
	}

	public void setPrio(Integer prio) {
		this.prio = prio;
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

	@Override
	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	@Override
	public long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public Boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
	}

	public Boolean isKeepState() {
		return keepState;
	}

	public void setKeepState(Boolean keepState) {
		this.keepState = keepState;
	}
	
}
