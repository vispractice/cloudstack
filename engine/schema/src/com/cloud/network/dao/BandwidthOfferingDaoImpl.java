package com.cloud.network.dao;

import java.util.List;

import javax.ejb.Local;

import org.springframework.stereotype.Component;

import com.cloud.offering.BandwidthOffering.BandwidthOfferingState;
import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;

@Component
@Local(value = { BandwidthOfferingDao.class })
public class BandwidthOfferingDaoImpl  extends GenericDaoBase<BandwidthOfferingVO, Long> implements BandwidthOfferingDao{
    private final SearchBuilder<BandwidthOfferingVO> StateSearch;
    
    protected BandwidthOfferingDaoImpl(){
        StateSearch = createSearchBuilder();
        StateSearch.and("state", StateSearch.entity().getState(), SearchCriteria.Op.EQ);
        StateSearch.and("removed", StateSearch.entity().getRemoved(), SearchCriteria.Op.NULL);
        StateSearch.done();
    }
    @Override
    public List<BandwidthOfferingVO> listByState(BandwidthOfferingState state) {
        SearchCriteria<BandwidthOfferingVO> sc = StateSearch.create();
        sc.setParameters("state", state);
        return listBy(sc);
    }
    

}
