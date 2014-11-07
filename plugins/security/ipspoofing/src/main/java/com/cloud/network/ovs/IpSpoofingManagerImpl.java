// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.network.ovs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.naming.ConfigurationException;

import org.apache.cloudstack.engine.orchestration.service.NetworkOrchestrationService;
import org.apache.log4j.Logger;

import com.cloud.agent.AgentManager;
import com.cloud.agent.Listener;
import com.cloud.agent.api.AgentControlAnswer;
import com.cloud.agent.api.AgentControlCommand;
import com.cloud.agent.api.Answer;
import com.cloud.agent.api.Command;
import com.cloud.agent.api.StartupCommand;
import com.cloud.agent.api.StartupRoutingCommand;
import com.cloud.agent.api.security.SetupIpSpoofingAnswer;
import com.cloud.agent.api.security.SetupIpSpoofingCommand;
import com.cloud.agent.api.security.TeardownIpSpoofingAnswer;
import com.cloud.agent.api.security.TeardownIpSpoofingCommand;
import com.cloud.agent.api.to.VirtualMachineTO;
import com.cloud.exception.AgentUnavailableException;
import com.cloud.exception.ConnectionException;
import com.cloud.exception.OperationTimedoutException;
import com.cloud.host.Host;
import com.cloud.host.Status;
import com.cloud.service.dao.ServiceOfferingDao;
import com.cloud.utils.component.ManagerBase;
import com.cloud.vm.NicProfile;
import com.cloud.vm.VMInstanceVO;
import com.cloud.vm.VirtualMachine;
import com.cloud.vm.VirtualMachineManager;
import com.cloud.vm.VirtualMachineProfile;
import com.cloud.vm.VirtualMachineProfileImpl;
import com.cloud.vm.dao.VMInstanceDao;

public class IpSpoofingManagerImpl extends ManagerBase implements IpSpoofingManager,Listener {
	private static final Logger s_logger = Logger.getLogger(IpSpoofingManagerImpl.class);
    @Inject
    AgentManager _agentMgr;
    @Inject
    VMInstanceDao _vmDao;
    @Inject
    NetworkOrchestrationService _networkMgr;
    @Inject
    ServiceOfferingDao _offeringDao;
    @Inject
    VirtualMachineManager _itMgr;
    
	private int eventHook = -1;
	private boolean ipspoofingEnabled = false;

	@Override
	public boolean configure(String name, Map<String, Object> params)
			throws ConfigurationException {
		super.configure(name, params);
		
		String value = (String)params.get("ovs.ipspoofing.protect");
		ipspoofingEnabled = Boolean.parseBoolean(value);
		
		return true;
	}

	@Override
	public boolean processAnswers(long agentId, long seq, Answer[] answers) {
		if(answers != null){
			for(Answer answer:answers){
				if(answer instanceof SetupIpSpoofingAnswer){
					s_logger.debug("received setup ip spoofing answer " + answer);
				}else if(answer instanceof TeardownIpSpoofingAnswer){
					s_logger.debug("received tear down ip spoofing answer " + answer);
				}
			}
		}
		return false;
	}

	@Override
	public boolean processCommands(long agentId, long seq, Command[] commands) {
		return false;
	}

	@Override
	public AgentControlAnswer processControlCommand(long agentId,
			AgentControlCommand cmd) {
		return null;
	}

	@Override
	public void processConnect(Host host, StartupCommand cmd,
			boolean forRebalance) throws ConnectionException {
		
		if(cmd instanceof StartupRoutingCommand){
			try {
				if(ipspoofingEnabled){
					_agentMgr.send(host.getId(), genSetupIpSpoofingCommand(host.getId()));
				}else{
					_agentMgr.send(host.getId(), genTeardownIpSpoofingCommand(host.getId()));
				}
			} catch (AgentUnavailableException e) {
				s_logger.error("", e);
			} catch (OperationTimedoutException e) {
				s_logger.error("", e);
			}
		}
	}
	
	private SetupIpSpoofingCommand genSetupIpSpoofingCommand(long hostId){
		SetupIpSpoofingCommand cmd = new SetupIpSpoofingCommand();
		List<VMInstanceVO> vmVOs = _vmDao.listByHostId(hostId);
		cmd.setVms(toVmTO(vmVOs));
		return cmd;
	}
	
	private TeardownIpSpoofingCommand genTeardownIpSpoofingCommand(long hostId){
		TeardownIpSpoofingCommand cmd = new TeardownIpSpoofingCommand();
		List<VMInstanceVO> vmVOs = _vmDao.listByHostId(hostId);
		cmd.setVms(toVmTO(vmVOs));
		return cmd;
	}
	
	private List<VirtualMachineTO> toVmTO(List<VMInstanceVO> vmVOs){
		if(vmVOs != null && !vmVOs.isEmpty()){
			List<VirtualMachineTO> tos = new ArrayList<VirtualMachineTO>(vmVOs.size());
			for(VirtualMachine vm:vmVOs){
				tos.add(toVmTO(vm));
			}
			return tos;
		}
		
		return null;
	}
	
	private VirtualMachineTO toVmTO(VirtualMachine vm){
		VirtualMachineProfile profile = 
				new VirtualMachineProfileImpl(vm, null, _offeringDao.findById(vm.getId(), vm.getServiceOfferingId()), null, null);
        for (NicProfile nic : _networkMgr.getNicProfiles(vm)) {
        	profile.addNic(nic);
        }
        
		return _itMgr.toVmTO(profile);
	}

	@Override
	public boolean processDisconnect(long agentId, Status state) {
		return false;
	}

	@Override
	public boolean isRecurring() {
		return false;
	}

	@Override
	public int getTimeout() {
		return 0;
	}

	@Override
	public boolean processTimeout(long agentId, long seq) {
		return false;
	}

	@Override
	public boolean start() {
		super.start();
		if(ipspoofingEnabled){
			eventHook = _agentMgr.registerForHostEvents(this, true, false, true);
		}
		return true;
	}

	@Override
	public boolean stop() {
		super.stop();
		if(eventHook != -1){
			_agentMgr.unregisterForHostEvents(eventHook);
		}
		return true;
	}
}
