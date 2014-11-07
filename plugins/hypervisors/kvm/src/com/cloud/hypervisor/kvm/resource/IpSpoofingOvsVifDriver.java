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
package com.cloud.hypervisor.kvm.resource;

import java.util.Map;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;
import org.libvirt.LibvirtException;

import com.cloud.agent.api.to.NicTO;
import com.cloud.exception.InternalErrorException;
import com.cloud.hypervisor.kvm.resource.LibvirtVMDef.InterfaceDef;
import com.cloud.network.Networks;
import com.cloud.utils.NumbersUtil;

public class IpSpoofingOvsVifDriver extends OvsVifDriver {
	private static final Logger s_logger = Logger.getLogger(IpSpoofingOvsVifDriver.class);
	private boolean ipspoofingEnabled = false;
	private int _timeout;
	private OvsController _controller;

	@Override
	public void configure(Map<String, Object> params)
			throws ConfigurationException {
		super.configure(params);

		String value = (String) params.get("ovs.ipspoofing.protect");
		ipspoofingEnabled = Boolean.parseBoolean(value);

		value = (String) params.get("scripts.timeout");
		_timeout = NumbersUtil.parseInt(value, 30 * 60) * 1000;
		
		_controller = new OvsController();
	}

	@Override
	public InterfaceDef plug(NicTO nic, String guestOsType)
			throws InternalErrorException, LibvirtException {
		InterfaceDef def = super.plug(nic, guestOsType);

		if (ipspoofingEnabled) {
			s_logger.debug("ip spoofing protect is opened while plug nic "+nic.getName());
			if (nic.getType() == Networks.TrafficType.Guest) {
				_controller.addIpSpoofingProtectForMac(
						_bridges.get("guest"), nic.getIp(), nic.getMac());
				s_logger.debug("ovs ipsoofing plug with "+
						_bridges.get("guest")+"/"+nic.getIp()+"/"+nic.getMac());
			}
		}

		return def;
	}

	@Override
	public void unplug(InterfaceDef iface) {
		super.unplug(iface);
		if (ipspoofingEnabled) {
			s_logger.debug("ip spoofing protect is opened while plug iface "+iface.getBrName());
			_controller.delIpSpoofingProtectForMac(
					_bridges.get("guest"), "ip", iface.getMacAddress());
			s_logger.debug("ovs ipsoofing unplug with "+
					_bridges.get("guest")+"/"+iface.getMacAddress());
		}
	}

}
