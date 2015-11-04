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

@APICommand(name = "updateBandwidthRule", description="Update a bandwidth rule", responseObject=SuccessResponse.class)
public class UpdateBandwidthRuleCmd extends BaseAsyncCmd {
	public static final Logger s_logger = Logger.getLogger(UpdateBandwidthRuleCmd.class.getName());
    private static final String s_name = "updatebandwidthruleresponse";
    
    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    @Parameter(name=ApiConstants.ID, type=CommandType.UUID,  required=true, entityType=BandwidthRulesResponse.class, description="list by network id")
    private Long bandwidthRuleId;
    
    @Parameter(name=ApiConstants.BANDWIDTH_RATE, type=CommandType.INTEGER, description="the rate of the bandwidth rule in Kbit.")
    private Integer rate;
	
	@Parameter(name=ApiConstants.BANDWIDTH_CEIL, type=CommandType.INTEGER, description="the ceil of the bandwidth rule in Kbit.")
    private Integer ceil;
	
	@Parameter(name=ApiConstants.BANDWIDTH_PRIO, type=CommandType.INTEGER, description="the prio of the bandwidth rule.")
    private Integer prio;
	
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

	public Long getBandwidthRuleId() {
		return bandwidthRuleId;
	}

	public Integer getRate() {
		return rate;
	}

	public Integer getCeil() {
		return ceil;
	}

	public Integer getPrio() {
		return prio;
	}
	
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
	@Override
	public String getEventType() {
		return EventTypes.EVENT_BANDWIDTH_RULE_EDIT;
	}


	@Override
	public String getEventDescription() {
		return ("Update the bandwidth rule id=" + bandwidthRuleId);
	}

	@Override
	public void execute() {
		CallContext.current().setEventDetails("Bandwidth Rule Id: " + bandwidthRuleId);
      boolean result = _bandwidthService.updateBandwidthRule(this);
      if (result) {
          SuccessResponse response = new SuccessResponse(getCommandName());
          this.setResponseObject(response);
      } else {
          throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to update bandwidth rule");
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
