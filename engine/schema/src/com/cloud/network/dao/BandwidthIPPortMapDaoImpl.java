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
	
	protected BandwidthIPPortMapDaoImpl(){
		BandwidthRulesIdSearch = createSearchBuilder();
		BandwidthRulesIdSearch.and("bandwidthRulesId", BandwidthRulesIdSearch.entity().getBandwidthRulesId(), SearchCriteria.Op.EQ);
		BandwidthRulesIdSearch.done();
	}

	@Override
	public List<BandwidthIPPortMapVO> listByBandwidthRulesId(long bandwidthRulesId) {
		SearchCriteria<BandwidthIPPortMapVO> sc = BandwidthRulesIdSearch.create();
        sc.setParameters("bandwidthRulesId", bandwidthRulesId);
        return listBy(sc);
	}
}
