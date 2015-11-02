package org.apache.cloudstack.api.command.user.offering;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.BaseCmd.CommandType;
import org.apache.cloudstack.api.response.BandwidthOfferingResponse;
import org.apache.cloudstack.api.response.DiskOfferingResponse;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.log4j.Logger;

@APICommand(name = "listBandwidthOfferings", description="Lists all available Bandwidth offerings.", responseObject=BandwidthOfferingResponse.class)
public class ListBandwidthOfferingsCmd extends BaseListCmd {
	public static final Logger s_logger = Logger.getLogger(ListBandwidthOfferingsCmd.class.getName());

    private static final String s_name = "listbandwidthofferingsresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
	
    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType = BandwidthOfferingResponse.class,
            description="ID of the bandwidth offering")
    private Long id;

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, description="name of the bandwidth offering")
    private String bandwidthOfferingName;
    
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
    
    public Long getId() {
		return id;
	}

	public String getBandwidthOfferingName() {
		return bandwidthOfferingName;
	}
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
    @Override
	public void execute() {

    	ListResponse<BandwidthOfferingResponse> response = _queryService.searchForBandwidthOfferings(this);
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
	}

	@Override
	public String getCommandName() {
		return s_name;
	}
	
}
