package org.apache.cloudstack.api.command.admin.offering;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.response.BandwidthOfferingResponse;
import org.apache.log4j.Logger;

import com.cloud.offering.BandwidthOffering;
import com.cloud.user.Account;

@APICommand(name = "updateBandwidthOffering", description="Updates a bandwidth offering.", responseObject=BandwidthOfferingResponse.class)
public class UpdateBandwidthOfferingCmd extends BaseCmd{
	public static final Logger s_logger = Logger.getLogger(UpdateBandwidthOfferingCmd.class.getName());
    private static final String s_name = "updatebandwidthofferingresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.DISPLAY_TEXT, type=CommandType.STRING, description="updates alternate display text of the bandwidth offering with this value", length=4096)
    private String displayText;

    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType=BandwidthOfferingResponse.class,
            required=true, description="ID of the bandwidth offering")
    private Long id;

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, description="updates name of the bandwidth offering with this value")
    private String bandwidthOfferingName;
    
    @Parameter(name=ApiConstants.BANDWIDTH_RATE, type=CommandType.INTEGER, description="the rate of the bandwidth offering in Kbit.")
    private Integer rate;
	
	@Parameter(name=ApiConstants.BANDWIDTH_CEIL, type=CommandType.INTEGER, description="the ceil of the bandwidth offering in Kbit.")
    private Integer ceil;
	
	/////////////////////////////////////////////////////
	/////////////////// Accessors ///////////////////////
	/////////////////////////////////////////////////////

	public String getDisplayText() {
		return displayText;
	}

	public Long getId() {
		return id;
	}

	public String getBandwidthOfferingName() {
		return bandwidthOfferingName;
	}

	public Integer getRate() {
		return rate;
	}

	public Integer getCeil() {
		return ceil;
	}
	
	/////////////////////////////////////////////////////
	/////////////// API Implementation///////////////////
	/////////////////////////////////////////////////////
	
	@Override
	public String getCommandName() {
		return s_name;
	}

	@Override
	public long getEntityOwnerId() {
		return Account.ACCOUNT_ID_SYSTEM;
	}
	
	@Override
	public void execute() {
		BandwidthOffering result = _configService.updateBandwidthOffering(this);
		if (result != null){
			
		}
	}
}
