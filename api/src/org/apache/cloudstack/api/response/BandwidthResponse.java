package org.apache.cloudstack.api.response;

import org.apache.cloudstack.api.BaseResponse;

import com.cloud.serializer.Param;
import com.google.gson.annotations.SerializedName;

public class BandwidthResponse  extends BaseResponse {
	//id, multilineId, dataCenterId, inTraffic, outTraffic
	@SerializedName("id") @Param(description="the id of the bandwidth rule")
    private String id;
	
	@SerializedName("multilineId") @Param(description="the multiline id of the bandwidth")
    private String multilineId;
	
	@SerializedName("zoneId") @Param(description="the zone id of the bandwidth")
    private String zoneId;
	
	@SerializedName("inTraffic") @Param(description="the inTraffic of the bandwidth in Kbit")
	private Integer inTraffic;
	    
	@SerializedName("outTraffic") @Param(description="the outTraffic of the bandwidth in Kbit")
	private Integer outTraffic;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMultilineId() {
		return multilineId;
	}

	public void setMultilineId(String multilineId) {
		this.multilineId = multilineId;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getInTraffic() {
		return inTraffic;
	}

	public void setInTraffic(Integer inTraffic) {
		this.inTraffic = inTraffic;
	}

	public Integer getOutTraffic() {
		return outTraffic;
	}

	public void setOutTraffic(Integer outTraffic) {
		this.outTraffic = outTraffic;
	}
    
}
