package com.cloud.agent.api.routing;

public class MutilineRouteLabelRuleCommand extends NetworkElementCommand {
    //this command is only used by the update default mutiline static nat label
    String mutilineLabel;
    String vmIpAddress;

    public MutilineRouteLabelRuleCommand(){

    }
    public MutilineRouteLabelRuleCommand(String mutilineLabel, String vmIpAddress){
        this.mutilineLabel = mutilineLabel;
        this.vmIpAddress = vmIpAddress;
    }
    public String getMutilineLabel() {
        return mutilineLabel;
    }
    public void setMutilineLabel(String mutilineLabel) {
        this.mutilineLabel = mutilineLabel;
    }
    public String getVmIpAddress() {
        return vmIpAddress;
    }
    public void setVmIpAddress(String vmIpAddress) {
        this.vmIpAddress = vmIpAddress;
    }

}
