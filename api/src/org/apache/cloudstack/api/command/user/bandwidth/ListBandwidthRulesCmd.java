package org.apache.cloudstack.api.command.user.bandwidth;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListTaggedResourcesCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.response.BandwidthRulesResponse;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.cloudstack.api.response.NetworkResponse;
import org.apache.log4j.Logger;

@APICommand(name = "listBandwidthRules", description="Lists all bandwidth rules", responseObject=BandwidthRulesResponse.class)
public class ListBandwidthRulesCmd  extends BaseListTaggedResourcesCmd {
    public static final Logger s_logger = Logger.getLogger(ListBandwidthRulesCmd.class.getName());
    private static final String s_name = "listbandwidthrulesresponse";
    
    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType = BandwidthRulesResponse.class,
            description="Lists bandwidth rule with the specified ID.")
    private Long id;
    
    @Parameter(name=ApiConstants.NETWORK_ID, type=CommandType.UUID, entityType = NetworkResponse.class,
            description="list bandwidth rules for ceratin network", since="4.3")
    private Long networkId;
    
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public Long getNetworkId() {
        return networkId;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
    
    @Override
    public void execute(){
        ListResponse<BandwidthRulesResponse> response = _bandwidthService.searchForBandwidthRules(this);
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }

    @Override
    public String getCommandName() {
        return s_name;
    }

}
