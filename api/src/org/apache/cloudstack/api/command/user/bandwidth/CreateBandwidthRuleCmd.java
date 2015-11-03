package org.apache.cloudstack.api.command.user.bandwidth;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseAsyncCreateCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.response.NetworkResponse;
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.cloudstack.context.CallContext;
import org.apache.log4j.Logger;
import com.cloud.event.EventTypes;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.user.Account;

@APICommand(name = "createBandwidthRule", description="create a bandwidth offering.", responseObject=SuccessResponse.class)
public class CreateBandwidthRuleCmd extends BaseAsyncCreateCmd{
	public static final Logger s_logger = Logger.getLogger(CreateBandwidthRuleCmd.class.getName());

    private static final String s_name = "createbandwidthruleresponse";
    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    //network_id, rate, ceil, prio, type
    @Parameter(name=ApiConstants.NETWORK_ID, type=CommandType.UUID, entityType=NetworkResponse.class,
            description="The network id which this bandwidth rule was used.")
    private Long networkId;
    
    @Parameter(name=ApiConstants.BANDWIDTH_RATE, type=CommandType.INTEGER, required=true, description="the rate of the bandwidth rule in Kbit.")
    private Integer rate;
	
	@Parameter(name=ApiConstants.BANDWIDTH_CEIL, type=CommandType.INTEGER, required=true, description="the ceil of the bandwidth rule in Kbit.")
    private Integer ceil;
	
	@Parameter(name=ApiConstants.BANDWIDTH_PRIO, type=CommandType.INTEGER, required=true, description="the prio of the bandwidth rule.")
    private Integer prio;
	
	@Parameter(name=ApiConstants.BANDWIDTH_TYPE, type=CommandType.STRING, required=true, description="the type of the bandwidth rule. Values are inTraffic and outTraffic")
    private String type;
    
	/////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
	public Long getNetworkId() {
		return networkId;
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

	public String getType() {
		return type;
	}
	
    // ///////////////////////////////////////////////////
    // ///////////// API Implementation///////////////////
    // ///////////////////////////////////////////////////	
	private boolean checkType(){
		if(type.equalsIgnoreCase("InTraffic") || type.equalsIgnoreCase("OutTraffic")){
			return true;
		} else {
			return false;
		}
			
	}
	
	@Override
	public void create() throws ResourceAllocationException {
		if(!checkType()){
			throw new InvalidParameterValueException("The bandwidth rule type must be inTraffic or outTraffic.");
		}
		// TODO store to db
		
	}
    
	@Override
	public void execute(){
		// TODO send to agent execute the rule and make it work.
		
	}
	
	@Override
	public String getEventType() {
		return EventTypes.EVENT_BANDWIDTH_RULE_CREATE;
	}

	@Override
	public String getEventDescription() {
		return "creating bandwidth rule:";
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
