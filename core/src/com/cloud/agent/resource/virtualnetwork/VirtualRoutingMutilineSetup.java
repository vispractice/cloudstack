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
    private static int s_multilineNumbers = 0;
    private static int s_tableNumber = 30;
    private static Set<String> s_allMultilineTableLabels = new HashSet<String>();
    private static Map<String, String> s_tableLabelToRouteRules = new HashMap<String, String>();
    private static Map<String, String> s_tableLabelGroupToRouteRules = new HashMap<String, String>();

    //sort the mutiline labels
    private static void sort(List<String> sourceLabels, List<String> targetLabels) {

        for (int i = 1; i <= s_multilineNumbers; i++) {
            if (targetLabels.size() == i) {
                String tmpLabel = "";
                for (Object obj : targetLabels) {
                    tmpLabel = tmpLabel + obj;
                }
                tmpLabel = tmpLabel.substring(1, tmpLabel.length());
                s_allMultilineTableLabels.add(tmpLabel);
                if (i == s_multilineNumbers) {
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

    public Set<String> setAllMultilineTableLables(int multilineNumbers, String[] labels){
        s_multilineNumbers = multilineNumbers;
        if(s_multilineNumbers == 0 || labels.length == 0){
            throw new InvalidParameterValueException("You must input the multiline Numbers and lables in the VR when using the multiline feature.");
        }
        sort(Arrays.asList(labels), new ArrayList<String>());
        return s_allMultilineTableLabels;
    }

    //create route table.   echo \"5 Table_eth5\" >> /etc/iproute2/rt_tables;
    private String setCreateRouteTableLableRulesCmd(Set<String> allMultilineTableLabels){
        String createTableLabelRulesCmd = "";
        int tableNumber = s_tableNumber;
        for(String tableLabel : allMultilineTableLabels){
            createTableLabelRulesCmd += "echo \""+ tableNumber + " " + tableLabel + "\"  >> /etc/iproute2/rt_tables;";
            tableNumber++;
        }
        return createTableLabelRulesCmd;
    }
    public String getCreateRouteTableLableRulesCmd(){
        return setCreateRouteTableLableRulesCmd(s_allMultilineTableLabels);
    }

    //label ="CTCC",routeRules="gw_net1,net2,...,nets"
    public void addTableLabelTORouteRules(String label, String routeRules){
        s_tableLabelToRouteRules.put(label, routeRules);
    }

    public Map<String, String> getTableLabelTORouteRules(){
        return s_tableLabelToRouteRules;
    }
    // route main table rules, labelToRouteRules < label ="CTCC",routeRules="gw_net1,net2,...,nets" >
    //ip route add default via 10.204.120.1
    //ip route add 10.204.104.0/24 via 10.204.119.1
    private String setMainTableToRouteRulesCmd(String vrDefaultLabel, Map<String, String> labelToRouteRules){
        String mainTableRouteRulesCmd = "";
        for(Map.Entry<String, String> labelToRouteRule : labelToRouteRules.entrySet()){
            if(labelToRouteRule.getKey().equals(vrDefaultLabel)){
                mainTableRouteRulesCmd += "ip route add default via " + labelToRouteRule.getValue().split("_")[0] + ";";
            }else{
                String[] gatewayToNets = labelToRouteRule.getValue().split("_");
                String gateway = gatewayToNets[0];
                String nets = gatewayToNets[1];
                String[] netsArray = nets.split(",");
                for(String net : netsArray){
                    mainTableRouteRulesCmd += "ip route add " + net + " via " + gateway + ";";
                }
            }
        }
        return mainTableRouteRulesCmd;
    }

    public String getMainTableToRouteRulesCmd(String vrDefaultLabel){
        return setMainTableToRouteRulesCmd(vrDefaultLabel, s_tableLabelToRouteRules);
    }
    //labelGroup == _multilineLabels ="CTCC_CUCC" and so on. route tables rules, labelToRouteRules <label ="CTCC",routeRules="gw_net1,net2,...,nets">
    //ip route add default via 10.204.120.1 table labelGroup
    //ip route add 10.204.104.0/24 via 10.204.119.1 table labelGroup
    private String setTableLabelGroupToRouteRulesCmd(String labelGroup, Map<String, String> labelToRouteRules){
        String[] labels = labelGroup.split("_");
        String defaultgateway = "";
        String tableLabelGroupToRouteRulesCmd ="";
        for(int num = 0; num <= labels.length-1; num++){
            String routeRules;
            //default route rule
            if(num == 0){
                routeRules = labelToRouteRules.get(labels[0]);
                defaultgateway = routeRules.split("_")[0];
                tableLabelGroupToRouteRulesCmd += "ip route add default via " + defaultgateway + " table " + labelGroup + ";";
            }else{
                routeRules = labelToRouteRules.get(labels[num]);
                String[] LabelGatewayNets = routeRules.split("_");
                String gateway = LabelGatewayNets[0];
                String nets = LabelGatewayNets[1];
                String[] netsArray = nets.split(",");
                for(String net : netsArray){
                    tableLabelGroupToRouteRulesCmd += "ip route add " + net + " via " + gateway + " table " + labelGroup + ";";
                }
            }
        }
        return tableLabelGroupToRouteRulesCmd;
    }

    public String getTableLabelGroupToRouteRulesCmd(){
        String tablesLabelGroupToRouteRulesCmd = "";
        for(String multilineLabel : s_allMultilineTableLabels){
            tablesLabelGroupToRouteRulesCmd += setTableLabelGroupToRouteRulesCmd(multilineLabel, s_tableLabelToRouteRules);
        }
        return tablesLabelGroupToRouteRulesCmd;
    }

    //labelGroup == _multilineLabels ="CTCC_CUCC" and so on. labelToRouteRules = label_gw_nets = "CTCC_10.204.120.1_12.235.20.0/24,12.23.10.0/24,"
    private void setTableLabelGroupToRouteRules(String labelGroup, Map<String, String> labelToRouteRules){
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
        s_tableLabelGroupToRouteRules.put(labelGroup, labelGroupToRouteRules);
    }

    private Map<String, String> getTableLabelGroupToRouteRules(){
        return s_tableLabelGroupToRouteRules;
    }

//    public static void main(String[] args) {
//        String[] datas = new String[] { "_a", "_b", "_c", "_d" };
//        int mutilineNumbers = 4;
//        VirtualRoutingMutilineSetup virtualRoutingMutilineSetup =new VirtualRoutingMutilineSetup();
//        System.out.println("lables == " + virtualRoutingMutilineSetup.setAllMultilineTableLables(mutilineNumbers,datas));
//        virtualRoutingMutilineSetup.addTableLabelTORouteRules("a", "gw1_net1,net2,net3");
//        virtualRoutingMutilineSetup.addTableLabelTORouteRules("b", "gw2_net1,net2,net3");
//        virtualRoutingMutilineSetup.addTableLabelTORouteRules("c", "gw3_net1,net2,net3");
//        virtualRoutingMutilineSetup.addTableLabelTORouteRules("d", "gw4_net1,net2,net3");
//        System.out.println("send1 == " + virtualRoutingMutilineSetup.getMainTableToRouteRulesCmd("a"));
//        System.out.println("send2 == " + virtualRoutingMutilineSetup.getCreateRouteTableLableRulesCmd());
//        System.out.println("send3 == " + virtualRoutingMutilineSetup.getTableLabelGroupToRouteRulesCmd());
//    }
}
