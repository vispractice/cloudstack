package com.cloud.network.dao;

import java.util.List;

import javax.ejb.Local;

import org.springframework.stereotype.Component;

import com.cloud.network.rules.BandwidthClassRule.BandwidthType;
import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;

@Component
@Local(value = { BandwidthRulesDao.class })
public class BandwidthRulesDaoImpl extends GenericDaoBase<BandwidthRulesVO, Long> implements BandwidthRulesDao {
    private final SearchBuilder<BandwidthRulesVO> bandwidthOfferingIdSearch;
    private final SearchBuilder<BandwidthRulesVO> bandwidthIdAndTypeSearch;
    private final SearchBuilder<BandwidthRulesVO> NetworksIdSearch;
    private final SearchBuilder<BandwidthRulesVO> bandwidthIdSearch;
    
    protected BandwidthRulesDaoImpl(){
        bandwidthOfferingIdSearch = createSearchBuilder();
        bandwidthOfferingIdSearch.and("bandwidthOfferingId", bandwidthOfferingIdSearch.entity().getBandwidthOfferingId(), SearchCriteria.Op.EQ);
        bandwidthOfferingIdSearch.done();
        
        bandwidthIdAndTypeSearch = createSearchBuilder();
        bandwidthIdAndTypeSearch.and("bandwidthId", bandwidthIdAndTypeSearch.entity().getBandwidthId(), SearchCriteria.Op.EQ);
        bandwidthIdAndTypeSearch.and("type", bandwidthIdAndTypeSearch.entity().getType(), SearchCriteria.Op.EQ);
        bandwidthIdAndTypeSearch.done();
        
        NetworksIdSearch = createSearchBuilder();
        NetworksIdSearch.and("networksId", NetworksIdSearch.entity().getNetworksId(), SearchCriteria.Op.EQ);
        NetworksIdSearch.done();
        
        bandwidthIdSearch = createSearchBuilder();
        bandwidthIdSearch.and("bandwidthId", bandwidthIdSearch.entity().getBandwidthId(), SearchCriteria.Op.EQ);
        bandwidthIdSearch.done();
    }
    
    @Override
    public List<BandwidthRulesVO> listByBandwidthOfferingId(long bandwidthOfferingId) {
        SearchCriteria<BandwidthRulesVO> sc = bandwidthOfferingIdSearch.create();
        sc.setParameters("bandwidthOfferingId", bandwidthOfferingId);
        return listBy(sc);
    }

    @Override
    public List<BandwidthRulesVO> listByBandwidthIdAndType(long bandwidthId, BandwidthType type) {
        SearchCriteria<BandwidthRulesVO> sc = bandwidthIdAndTypeSearch.create();
        sc.setParameters("bandwidthId", bandwidthId);
        sc.setParameters("type", type.toString());
        return listBy(sc);
    }

    @Override
    public List<BandwidthRulesVO> listByNetworksId(long networksId) {
        SearchCriteria<BandwidthRulesVO> sc = NetworksIdSearch.create();
        sc.setParameters("networksId", networksId);
        return listBy(sc);
    }

    @Override
    public List<BandwidthRulesVO> listByBandwidthId(long bandwidthId) {
        SearchCriteria<BandwidthRulesVO> sc = bandwidthIdSearch.create();
        sc.setParameters("bandwidthId", bandwidthId);
        return listBy(sc);
    }

}
