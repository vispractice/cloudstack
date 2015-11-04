package org.apache.cloudstack.api.command.user.bandwidth;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseAsyncCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.BandwidthRulesResponse;
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.cloudstack.context.CallContext;
import org.apache.log4j.Logger;

import com.cloud.event.EventTypes;
import com.cloud.user.Account;

@APICommand(name = "deleteBandwidthRule", description="Delete a bandwidth rule", responseObject=SuccessResponse.class)
public class DeleteBandwidthRuleCmd extends BaseAsyncCmd {
	public static final Logger s_logger = Logger.getLogger(DeleteBandwidthRuleCmd.class.getName());
    private static final String s_name = "deletebandwidthruleresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType = BandwidthRulesResponse.class,
            required=true, description="the ID of the bandwidth rule")
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
	public String getEventType() {
		return EventTypes.EVENT_BANDWIDTH_RULE_DELETE;
	}
	
	@Override
	public String getEventDescription() {
		return ("Deleting bandwidth rule id=" + id);
	}

	@Override
	public void execute() {
		CallContext.current().setEventDetails("Bandwidth Rule Id: " + id);
        boolean result = _bandwidthService.deleteBandwidthRule(this);
        if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to delete bandwidth rule");
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
