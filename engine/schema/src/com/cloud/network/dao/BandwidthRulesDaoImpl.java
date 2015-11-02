package com.cloud.network.dao;

import java.util.List;

import javax.ejb.Local;

import org.springframework.stereotype.Component;

import com.cloud.storage.DiskOfferingVO;
import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;

@Component
@Local(value = { BandwidthRulesDao.class })
public class BandwidthRulesDaoImpl extends GenericDaoBase<BandwidthRulesVO, Long> implements BandwidthRulesDao {
	private final SearchBuilder<BandwidthRulesVO> bandwidthOfferingIdSearch;
	
	protected BandwidthRulesDaoImpl(){
		bandwidthOfferingIdSearch = createSearchBuilder();
		bandwidthOfferingIdSearch.and("bandwidthOfferingId", bandwidthOfferingIdSearch.entity().getBandwidthOfferingId(), SearchCriteria.Op.EQ);
		bandwidthOfferingIdSearch.done();
	}
	
	@Override
	public List<BandwidthRulesVO> listByBandwidthOfferingId(long bandwidthOfferingId) {
		SearchCriteria<BandwidthRulesVO> sc = bandwidthOfferingIdSearch.create();
		sc.setParameters("bandwidthOfferingId", bandwidthOfferingId);
		return listBy(sc);
	}

}
