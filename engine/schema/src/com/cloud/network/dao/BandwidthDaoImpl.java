package com.cloud.network.dao;

import javax.ejb.Local;

import org.springframework.stereotype.Component;

import com.cloud.utils.db.GenericDaoBase;

@Component
@Local(value = { BandwidthDao.class })
public class BandwidthDaoImpl extends GenericDaoBase<BandwidthVO, Long> implements BandwidthDao {

}
