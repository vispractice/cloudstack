package com.cloud.network.dao;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.cloud.network.rules.Bandwidth;

@Entity
@Table(name="bandwidth")
public class BandwidthVO implements Bandwidth{
    //bandwidth(uuid,id,multiline_id,date_center_id, in_traffic, out_traffic)
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
    
    @Column(name="uuid")
    private String uuid;
    
    @Column(name="multiline_id")
    private Long multilineId;
    
    @Column(name="data_center_id")
    private Long dataCenterId;
    
    @Column(name="in_traffic")
    private Integer inTraffic;

    @Column(name="out_traffic")
    private Integer outTraffic;
    
    public BandwidthVO(){
        uuid = UUID.randomUUID().toString();
    }
    
    public BandwidthVO(Long multilineId, Long dataCenterId, Integer inTraffic, Integer outTraffic){
        uuid = UUID.randomUUID().toString();
        this.multilineId = multilineId;
        this.dataCenterId = dataCenterId;
        this.inTraffic = inTraffic;
        this.outTraffic = outTraffic;
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

    public Long getMultilineId() {
        return multilineId;
    }

    public void setMultilineId(Long multilineId) {
        this.multilineId = multilineId;
    }

    public Long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public Integer getInTraffic() {
        return inTraffic;
    }

    public void setInTraffic(Integer inTraffic) {
        this.inTraffic = inTraffic;
    }

    public Integer getOutTraffic() {
        return outTraffic;
    }

    public void setOutTraffic(Integer outTraffic) {
        this.outTraffic = outTraffic;
    }
}
