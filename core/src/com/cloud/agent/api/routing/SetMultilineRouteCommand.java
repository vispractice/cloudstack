package com.cloud.agent.api.routing;

import java.util.HashMap;
//Andrew ling add, add the multiline route command for the VR
public class SetMultilineRouteCommand extends NetworkElementCommand{
	//In the routeRules HashMap,The store format <multiline label,<gateway;net1,net2,...,nets>> 
	//The store format like :<ctcc,<10.204.120.1; 10.204.104.0/24,10.204.105.0/24>>
	HashMap<String, HashMap<String, String>> routeRules;
	String VRLabelToDefaultGateway;
	
	public String getVRLabelToDefaultGateway() {
		return VRLabelToDefaultGateway;
	}
	public void setVRLabelToDefaultGateway(String vRLabelToDefaultGateway) {
		VRLabelToDefaultGateway = vRLabelToDefaultGateway;
	}
	public SetMultilineRouteCommand(){
		
	}
	public SetMultilineRouteCommand(HashMap<String, HashMap<String, String>> routeRules){
		this.routeRules = routeRules;
	}
	public HashMap<String, HashMap<String, String>> getRouteRules() {
		return routeRules;
	}
	public void setRouteRules(HashMap<String, HashMap<String, String>> routeRules) {
		this.routeRules = routeRules;
	}
}
