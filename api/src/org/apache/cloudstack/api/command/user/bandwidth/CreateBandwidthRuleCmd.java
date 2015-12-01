package org.apache.cloudstack.api.command.user.bandwidth;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseAsyncCreateCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.BandwidthOfferingResponse;
import org.apache.cloudstack.api.response.NetworkResponse;
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.log4j.Logger;

import com.cloud.event.EventTypes;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.Network;

@APICommand(name = "createBandwidthRule", description="create a bandwidth offering.", responseObject=SuccessResponse.class)
public class CreateBandwidthRuleCmd extends BaseAsyncCreateCmd{
	public static final Logger s_logger = Logger.getLogger(CreateBandwidthRuleCmd.class.getName());

    private static final String s_name = "createbandwidthruleresponse";
    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    //network_id, rate, ceil, prio, type bandwidth_offering_id
    @Parameter(name=ApiConstants.NETWORK_ID, type=CommandType.UUID, required=true, entityType=NetworkResponse.class,
            description="The network id which this bandwidth rule was used.")
    private Long networkId;
    
    @Parameter(name=ApiConstants.BANDWIDTH_OFFERING_ID, type=CommandType.UUID, entityType=BandwidthOfferingResponse.class,
            description="The bandwidth offering id which this bandwidth rule was used.")
    private Long bandwidthOfferingId;
    
    @Parameter(name=ApiConstants.BANDWIDTH_RATE, type=CommandType.INTEGER, description="the rate of the bandwidth rule in Kbit.")
    private Integer rate;
	
	@Parameter(name=ApiConstants.BANDWIDTH_CEIL, type=CommandType.INTEGER, description="the ceil of the bandwidth rule in Kbit.")
    private Integer ceil;
	
	@Parameter(name=ApiConstants.BANDWIDTH_PRIO, type=CommandType.INTEGER, required=true, description="the prio of the bandwidth rule.")
    private Integer prio;
	
	@Parameter(name=ApiConstants.BANDWIDTH_TYPE, type=CommandType.STRING, required=true, description="the type of the bandwidth rule. Values are inTraffic and outTraffic")
    private String type;
	
	@Parameter(name=ApiConstants.MULTILINE_LABEL, type=CommandType.STRING,required=true, description="The bandwidth rule in this network which multiline label.")
    private String multilineLabel;
    
	/////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
	public Long getNetworkId() {
		return networkId;
	}
    
	public Long getBandwidthOfferingId() {
		return bandwidthOfferingId;
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
	
    public String getMultilineLabel() {
		return multilineLabel;
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
		if(bandwidthOfferingId == null){
			if(rate < 0 || ceil < 0){
				throw new InvalidParameterValueException("The bandwidth rule parameter: rate, ceil can not less than zore.");
			}
			if(rate > ceil){
				throw new InvalidParameterValueException("The bandwidth rule parameter: rate must  less than or equal ceil.");
			}
		}
		if(prio < 1 || prio > 7){
			throw new InvalidParameterValueException("The bandwidth rule parameter: prio available scope is 1~7.");
		}
		
		Long ruleId = _bandwidthService.createBandwidthRule(this);
		if(ruleId == null){
			throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to create bandwidth rule");
		}
		setEntityId(ruleId);
	}
    
	@Override
	public void execute() throws ResourceUnavailableException{
		boolean result = _bandwidthService.applyBandwidthRule(getEntityId());
		if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
        } else {
        	_bandwidthService.revokeRelatedBandwidthRule(getEntityId());
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to create bandwidth rule");
        }
		
	}
	
	@Override
	public String getEventType() {
		return EventTypes.EVENT_BANDWIDTH_RULE_CREATE;
	}

	@Override
	public String getEventDescription() {
		Network network = _networkService.getNetwork(networkId);
        return ("Creating bandwidth rule for network: " + network);
	}

	@Override
	public String getCommandName() {
		return s_name;
	}

	@Override
	public long getEntityOwnerId() {
		Network network = _networkService.getNetwork(networkId);
        return network.getAccountId();
	}

}
