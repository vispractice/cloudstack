package org.apache.cloudstack.api.command.user.bandwidth;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.BandwidthResponse;
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.cloudstack.context.CallContext;
import org.apache.log4j.Logger;

import com.cloud.user.Account;

@APICommand(name = "updateBandwidth",description = "update a bandwidth", responseObject = SuccessResponse.class)
public class UpdateBandwidthCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(UpdateBandwidthCmd.class.getName());

    private static final String s_name = "updatebandwidthresponse";
    // ///////////////////////////////////////////////////
    // ////////////// API parameters /////////////////////
    // ///////////////////////////////////////////////////
    @Parameter(name = ApiConstants.ID, type = CommandType.UUID, entityType = BandwidthResponse.class, required = true, description = "the ID of the bandwidth。")
    private Long bandwidthId;

    @Parameter(name = ApiConstants.BANDWIDTH_IN, type = CommandType.INTEGER, required = true, description = "the in traffic of the bandwidth offering in Kbit.")
    private Integer inTraffic;

    @Parameter(name = ApiConstants.BANDWIDTH_OUT, type = CommandType.INTEGER, required = true, description = "the out traffic of the bandwidth offering in Kbit.")
    private Integer outTraffic;

    // ///////////////////////////////////////////////////
    // ///////////////// Accessors ///////////////////////
    // ///////////////////////////////////////////////////

    public Long getBandwidthId() {
        return bandwidthId;
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
        boolean result = _bandwidthService.updateBandwidth(this);
        if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to update bandwidth.");
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
