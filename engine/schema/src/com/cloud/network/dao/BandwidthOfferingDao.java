package com.cloud.network.dao;

import java.util.List;

import com.cloud.offering.BandwidthOffering.BandwidthOfferingState;
import com.cloud.utils.db.GenericDao;

public interface BandwidthOfferingDao extends GenericDao<BandwidthOfferingVO, Long> {
    List<BandwidthOfferingVO> listByState(BandwidthOfferingState state);
}
