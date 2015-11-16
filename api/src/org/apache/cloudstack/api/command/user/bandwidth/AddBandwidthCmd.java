package org.apache.cloudstack.api.command.user.bandwidth;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.cloudstack.api.response.ZoneResponse;
import org.apache.cloudstack.context.CallContext;
import org.apache.log4j.Logger;
import com.cloud.user.Account;

@APICommand(name = "addBandwidth",description = "add a bandwidth", responseObject = SuccessResponse.class)
public class AddBandwidthCmd extends BaseCmd{

	public static final Logger s_logger = Logger.getLogger(AddBandwidthCmd.class.getName());

    private static final String s_name = "addbandwidthresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    @Parameter(name=ApiConstants.MULTILINE_ID, type=CommandType.STRING, required=true, description= "the ID of the multiline")
    private String multilineId;
    
    @Parameter(name=ApiConstants.ZONE_ID, type=CommandType.UUID, required=true, entityType=ZoneResponse.class, description= "the ID of the zone")
    private Long zoneId;

    @Parameter(name=ApiConstants.BANDWIDTH_IN, type=CommandType.INTEGER, required=true, description="the in traffic of the bandwidth offering in Kbit.")
    private Integer inTraffic;
	
	@Parameter(name=ApiConstants.BANDWIDTH_OUT, type=CommandType.INTEGER, required=true, description="the out traffic of the bandwidth offering in Kbit.")
    private Integer outTraffic;
	
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
	
	public String getMultilineId() {
		return multilineId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public Integer getInTraffic() {
		return inTraffic;
	}

	public Integer getOutTraffic() {
		return outTraffic;
	}
    
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
	
	@Override
	public void execute() {
		boolean result = _bandwidthService.addBandwidth(this);
		if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to create bandwidth.");
        }
	}

	@Override
	public String getCommandName() {
		return s_name;
	}

	@Override
	public long getEntityOwnerId() {
		Account account = CallContext.current().getCallingAccount();

        if (account != null) {
            return account.getId();
        }

        return Account.ACCOUNT_ID_SYSTEM;
	}

}
