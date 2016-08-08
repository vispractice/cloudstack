package org.apache.cloudstack.api.response;

import org.apache.cloudstack.api.BaseResponse;

import com.cloud.serializer.Param;
import com.google.gson.annotations.SerializedName;

public class BandwidthFilterRuleResponse extends BaseResponse {

    @SerializedName("filterRuleId")
    @Param(description = "the id of the bandwidth filter rule")
    private String filterRuleId;

    @SerializedName("ipAddress")
    @Param(description = "the ipAddress of the bandwidth filter rule")
    private String ipAddress;

    @SerializedName("protocol")
    @Param(description = "the protocol of the bandwidth filter rule")
    private String protocol;

    @SerializedName("startPort")
    @Param(description = "the start port of the bandwidth filter rule")
    private Integer startPort;

    @SerializedName("endPort")
    @Param(description = "the end port of the bandwidth filter rule")
    private Integer endPort;

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setStartPort(Integer startPort) {
        this.startPort = startPort;
    }

    public void setEndPort(Integer endPort) {
        this.endPort = endPort;
    }

    public void setFilterRuleId(String filterRuleId) {
        this.filterRuleId = filterRuleId;
    }

}
