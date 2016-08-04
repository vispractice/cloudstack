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
package org.apache.cloudstack.api.command.admin.multiline;

import java.util.ArrayList;
import java.util.List;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.response.DomainResponse;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.cloudstack.api.response.MultilineResponse;
import org.apache.log4j.Logger;

import com.cloud.utils.Pair;

@APICommand(name = "listMultiline", description="Lists multiline label", responseObject=MultilineResponse.class)
public class ListMultilineCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListMultilineCmd.class.getName());

    private static final String s_name = "listmultilineresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType=DomainResponse.class,
            description="List multiline by label ID.")
    private Long id;

    @Parameter(name=ApiConstants.MULTILINE_LABEL, type=CommandType.STRING, description="The multiline label.")
    private String multilineLabel;

    @Parameter(name=ApiConstants.LIST_ALL, type=CommandType.BOOLEAN, description="If set to false, list only default multiline label; if set to true - list all multiline label. Default value is true")
    private Boolean listAll;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMultilineLabel() {
        return multilineLabel;
    }

    public void setMultilineLabel(String multilineLabel) {
        this.multilineLabel = multilineLabel;
    }

    public static String getsName() {
        return s_name;
    }

    public boolean listAll() {
        return listAll == null ? true : listAll;
    }
  
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
       
    @Override
    public String getCommandName() {
        return s_name;
    }
    
    @Override
    public void execute(){
        Pair<List<? extends Multiline>, Integer> result = _mgr.searchForMultiline(this);
        ListResponse<MultilineResponse> response = new ListResponse<MultilineResponse>();
        List<MultilineResponse> multilineResponse = new ArrayList<MultilineResponse>();
        for (Multiline multiline : result.first()) {
            MultilineResponse multilineRes = _responseGenerator.createMultilineResponse(multiline);
            multilineRes.setObjectName("multilines");
            multilineResponse.add(multilineRes);
        }
        response.setResponses(multilineResponse, result.second());
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }
}
