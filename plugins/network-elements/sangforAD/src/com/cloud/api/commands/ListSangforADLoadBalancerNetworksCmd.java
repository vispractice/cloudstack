package com.cloud.api.commands;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseListCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.cloudstack.api.response.NetworkResponse;
import org.apache.log4j.Logger;

import com.cloud.api.response.SangforADLoadBalancerResponse;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.Network;
import com.cloud.network.element.SangforADLoadBalancerElementService;
import com.cloud.utils.exception.CloudRuntimeException;

/**
 * @author root
 * @Date 2015/06/12
 * 获取外部网络设备的网络信息列表api入口<br>
 * 1.ApiSerlet获取UI前端api参数并利用反射注入当前对应属性<br>
 * 2.执行获取外部网络设备相关网络操作<br>
 * 3.返回外部网络设备网络相关信息<br>
 */
@APICommand(name = "listSangforADLoadBalancerNetworks", responseObject=NetworkResponse.class, description="lists network that are using SangforAD load balancer device")
public class ListSangforADLoadBalancerNetworksCmd extends BaseListCmd {
	
	public static final Logger s_logger = Logger.getLogger(ListSangforADLoadBalancerNetworksCmd.class.getName());
	private static final String s_name = "listsangforADloadbalancernetworksresponse";
	@Inject
	SangforADLoadBalancerElementService _sangforADlbService;
	
    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
	
	@Parameter(name=ApiConstants.LOAD_BALANCER_DEVICE_ID, type=CommandType.UUID, entityType = SangforADLoadBalancerResponse.class,
            required = true, description="SangforAD load balancer device ID")
    private Long lbDeviceId;
	
	/////////////////////////////////////////////////////
	/////////////////// Accessors ///////////////////////
	/////////////////////////////////////////////////////
	
	public Long getLoadBalancerDeviceId() {
        return lbDeviceId;
    }
	
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
	
	/**
	 * 列出外部网络设备网络相关信息列表<br>
	 * 1.获取外部网络设备
	 * 2.根据设备ID获取相关网络映射
	 * 3.获取设备相关网络
	 * @throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException
	 */
	@Override
	public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException {
		
		 try {
	            List<? extends Network> networks  = _sangforADlbService.listNetworks(this);
	            ListResponse<NetworkResponse> response = new ListResponse<NetworkResponse>();
	            List<NetworkResponse> networkResponses = new ArrayList<NetworkResponse>();

	            if (networks != null && !networks.isEmpty()) {
	                for (Network network : networks) {
	                    NetworkResponse networkResponse = _responseGenerator.createNetworkResponse(network);
	                    networkResponses.add(networkResponse);
	                }
	            }

	            response.setResponses(networkResponses);
	            response.setResponseName(getCommandName());
	            this.setResponseObject(response);
	        }  catch (InvalidParameterValueException invalidParamExcp) {
	            throw new ServerApiException(ApiErrorCode.PARAM_ERROR, invalidParamExcp.getMessage());
	        } catch (CloudRuntimeException runtimeExcp) {
	            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, runtimeExcp.getMessage());
	        }
	}

	@Override
	public String getCommandName() {
		return s_name;
	}

}