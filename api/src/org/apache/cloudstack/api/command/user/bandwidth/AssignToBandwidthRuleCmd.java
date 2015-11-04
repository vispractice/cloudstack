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

@APICommand(name = "assignToBandwidthRule", description="Assigns filter: ip, startPort and endPort to a bandwidth rule.", responseObject=SuccessResponse.class)
public class AssignToBandwidthRuleCmd extends BaseAsyncCmd {
    public static final Logger s_logger = Logger.getLogger(AssignToBandwidthRuleCmd.class.getName());

    private static final String s_name = "assigntobandwidthruleresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType = BandwidthRulesResponse.class,
            required=true, description="the ID of the bandwidth rule")
    private Long bandwidthRuleId;
    
    @Parameter(name=ApiConstants.BANDWIDTH_RULE_IP, type=CommandType.STRING, required=true, description="the IP address in this bandwidth rule for filter rule.")
    private String ip;
    
    @Parameter(name = ApiConstants.START_PORT, type = CommandType.INTEGER, required=true, description = "the starting port of bandwidth rule for filter rule.")
    private Integer startPort;

    @Parameter(name = ApiConstants.END_PORT, type = CommandType.INTEGER, required=true, description = "the ending port of bandwidth rule for filter rule.")
    private Integer endPort;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

	public Long getBandwidthRuleId() {
		return bandwidthRuleId;
	}

	public String getIp() {
		return ip;
	}

	public Integer getStartPort() {
		return startPort;
	}

	public Integer getEndPort() {
		return endPort;
	}
	
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
	
	@Override
	public String getEventType() {
		return EventTypes.EVENT_ASSIGN_TO_BANDWIDTH_RULE;
	}

	@Override
	public String getEventDescription() {
		return ("Assign bandwidth filter rule: IP="+getIp()+", start port="+getStartPort()+" and end port="+getEndPort()+" to the bandwidth rule id="+ getBandwidthRuleId());
	}

	@Override
	public void execute(){
		CallContext.current().setEventDetails("bandwidth filter rule Id= "+getBandwidthRuleId()+" IP="+getIp()+", start port="+getStartPort()+" and end port="+getEndPort());
		boolean result = _bandwidthService.assignToBandwidthRule(this);
        if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to assign bandwidth filter rule");
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
