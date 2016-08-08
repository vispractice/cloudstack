package com.cloud.offering;

import java.util.Date;

import org.apache.cloudstack.acl.InfrastructureEntity;
import org.apache.cloudstack.api.Identity;
import org.apache.cloudstack.api.InternalIdentity;

public interface BandwidthOffering extends InfrastructureEntity, Identity, InternalIdentity {
    public enum BandwidthOfferingState{
        Inactive,
        Active,
    }
    String getUniqueName();

    String getName();

    String getDisplayText();

    Date getCreated();

    /**
     * @return rate in Kbit
     */
    Integer getRate();

    /**
     * @return ceil size in Kbit
     */
    Integer getCeil();

    BandwidthOfferingState getState();

    Long getDataCenterId();
}
