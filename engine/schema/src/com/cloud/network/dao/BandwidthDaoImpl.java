package com.cloud.network.dao;

import javax.ejb.Local;

import org.springframework.stereotype.Component;

import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;

@Component
@Local(value = { BandwidthDao.class })
public class BandwidthDaoImpl extends GenericDaoBase<BandwidthVO, Long> implements BandwidthDao {
	private final SearchBuilder<BandwidthVO> bandwidthMultilineIdSearch;
	
	protected BandwidthDaoImpl(){
		bandwidthMultilineIdSearch = createSearchBuilder();
		bandwidthMultilineIdSearch.and("multilineId", bandwidthMultilineIdSearch.entity().getMultilineId(), SearchCriteria.Op.EQ);
		bandwidthMultilineIdSearch.done();
	}
	
	
	@Override
	public BandwidthVO getBandwidthByMultilineId(Long multilineId) {
		SearchCriteria<BandwidthVO> sc = bandwidthMultilineIdSearch.create();
		sc.setParameters("multilineId", multilineId);
		return findOneBy(sc);
	}

}
