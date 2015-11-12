package com.cloud.network.dao;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cloud.offering.BandwidthOffering;
import com.cloud.utils.db.GenericDao;

@Entity
@Table(name="bandwidth_offering")
public class BandwidthOfferingVO implements BandwidthOffering{
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
	
	@Column(name="uuid")
    private String uuid;
	
	@Column(name="data_center_id")
    private Long dataCenterId;
	
	@Column(name = "unique_name")
    private String uniqueName;

    @Column(name = "name")
    private String name = null;

    @Column(name = "display_text", length = 4096)
    private String displayText = null;
    
    @Column(name="rate")
    private Integer rate;

    @Column(name="ceil")
    private Integer ceil;
    
    @Column(name = GenericDao.REMOVED_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    private Date removed;

    @Column(name = GenericDao.CREATED_COLUMN)
    private Date created;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    BandwidthOfferingState state;
    
    public BandwidthOfferingVO(){
    	uuid = UUID.randomUUID().toString();
    }
	
    public BandwidthOfferingVO(Long dataCenterId, String name, String displayText, Integer rate, Integer ceil){
    	this.dataCenterId = dataCenterId;
    	this.name = name;
    	this.displayText = displayText;
    	this.rate = rate;
    	this.ceil = ceil;
    	uuid = UUID.randomUUID().toString();
    	state = BandwidthOfferingState.Active;
    }
    
	public Date getRemoved() {
		return removed;
	}

	public void setRemoved(Date removed) {
		this.removed = removed;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public long getId() {
		return id;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	@Override
	public String getUniqueName() {
		return uniqueName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	
	@Override
	public String getDisplayText() {
		return displayText;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	
	@Override
	public Integer getRate() {
		return rate;
	}

	public void setCeil(Integer ceil) {
		this.ceil = ceil;
	}
	
	@Override
	public Integer getCeil() {
		return ceil;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public Date getCreated() {
		return created;
	}
    
	@Override
	public BandwidthOfferingState getState() {
		return state;
	}

	public void setState(BandwidthOfferingState state) {
		this.state = state;
	}
    
	@Override
	public Long getDataCenterId() {
		return dataCenterId;
	}

	public void setDataCenterId(Long dateCenterId) {
		this.dataCenterId = dateCenterId;
	}
	
}
