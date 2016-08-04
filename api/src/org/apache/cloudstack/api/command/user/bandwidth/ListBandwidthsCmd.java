package org.apache.cloudstack.api.command.user.bandwidth;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.response.BandwidthResponse;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.cloudstack.api.response.ZoneResponse;
import org.apache.log4j.Logger;

@APICommand(name = "listBandwidths", description="Lists all available Bandwidths in the specific zone.", responseObject=BandwidthResponse.class)
public class ListBandwidthsCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListBandwidthsCmd.class.getName());

    private static final String s_name = "listbandwidthsresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    
    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType = BandwidthResponse.class,
            description="ID of the bandwidth")
    private Long id;
    
    @Parameter(name=ApiConstants.ZONE_ID, type=CommandType.UUID, entityType=ZoneResponse.class, description= "the ID of the zone")
    private Long zoneId;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
    
    public Long getId() {
        return id;
    }

    public Long getZoneId() {
        return zoneId;
    }
    
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
    
    @Override
    public void execute(){
        ListResponse<BandwidthResponse> response = _bandwidthService.searchForBandwidths(this);
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
        
    }

    @Override
    public String getCommandName() {
        return s_name;
    }
}
