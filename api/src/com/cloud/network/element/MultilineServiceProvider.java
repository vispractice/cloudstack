package com.cloud.network.element;

import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.Network;

public interface MultilineServiceProvider extends NetworkElement, IpDeployingRequester {

    boolean updateMultilineRouteLabelRule(final Network network, final String newMutilineLabel, final String vmIpAddress) throws ResourceUnavailableException;
}
