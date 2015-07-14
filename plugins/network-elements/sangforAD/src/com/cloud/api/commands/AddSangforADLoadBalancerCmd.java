package com.cloud.api.commands;

import javax.inject.Inject;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseAsyncCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.BaseCmd.CommandType;
import org.apache.cloudstack.api.response.PhysicalNetworkResponse;
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
 * 新增的SangforAD负载均衡外部网络设备api入口<br>
 * 1.ApiSerlet获取UI前端api参数并利用反射注入当前对应属性<br>
 * 3.必须参数：物理网络ID，URL（），设备用户名，密码，设备类型 <br>
 * https://10.204.104.165?publicinterface=10.204.104.165&privateinterface=10.204.104.165&numretries=2&lbdevicecapacity=5&lbdevicededicated=true
 * 4.执行添加SangforAD负载均衡外部网络设备操作<br>
 * 5.返回添加SangforAD负载均衡外部网络设备信息<br>
 */
@APICommand(name = "addSangforADLoadBalancer", responseObject = SangforADLoadBalancerResponse.class, description = "Adds a sangforAD load balancer device")
public class AddSangforADLoadBalancerCmd extends BaseAsyncCmd {

	public static final Logger s_logger = Logger.getLogger(AddSangforADLoadBalancerCmd.class.getName());
	private static final String s_name = "addSangforADloadbalancerresponse";
	@Inject
	SangforADLoadBalancerElementService _sangforADLBService;

	// ///////////////////////////////////////////////////
	// ////////////// API parameters /////////////////////
	// ///////////////////////////////////////////////////

	@Parameter(name = ApiConstants.PHYSICAL_NETWORK_ID, type = CommandType.UUID, entityType = PhysicalNetworkResponse.class, required = true, description = "the Physical Network ID")
	private Long physicalNetworkId;

	@Parameter(name = ApiConstants.URL, type = CommandType.STRING, required = true, description = "URL of the sangforAD load balancer appliance.")
	private String url;

	@Parameter(name = ApiConstants.USERNAME, type = CommandType.STRING, required = true, description = "Credentials to reach sangforAD load balancer device")
	private String username;

	@Parameter(name = ApiConstants.PASSWORD, type = CommandType.STRING, required = true, description = "Credentials to reach sangforAD load balancer device")
	private String password;

	 @Parameter(name = ApiConstants.NETWORK_DEVICE_TYPE, type = CommandType.STRING, required = true, description = "supports only  sangforADLoadBalancer")
	 private String deviceType;

	// ///////////////////////////////////////////////////
	// ///////////////// Accessors ///////////////////////
	// ///////////////////////////////////////////////////

	public Long getPhysicalNetworkId() {
		return physicalNetworkId;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDeviceType() {
		return deviceType;
	}

	// ///////////////////////////////////////////////////
	// ///////////// API Implementation///////////////////
	// ///////////////////////////////////////////////////

	@Override
	public String getEventType() {
		return EventTypes.EVENT_EXTERNAL_LB_DEVICE_ADD;
	}

	@Override
	public String getEventDescription() {
		return  "Adding a sangforAD load balancer device";
	}
	
	/**
	 * 新增外部网络设备<br>
	 * 1.添加网络设备：当前参数传入SangforADLBService进行添加网络设备业务处理，返回添加数据库里的所有信息<br>
	 * 2.验证添加设备：判断返回设备信息是否为null（验证设备是否添加成功），若返回为null直接抛异常信息到UI界面，否则返回完整设备信息到UI界面<br>
	 * 3.封装返回信息：添加设备返回信息进行封装，并返回response对象<br>
	 * 4.返回请求：调用父类setResponseObject(Object responseObject)返回封装设备信息
	 */
	@Override
	public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException, NetworkRuleConflictException {
		// TODO Auto-generated method stub
		try {
			ExternalLoadBalancerDeviceVO lbDeviceVO = _sangforADLBService.addSangforADLoadBalancer(this);
			if (lbDeviceVO != null) {
				SangforADLoadBalancerResponse response = _sangforADLBService.createSangforADLoadBalancerResponse(lbDeviceVO);
				response.setObjectName("sangforADloadbalancer");
				response.setResponseName(getCommandName());
				this.setResponseObject(response);
			} else {
				throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to add sangforAD load balancer due to internal error.");
			}
		} catch (InvalidParameterValueException invalidParamExcp) {
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