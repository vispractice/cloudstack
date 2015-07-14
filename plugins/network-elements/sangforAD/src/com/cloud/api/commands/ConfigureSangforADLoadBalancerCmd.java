package com.cloud.api.commands;

import javax.inject.Inject;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseAsyncCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
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
import com.cloud.network.dao.ExternalLoadBalancerDeviceVO;
import com.cloud.network.element.SangforADLoadBalancerElementService;
import com.cloud.utils.exception.CloudRuntimeException;
/**
 * @author root
 * @Date 2015/06/12
 * 配置外部网络设备api入口<br>
 * 1.ApiSerlet获取UI前端api参数并利用反射注入当前对应属性<br>
 * 2.执行配置外部网络设备操作<br>
 * 3.返回配置网络设备信息<br>
 */
@APICommand(name = "configureSangforADLoadBalancer", responseObject=SangforADLoadBalancerResponse.class, description="Configures a Vispracitce load balancer device")
public class ConfigureSangforADLoadBalancerCmd extends BaseAsyncCmd {

	
	public static final Logger s_logger = Logger.getLogger(ConfigureSangforADLoadBalancerCmd.class.getName());
      private static final String s_name = "configuresangforADloadbalancerresponse";
      @Inject SangforADLoadBalancerElementService _sangforADLBService;
    

      /////////////////////////////////////////////////////
      //////////////// API parameters /////////////////////
      /////////////////////////////////////////////////////

      @Parameter(name=ApiConstants.LOAD_BALANCER_DEVICE_ID, type=CommandType.UUID, entityType = SangforADLoadBalancerResponse.class,
              required=true, description="SangforAD load balancer device ID")
      private Long lbDeviceId;

      @Parameter(name=ApiConstants.LOAD_BALANCER_DEVICE_CAPACITY, type=CommandType.LONG, required=false, description="capacity of the device, Capacity will be interpreted as number of networks device can handle")
      private Long capacity;

      /////////////////////////////////////////////////////
      /////////////////// Accessors ///////////////////////
      /////////////////////////////////////////////////////

      public Long getLoadBalancerDeviceId() {
          return lbDeviceId;
      }

      public Long getLoadBalancerCapacity() {
          return capacity;
      }

      /////////////////////////////////////////////////////
      /////////////// API Implementation///////////////////
      /////////////////////////////////////////////////////
    
	@Override
	public String getEventType() {
		return EventTypes.EVENT_EXTERNAL_LB_DEVICE_CONFIGURE;
	}

	@Override
	public String getEventDescription() {
		return "Configuring a SangforAD load balancer device";
	}

	/**
	 * 执行配置外部网络设备<br>
	 * 1. 启用外部网络设备
	 * 2. 修改配置到数据库
	 * @throws  ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException,	NetworkRuleConflictException
	 */
	@Override
	public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException,
			NetworkRuleConflictException {
		try {
			ExternalLoadBalancerDeviceVO lbDeviceVO = _sangforADLBService.configureSangforADLoadBalancer(this);
            if (lbDeviceVO != null) {
                SangforADLoadBalancerResponse response = _sangforADLBService.createSangforADLoadBalancerResponse(lbDeviceVO);
                response.setObjectName("sangforADloadbalancer");
                response.setResponseName(getCommandName());
                this.setResponseObject(response);
            } else {
                throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to configure SangforAD load balancer device due to internal error.");
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
