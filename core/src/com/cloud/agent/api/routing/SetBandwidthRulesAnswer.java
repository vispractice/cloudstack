package com.cloud.agent.api.routing;

import com.cloud.agent.api.Answer;

public class SetBandwidthRulesAnswer extends Answer {
    String[] results;
    
    protected SetBandwidthRulesAnswer(){
        
    }
    
    public SetBandwidthRulesAnswer(SetBandwidthRulesCommand cmd, boolean success, String[] results) {
        super(cmd, success, null);
        this.results = results;
    }

    public String[] getResults() {
        return results;
    }

}
