package com.cloud.network.dao;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.cloudstack.api.InternalIdentity;

import com.cloud.utils.net.NetUtils;

@Entity
@Table(name="bandwidth_ip_port_map")
public class BandwidthIPPortMapVO implements InternalIdentity {
	//parameter : id, bandwidth_rules_id, ip_address, start_port, end_port
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
	
	@Column(name="uuid")
    private String uuid;
	
	@Column(name="bandwidth_rules_id")
    private Long bandwidthRulesId;

	@Column(name="ip_address")
    private String ipAddress;
	
	@Column(name="protocol")
    String protocol;
	
	@Column(name="start_port")
    private Integer bandwidthPortStart;

    @Column(name="end_port")
    private Integer bandwidthPortEnd;
	
    public BandwidthIPPortMapVO(){
    	uuid = UUID.randomUUID().toString();
    }
    
    public BandwidthIPPortMapVO(long bandwidthRulesId, String ipAddress, String protocol, Integer bandwidthPortStart, Integer bandwidthPortEnd){
    	uuid = UUID.randomUUID().toString();
    	this.bandwidthRulesId = bandwidthRulesId;
    	this.ipAddress = ipAddress;
    	this.protocol = protocol;
    	this.bandwidthPortStart = bandwidthPortStart;
    	this.bandwidthPortEnd = bandwidthPortEnd;
    }
    
	public long getBandwidthRulesId() {
		return bandwidthRulesId;
	}

	public void setBandwidthRulesId(long bandwidthRulesId) {
		this.bandwidthRulesId = bandwidthRulesId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Integer getBandwidthPortStart() {
		return bandwidthPortStart;
	}

	public void setBandwidthPortStart(Integer bandwidthPortStart) {
		this.bandwidthPortStart = bandwidthPortStart;
	}

	public Integer getBandwidthPortEnd() {
		return bandwidthPortEnd;
	}

	public void setBandwidthPortEnd(Integer bandwidthPortEnd) {
		this.bandwidthPortEnd = bandwidthPortEnd;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
