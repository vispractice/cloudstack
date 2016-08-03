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
package org.apache.cloudstack.api.response;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseResponse;
import org.apache.cloudstack.api.EntityReference;
import org.apache.cloudstack.api.command.admin.multiline.Multiline;

import com.cloud.dc.Vlan;
import com.cloud.serializer.Param;
import com.google.gson.annotations.SerializedName;

@EntityReference(value=Multiline.class)
@SuppressWarnings("unused")
public class MultilineResponse extends BaseResponse {
    @SerializedName(ApiConstants.ID) @Param(description="the ID of the VLAN IP range")
    private String id;
    
    @SerializedName(ApiConstants.NAME) @Param(description="the multiline name")
    private String name;

    @SerializedName(ApiConstants.MULTILINE_LABEL) @Param(description="the multiline label")
    private String multilineLabel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMultilineLabel() {
		return multilineLabel;
	}

	public void setMultilineLabel(String multilineLabel) {
		this.multilineLabel = multilineLabel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}