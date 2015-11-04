package com.cloud.network.dao;

import com.cloud.utils.db.GenericDao;

public interface BandwidthDao extends GenericDao<BandwidthVO, Long> {
	BandwidthVO getBandwidthByMultilineId(Long multilineId);
}
