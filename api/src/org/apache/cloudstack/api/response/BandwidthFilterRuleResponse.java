package org.apache.cloudstack.api.response;

import org.apache.cloudstack.api.BaseResponse;

import com.cloud.serializer.Param;
import com.google.gson.annotations.SerializedName;

public class BandwidthFilterRuleResponse extends BaseResponse {
	@SerializedName("ipAddress")
	@Param(description = "the ipAddress of the bandwidth filter rule")
	private String ipAddress;

	@SerializedName("startPort")
	@Param(description = "the start port of the bandwidth filter rule")
	private Integer startPort;

	@SerializedName("endPort")
	@Param(description = "the end port of the bandwidth filter rule")
	private Integer endPort;

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setStartPort(Integer startPort) {
		this.startPort = startPort;
	}

	public void setEndPort(Integer endPort) {
		this.endPort = endPort;
	}

}
