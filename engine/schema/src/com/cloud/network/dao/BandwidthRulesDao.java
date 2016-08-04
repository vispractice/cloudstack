package com.cloud.network.dao;

import java.util.List;

import com.cloud.network.rules.BandwidthClassRule.BandwidthType;
import com.cloud.utils.db.GenericDao;

public interface BandwidthRulesDao extends GenericDao<BandwidthRulesVO, Long>{
    List<BandwidthRulesVO> listByBandwidthOfferingId(long bandwidthOfferingId);
    List<BandwidthRulesVO> listByBandwidthId(long bandwidthId);
    List<BandwidthRulesVO> listByBandwidthIdAndType(long bandwidthId, BandwidthType type);
    List<BandwidthRulesVO> listByNetworksId(long networksId);
}
