package org.apache.cloudstack.api.command.user.bandwidth;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseAsyncCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.cloudstack.context.CallContext;
import org.apache.log4j.Logger;

import com.cloud.event.EventTypes;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.user.Account;

@APICommand(name = "removeFromBandwidthRule", description="Removes filter from a bandwidth rule.", responseObject=SuccessResponse.class)
public class RemoveFromBandwidthRuleCmd extends BaseAsyncCmd{
    public static final Logger s_logger = Logger.getLogger(RemoveFromBandwidthRuleCmd.class.getName());

    private static final String s_name = "removefrombandwidthruleresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.ID, type=CommandType.STRING, required=true, description="the ID of this bandwidth rule for filter rule.")
    private String id;
    
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public String getId() {
        return id;
    }
    
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

	@Override
	public String getEventType() {
		return EventTypes.EVENT_REMOVE_FROM_BANDWIDTH_RULE;
	}

	@Override
	public String getEventDescription() {
		return ("Remove bandwidth filter rule by id="+getId());
	}

	@Override
	public void execute() throws ResourceUnavailableException {
		CallContext.current().setEventDetails("bandwidth filter rule Id= " + getId());
		boolean result = _bandwidthService.removeFromBandwidthRule(this);
        if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to remove bandwidth filter rule");
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

        return Account.ACCOUNT_ID_SYSTEM; // no account info given, parent this command to SYSTEM so ERROR events are tracked
	}

}