package com.cloud.agent.resource.virtualnetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cloud.exception.InvalidParameterValueException;
/**
 * @author Andrew ling
 * */
public class VirtualRoutingMutilineSetup {
	
    //Andrew Ling add the multiline feature in the VR 
    private static int _multilineNumbers = 0;
    private static Set<String> _allMultilineTableLabels = new HashSet<String>();
    private static Map<String, String> _tableLabelToRouteRules = new HashMap<String, String>();
    private static Map<String, String> _tableLabelGroupToRouteRules = new HashMap<String, String>();
    
    
    private static void sort(List<String> sourceLabels, List<String> targetLabels) {

		for (int i = 1; i <= _multilineNumbers; i++) {
			if (targetLabels.size() == i) {
				String tmpLabel = "";
				for (Object obj : targetLabels) {
					// System.out.print(obj);
					tmpLabel = tmpLabel + obj;
				}
				tmpLabel = tmpLabel.substring(1, tmpLabel.length());
				_allMultilineTableLabels.add(tmpLabel);
//				System.out.println(tmpLabel);
				if (i == _multilineNumbers) {
					return;
				}
			}
		}
		for (int i = 0; i < sourceLabels.size(); i++) {
			List<String> newSourceLabels = new ArrayList<String>(sourceLabels);
			List<String> newTargetLabels = new ArrayList<String>(targetLabels);
			newTargetLabels.add(newSourceLabels.get(i));
			newSourceLabels.remove(i);
			sort(newSourceLabels, newTargetLabels);
		}
	}
    
    public Set<String> getAllMultilineTableLables(int multilineNumbers, String[] labels){
    	_multilineNumbers = multilineNumbers;
    	if(_multilineNumbers == 0 || labels.length == 0){
    		throw new InvalidParameterValueException("You must input the multiline Numbers and lables in the VR when using the multiline feature.");
    	}
    	sort(Arrays.asList(labels), new ArrayList<String>());
    	return _allMultilineTableLabels;
    }
    
    //lable ="CTCC",routeRules="gw-nets"
    public void addTableLabelTORouteRules(String label, String routeRules){
    	_tableLabelToRouteRules.put(label, routeRules);
    }
    
    public Map<String, String> getTableLabelTORouteRules(){
    	return _tableLabelToRouteRules;
    }
    
    //labelGroup == _multilineLabels ="CTCC_CUCC" and so on. labelToRouteRules = label_gw_nets = "CTCC_10.204.120.1_12.235.20.0/24,12.23.10.0/24,"
    public void setTableLabelGroupToRouteRules(String labelGroup, Map<String, String> labelToRouteRules){
    	String[] labels = labelGroup.split("_");
    	String labelGroupToRouteRules = "";
    	String defaultgateway = "";
    	for(int num = 0; num <= labels.length-1; num++){
    		String routeRules;
    		//default route rule
    		if(num == 0){
    			routeRules = labelToRouteRules.get(labels[0]);
    			defaultgateway = "default_" + routeRules.split("_")[0];
    			labelGroupToRouteRules = labelGroupToRouteRules + defaultgateway;
    		}else{
    			routeRules = labelToRouteRules.get(labels[num]);
    			String[] LabelGatewayNets = routeRules.split("_");
    			String gateway = LabelGatewayNets[0];
    			String Nets = LabelGatewayNets[1];
    			labelGroupToRouteRules = labelGroupToRouteRules + "-" + gateway + "_" + Nets; 
    		}
    	}
    	_tableLabelGroupToRouteRules.put(labelGroup, labelGroupToRouteRules);
    }
    
    public Map<String, String> getLabelGroupToRouteRules(){
    	return _tableLabelGroupToRouteRules;
    }
    
    public static void main(String[] args) {
    	
    	String[] datas = new String[] { "_a", "_b", "_c", "_d" };
    	int mutilineNumbers = 4;
//		 sort(Arrays.asList(datas), new ArrayList<String>());
//		 System.out.println("lables == " + multilineLables);
    	VirtualRoutingMutilineSetup virtualRoutingMutilineSetup =new VirtualRoutingMutilineSetup();
    	System.out.println("lables == " + virtualRoutingMutilineSetup.getAllMultilineTableLables(mutilineNumbers,datas));
    	virtualRoutingMutilineSetup.addTableLabelTORouteRules("a", "gw1_net1,net2,net3");
    	virtualRoutingMutilineSetup.addTableLabelTORouteRules("b", "gw2_net1,net2,net3");
    	virtualRoutingMutilineSetup.addTableLabelTORouteRules("c", "gw3_net1,net2,net3");
    	virtualRoutingMutilineSetup.addTableLabelTORouteRules("d", "gw4_net1,net2,net3");
    	Set<String> multilineLabels = virtualRoutingMutilineSetup.getAllMultilineTableLables(mutilineNumbers,datas);
    	//遍历multilineLabels
    	for(String multilineLabel : multilineLabels){
    		virtualRoutingMutilineSetup.setTableLabelGroupToRouteRules(multilineLabel, virtualRoutingMutilineSetup.getTableLabelTORouteRules());
    	}
    	System.out.println("send == " + virtualRoutingMutilineSetup.getLabelGroupToRouteRules());
    }
}
