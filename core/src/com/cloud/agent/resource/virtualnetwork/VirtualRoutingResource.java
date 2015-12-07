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
package com.cloud.agent.resource.virtualnetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.naming.ConfigurationException;

import com.cloud.agent.api.routing.SetMonitorServiceCommand;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.cloud.agent.api.Answer;
import com.cloud.agent.api.BumpUpPriorityCommand;
import com.cloud.agent.api.CheckRouterAnswer;
import com.cloud.agent.api.CheckRouterCommand;
import com.cloud.agent.api.CheckS2SVpnConnectionsAnswer;
import com.cloud.agent.api.CheckS2SVpnConnectionsCommand;
import com.cloud.agent.api.Command;
import com.cloud.agent.api.GetDomRVersionAnswer;
import com.cloud.agent.api.GetDomRVersionCmd;
import com.cloud.agent.api.proxy.CheckConsoleProxyLoadCommand;
import com.cloud.agent.api.proxy.ConsoleProxyLoadAnswer;
import com.cloud.agent.api.proxy.WatchConsoleProxyLoadCommand;
import com.cloud.agent.api.routing.CreateIpAliasCommand;
import com.cloud.agent.api.routing.DeleteIpAliasCommand;
import com.cloud.agent.api.routing.DhcpEntryCommand;
import com.cloud.agent.api.routing.DnsMasqConfigCommand;
import com.cloud.agent.api.routing.IpAliasTO;
import com.cloud.agent.api.routing.IpAssocAnswer;
import com.cloud.agent.api.routing.IpAssocCommand;
import com.cloud.agent.api.routing.LoadBalancerConfigCommand;
import com.cloud.agent.api.routing.NetworkElementCommand;
import com.cloud.agent.api.routing.RemoteAccessVpnCfgCommand;
import com.cloud.agent.api.routing.SavePasswordCommand;
import com.cloud.agent.api.routing.SetBandwidthRulesAnswer;
import com.cloud.agent.api.routing.SetBandwidthRulesCommand;
import com.cloud.agent.api.routing.SetFirewallRulesAnswer;
import com.cloud.agent.api.routing.SetFirewallRulesCommand;
import com.cloud.agent.api.routing.SetMultilineRouteCommand;
import com.cloud.agent.api.routing.SetPortForwardingRulesAnswer;
import com.cloud.agent.api.routing.SetPortForwardingRulesCommand;
import com.cloud.agent.api.routing.SetPortForwardingRulesVpcCommand;
import com.cloud.agent.api.routing.SetStaticNatRulesAnswer;
import com.cloud.agent.api.routing.SetStaticNatRulesCommand;
import com.cloud.agent.api.routing.SetStaticRouteAnswer;
import com.cloud.agent.api.routing.SetStaticRouteCommand;
import com.cloud.agent.api.routing.Site2SiteVpnCfgCommand;
import com.cloud.agent.api.routing.VmDataCommand;
import com.cloud.agent.api.routing.VpnUsersCfgCommand;
import com.cloud.agent.api.to.BandwidthRuleTO;
import com.cloud.agent.api.to.BandwidthRuleTO.BandwidthFilterTO;
import com.cloud.agent.api.to.DhcpTO;
import com.cloud.agent.api.to.FirewallRuleTO;
import com.cloud.agent.api.to.IpAddressTO;
import com.cloud.agent.api.to.PortForwardingRuleTO;
import com.cloud.agent.api.to.StaticNatRuleTO;
import com.cloud.exception.InternalErrorException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.network.HAProxyConfigurator;
import com.cloud.network.LoadBalancerConfigurator;
import com.cloud.network.rules.BandwidthClassRule.BandwidthType;
import com.cloud.network.rules.FirewallRule;
import com.cloud.utils.NumbersUtil;
import com.cloud.utils.component.ComponentLifecycle;
import com.cloud.utils.component.Manager;
import com.cloud.utils.net.NetUtils;
import com.cloud.utils.script.OutputInterpreter;
import com.cloud.utils.script.Script;
import com.cloud.utils.ssh.SshHelper;

/**
 * VirtualNetworkResource controls and configures virtual networking
 *
 * @config
 * {@table
 *    || Param Name | Description | Values | Default ||
 *  }
 **/
@Local(value = {VirtualRoutingResource.class})
public class VirtualRoutingResource implements Manager {
    private static final Logger s_logger = Logger.getLogger(VirtualRoutingResource.class);
    private String _publicIpAddress;
    private String _firewallPath;
    private String _loadbPath;
    private String _dhcpEntryPath;
    private String _publicEthIf;
    private String _privateEthIf;
    private String _bumpUpPriorityPath;
    private String _routerProxyPath;
    private String _createIpAliasPath;
    private String _deleteIpAliasPath;
    private String _callDnsMasqPath;

    private int _timeout;
    private int _startTimeout;
    private String _scriptsDir;
    private String _name;
    private int _sleep;
    private int _retry;
    private int _port;
    private int _maxParamterNum = 1000;
    private int _tcProtocolTcpNum = 6;
    private int _tcProtocolUdpNum = 17;
    private int _tcProtocolIcmpNum = 1;

    public Answer executeRequest(final Command cmd) {
        try {
            if (cmd instanceof SetPortForwardingRulesVpcCommand) {
                return execute((SetPortForwardingRulesVpcCommand)cmd);
            } else if (cmd instanceof SetPortForwardingRulesCommand) {
                return execute((SetPortForwardingRulesCommand)cmd);
            } else if (cmd instanceof SetStaticRouteCommand) {
                return execute((SetStaticRouteCommand)cmd);
            } else if (cmd instanceof SetStaticNatRulesCommand) {
                return execute((SetStaticNatRulesCommand)cmd);
            } else if (cmd instanceof LoadBalancerConfigCommand) {
                return execute((LoadBalancerConfigCommand)cmd);
            } else if (cmd instanceof IpAssocCommand) {
                return execute((IpAssocCommand)cmd);
            } else if (cmd instanceof CheckConsoleProxyLoadCommand) {
                return execute((CheckConsoleProxyLoadCommand)cmd);
            } else if (cmd instanceof WatchConsoleProxyLoadCommand) {
                return execute((WatchConsoleProxyLoadCommand)cmd);
            } else if (cmd instanceof SavePasswordCommand) {
                return execute((SavePasswordCommand)cmd);
            } else if (cmd instanceof DhcpEntryCommand) {
                return execute((DhcpEntryCommand)cmd);
            } else if (cmd instanceof CreateIpAliasCommand) {
                return execute((CreateIpAliasCommand)cmd);
            } else if (cmd instanceof DnsMasqConfigCommand) {
                return execute((DnsMasqConfigCommand)cmd);
            } else if (cmd instanceof DeleteIpAliasCommand) {
                return execute((DeleteIpAliasCommand)cmd);
            } else if (cmd instanceof VmDataCommand) {
                return execute((VmDataCommand)cmd);
            } else if (cmd instanceof CheckRouterCommand) {
                return execute((CheckRouterCommand)cmd);
            } else if (cmd instanceof SetFirewallRulesCommand) {
                return execute((SetFirewallRulesCommand)cmd);
            } else if (cmd instanceof BumpUpPriorityCommand) {
                return execute((BumpUpPriorityCommand)cmd);
            } else if (cmd instanceof RemoteAccessVpnCfgCommand) {
                return execute((RemoteAccessVpnCfgCommand)cmd);
            } else if (cmd instanceof VpnUsersCfgCommand) {
                return execute((VpnUsersCfgCommand)cmd);
            } else if (cmd instanceof GetDomRVersionCmd) {
                return execute((GetDomRVersionCmd)cmd);
            } else if (cmd instanceof Site2SiteVpnCfgCommand) {
                return execute((Site2SiteVpnCfgCommand)cmd);
            } else if (cmd instanceof CheckS2SVpnConnectionsCommand) {
                return execute((CheckS2SVpnConnectionsCommand)cmd);
            } else if (cmd instanceof SetMonitorServiceCommand) {
                return execute((SetMonitorServiceCommand) cmd);
              //Andrew ling add, accept the multiline route command from the manger
        	} else if (cmd instanceof SetMultilineRouteCommand) {
        		return execute((SetMultilineRouteCommand) cmd);
            //Andrew ling add, accept the bandwidth rules command from the manger
    		} else if (cmd instanceof SetBandwidthRulesCommand) {
    		return execute((SetBandwidthRulesCommand) cmd);
    		}
            else {
                return Answer.createUnsupportedCommandAnswer(cmd);
            }
        } catch (final IllegalArgumentException e) {
            return new Answer(cmd, false, e.getMessage());
        }
    }

    private Answer execute(VpnUsersCfgCommand cmd) {
        for (VpnUsersCfgCommand.UsernamePassword userpwd : cmd.getUserpwds()) {
            String args = "";
            if (!userpwd.isAdd()) {
                args += "-U ";
                args += userpwd.getUsername();
            } else {
                args += "-u ";
                args += userpwd.getUsernamePassword();
            }
            String result = routerProxy("vpn_l2tp.sh", cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP), args);
            if (result != null) {
                return new Answer(cmd, false, "Configure VPN user failed for user " + userpwd.getUsername());
            }
        }
        return new Answer(cmd);
    }

    private Answer execute(RemoteAccessVpnCfgCommand cmd) {
        String args = "";
        if (cmd.isCreate()) {
            args += "-r ";
            args += cmd.getIpRange();
            args += " -p ";
            args += cmd.getPresharedKey();
            args += " -s ";
            args += cmd.getVpnServerIp();
            args += " -l ";
            args += cmd.getLocalIp();
            args += " -c ";
        } else {
            args += "-d ";
            args += " -s ";
            args += cmd.getVpnServerIp();
        }
        args += " -C " + cmd.getLocalCidr();
        args += " -i " + cmd.getPublicInterface();
        String result = routerProxy("vpn_l2tp.sh", cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP), args);
        if (result != null) {
            return new Answer(cmd, false, "Configure VPN failed");
        }
        return new Answer(cmd);
    }

    private Answer execute(SetFirewallRulesCommand cmd) {
        String[] results = new String[cmd.getRules().length];
        for (int i = 0; i < cmd.getRules().length; i++) {
            results[i] = "Failed";
        }
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        String egressDefault = cmd.getAccessDetail(NetworkElementCommand.FIREWALL_EGRESS_DEFAULT);

        if (routerIp == null) {
            return new SetFirewallRulesAnswer(cmd, false, results);
        }

        FirewallRuleTO[] allrules = cmd.getRules();
        FirewallRule.TrafficType trafficType = allrules[0].getTrafficType();

        String[][] rules = cmd.generateFwRules();
        final Script command = new Script(_firewallPath, _timeout, s_logger);
        command.add(routerIp);
        command.add("-F");

        if (trafficType == FirewallRule.TrafficType.Egress) {
            command.add("-E");
            if (egressDefault.equals("true")) {
                command.add("-P ", "1");
            } else if (egressDefault.equals("System")) {
                command.add("-P ", "2");
            } else {
                command.add("-P ", "0");
            }
        }

        StringBuilder sb = new StringBuilder();
        String[] fwRules = rules[0];
        if (fwRules.length > 0) {
            for (int i = 0; i < fwRules.length; i++) {
                sb.append(fwRules[i]).append(',');
            }
            command.add("-a", sb.toString());
        }

        String result = command.execute();
        if (result != null) {
            return new SetFirewallRulesAnswer(cmd, false, results);
        }
        return new SetFirewallRulesAnswer(cmd, true, null);

    }

    private Answer execute(SetPortForwardingRulesCommand cmd) {
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        String[] results = new String[cmd.getRules().length];
        int i = 0;
        boolean endResult = true;
        for (PortForwardingRuleTO rule : cmd.getRules()) {
            String result = null;
            final Script command = new Script(_firewallPath, _timeout, s_logger);

            command.add(routerIp);
            command.add(rule.revoked() ? "-D" : "-A");
            command.add("-P ", rule.getProtocol().toLowerCase());
            command.add("-l ", rule.getSrcIp());
            command.add("-p ", rule.getStringSrcPortRange());
            command.add("-r ", rule.getDstIp());
            command.add("-d ", rule.getStringDstPortRange());
            result = command.execute();
            if (result == null) {
                results[i++] = null;
            } else {
                results[i++] = "Failed";
                endResult = false;
            }
        }

        return new SetPortForwardingRulesAnswer(cmd, results, endResult);
    }

    protected Answer SetVPCStaticNatRules(SetStaticNatRulesCommand cmd) {
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        String[] results = new String[cmd.getRules().length];
        int i = 0;
        boolean endResult = true;

        for (StaticNatRuleTO rule : cmd.getRules()) {
            String args = rule.revoked() ? " -D" : " -A";
            args += " -l " + rule.getSrcIp();
            args += " -r " + rule.getDstIp();

            String result = routerProxy("vpc_staticnat.sh", routerIp, args);

            if (result == null) {
                results[i++] = null;
            } else {
                results[i++] = "Failed";
                endResult = false;
            }
        }
        return new SetStaticNatRulesAnswer(cmd, results, endResult);

    }

    private Answer execute(SetStaticNatRulesCommand cmd) {
        if (cmd.getVpcId() != null) {
            return SetVPCStaticNatRules(cmd);
        }
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        String[] results = new String[cmd.getRules().length];
        int i = 0;
        boolean endResult = true;
        for (StaticNatRuleTO rule : cmd.getRules()) {
            String result = null;
            final Script command = new Script(_firewallPath, _timeout, s_logger);
            command.add(routerIp);
            command.add(rule.revoked() ? "-D" : "-A");

            //1:1 NAT needs instanceip;publicip;domrip;op
            command.add(" -l ", rule.getSrcIp());
            command.add(" -r ", rule.getDstIp());

            if (rule.getProtocol() != null) {
                command.add(" -P ", rule.getProtocol().toLowerCase());
            }

            command.add(" -d ", rule.getStringSrcPortRange());
            command.add(" -G ");
            
            //TODO Andrew ling add, Mutiline static nat feature.
            if(rule.IsMultiline()){
            	if(rule.getMultilineLabelSeq() != null && !rule.getMultilineLabelSeq().isEmpty()){
                	command.add(" -L ", rule.getMultilineLabelSeq());
                } else {
                	command.add(" -L ", "none");
                }
            }
            result = command.execute();
            if (result == null) {
                results[i++] = null;
            } else {
                results[i++] = "Failed";
                endResult = false;
            }
        }

        return new SetStaticNatRulesAnswer(cmd, results, endResult);
    }

    protected Answer VPCLoadBalancerConfig(final LoadBalancerConfigCommand cmd) {
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);

        if (routerIp == null) {
            return new Answer(cmd);
        }

        LoadBalancerConfigurator cfgtr = new HAProxyConfigurator();
        String[] config = cfgtr.generateConfiguration(cmd);
        String tmpCfgFileContents = "";
        for (int i = 0; i < config.length; i++) {
            tmpCfgFileContents += config[i];
            tmpCfgFileContents += "\n";
        }
        File permKey = new File("/root/.ssh/id_rsa.cloud");

        try {
            SshHelper.scpTo(routerIp, 3922, "root", permKey, null, "/etc/haproxy/", tmpCfgFileContents.getBytes(), "haproxy.cfg.new", null);

            String[][] rules = cfgtr.generateFwRules(cmd);

            String[] addRules = rules[LoadBalancerConfigurator.ADD];
            String[] removeRules = rules[LoadBalancerConfigurator.REMOVE];
            String[] statRules = rules[LoadBalancerConfigurator.STATS];

            String ip = cmd.getNic().getIp();
            String args = " -i " + ip;
            StringBuilder sb = new StringBuilder();
            if (addRules.length > 0) {
                for (int i = 0; i < addRules.length; i++) {
                    sb.append(addRules[i]).append(',');
                }

                args += " -a " + sb.toString();
            }

            sb = new StringBuilder();
            if (removeRules.length > 0) {
                for (int i = 0; i < removeRules.length; i++) {
                    sb.append(removeRules[i]).append(',');
                }

                args += " -d " + sb.toString();
            }

            sb = new StringBuilder();
            if (statRules.length > 0) {
                for (int i = 0; i < statRules.length; i++) {
                    sb.append(statRules[i]).append(',');
                }

                args += " -s " + sb.toString();
            }

            String result = routerProxy("vpc_loadbalancer.sh", routerIp, args);

            if (result != null) {
                return new Answer(cmd, false, "LoadBalancerConfigCommand failed");
            }
            return new Answer(cmd);

        } catch (Exception e) {
            return new Answer(cmd, e);
        }
    }

    private Answer execute(LoadBalancerConfigCommand cmd) {
        if (cmd.getVpcId() != null) {
            return VPCLoadBalancerConfig(cmd);
        }

        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        File tmpCfgFile = null;
        try {
            String cfgFilePath = "";
            LoadBalancerConfigurator cfgtr = new HAProxyConfigurator();
            String[] config = cfgtr.generateConfiguration(cmd);
            String[][] rules = cfgtr.generateFwRules(cmd);
            if (routerIp != null) {
                tmpCfgFile = File.createTempFile(routerIp.replace('.', '_'), "cfg");
                final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tmpCfgFile)));
                for (int i = 0; i < config.length; i++) {
                    out.println(config[i]);
                }
                out.close();
                cfgFilePath = tmpCfgFile.getAbsolutePath();
            }

            final String result = setLoadBalancerConfig(cfgFilePath,
                    rules[LoadBalancerConfigurator.ADD],
                    rules[LoadBalancerConfigurator.REMOVE],
                    rules[LoadBalancerConfigurator.STATS],
                    routerIp);

            return new Answer(cmd, result == null, result);
        } catch (final IOException e) {
            return new Answer(cmd, false, e.getMessage());
        } finally {
            if (tmpCfgFile != null) {
                tmpCfgFile.delete();
            }
        }
    }

    protected Answer execute(VmDataCommand cmd) {
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        Map<String, List<String[]>> data = new HashMap<String, List<String[]>>();
        data.put(cmd.getVmIpAddress(), cmd.getVmData());

        String json = new Gson().toJson(data);
        s_logger.debug("JSON IS:" + json);

        json = Base64.encodeBase64String(json.getBytes());

        String args = "-d " + json;

        final String result = routerProxy("vmdata.py", routerIp, args);
        if (result != null) {
            return new Answer(cmd, false, "VmDataCommand failed, check agent logs");
        }
        return new Answer(cmd);
    }

    protected Answer execute(final IpAssocCommand cmd) {
        IpAddressTO[] ips = cmd.getIpAddresses();
        String[] results = new String[cmd.getIpAddresses().length];
        int i = 0;
        String result = null;
        String routerName = cmd.getAccessDetail(NetworkElementCommand.ROUTER_NAME);
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        for (IpAddressTO ip : ips) {
            result = assignPublicIpAddress(routerName, routerIp, ip.getPublicIp(), ip.isAdd(),
                    ip.isFirstIP(), ip.isSourceNat(), ip.getBroadcastUri(), ip.getVlanGateway(), ip.getVlanNetmask(),
                    ip.getVifMacAddress(), 2, false);
            if (result != null) {
                results[i++] = IpAssocAnswer.errorResult;
            } else {
                results[i++] = ip.getPublicIp() + " - success";
                ;
            }
        }
        return new IpAssocAnswer(cmd, results);
    }

    private String setLoadBalancerConfig(final String cfgFile,
            final String[] addRules, final String[] removeRules, final String[] statsRules, String routerIp) {

        if (routerIp == null) {
            routerIp = "none";
        }

        final Script command = new Script(_loadbPath, _timeout, s_logger);

        command.add("-i", routerIp);
        command.add("-f", cfgFile);

        StringBuilder sb = new StringBuilder();
        if (addRules.length > 0) {
            for (int i = 0; i < addRules.length; i++) {
                sb.append(addRules[i]).append(',');
            }
            command.add("-a", sb.toString());
        }

        sb = new StringBuilder();
        if (removeRules.length > 0) {
            for (int i = 0; i < removeRules.length; i++) {
                sb.append(removeRules[i]).append(',');
            }
            command.add("-d", sb.toString());
        }

        sb = new StringBuilder();
        if (statsRules.length > 0) {
            for (int i = 0; i < statsRules.length; i++) {
                sb.append(statsRules[i]).append(',');
            }
            command.add("-s", sb.toString());
        }

        return command.execute();
    }

    protected Answer execute(final SavePasswordCommand cmd) {
        final String password = cmd.getPassword();
        final String routerPrivateIPAddress = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        final String vmName = cmd.getVmName();
        final String vmIpAddress = cmd.getVmIpAddress();
        final String local = vmName;

        String args = "-v " + vmIpAddress;
        args += " -p " + password;
        
        String result = routerProxy("savepassword.sh", routerPrivateIPAddress, args);
        if (result != null) {
            return new Answer(cmd, false, "Unable to save password to DomR.");
        }
        return new Answer(cmd);
    }

    protected Answer execute(final DhcpEntryCommand cmd) {
        final Script command = new Script(_dhcpEntryPath, _timeout, s_logger);
        command.add("-r", cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP));
        if (cmd.getVmIpAddress() != null) {
            command.add("-v", cmd.getVmIpAddress());
        }
        command.add("-m", cmd.getVmMac());
        command.add("-n", cmd.getVmName());

        if (cmd.getDefaultRouter() != null) {
            command.add("-d", cmd.getDefaultRouter());
        }
        if (cmd.getStaticRoutes() != null) {
            command.add("-s", cmd.getStaticRoutes());
        }

        if (cmd.getDefaultDns() != null) {
            command.add("-N", cmd.getDefaultDns());
        }

        if (cmd.getVmIp6Address() != null) {
            command.add("-6", cmd.getVmIp6Address());
            command.add("-u", cmd.getDuid());
        }

        if (!cmd.isDefault()) {
            command.add("-z");
        }

        final String result = command.execute();
        return new Answer(cmd, result == null, result);
    }

    protected Answer execute(final CreateIpAliasCommand cmd) {
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        final Script command = new Script(_createIpAliasPath, _timeout, s_logger);
        List<IpAliasTO> ipAliasTOs = cmd.getIpAliasList();
        String args = "";
        command.add(routerIp);
        for (IpAliasTO ipaliasto : ipAliasTOs) {
            args = args + ipaliasto.getAlias_count() + ":" + ipaliasto.getRouterip() + ":" + ipaliasto.getNetmask() + "-";
        }
        command.add(args);
        final String result = command.execute();
        return new Answer(cmd, result == null, result);
    }

    protected Answer execute(final DeleteIpAliasCommand cmd) {
        final Script command = new Script(_deleteIpAliasPath, _timeout, s_logger);
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        String args = "";
        command.add(routerIp);
        List<IpAliasTO> revokedIpAliasTOs = cmd.getDeleteIpAliasTos();
        for (IpAliasTO ipAliasTO : revokedIpAliasTOs) {
            args = args + ipAliasTO.getAlias_count() + ":" + ipAliasTO.getRouterip() + ":" + ipAliasTO.getNetmask() + "-";
        }
        args = args + "- ";
        List<IpAliasTO> activeIpAliasTOs = cmd.getCreateIpAliasTos();
        for (IpAliasTO ipAliasTO : activeIpAliasTOs) {
            args = args + ipAliasTO.getAlias_count() + ":" + ipAliasTO.getRouterip() + ":" + ipAliasTO.getNetmask() + "-";
        }
        command.add(args);
        final String result = command.execute();
        return new Answer(cmd, result == null, result);
    }

    protected Answer execute(final DnsMasqConfigCommand cmd) {
        final Script command = new Script(_callDnsMasqPath, _timeout, s_logger);
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        List<DhcpTO> dhcpTos = cmd.getIps();
        String args = "";
        for (DhcpTO dhcpTo : dhcpTos) {
            args = args + dhcpTo.getRouterIp() + ":" + dhcpTo.getGateway() + ":" + dhcpTo.getNetmask() + ":" + dhcpTo.getStartIpOfSubnet() + "-";
        }
        command.add(routerIp);
        command.add(args);
        final String result = command.execute();
        return new Answer(cmd, result == null, result);
    }

    public String getRouterStatus(String routerIP) {
        return routerProxyWithParser("checkrouter.sh", routerIP, null);
    }

    public String routerProxyWithParser(String script, String routerIP, String args) {
        final Script command = new Script(_routerProxyPath, _timeout, s_logger);
        final OutputInterpreter.OneLineParser parser = new OutputInterpreter.OneLineParser();
        command.add(script);
        command.add(routerIP);
        if (args != null) {
            command.add(args);
        }
        String result = command.execute(parser);
        if (result == null) {
            return parser.getLine();
        }
        return null;
    }

    private CheckS2SVpnConnectionsAnswer execute(CheckS2SVpnConnectionsCommand cmd) {
        final String routerIP = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);

        String args = "";
        for (String ip : cmd.getVpnIps()) {
            args += " " + ip;
        }

        final String result = routerProxy("checkbatchs2svpn.sh", routerIP, args);
        if (result == null || result.isEmpty()) {
            return new CheckS2SVpnConnectionsAnswer(cmd, false, "CheckS2SVpnConneciontsCommand failed");
        }
        return new CheckS2SVpnConnectionsAnswer(cmd, true, result);
    }

    public String routerProxy(String script, String routerIP, String args) {
        final Script command = new Script(_routerProxyPath, _timeout, s_logger);
        command.add(script);
        command.add(routerIP);
        if (args != null) {
            command.add(args);
        }
        return command.execute();
    }

    protected Answer execute(CheckRouterCommand cmd) {
        final String routerPrivateIPAddress = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);

        final String result = getRouterStatus(routerPrivateIPAddress);
        if (result == null || result.isEmpty()) {
            return new CheckRouterAnswer(cmd, "CheckRouterCommand failed");
        }
        return new CheckRouterAnswer(cmd, result, true);
    }

    protected Answer execute(BumpUpPriorityCommand cmd) {
        final String routerPrivateIPAddress = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        final Script command = new Script(_bumpUpPriorityPath, _timeout, s_logger);
        final OutputInterpreter.OneLineParser parser = new OutputInterpreter.OneLineParser();
        command.add(routerPrivateIPAddress);
        String result = command.execute(parser);
        if (result != null) {
            return new Answer(cmd, false, "BumpUpPriorityCommand failed: " + result);
        }
        return new Answer(cmd, true, null);
    }

    protected String getDomRVersion(String routerIP) {
        return routerProxyWithParser("get_template_version.sh", routerIP, null);
    }

    protected Answer execute(GetDomRVersionCmd cmd) {
        final String routerPrivateIPAddress = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);

        final String result = getDomRVersion(routerPrivateIPAddress);
        if (result == null || result.isEmpty()) {
            return new GetDomRVersionAnswer(cmd, "GetDomRVersionCmd failed");
        }
        String[] lines = result.split("&");
        if (lines.length != 2) {
            return new GetDomRVersionAnswer(cmd, result);
        }
        return new GetDomRVersionAnswer(cmd, result, lines[0], lines[1]);
    }

    protected Answer execute(final CheckConsoleProxyLoadCommand cmd) {
        return executeProxyLoadScan(cmd, cmd.getProxyVmId(), cmd.getProxyVmName(), cmd.getProxyManagementIp(), cmd.getProxyCmdPort());
    }

    protected Answer execute(final WatchConsoleProxyLoadCommand cmd) {
        return executeProxyLoadScan(cmd, cmd.getProxyVmId(), cmd.getProxyVmName(), cmd.getProxyManagementIp(), cmd.getProxyCmdPort());
    }

    protected Answer execute(Site2SiteVpnCfgCommand cmd) {
        String args;
        if (cmd.isCreate()) {
            args = "-A";
            args += " -l ";
            args += cmd.getLocalPublicIp();
            args += " -n ";
            args += cmd.getLocalGuestCidr();
            args += " -g ";
            args += cmd.getLocalPublicGateway();
            args += " -r ";
            args += cmd.getPeerGatewayIp();
            args += " -N ";
            args += cmd.getPeerGuestCidrList();
            args += " -e ";
            args += "\"" + cmd.getEspPolicy() + "\"";
            args += " -i ";
            args += "\"" + cmd.getIkePolicy() + "\"";
            args += " -t ";
            args += Long.toString(cmd.getIkeLifetime());
            args += " -T ";
            args += Long.toString(cmd.getEspLifetime());
            args += " -s ";
            args += "\"" + cmd.getIpsecPsk() + "\"";
            args += " -d ";
            if (cmd.getDpd()) {
                args += "1";
            } else {
                args += "0";
            }
            if (cmd.isPassive()) {
            	args += " -p ";
            }
        } else {
            args = "-D";
            args += " -r ";
            args += cmd.getPeerGatewayIp();
            args += " -n ";
            args += cmd.getLocalGuestCidr();
            args += " -N ";
            args += cmd.getPeerGuestCidrList();
        }
        String result = routerProxy("ipsectunnel.sh", cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP), args);
        if (result != null) {
            return new Answer(cmd, false, "Configure site to site VPN failed due to " + result);
        }
        return new Answer(cmd);
    }

    private Answer executeProxyLoadScan(final Command cmd, final long proxyVmId, final String proxyVmName, final String proxyManagementIp, final int cmdPort) {
        String result = null;

        final StringBuffer sb = new StringBuffer();
        sb.append("http://").append(proxyManagementIp).append(":" + cmdPort).append("/cmd/getstatus");

        boolean success = true;
        try {
            final URL url = new URL(sb.toString());
            final URLConnection conn = url.openConnection();

            final InputStream is = conn.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            final StringBuilder sb2 = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb2.append(line + "\n");
                }
                result = sb2.toString();
            } catch (final IOException e) {
                success = false;
            } finally {
                try {
                    is.close();
                } catch (final IOException e) {
                    s_logger.warn("Exception when closing , console proxy address : " + proxyManagementIp);
                    success = false;
                }
            }
        } catch (final IOException e) {
            s_logger.warn("Unable to open console proxy command port url, console proxy address : " + proxyManagementIp);
            success = false;
        }

        return new ConsoleProxyLoadAnswer(cmd, proxyVmId, proxyVmName, success, result);
    }

    public String configureMonitor(final String routerIP, final String config, final String disable) {

        String args= " -c " + config;
        if (disable != null) {
            args = args + "-d";
        }
        return  routerProxy("monitor_service.sh", routerIP, args);
    }

    public String assignGuestNetwork(final String dev, final String routerIP,
            final String routerGIP, final String gateway, final String cidr,
            final String netmask, final String dns, final String domainName) {

        String args = " -C";
        args += " -d " + dev;
        args += " -i " + routerGIP;
        args += " -g " + gateway;
        args += " -m " + cidr;
        args += " -n " + netmask;
        if (dns != null && !dns.isEmpty()) {
            args += " -s " + dns;
        }
        if (domainName != null && !domainName.isEmpty()) {
            args += " -e " + domainName;
        }
        return routerProxy("vpc_guestnw.sh", routerIP, args);
    }

    public String assignNetworkACL(final String routerIP, final String dev,
            final String routerGIP, final String netmask, final String rule, String privateGw) {
        String args = " -d " + dev;
        if (privateGw != null) {
            args += " -a " + rule;
            return routerProxy("vpc_privategw_acl.sh", routerIP, args);
        } else {
            args += " -i " + routerGIP;
            args += " -m " + netmask;
            args += " -a " + rule;
            return routerProxy("vpc_acl.sh", routerIP, args);
        }
    }

    public String assignSourceNat(final String routerIP, final String pubIP, final String dev) {
        String args = " -A ";
        args += " -l ";
        args += pubIP;
        args += " -c ";
        args += dev;
        return routerProxy("vpc_snat.sh", routerIP, args);
    }

    private SetPortForwardingRulesAnswer execute(SetPortForwardingRulesVpcCommand cmd) {
        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        String[] results = new String[cmd.getRules().length];
        int i = 0;

        boolean endResult = true;
        for (PortForwardingRuleTO rule : cmd.getRules()) {
            String args = rule.revoked() ? " -D" : " -A";
            args += " -P " + rule.getProtocol().toLowerCase();
            args += " -l " + rule.getSrcIp();
            args += " -p " + rule.getStringSrcPortRange();
            args += " -r " + rule.getDstIp();
            args += " -d " + rule.getStringDstPortRange().replace(":", "-");

            String result = routerProxy("vpc_portforwarding.sh", routerIp, args);

            if (result != null) {
                results[i++] = "Failed";
                endResult = false;
            } else {
                results[i++] = null;
            }
        }
        return new SetPortForwardingRulesAnswer(cmd, results, endResult);
    }

    public void assignVpcIpToRouter(final String routerIP, final boolean add, final String pubIP,
            final String nicname, final String gateway, final String netmask, final String subnet, boolean sourceNat) throws InternalErrorException {
        String args = "";
        String snatArgs = "";

        if (add) {
            args += " -A ";
            snatArgs += " -A ";
        } else {
            args += " -D ";
            snatArgs += " -D ";
        }

        args += " -l ";
        args += pubIP;
        args += " -c ";
        args += nicname;
        args += " -g ";
        args += gateway;
        args += " -m ";
        args += netmask;
        args += " -n ";
        args += subnet;

        String result = routerProxy("vpc_ipassoc.sh", routerIP, args);
        if (result != null) {
            throw new InternalErrorException("KVM plugin \"vpc_ipassoc\" failed:" + result);
        }
        if (sourceNat) {
            snatArgs += " -l " + pubIP;
            snatArgs += " -c " + nicname;

            result = routerProxy("vpc_privateGateway.sh", routerIP, snatArgs);
            if (result != null) {
                throw new InternalErrorException("KVM plugin \"vpc_privateGateway\" failed:" + result);
            }

        }
    }

    private SetStaticRouteAnswer execute(SetStaticRouteCommand cmd) {
        String routerIP = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        try {
            String[] results = new String[cmd.getStaticRoutes().length];
            String[][] rules = cmd.generateSRouteRules();
            StringBuilder sb = new StringBuilder();
            String[] srRules = rules[0];

            for (int i = 0; i < srRules.length; i++) {
                sb.append(srRules[i]).append(',');
            }

            String args = " -a " + sb.toString();
            String result = routerProxy("vpc_staticroute.sh", routerIP, args);

            if (result != null) {
                for (int i = 0; i < results.length; i++) {
                    results[i] = "Failed";
                }
                return new SetStaticRouteAnswer(cmd, false, results);
            }

            return new SetStaticRouteAnswer(cmd, true, results);
        } catch (Exception e) {
            String msg = "SetStaticRoute failed due to " + e.toString();
            s_logger.error(msg, e);
            return new SetStaticRouteAnswer(cmd, false, null);
        }
    }

    private Answer execute(SetMonitorServiceCommand cmd) {

        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        String disable =  cmd.getAccessDetail(NetworkElementCommand.ROUTER_MONITORING_DISABLE);
        String config = cmd.getConfiguration();


        String result = configureMonitor(routerIp, config, disable);

        if (result != null) {
            return new Answer(cmd, false, "SetMonitorServiceCommand failed");
        }
        return new Answer(cmd);

    }
    
    //Andrew ling add
    private Answer execute(SetMultilineRouteCommand cmd){
    	String script = "route_rules.sh";
    	String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
    	//The store format like :<ctcc,<10.204.120.1; 10.204.104.0/24,10.204.105.0/24>>
    	//VRLabelToDefaultGateway format like : ctcc-10.204.104.1
    	//one route rule like :ip route add default via 10.204.120.1 table ctcc; ip route add 10.204.104.0/24 via 10.204.119.1 table ctcc; there will be many rules in the map.
    	//In this execute operation , if need delete first and then add rule or not? Not delete by now, but it must be remarked.
    	String VRLabelToDefaultGateway = cmd.getVRLabelToDefaultGateway();
    	if(VRLabelToDefaultGateway == "" || VRLabelToDefaultGateway == null){
    		throw new InvalidParameterValueException("You must input the default gateway in the VR when using the multiline feature.");
    	}
    	String VRLableDefault = VRLabelToDefaultGateway.split("-")[0];
//    	String VRDefaultGateway = VRLabelToDefaultGateway.split("-")[1];
    	VirtualRoutingMutilineSetup virtualRoutingMutilineSetup =new VirtualRoutingMutilineSetup();
    	HashMap<String, HashMap<String, String>> routeRulesMap = cmd.getRouteRules();
    	int mutilineNumbers = routeRulesMap.size();
    	if(mutilineNumbers == 0){
    		throw new InvalidParameterValueException("You must input all the mutiline networks in the VR when using the multiline feature.");
    	}
    	String[] multilineLabels = new String[mutilineNumbers];
    	int labelNumber = 0;
    	for(Map.Entry<String,  HashMap<String, String>> entry : routeRulesMap.entrySet()){
    		String label = entry.getKey();
    		if(label !="" && label != null){
    			multilineLabels[labelNumber] = "_" + label;
    		}
    		HashMap<String, String> gatewayNetsStrings = entry.getValue();
    		for(Map.Entry<String, String> gatewayNetsString : gatewayNetsStrings.entrySet()){
    			String gateway = gatewayNetsString.getKey();
    			String netsStrings = gatewayNetsString.getValue();
    			String newGatewayNetsString = gateway + "_" + netsStrings;
    			virtualRoutingMutilineSetup.addTableLabelTORouteRules(label, newGatewayNetsString);
    		}
    		labelNumber++;
    	}
    	virtualRoutingMutilineSetup.setAllMultilineTableLables(mutilineNumbers, multilineLabels);
    	//创建main表规则,删除原来的默认路由规则，创建指定默认路由设定其他非默认路由规则
    	String mainTableToRouteRulesCmd = virtualRoutingMutilineSetup.getMainTableToRouteRulesCmd(VRLableDefault);
    	String[] mainTableToRouteRulesCmdToArray = splitScripteParameter(mainTableToRouteRulesCmd, ";", _maxParamterNum);
    	//创建多线路配置方案路由表
    	String createRouteTableLableRulesCmd = virtualRoutingMutilineSetup.getCreateRouteTableLableRulesCmd();
    	//创建路由表的规则
    	String tableLabelGroupToRouteRulesCmd = virtualRoutingMutilineSetup.getTableLabelGroupToRouteRulesCmd();
    	String[] tableLabelGroupToRouteRulesCmdToArray = splitScripteParameter(tableLabelGroupToRouteRulesCmd, ";", _maxParamterNum);
    	
    	for(String mainTableToRouteRules : mainTableToRouteRulesCmdToArray){
    		String result = routerProxy(script, routerIp, mainTableToRouteRules);
        	if (result != null){
        		return new Answer( cmd, false, "SetMultilineRouteCommand failed besause can not create main route table rules.");
        	}
    	}
    	
    	script = "none.sh";
    	String result = routerProxy(script, routerIp, createRouteTableLableRulesCmd);
    	if (result != null){
    		return new Answer( cmd, false, "SetMultilineRouteCommand failed.besause can not create route tables.");
    	}
    	
    	for(String tableLabelGroupToRouteRules : tableLabelGroupToRouteRulesCmdToArray){
    		result = routerProxy(script, routerIp, tableLabelGroupToRouteRules);
        	if (result != null){
        		return new Answer( cmd, false, "SetMultilineRouteCommand failed.besause can not create route tables rules.");
        	}
    	}
    	return new Answer(cmd);
    }
    
    private String[] splitScripteParameter(String scripteParameters, String regex, int maxNumPerLine){
    	String[] scripteParametersToArray = scripteParameters.split(regex);
    	int scripteParameterNO = 0;
    	int num = scripteParametersToArray.length / maxNumPerLine;
    	String[] scripteParametersResult;
    	if(num == 0){
    		scripteParametersResult = new String[1];
    	} else {
    		if(scripteParametersToArray.length % maxNumPerLine == 0){
    			scripteParametersResult = new String[num];
    		}else{
    			scripteParametersResult = new String[num+1];
    		}
    	}
    	for(int i = 0; i < scripteParametersResult.length; i++ ){
    		scripteParametersResult[i] = "";
    	}
    	if(scripteParametersToArray.length > maxNumPerLine){
    		for(int i = 0; i < scripteParametersToArray.length; i++){
    			if(i != 0 && i % maxNumPerLine == 0){
    				scripteParameterNO++;
    			}
    			scripteParametersResult[scripteParameterNO] += scripteParametersToArray[i] + ";";
    		}
    		
    	}else{
    		scripteParametersResult[0] = scripteParameters;
    		return scripteParametersResult;
    	}
    	return scripteParametersResult;
    }
    
    //Andrew ling add
    private SetBandwidthRulesAnswer execute(SetBandwidthRulesCommand cmd){
//    	tc qdisc add dev eth0 root handle 1: htb r2q 1 这一段在网卡创建的时候执行。
//    	tc class add dev eth0 parent 1: classid 1:2 htb rate 1000kbit ceil 2000kbit prio 2
//    	tc class add dev eth0 parent 1: classid 1:3 htb rate 1mbit ceil 2mbit prio 2
//    	tc qdisc add dev eth0 parent 1:2 handle 2: sfq perturb 10
//    	tc qdisc add dev eth0 parent 1:3 handle 3: sfq perturb 10
//    	tc filter add dev eth0 protocol ip parent 1: prio 2 u32 match ip dst 192.168.0.2 match ip dport 80 0xffff flowid 1:2
//    	tc filter add dev eth0 protocol ip parent 1: prio 2 u32 match ip dst 192.168.0.3 flowid 1:3
    	 String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
         String[] results = new String[cmd.getRules().length];
         int i = 0;
		boolean endResult = true;
		for (BandwidthRuleTO rule : cmd.getRules()) {
			String executeRules = "";
			int deviceId = rule.getDeviceId();
			int prio = rule.getPrio();
			int trafficRuleId = rule.getTrafficRuleId();
			int rate = -1;
			int ceil = -1;
			String script = "";
			if (rule.isKeepState()) {
				// no need to create class, just create or delete the filter rules.when delete the filter,must delete all the filters and create the still use filters.
				boolean  isDel = checkFiltersIncludeDel(rule.getBandwidthFilters());
				executeRules += buildFilterRules(rule.getBandwidthFilters(), isDel, false, rule.getType(), deviceId, prio, trafficRuleId);
				// if isDel is true, then need to execute the script, if not, then no need to execute the script.
				if(isDel){
					String deleteAllFilterRule = " -D -c eth"+ deviceId +" -r "+trafficRuleId+" -p " + prio;
					script = "bandwidth_rule.sh";
					String deleteResult = routerProxy(script, routerIp, deleteAllFilterRule);
					String result = null;
					if(!executeRules.isEmpty()){
						script = "none.sh";
						result = routerProxy(script, routerIp, executeRules);
					}
					
					if (deleteResult != null || result != null) {
						results[i++] = "Failed";
						endResult = false;
					} else {
						results[i++] = null;
					}
				} else {
					script = "none.sh";
					String result = routerProxy(script, routerIp, executeRules);
					if (result != null) {
						results[i++] = "Failed";
						endResult = false;
					} else {
						results[i++] = null;
					}
				}

			} else {
				rate = rule.getRate();
				ceil = rule.getCeil();

				if (rule.isRevoked()) {
					// before delete bandwidth rule, need to delete the filter rules first
					String deleteAllFilterRule = " -D -c eth"+ deviceId +" -r "+trafficRuleId+" -p " + prio;
					script = "bandwidth_rule.sh";
					String deleteResult = routerProxy(script, routerIp, deleteAllFilterRule);
					// tc qdisc del dev eth0 parent 1:2 handle 2: sfq perturb 10
					//tc class del dev eth0 parent 1: classid 1:2 htb rate 1000kbit ceil 2000kbit prio 2
					executeRules +=  "tc qdisc del dev eth"+ deviceId + " parent 1:" + trafficRuleId+ " handle " + trafficRuleId + ": sfq perturb 10;" 
					        + "tc class del dev eth" + deviceId+ " parent 1: classid 1:" + trafficRuleId+ " htb rate " + rate + "kbit ceil " + ceil
							+ "kbit prio " + prio + ";";
					script = "none.sh";
					String result = routerProxy(script, routerIp, executeRules);
					if (deleteResult != null || result != null) {
						results[i++] = "Failed";
						endResult = false;
					} else {
						results[i++] = null;
					}
				} else {
					// add the bandwidth rule first and then add the filter rules
					executeRules += "tc class add dev eth" + deviceId+ " parent 1: classid 1:" + trafficRuleId+ " htb rate " + rate + "kbit ceil " + ceil
							+ "kbit prio " + prio + "; tc qdisc add dev eth"+ deviceId + " parent 1:" + trafficRuleId+ " handle " + trafficRuleId + ": sfq perturb 10;";
					executeRules += buildFilterRules(rule.getBandwidthFilters(), false, true, rule.getType(), deviceId, prio, trafficRuleId);
					script = "none.sh";
					String result = routerProxy(script, routerIp, executeRules);
					if (result != null) {
						results[i++] = "Failed";
						endResult = false;
					} else {
						results[i++] = null;
					}
				}
			}
		}
		return new SetBandwidthRulesAnswer(cmd, endResult, results);
	}
    
    private boolean checkFiltersIncludeDel(BandwidthFilterTO[] bandwidthFilters){
    	for (BandwidthFilterTO filters : bandwidthFilters) {
    		if(filters.isRevoke()){
    			return true;
    		}
    	}
    	return false;
    }
    
    private String buildFilterRules(BandwidthFilterTO[] bandwidthFilters, boolean isDel, boolean isAddAll, BandwidthType type, int deviceId, int prio, int trafficRuleId){
    	String filterRules = "";
    	if(isAddAll){
    		//retrun all the filter rules
    		for (BandwidthFilterTO filter : bandwidthFilters) {
        		if(!filter.isAlreadyAdded() && !filter.isRevoke()){
        			filterRules += buildFilterRule(filter, type, deviceId, prio, trafficRuleId);
        		}
        	}
    		return filterRules;
    	} else {
    		if(!isDel){
        		//return the filter rules superposition,返回额外增加的filter规则
        		for (BandwidthFilterTO filter : bandwidthFilters) {
            		if(!filter.isAlreadyAdded() && !filter.isRevoke()){
            			filterRules += buildFilterRule(filter, type, deviceId, prio, trafficRuleId);
            		}
            	}
        		return filterRules;
        	}
        	if(isDel){
        		//return all the already added filter rules
        		for (BandwidthFilterTO filter : bandwidthFilters) {
            		if(filter.isAlreadyAdded() && !filter.isRevoke()){
            			filterRules += buildFilterRule(filter, type, deviceId, prio, trafficRuleId);
            		}
            	}
        		return filterRules;
        	}
    	}
    	return null;
    }
    
    private String buildFilterRule(BandwidthFilterTO bandwidthFilter, BandwidthType type, int deviceId, int prio, int trafficRuleId){
    	String filterRule = "";
    	String ip = bandwidthFilter.getIp();
		Integer startPort = bandwidthFilter.getStartPort();
		Integer endPort = bandwidthFilter.getEndPort();
		String protocol = bandwidthFilter.getProtocol();
		String trafficType = "";
//		String portType = "";
		int protocolNum = _tcProtocolTcpNum;
		if (type.equals(BandwidthType.InTraffic)) {
			trafficType = "dst";
//			portType = "dport";
		} else if (type.equals(BandwidthType.OutTraffic)) {
			trafficType = "src";
//			portType = "sport";
		} else {
			throw new InvalidParameterValueException("The bandwidth type is not rigth, it only support two type, include in traffic and out traffic.");
		}
        if(protocol != null && protocol.equalsIgnoreCase(NetUtils.TCP_PROTO)){
        	protocolNum = _tcProtocolTcpNum;
        }
        if (protocol != null && protocol.equalsIgnoreCase(NetUtils.UDP_PROTO)){
        	protocolNum = _tcProtocolUdpNum;
        }
		if(protocol == null && startPort == null && endPort == null){
//			tc filter add dev eth0 protocol ip parent 1: prio 2 u32 match ip dst 192.168.0.3 flowid 1:3
			filterRule += "tc filter add dev eth" + deviceId + " protocol ip parent 1: prio " + prio
					+ " u32 match ip " + trafficType + " " + ip + " flowid 1:"+ trafficRuleId + ";";
			return filterRule;
		}
		Map<Integer, String> portRangeParams = createBandwidthPortRangeParams(startPort, endPort);
		for (Map.Entry<Integer, String> entry : portRangeParams.entrySet()) {
			// tc filter add dev eth0 protocol ip parent 1: prio 2 u32 match ip dst 192.168.0.2 match ip tcp/udp dport 80 0xffff flowid 1:2
			// tc filter add dev eth0 protocol ip parent 1: prio 2 u32 match ip dst 10.207.110.230 match udp/tcp dst 25 0xffff match ip protocol 17/6 0xff flowid 1:2;
			filterRule += "tc filter add dev eth" + deviceId + " protocol ip parent 1: prio " + prio
					+ " u32 match ip " + trafficType + " " + ip + " match " + protocol + " " + trafficType + " " + entry.getKey() + " "
					+ entry.getValue() + " match ip protocol " + protocolNum + " 0xff flowid 1:"+ trafficRuleId + ";";
		}
		return filterRule;
    }
    
    private  Map<Integer, String> createBandwidthPortRangeParams(int startPort, int endPort){
		Map<Integer, String> mask = new HashMap<Integer, String>();
		mask.put(1, "0xffff");
		mask.put(2, "0xfffe");
		mask.put(4, "0xfffc");
		mask.put(8, "0xfff8");
		mask.put(16, "0xfff0");
		mask.put(32, "0xffe0");
		mask.put(64, "0xffc0");
		mask.put(128, "0xff80");
		mask.put(256, "0xff00");
		mask.put(512, "0xfe00");
		mask.put(1024, "0xfc00");
		mask.put(2048, "0xf800");
		mask.put(4096, "0xf000");
		mask.put(8192, "0xe000");
		mask.put(16384, "0xc000");
		mask.put(32768, "0x8000");
		mask.put(65536, "0x0000");
		
		Map<Integer, String> result = new HashMap<Integer, String>();
		if(startPort == endPort){
			result.put(startPort, mask.get(1));
			return result;
		}
		//找到开始端口对应的最大端口区间值
		if(startPort % 2 != 0){
			if(startPort == 1){
				result.put(startPort, mask.get(1));
			} else{
				result.put(startPort, mask.get(1));
				startPort = startPort + 1;
			}
		}
		if(startPort == endPort){
			result.put(startPort, mask.get(1));
			return result;
		}
		
		////////////////会循环部分/////////////////
		endPort++;
		while(startPort != endPort){
			int portDifference = endPort - startPort;
			//开始端口能达到的最大区间值A
			int startPort2IntervalTop = getTop2Exponentiation(startPort);
			
			//结束端口和开始端口之差能达到的最大区间值B
			int portDifference2intervalTop = getTop2Exponentiation(portDifference);
			
			//最大区间值B只能
			if(portDifference2intervalTop >= startPort2IntervalTop){
				result.put(startPort, mask.get(startPort2IntervalTop));
				startPort = startPort + startPort2IntervalTop;
			} else {
				result.put(startPort, mask.get(portDifference2intervalTop));
				startPort = startPort + portDifference2intervalTop;
			}
		}
		return result;
	}
	
	//开始端口的最大能取到的区间值
	private  int getTop2Exponentiation(int value){
		int i = 0;
		double Top2Exponentiation = 0;
		while(value >= Top2Exponentiation){
			Top2Exponentiation = Math.pow(2, i);
			i ++;
		}
		Top2Exponentiation = Math.pow(2, i-2);
		return (int)Top2Exponentiation;
	}
    
    
    
    public String assignPublicIpAddress(final String vmName,
            final String privateIpAddress, final String publicIpAddress,
            final boolean add, final boolean firstIP, final boolean sourceNat,
            final String broadcastUri, final String vlanGateway,
            final String vlanNetmask, final String vifMacAddress, int nicNum, boolean newNic) {

        String args = "";
        if (add) {
            args += "-A";
        } else {
            args += "-D";
        }
        String cidrSize = Long.toString(NetUtils.getCidrSize(vlanNetmask));
        if (sourceNat) {
            args += " -s";
        }
        if (firstIP) {
            args += " -f";
        }
        args += " -l ";
        args += publicIpAddress + "/" + cidrSize;

        String publicNic = "eth" + nicNum;
        args += " -c ";
        args += publicNic;

        args += " -g ";
        args += vlanGateway;

        if (newNic) {
            args += " -n";
        }

        return routerProxy("ipassoc.sh", privateIpAddress, args);
    }

    private void deleteBridge(String brName) {
        Script cmd = new Script("/bin/sh", _timeout);
        cmd.add("-c");
        cmd.add("ifconfig " + brName + " down;brctl delbr " + brName);
        cmd.execute();
    }

    private boolean isDNSmasqRunning(String dnsmasqName) {
        Script cmd = new Script("/bin/sh", _timeout);
        cmd.add("-c");
        cmd.add("ls -l /var/run/libvirt/network/" + dnsmasqName + ".pid");
        String result = cmd.execute();
        if (result != null) {
            return false;
        } else {
            return true;
        }
    }

    private void stopDnsmasq(String dnsmasqName) {
        Script cmd = new Script("/bin/sh", _timeout);
        cmd.add("-c");
        cmd.add("kill -9 `cat /var/run/libvirt/network/" + dnsmasqName + ".pid`");
        cmd.execute();
    }

    //    protected Answer execute(final SetFirewallRuleCommand cmd) {
    //    	String args;
    //    	if(cmd.getProtocol().toLowerCase().equals(NetUtils.NAT_PROTO)){
    //    		//1:1 NAT needs instanceip;publicip;domrip;op
    //    		if(cmd.isCreate()) {
    //                args = "-A";
    //            } else {
    //                args = "-D";
    //            }
    //
    //    		args += " -l " + cmd.getPublicIpAddress();
    //    		args += " -i " + cmd.getRouterIpAddress();
    //    		args += " -r " + cmd.getPrivateIpAddress();
    //    		args += " -G " + cmd.getProtocol();
    //    	}else{
    //    		if (cmd.isEnable()) {
    //    			args = "-A";
    //    		} else {
    //    			args = "-D";
    //    		}
    //
    //    		args += " -P " + cmd.getProtocol().toLowerCase();
    //    		args += " -l " + cmd.getPublicIpAddress();
    //    		args += " -p " + cmd.getPublicPort();
    //    		args += " -n " + cmd.getRouterName();
    //    		args += " -i " + cmd.getRouterIpAddress();
    //    		args += " -r " + cmd.getPrivateIpAddress();
    //    		args += " -d " + cmd.getPrivatePort();
    //    		args += " -N " + cmd.getVlanNetmask();
    //
    //    		String oldPrivateIP = cmd.getOldPrivateIP();
    //    		String oldPrivatePort = cmd.getOldPrivatePort();
    //
    //    		if (oldPrivateIP != null) {
    //    			args += " -w " + oldPrivateIP;
    //    		}
    //
    //    		if (oldPrivatePort != null) {
    //    			args += " -x " + oldPrivatePort;
    //    		}
    //    	}
    //
    //    	final Script command = new Script(_firewallPath, _timeout, s_logger);
    //    	String [] argsArray = args.split(" ");
    //    	for (String param : argsArray) {
    //    		command.add(param);
    //    	}
    //    	String result = command.execute();
    //    	return new Answer(cmd, result == null, result);
    //    }

    protected String getDefaultScriptsDir() {
        return "scripts/network/domr/dom0";
    }

    protected String findScript(final String script) {
        return Script.findScript(_scriptsDir, script);
    }

    @Override
    public boolean configure(final String name, final Map<String, Object> params) throws ConfigurationException {
        _name = name;

        _scriptsDir = (String)params.get("domr.scripts.dir");
        if (_scriptsDir == null) {
            if (s_logger.isInfoEnabled()) {
                s_logger.info("VirtualRoutingResource _scriptDir can't be initialized from domr.scripts.dir param, use default");
            }
            _scriptsDir = getDefaultScriptsDir();
        }

        if (s_logger.isInfoEnabled()) {
            s_logger.info("VirtualRoutingResource _scriptDir to use: " + _scriptsDir);
        }

        String value = (String)params.get("scripts.timeout");
        _timeout = NumbersUtil.parseInt(value, 120) * 1000;

        value = (String)params.get("start.script.timeout");
        _startTimeout = NumbersUtil.parseInt(value, 360) * 1000;

        value = (String)params.get("ssh.sleep");
        _sleep = NumbersUtil.parseInt(value, 10) * 1000;

        value = (String)params.get("ssh.retry");
        _retry = NumbersUtil.parseInt(value, 36);

        value = (String)params.get("ssh.port");
        _port = NumbersUtil.parseInt(value, 3922);

        _publicIpAddress = (String)params.get("public.ip.address");
        if (_publicIpAddress != null) {
            s_logger.warn("Incoming public ip address is overriden.  Will always be using the same ip address: " + _publicIpAddress);
        }

        _firewallPath = findScript("call_firewall.sh");
        if (_firewallPath == null) {
            throw new ConfigurationException("Unable to find the call_firewall.sh");
        }

        _loadbPath = findScript("call_loadbalancer.sh");
        if (_loadbPath == null) {
            throw new ConfigurationException("Unable to find the call_loadbalancer.sh");
        }

        _dhcpEntryPath = findScript("dhcp_entry.sh");
        if (_dhcpEntryPath == null) {
            throw new ConfigurationException("Unable to find dhcp_entry.sh");
        }

        _publicEthIf = (String)params.get("public.network.device");
        if (_publicEthIf == null) {
            _publicEthIf = "xenbr1";
        }
        _publicEthIf = _publicEthIf.toLowerCase();

        _privateEthIf = (String)params.get("private.network.device");
        if (_privateEthIf == null) {
            _privateEthIf = "xenbr0";
        }
        _privateEthIf = _privateEthIf.toLowerCase();

        _bumpUpPriorityPath = findScript("bumpUpPriority.sh");
        if (_bumpUpPriorityPath == null) {
            throw new ConfigurationException("Unable to find bumpUpPriority.sh");
        }

        _routerProxyPath = findScript("router_proxy.sh");
        if (_routerProxyPath == null) {
            throw new ConfigurationException("Unable to find router_proxy.sh");
        }
        _createIpAliasPath = findScript("createipAlias.sh");
        if (_createIpAliasPath == null) {
            throw new ConfigurationException("unable to find createipAlias.sh");
        }
        _deleteIpAliasPath = findScript("deleteipAlias.sh");
        if (_deleteIpAliasPath == null) {
            throw new ConfigurationException("unable to find deleteipAlias.sh");
        }
        _callDnsMasqPath = findScript("call_dnsmasq.sh");
        if (_callDnsMasqPath == null) {
            throw new ConfigurationException("unable to find call_dnsmasq.sh");
        }

        return true;
    }

    public String connect(final String ipAddress) {
        return connect(ipAddress, _port);
    }

    public String connect(final String ipAddress, final int port) {
        for (int i = 0; i <= _retry; i++) {
            SocketChannel sch = null;
            try {
                if (s_logger.isDebugEnabled()) {
                    s_logger.debug("Trying to connect to " + ipAddress);
                }
                sch = SocketChannel.open();
                sch.configureBlocking(true);

                final InetSocketAddress addr = new InetSocketAddress(ipAddress, port);
                sch.connect(addr);
                return null;
            } catch (final IOException e) {
                if (s_logger.isDebugEnabled()) {
                    s_logger.debug("Could not connect to " + ipAddress);
                }
            } finally {
                if (sch != null) {
                    try {
                        sch.close();
                    } catch (final IOException e) {
                    }
                }
            }
            try {
                Thread.sleep(_sleep);
            } catch (final InterruptedException e) {
            }
        }

        s_logger.debug("Unable to logon to " + ipAddress);

        return "Unable to connect";
    }

    public boolean connect(final String ipAddress, int retry, int sleep) {
        for (int i = 0; i <= retry; i++) {
            SocketChannel sch = null;
            try {
                if (s_logger.isDebugEnabled()) {
                    s_logger.debug("Trying to connect to " + ipAddress);
                }
                sch = SocketChannel.open();
                sch.configureBlocking(true);

                final InetSocketAddress addr = new InetSocketAddress(ipAddress, _port);
                sch.connect(addr);
                return true;
            } catch (final IOException e) {
                if (s_logger.isDebugEnabled()) {
                    s_logger.debug("Could not connect to " + ipAddress);
                }
            } finally {
                if (sch != null) {
                    try {
                        sch.close();
                    } catch (final IOException e) {}
                }
            }
            try {
                Thread.sleep(sleep);
            } catch (final InterruptedException e) {
            }
        }

        s_logger.debug("Unable to logon to " + ipAddress);

        return false;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }

    @Override
    public int getRunLevel() {
        return ComponentLifecycle.RUN_LEVEL_COMPONENT;
    }

    public void setRunLevel() {
    }

    @Override
    public void setConfigParams(Map<String, Object> params) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> getConfigParams() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRunLevel(int level) {
        // TODO Auto-generated method stub

    }

}
