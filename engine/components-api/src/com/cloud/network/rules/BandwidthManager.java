package com.cloud.network.rules;


public interface BandwidthManager{
	//内部中关于带宽的接口
	boolean checkBandwidthCapacity(Integer newRate);
	
}
