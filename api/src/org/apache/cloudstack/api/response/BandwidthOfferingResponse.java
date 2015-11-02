package org.apache.cloudstack.api.response;

import java.util.Date;

import org.apache.cloudstack.api.BaseResponse;
import org.apache.cloudstack.api.EntityReference;

import com.cloud.offering.BandwidthOffering;
import com.cloud.serializer.Param;
import com.google.gson.annotations.SerializedName;
@EntityReference(value=BandwidthOffering.class)
public class BandwidthOfferingResponse extends BaseResponse {
	//id, name, display_text, rate, ceil, created
	@SerializedName("id") @Param(description="the id of the bandwidth offering")
    private String id;

    @SerializedName("name") @Param(description="the name of the bandwidth offering")
    private String name;

    @SerializedName("displaytext") @Param(description="an alternate display text of the bandwidth offering.")
    private String displayText;
    
    @SerializedName("rate") @Param(description="the rate of the bandwidth offering in Kbit")
    private Integer rate;
    
    @SerializedName("ceil") @Param(description="the ceil of the bandwidth offering in Kbit")
    private Integer ceil;
    
    @SerializedName("created") @Param(description="the date this bandwidth offering was created")
    private Date created;
    
//    @SerializedName("zoneId") @Param(description="the zone ID this bandwidth offering belong to. Ignore this information as it is not must need when all in the same zone.")
//    private String zoneId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public Integer getCeil() {
		return ceil;
	}

	public void setCeil(Integer ceil) {
		this.ceil = ceil;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
