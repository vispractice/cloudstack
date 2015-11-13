package com.cloud.network.dao;

import java.util.List;

import com.cloud.utils.db.GenericDao;

public interface BandwidthIPPortMapDao extends GenericDao<BandwidthIPPortMapVO, Long> {
	List<BandwidthIPPortMapVO> listByBandwidthRulesId(long bandwidthRulesId);
	BandwidthIPPortMapVO findOneByBWClassIdIpPorts(long bandwidthRulesId, String ip, int startPort, int endPort);
	List<BandwidthIPPortMapVO> listByBWClassIdIp(long bandwidthRulesId, String ip);
}
