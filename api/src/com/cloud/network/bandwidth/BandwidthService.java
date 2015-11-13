package com.cloud.network.bandwidth;

import org.apache.cloudstack.api.command.user.bandwidth.AddBandwidthCmd;
import org.apache.cloudstack.api.command.user.bandwidth.AssignToBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.CreateBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.DeleteBandwidthCmd;
import org.apache.cloudstack.api.command.user.bandwidth.DeleteBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.ListBandwidthRulesCmd;
import org.apache.cloudstack.api.command.user.bandwidth.ListBandwidthsCmd;
import org.apache.cloudstack.api.command.user.bandwidth.RemoveFromBandwidthRuleCmd;
import org.apache.cloudstack.api.command.user.bandwidth.UpdateBandwidthCmd;
import org.apache.cloudstack.api.command.user.bandwidth.UpdateBandwidthRuleCmd;
import org.apache.cloudstack.api.response.BandwidthResponse;
import org.apache.cloudstack.api.response.BandwidthRulesResponse;
import org.apache.cloudstack.api.response.ListResponse;

import com.cloud.exception.ResourceUnavailableException;

public interface BandwidthService {
	//业务层的响应接口，从页面发送过来的请求，内部接收的接口方法
	Long createBandwidthRule(CreateBandwidthRuleCmd cmd);
	boolean applyBandwidthRule(Long ruleId) throws ResourceUnavailableException;
	boolean revokeRelatedBandwidthRule(Long ruleId);
	boolean deleteBandwidthRule(DeleteBandwidthRuleCmd cmd) throws ResourceUnavailableException;
	boolean updateBandwidthRule(UpdateBandwidthRuleCmd cmd) throws ResourceUnavailableException;
	ListResponse<BandwidthRulesResponse> searchForBandwidthRules(ListBandwidthRulesCmd cmd);
	boolean assignToBandwidthRule(AssignToBandwidthRuleCmd cmd) throws ResourceUnavailableException;
	boolean removeFromBandwidthRule(RemoveFromBandwidthRuleCmd cmd) throws ResourceUnavailableException;
	boolean addBandwidth(AddBandwidthCmd cmd);
	boolean deleteBandwidth(DeleteBandwidthCmd cmd);
	boolean updateBandwidth(UpdateBandwidthCmd cmd);
	ListResponse<BandwidthResponse> searchForBandwidths(ListBandwidthsCmd cmd);
	
}
