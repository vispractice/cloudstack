package com.cloud.network.element;

import java.util.List;

import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.Network;
import com.cloud.network.rules.BandwidthRule;

public interface BandwidthServiceProvider extends NetworkElement, IpDeployingRequester {
    /**
     * Apply rules
     *
     * @param network
     * @param rules
     * @return
     * @throws ResourceUnavailableException
     */
    boolean applyBandwidthRules(Network network, List<BandwidthRule> rules) throws ResourceUnavailableException;
}
