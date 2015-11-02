package org.apache.cloudstack.api.command.admin.offering;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.BandwidthOfferingResponse;
import org.apache.cloudstack.api.response.ZoneResponse;
import org.apache.log4j.Logger;
import com.cloud.offering.BandwidthOffering;
import com.cloud.user.Account;

@APICommand(name = "createBandwidthOffering", description="Creates a bandwidth offering.", responseObject=BandwidthOfferingResponse.class)
public class CreateBandwidthOfferingCmd extends BaseCmd{
	//create a bandwidth offering for the bandwidth rule control module.
	//parameter: name, display_text, rate, ceil
	//store the parameters in the DB.
	
	public static final Logger s_logger = Logger.getLogger(CreateBandwidthOfferingCmd.class.getName());
    private static final String _name = "createbandwidthofferingresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
	
    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType=ZoneResponse.class, description= "the ID of the zone")
    private Long zoneId;
    
    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, required=true, description="the name of the bandwidth offering")
    private String bandwidthOfferingName;
	
	@Parameter(name=ApiConstants.DISPLAY_TEXT, type=CommandType.STRING, required=true, description="the display text of the bandwidth offering")
    private String displayText;
	
	@Parameter(name=ApiConstants.BANDWIDTH_RATE, type=CommandType.INTEGER, required=true, description="the rate of the bandwidth offering in Kbit.")
    private Integer rate;
	
	@Parameter(name=ApiConstants.BANDWIDTH_CEIL, type=CommandType.INTEGER, required=true, description="the ceil of the bandwidth offering in Kbit.")
    private Integer ceil;
	
	/////////////////////////////////////////////////////
	/////////////////// Accessors ///////////////////////
	/////////////////////////////////////////////////////
	
	public String getBandwidthOfferingName() {
		return bandwidthOfferingName;
	}

	public String getDisplayText() {
		return displayText;
	}

	public Integer getRate() {
		return rate;
	}

	public Integer getCeil() {
		return ceil;
	}
	
	public Long getZoneId() {
		return zoneId;
	}

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
	
	@Override
	public void execute(){
		BandwidthOffering result = _configService.createBandwidthOffering(this);
		if (result != null){
			//TODO get the response
			BandwidthOfferingResponse response = _responseGenerator.createBandwidthOfferingResponse(result);
            response.setResponseName(getCommandName());
            this.setResponseObject(response);
		} else {
			throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to create bandwidth offering");
		}
		
	}
	
	@Override
	public String getCommandName() {
		return _name;
	}

	@Override
	public long getEntityOwnerId() {
		return Account.ACCOUNT_ID_SYSTEM;
	}
}
