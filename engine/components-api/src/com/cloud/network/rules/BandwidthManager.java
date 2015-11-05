package com.cloud.network.rules;

import java.util.List;

import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.Network;
import com.cloud.offering.BandwidthOffering;


public interface BandwidthManager{
	//内部中关于带宽的接口
	boolean checkBandwidthCapacity(Integer newRate);
	boolean applyBandwidthRules(Network network, List<BandwidthRule> rules) throws ResourceUnavailableException;
	//update operation include remove old rules and create new rules
	boolean updateOfferingRefreshRules(BandwidthOffering newOffering, BandwidthOffering oldOffering);
	
	
	
}
