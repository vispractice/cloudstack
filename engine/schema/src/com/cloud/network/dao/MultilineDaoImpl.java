// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.network.dao;

import java.util.List;

import javax.ejb.Local;

import org.springframework.stereotype.Component;

import com.cloud.utils.db.DB;
import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;
import com.cloud.utils.db.SearchCriteria.Op;
/**
 * 
 * @author add by hai.li
 * @date 2015.08.17
 * @descrption 
 */
@Component
@Local(value = MultilineVO.class)
public class MultilineDaoImpl extends GenericDaoBase<MultilineVO, Long> implements MultilineDao {
    final SearchBuilder<MultilineVO> labelSearch;
    final SearchBuilder<MultilineVO> isDefaultSearch;

    protected MultilineDaoImpl() {
    	super();
    	labelSearch = createSearchBuilder();
    	labelSearch.and("label", labelSearch.entity().getLabel(), Op.EQ);
    	labelSearch.done();
    	
    	isDefaultSearch = createSearchBuilder();
    	isDefaultSearch.and("isDefault", isDefaultSearch.entity().getIsDefault(), Op.EQ);
    	isDefaultSearch.done();
    }
    
	@Override
	public List<MultilineVO> getAllMultiline() {
		  SearchCriteria<MultilineVO> sc = labelSearch.create();
	      return listBy(sc);
	}

	@Override
	public MultilineVO getMultilineByLabel(String label) {
		  SearchCriteria<MultilineVO> sc = labelSearch.create();
	      sc.setParameters("label", label);
	      return findOneBy(sc);
	}

	@Override
	public MultilineVO getDefaultMultiline() {
		  SearchCriteria<MultilineVO> sc = isDefaultSearch.create();
	      sc.setParameters("isDefault", Boolean.TRUE);
	      return findOneBy(sc);
	}

	@Override
	public List<MultilineVO> getNoDefaultMultiline() {
		 SearchCriteria<MultilineVO> sc = isDefaultSearch.create();
	     sc.setParameters("isDefault", Boolean.FALSE);
	     return search(sc, null);
	}
}
