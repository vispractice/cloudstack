package org.apache.cloudstack.api.command.admin.offering;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.BandwidthOfferingResponse;
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.log4j.Logger;
import com.cloud.user.Account;

@APICommand(name = "deleteBandwidthOffering", description="Updates a bandwidth offering.", responseObject=SuccessResponse.class)
public class DeleteBandwidthOfferingCmd extends BaseCmd {
	public static final Logger s_logger = Logger.getLogger(DeleteBandwidthOfferingCmd.class.getName());
    private static final String s_name = "deletebandwidthofferingresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType=BandwidthOfferingResponse.class,
            required=true, description="ID of the bandwidth offering")
    private Long id;


    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }


    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
	
	
	@Override
	public void execute() {
		boolean result = _configService.deleteBandwidthOffering(this);
		if (result) {
			SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
		} else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to delete bandwidth offering");
        }
	}

	@Override
	public String getCommandName() {
		return s_name;
	}

	@Override
	public long getEntityOwnerId() {
		return Account.ACCOUNT_ID_SYSTEM;
	}

}
