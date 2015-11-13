package com.cloud.network.dao;

import java.util.List;

import javax.ejb.Local;

import org.springframework.stereotype.Component;

import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;

@Component
@Local(value = { BandwidthIPPortMapDao.class })
public class BandwidthIPPortMapDaoImpl extends GenericDaoBase<BandwidthIPPortMapVO, Long> implements BandwidthIPPortMapDao {
	private final SearchBuilder<BandwidthIPPortMapVO> BandwidthRulesIdSearch;
	private final SearchBuilder<BandwidthIPPortMapVO> AllFilterSearch;
	private final SearchBuilder<BandwidthIPPortMapVO> BandwidthRulesIdIpSearch;
	
	protected BandwidthIPPortMapDaoImpl(){
		BandwidthRulesIdSearch = createSearchBuilder();
		BandwidthRulesIdSearch.and("bandwidthRulesId", BandwidthRulesIdSearch.entity().getBandwidthRulesId(), SearchCriteria.Op.EQ);
		BandwidthRulesIdSearch.done();
		
		AllFilterSearch = createSearchBuilder();
		AllFilterSearch.and("bandwidthRulesId", AllFilterSearch.entity().getBandwidthRulesId(), SearchCriteria.Op.EQ);
		AllFilterSearch.and("ipAddress", AllFilterSearch.entity().getIpAddress(), SearchCriteria.Op.EQ);
		AllFilterSearch.and("bandwidthPortStart", AllFilterSearch.entity().getBandwidthPortStart(), SearchCriteria.Op.EQ);
		AllFilterSearch.and("bandwidthPortEnd", AllFilterSearch.entity().getBandwidthPortEnd(), SearchCriteria.Op.EQ);
		AllFilterSearch.done();
		
		BandwidthRulesIdIpSearch = createSearchBuilder();
		BandwidthRulesIdIpSearch.and("bandwidthRulesId", BandwidthRulesIdIpSearch.entity().getBandwidthRulesId(), SearchCriteria.Op.EQ);
		BandwidthRulesIdIpSearch.and("ipAddress", BandwidthRulesIdIpSearch.entity().getIpAddress(), SearchCriteria.Op.EQ);
		BandwidthRulesIdIpSearch.done();
		
	}

	@Override
	public List<BandwidthIPPortMapVO> listByBandwidthRulesId(long bandwidthRulesId) {
		SearchCriteria<BandwidthIPPortMapVO> sc = BandwidthRulesIdSearch.create();
        sc.setParameters("bandwidthRulesId", bandwidthRulesId);
        return listBy(sc);
	}

	@Override
	public BandwidthIPPortMapVO findOneByBWClassIdIpPorts(long bandwidthRulesId, String ip, int startPort, int endPort) {
		SearchCriteria<BandwidthIPPortMapVO> sc = AllFilterSearch.create();
        sc.setParameters("bandwidthRulesId", bandwidthRulesId);
        sc.setParameters("ipAddress", ip);
        sc.setParameters("bandwidthPortStart", startPort);
        sc.setParameters("bandwidthPortEnd", endPort);
		return findOneBy(sc);
	}

	@Override
	public List<BandwidthIPPortMapVO> listByBWClassIdIp(long bandwidthRulesId, String ip) {
		SearchCriteria<BandwidthIPPortMapVO> sc = BandwidthRulesIdIpSearch.create();
        sc.setParameters("bandwidthRulesId", bandwidthRulesId);
        sc.setParameters("ipAddress", ip);
		return listBy(sc);
	}
}
