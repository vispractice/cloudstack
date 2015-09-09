package com.cloud.agent.api.routing;

import java.util.HashMap;

import com.cloud.agent.api.to.NicTO;
//Andrew ling add, add the multiline route command for the VR
public class SetMultilineRouteCommand extends NetworkElementCommand{
	//In the routeRules HashMap,The store format <multiline label,<gateway,net-netmask>> 
	//The store format like :<ctcc,<10.204.120.1; 10.204.104.0-255.255.255.0,10.204.105.0-255.255.255.0>>
	//The default multiline store format like :<ctcc;<10.204.120.1; 0.0.0.0,10.204.104.0-255.255.255.0,10.204.105.0-255.255.255.0>>
	HashMap<String, HashMap<String, String>> routeRules;

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
