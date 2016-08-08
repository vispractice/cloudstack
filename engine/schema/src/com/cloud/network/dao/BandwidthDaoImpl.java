package com.cloud.network.dao;

import java.util.List;

import javax.ejb.Local;

import org.springframework.stereotype.Component;

import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;

@Component
@Local(value = { BandwidthDao.class })
public class BandwidthDaoImpl extends GenericDaoBase<BandwidthVO, Long> implements BandwidthDao {
    private final SearchBuilder<BandwidthVO> bandwidthMultilineIdSearch;
    private final SearchBuilder<BandwidthVO> bandwidthZoneIdSearch;

    protected BandwidthDaoImpl(){
        bandwidthMultilineIdSearch = createSearchBuilder();
        bandwidthMultilineIdSearch.and("multilineId", bandwidthMultilineIdSearch.entity().getMultilineId(), SearchCriteria.Op.EQ);
        bandwidthMultilineIdSearch.done();
        bandwidthZoneIdSearch = createSearchBuilder();
        bandwidthZoneIdSearch.and("dataCenterId", bandwidthZoneIdSearch.entity().getDataCenterId(), SearchCriteria.Op.EQ);
        bandwidthZoneIdSearch.done();

    }


    @Override
    public BandwidthVO getBandwidthByMultilineId(Long multilineId) {
        SearchCriteria<BandwidthVO> sc = bandwidthMultilineIdSearch.create();
        sc.setParameters("multilineId", multilineId);
        return findOneBy(sc);
    }


    @Override
    public List<BandwidthVO> listByZoneId(Long zoneId) {
        SearchCriteria<BandwidthVO> sc = bandwidthZoneIdSearch.create();
        sc.setParameters("dataCenterId", zoneId);
        return listBy(sc);
    }
}
