// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package org.apache.cloudstack.api.command.user.nat;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseAsyncCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.IPAddressResponse;
import org.apache.cloudstack.api.response.SuccessResponse;
import org.apache.log4j.Logger;
import com.cloud.event.EventTypes;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.NetworkRuleConflictException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.IpAddress;

@APICommand(name = "updateStaticNat", description="update static nat for given ip address", responseObject=SuccessResponse.class)
public class UpdateStaticNatCmd extends BaseAsyncCmd{
    public static final Logger s_logger = Logger.getLogger(UpdateStaticNatCmd.class.getName());

    private static final String s_name = "updateStaticNatresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.IP_ADDRESS_ID, type=CommandType.UUID, entityType = IPAddressResponse.class,
            required=true, description="the public IP address id for which static nat feature is being updateed")
    private Long ipAddressId;

    @Parameter(name=ApiConstants.IS_DEFAULT_STATIC, type=CommandType.BOOLEAN,required=true, description="is the default static nat")
    private Boolean isDefaultStaticNat;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getIpAddress() {
        return ipAddressId;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public String getEventType() {
        return EventTypes.EVENT_UPDATE_STATIC_NAT;
    }

    @Override
    public String getEventDescription() {
        return  ("updatling static nat for ip id=" + ipAddressId);
    }

    @Override
    public long getEntityOwnerId() {
        return _entityMgr.findById(IpAddress.class, ipAddressId).getAccountId();
    }

    @Override
    public void execute() throws ResourceUnavailableException , NetworkRuleConflictException{
        boolean result = _rulesService.updateStaticNat(ipAddressId, isDefaultStaticNat);

        if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            this.setResponseObject(response);
         } else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to update static nat");
        }
    }

    @Override
    public String getSyncObjType() {
        return BaseAsyncCmd.networkSyncObject;
    }

    @Override
    public Long getSyncObjId() {
        return getIp().getAssociatedWithNetworkId();
    }

    private IpAddress getIp() {
        IpAddress ip = _networkService.getIp(ipAddressId);
        if (ip == null) {
            throw new InvalidParameterValueException("Unable to find ip address by id " + ipAddressId);
        }
        return ip;
    }

}