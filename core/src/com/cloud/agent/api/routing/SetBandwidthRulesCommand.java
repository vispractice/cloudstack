package com.cloud.agent.api.routing;

import java.util.List;

import com.cloud.agent.api.to.BandwidthRuleTO;

public class SetBandwidthRulesCommand extends NetworkElementCommand {

    BandwidthRuleTO[] rules;

    protected SetBandwidthRulesCommand(){

    }

    public SetBandwidthRulesCommand(List <? extends BandwidthRuleTO> bandwidthRules){
        rules = new BandwidthRuleTO[bandwidthRules.size()];
        int i = 0;
        for(BandwidthRuleTO rule : bandwidthRules){
            rules[i++] = rule;
        }
    }

    public BandwidthRuleTO[] getRules() {
        return rules;
    }

}
