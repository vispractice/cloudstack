/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.cloud.hypervisor.kvm.resource;

import java.io.File;

import com.cloud.utils.script.Script;

public class OvsController {
	
	public void addFlow(String brName, String flow){
		Script.runSimpleBashScript("ovs-ofctl add-flow " +  flow);
	}
	
	public void addFlows(String brName, File flowFile){
		
	}
	
	public void delFlow(String brName, String flow){
		Script.runSimpleBashScript("ovs-ofctl del-flow " +  flow);
	}
	
	public void delFlows(String brName){
		
	}
	
	public String delIpSpoofingRules(String brName){
		return null;
	}
	
	public String addIpSpoofingProtectForMac(String brName, String ip, String mac){
		Script.runSimpleBashScript(addIpSpoofingPassFlow(brName, ip, mac));
		return Script.runSimpleBashScript(addIpSpoofingDenyFlow(brName, mac));
	}
	
	public String delIpSpoofingProtectForMac(String brName, String ip, String mac){
		return "";
	}
	
	public String addIpSpoofingPassFlow(String brName, String ip, String mac) {
		return "ovs-ofctl add-flow " + brName + " \"dl_src=" + mac
				+ " priority=39000 dl_type=0x0800 nw_src=" + ip
				+ " idle_timeout=0 action=normal\"";
	}

	public String addIpSpoofingDenyFlow(String brName, String mac) {
		return "ovs-ofctl add-flow " + brName + " \"dl_src=" + mac
				+ " priority=39000 dl_type=0x0800 nw_src=ANY"
				+ " idle_timeout=0 action=normal\"";
	}
	
}
