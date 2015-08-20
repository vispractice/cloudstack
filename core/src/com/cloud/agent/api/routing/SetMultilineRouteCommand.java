package com.cloud.agent.api.routing;

import java.util.HashMap;

import com.cloud.agent.api.to.NicTO;
//Andrew ling add, add the multiline route command for the VR
public class SetMultilineRouteCommand extends NetworkElementCommand{
	//In this map,key is multilineType,value is default route or not
//	HashMap <String,Boolean> multilineRulesType;
//	NicTO[] nics;
//	public SetMultilineRouteCommand(HashMap <String,Boolean> multilineRulesType){
//		this.multilineRulesType = multilineRulesType;
//	}
//	
//	public NicTO[] getNics() {
//		return nics;
//	}
//
//	public void setNics(NicTO[] nics) {
//		this.nics = nics;
//	}
//
//	public HashMap<String, Boolean> getMultilineRulesType() {
//		return multilineRulesType;
//	}
//
//	public void setMultilineRulesType(HashMap<String, Boolean> multilineRulesType) {
//		this.multilineRulesType = multilineRulesType;
//	}
	
//	String routeRules;
	//In the routeRules HashMap,gateway is key, net and netmask is value. The store format like : <10.204.120.1; 10.204.104.0-255.255.255.0,10.204.105.0-255.255.255.0>
	HashMap<String, String> routeRules;

	protected SetMultilineRouteCommand(){
		
	}
	public SetMultilineRouteCommand(HashMap<String, String> routeRules){
		this.routeRules = routeRules;
	}
	public HashMap<String, String> getRouteRules() {
		return routeRules;
	}
	public void setRouteRules(HashMap<String, String> routeRules) {
		this.routeRules = routeRules;
	}
}
