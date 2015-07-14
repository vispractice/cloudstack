package com.cloud.api.response;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseResponse;
import org.apache.cloudstack.api.EntityReference;

import com.cloud.network.dao.ExternalLoadBalancerDeviceVO;
import com.cloud.serializer.Param;
import com.google.gson.annotations.SerializedName;
/**
 * @author root
 * Date 2015/06/11
 * 响应api返回信息<br>
 */
@EntityReference(value=ExternalLoadBalancerDeviceVO.class)
public class SangforADLoadBalancerResponse extends BaseResponse {
	
    @SerializedName(ApiConstants.LOAD_BALANCER_DEVICE_ID) @Param(description="device id of the SangforAD load balancer")
    private String id;

    @SerializedName(ApiConstants.PHYSICAL_NETWORK_ID) @Param(description="the physical network to which this SangforAD load balancer belongs to")
    private String physicalNetworkId;

    @SerializedName(ApiConstants.PROVIDER) @Param(description="name of the provider")
    private String providerName;

    @SerializedName(ApiConstants.LOAD_BALANCER_DEVICE_NAME) @Param(description="device name")
    private String deviceName;

    @SerializedName(ApiConstants.LOAD_BALANCER_DEVICE_STATE) @Param(description="device state")
    private String deviceState;

    @SerializedName(ApiConstants.LOAD_BALANCER_DEVICE_CAPACITY) @Param(description="device capacity")
    private Long deviceCapacity;

    @SerializedName(ApiConstants.LOAD_BALANCER_DEVICE_DEDICATED) @Param(description="true if device is dedicated for an account")
    private Boolean dedicatedLoadBalancer;

    @SerializedName(ApiConstants.PUBLIC_INTERFACE) @Param(description="the public interface of the load balancer")
    private String publicInterface;

    @SerializedName(ApiConstants.PRIVATE_INTERFACE) @Param(description="the private interface of the load balancer")
    private String privateInterface;

    @SerializedName(ApiConstants.IP_ADDRESS) @Param(description="the management IP address of the external load balancer")
    private String ipAddress;
    
    public void setId(String lbDeviceId) {
        this.id = lbDeviceId;
    }

    public void setPhysicalNetworkId(String physicalNetworkId) {
        this.physicalNetworkId = physicalNetworkId;
    }

    public void setProvider(String provider) {
        this.providerName = provider;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceCapacity(long deviceCapacity) {
        this.deviceCapacity = deviceCapacity;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }

    public void setDedicatedLoadBalancer(boolean isDedicated) {
        this.dedicatedLoadBalancer = isDedicated;
    }

    public void setPublicInterface(String publicInterface) {
        this.publicInterface = publicInterface;
    }

    public void setPrivateInterface(String privateInterface) {
        this.privateInterface = privateInterface;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
}
