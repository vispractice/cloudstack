package com.cloud.api.commands;

import javax.inject.Inject;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseAsyncCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.cloudstack.context.CallContext;
import org.apache.log4j.Logger;
import com.cloud.api.response.SangforADLoadBalancerResponse;
import com.cloud.event.EventTypes;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.NetworkRuleConflictException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.element.SangforADLoadBalancerElementService;
import com.cloud.utils.exception.CloudRuntimeException;
/**
 * @author root
 * @Date 2015/06/12
 * 删除外部网络设备api入口<br>
 * 1.ApiSerlet获取UI前端api参数并利用反射注入当前对应属性<br>
 * 2.执行删除外部网络设备操作<br>
 * 3.返回删除成功设备信息<br>
 */
@APICommand(name = "deleteSangforADLoadBalancer", responseObject=SuccessResponse.class, description=" delete a SangforAD load balancer device")
public class DeleteSangforADLoadBalancerCmd extends BaseAsyncCmd {
	public static final Logger s_logger = Logger.getLogger(DeleteSangforADLoadBalancerCmd.class.getName());
    	private static final String s_name = "deletesangforADloadbalancerresponse";
    	@Inject
      SangforADLoadBalancerElementService _sangforADlbService;

    	/////////////////////////////////////////////////////
        //////////////// API parameters /////////////////////
        /////////////////////////////////////////////////////
    	@Parameter(name=ApiConstants.LOAD_BALANCER_DEVICE_ID, type=CommandType.UUID, entityType = SangforADLoadBalancerResponse.class,
                required=true, description="SangforAD load balancer device ID")
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
      
	@Override
	public String getEventType() {
		return EventTypes.EVENT_LOAD_BALANCER_DELETE;
	}

	@Override
	public String getEventDescription() {
		return "Deleting SangforAD load balancer device";
	}
	/**
	 * 删除外部网络设备<br>
	 * 1.获取负载均衡设备信息
	 * 2.判断设备是否可以删除（不被使用）
	 * 3.先删除主机挂载信息，再删除网络设备
	 * @throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException
	 */
	@Override
	public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException {
		 try {
	            boolean result = _sangforADlbService.deleteSangforADLoadBalancer(this);
	            if (result) {
	                SuccessResponse response = new SuccessResponse(getCommandName());
	                response.setResponseName(getCommandName());
	                this.setResponseObject(response);
	            } else {
	                throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to delete SangforAD load balancer device");
	            }
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

	@Override
	public long getEntityOwnerId() {
		return CallContext.current().getCallingAccount().getId();
	}

}
