package com.cloud.network.dao;

import java.util.List;

import com.cloud.utils.db.GenericDao;

public interface BandwidthRulesDao extends GenericDao<BandwidthRulesVO, Long>{
	List<BandwidthRulesVO> listByBandwidthOfferingId(long bandwidthOfferingId);
}
