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
import org.apache.cloudstack.api.response.PhysicalNetworkResponse;
import org.apache.log4j.Logger;
import com.cloud.api.response.SangforADLoadBalancerResponse;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.NetworkRuleConflictException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.dao.ExternalLoadBalancerDeviceVO;
import com.cloud.network.element.SangforADLoadBalancerElementService;
import com.cloud.utils.exception.CloudRuntimeException;
/**
 * @author root
 * @Date 2015/06/12
 * 获取外部网络设备信息列表api入口<br>
 * 1.ApiSerlet获取UI前端api参数并利用反射注入当前对应属性<br>
 * 2.执行获取外部网络设备列表操作<br>
 * 3.返回外部网络设备列表信息<br>
 */
@APICommand(name = "listSangforADLoadBalanvers", responseObject=SangforADLoadBalancerResponse.class, description="lists SangforAD load balancer devices")
public class ListSangforADLoadBalancersCmd extends BaseListCmd{
	
	public static final Logger s_logger = Logger.getLogger(ListSangforADLoadBalancersCmd.class.getName());
	private static final String s_name = "listsangforADloadbalancerresponse";
	@Inject SangforADLoadBalancerElementService _sangforADLBService;
	
    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.PHYSICAL_NETWORK_ID, type=CommandType.UUID, entityType = PhysicalNetworkResponse.class,
            description="the Physical Network ID")
    private Long physicalNetworkId;

    @Parameter(name=ApiConstants.LOAD_BALANCER_DEVICE_ID, type=CommandType.UUID, entityType = SangforADLoadBalancerResponse.class,
            description="SangforAD load balancer device ID")
    private Long lbDeviceId;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
    
    public Long getLoadBalancerDeviceId() {
        return lbDeviceId;
    }

    public Long getPhysicalNetworkId() {
        return physicalNetworkId;
    }
    
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
    
    /**
	 * 列出外部网络设备列表<br>
	 *  1.根据外部网络设备ID 获取列表信息
	 * @throws  ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException
	 */
	@Override
	public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException {
		try {
            List<ExternalLoadBalancerDeviceVO> lbDevices = _sangforADLBService.listSangforADLoadBalancers(this);
            ListResponse<SangforADLoadBalancerResponse> response = new ListResponse<SangforADLoadBalancerResponse>();
            List<SangforADLoadBalancerResponse> lbDevicesResponse = new ArrayList<SangforADLoadBalancerResponse>();

            if (lbDevices != null && !lbDevices.isEmpty()) {
                for (ExternalLoadBalancerDeviceVO lbDeviceVO : lbDevices) {
                	  SangforADLoadBalancerResponse lbdeviceResponse = _sangforADLBService.createSangforADLoadBalancerResponse(lbDeviceVO);
                    lbDevicesResponse.add(lbdeviceResponse);
                }
            }

            response.setResponses(lbDevicesResponse);
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
