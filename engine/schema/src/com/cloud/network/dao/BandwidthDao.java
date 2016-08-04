package com.cloud.network.dao;

import java.util.List;

import com.cloud.utils.db.GenericDao;

public interface BandwidthDao extends GenericDao<BandwidthVO, Long> {
    BandwidthVO getBandwidthByMultilineId(Long multilineId);
    List<BandwidthVO> listByZoneId(Long zoneId);
}
