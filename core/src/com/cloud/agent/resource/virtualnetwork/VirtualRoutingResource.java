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
//

package com.cloud.agent.resource.virtualnetwork;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

import com.cloud.agent.api.Answer;
import com.cloud.agent.api.CheckRouterAnswer;
import com.cloud.agent.api.CheckRouterCommand;
import com.cloud.agent.api.CheckS2SVpnConnectionsAnswer;
import com.cloud.agent.api.CheckS2SVpnConnectionsCommand;
import com.cloud.agent.api.GetDomRVersionAnswer;
import com.cloud.agent.api.GetDomRVersionCmd;
import com.cloud.agent.api.GetRouterAlertsAnswer;
import com.cloud.agent.api.routing.AggregationControlCommand;
import com.cloud.agent.api.routing.AggregationControlCommand.Action;
import com.cloud.agent.api.routing.GetRouterAlertsCommand;
import com.cloud.agent.api.routing.GroupAnswer;
import com.cloud.agent.api.routing.NetworkElementCommand;
import com.cloud.agent.api.routing.SetBandwidthRulesAnswer;
import com.cloud.agent.api.routing.SetBandwidthRulesCommand;
import com.cloud.agent.api.routing.SetMultilineRouteCommand;
import com.cloud.agent.api.to.BandwidthRuleTO;
import com.cloud.agent.api.to.BandwidthRuleTO.BandwidthFilterTO;
import com.cloud.agent.resource.virtualnetwork.facade.AbstractConfigItemFacade;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.network.rules.BandwidthClassRule.BandwidthType;
import com.cloud.utils.ExecutionResult;
import com.cloud.utils.NumbersUtil;
import com.cloud.utils.exception.CloudRuntimeException;
import com.cloud.utils.net.NetUtils;

/**
 * VirtualNetworkResource controls and configures virtual networking
 *
 * @config
 * {@table
 *    || Param Name | Description | Values | Default ||
 *  }
 **/
public class VirtualRoutingResource {

    private static final Logger s_logger = Logger.getLogger(VirtualRoutingResource.class);
    private VirtualRouterDeployer _vrDeployer;
    private Map<String, Queue<NetworkElementCommand>> _vrAggregateCommandsSet;
    protected Map<String, Lock> _vrLockMap = new HashMap<String, Lock>();

    private String _name;
    private int _sleep;
    private int _retry;
    private int _port;
    private int _eachTimeout;

    private String _cfgVersion = "1.0";
    private int _maxParamterNum = 1000;
    private int _tcProtocolTcpNum = 6;
    private int _tcProtocolUdpNum = 17;
    private int _tcProtocolIcmpNum = 1;

    public VirtualRoutingResource(VirtualRouterDeployer deployer) {
        _vrDeployer = deployer;
    }

    public Answer executeRequest(final NetworkElementCommand cmd) {
        boolean aggregated = false;
        String routerName = cmd.getAccessDetail(NetworkElementCommand.ROUTER_NAME);
        Lock lock;
        if (_vrLockMap.containsKey(routerName)) {
            lock = _vrLockMap.get(routerName);
        } else {
            lock = new ReentrantLock();
            _vrLockMap.put(routerName, lock);
        }
        lock.lock();

        try {
            ExecutionResult rc = _vrDeployer.prepareCommand(cmd);
            if (!rc.isSuccess()) {
                s_logger.error("Failed to prepare VR command due to " + rc.getDetails());
                return new Answer(cmd, false, rc.getDetails());
            }

            assert cmd.getRouterAccessIp() != null : "Why there is no access IP for VR?";

            if (cmd.isQuery()) {
                return executeQueryCommand(cmd);
            }

            if (cmd instanceof AggregationControlCommand) {
                return execute((AggregationControlCommand)cmd);
            }

            if (_vrAggregateCommandsSet.containsKey(routerName)) {
                _vrAggregateCommandsSet.get(routerName).add(cmd);
                aggregated = true;
                // Clean up would be done after command has been executed
                //TODO: Deal with group answer as well
                return new Answer(cmd);
            }

            //andrew ling add
            if(cmd instanceof SetMultilineRouteCommand){
                return execute((SetMultilineRouteCommand)cmd);
            }

            if(cmd instanceof SetBandwidthRulesCommand){
                return execute((SetBandwidthRulesCommand)cmd);
            }

            List<ConfigItem> cfg = generateCommandCfg(cmd);
            if (cfg == null) {
                return Answer.createUnsupportedCommandAnswer(cmd);
            }

            return applyConfig(cmd, cfg);
        } catch (final IllegalArgumentException e) {
            return new Answer(cmd, false, e.getMessage());
        } finally {
            lock.unlock();
            if (!aggregated) {
                ExecutionResult rc = _vrDeployer.cleanupCommand(cmd);
                if (!rc.isSuccess()) {
                    s_logger.error("Failed to cleanup VR command due to " + rc.getDetails());
                }
            }
        }
    }

    private Answer executeQueryCommand(NetworkElementCommand cmd) {
        if (cmd instanceof CheckRouterCommand) {
            return execute((CheckRouterCommand)cmd);
        } else if (cmd instanceof GetDomRVersionCmd) {
            return execute((GetDomRVersionCmd)cmd);
        } else if (cmd instanceof CheckS2SVpnConnectionsCommand) {
            return execute((CheckS2SVpnConnectionsCommand) cmd);
        } else if (cmd instanceof GetRouterAlertsCommand) {
            return execute((GetRouterAlertsCommand)cmd);
        } else {
            s_logger.error("Unknown query command in VirtualRoutingResource!");
            return Answer.createUnsupportedCommandAnswer(cmd);
        }
    }

    private ExecutionResult applyConfigToVR(String routerAccessIp, ConfigItem c) {
        return applyConfigToVR(routerAccessIp, c, VRScripts.DEFAULT_EXECUTEINVR_TIMEOUT);
    }

    private ExecutionResult applyConfigToVR(String routerAccessIp, ConfigItem c, int timeout) {
        if (c instanceof FileConfigItem) {
            FileConfigItem configItem = (FileConfigItem)c;
            return _vrDeployer.createFileInVR(routerAccessIp, configItem.getFilePath(), configItem.getFileName(), configItem.getFileContents());
        } else if (c instanceof ScriptConfigItem) {
            ScriptConfigItem configItem = (ScriptConfigItem)c;
            return _vrDeployer.executeInVR(routerAccessIp, configItem.getScript(), configItem.getArgs(), timeout);
        }
        throw new CloudRuntimeException("Unable to apply unknown configitem of type " + c.getClass().getSimpleName());
    }


    private Answer applyConfig(NetworkElementCommand cmd, List<ConfigItem> cfg) {


        if (cfg.isEmpty()) {
            return new Answer(cmd, true, "Nothing to do");
        }

        List<ExecutionResult> results = new ArrayList<ExecutionResult>();
        List<String> details = new ArrayList<String>();
        boolean finalResult = false;
        for (ConfigItem configItem : cfg) {
            long startTimestamp = System.currentTimeMillis();
            ExecutionResult result = applyConfigToVR(cmd.getRouterAccessIp(), configItem);
            if (s_logger.isDebugEnabled()) {
                long elapsed = System.currentTimeMillis() - startTimestamp;
                s_logger.debug("Processing " + configItem + " took " + elapsed + "ms");
            }
            if (result == null) {
                result = new ExecutionResult(false, "null execution result");
            }
            results.add(result);
            details.add(configItem.getInfo() + (result.isSuccess() ? " - success: " : " - failed: ") + result.getDetails());
            finalResult = result.isSuccess();
        }

        // Not sure why this matters, but log it anyway
        if (cmd.getAnswersCount() != results.size()) {
            s_logger.warn("Expected " + cmd.getAnswersCount() + " answers while executing " + cmd.getClass().getSimpleName() + " but received " + results.size());
        }


        if (results.size() == 1) {
            return new Answer(cmd, finalResult, results.get(0).getDetails());
        } else {
            return new GroupAnswer(cmd, finalResult, results.size(), details.toArray(new String[details.size()]));
        }
    }

    private CheckS2SVpnConnectionsAnswer execute(CheckS2SVpnConnectionsCommand cmd) {

        StringBuffer buff = new StringBuffer();
        for (String ip : cmd.getVpnIps()) {
            buff.append(ip);
            buff.append(" ");
        }
        ExecutionResult result = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.S2SVPN_CHECK, buff.toString());
        return new CheckS2SVpnConnectionsAnswer(cmd, result.isSuccess(), result.getDetails());
    }

    private GetRouterAlertsAnswer execute(GetRouterAlertsCommand cmd) {

        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        String args = cmd.getPreviousAlertTimeStamp();

        ExecutionResult result = _vrDeployer.executeInVR(routerIp, VRScripts.ROUTER_ALERTS, args);
        String alerts[] = null;
        String lastAlertTimestamp = null;

        if (result.isSuccess()) {
            if (!result.getDetails().isEmpty() && !result.getDetails().trim().equals("No Alerts")) {
                alerts = result.getDetails().trim().split("\\\\n");
                String[] lastAlert = alerts[alerts.length - 1].split(",");
                lastAlertTimestamp = lastAlert[0];
            }
            return new GetRouterAlertsAnswer(cmd, alerts, lastAlertTimestamp);
        } else {
            return new GetRouterAlertsAnswer(cmd, result.getDetails());
        }
    }

    private Answer execute(CheckRouterCommand cmd) {
        final ExecutionResult result = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.RVR_CHECK, null);
        if (!result.isSuccess()) {
            return new CheckRouterAnswer(cmd, result.getDetails());
        }
        return new CheckRouterAnswer(cmd, result.getDetails(), true);
    }

    private Answer execute(GetDomRVersionCmd cmd) {
        final ExecutionResult result = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.VERSION, null);
        if (!result.isSuccess()) {
            return new GetDomRVersionAnswer(cmd, "GetDomRVersionCmd failed");
        }
        String[] lines = result.getDetails().split("&");
        if (lines.length != 2) {
            return new GetDomRVersionAnswer(cmd, result.getDetails());
        }
        return new GetDomRVersionAnswer(cmd, result.getDetails(), lines[0], lines[1]);
    }

    public boolean configure(final String name, final Map<String, Object> params) throws ConfigurationException {
        _name = name;

        String value = (String)params.get("ssh.sleep");
        _sleep = NumbersUtil.parseInt(value, 10) * 1000;

        value = (String)params.get("ssh.retry");
        _retry = NumbersUtil.parseInt(value, 36);

        value = (String)params.get("ssh.port");
        _port = NumbersUtil.parseInt(value, 3922);

        value = (String)params.get("router.aggregation.command.each.timeout");
        _eachTimeout = NumbersUtil.parseInt(value, 3);

        if (_vrDeployer == null) {
            throw new ConfigurationException("Unable to find the resource for VirtualRouterDeployer!");
        }

        _vrAggregateCommandsSet = new HashMap<>();
        return true;
    }

    public boolean connect(final String ipAddress) {
        return connect(ipAddress, _port);
    }

    public boolean connect(final String ipAddress, final int port) {
        return connect(ipAddress, port, _sleep);
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
                    } catch (final IOException e) {
                    }
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

    private List<ConfigItem> generateCommandCfg(NetworkElementCommand cmd) {
        /*
         * [TODO] Still have to migrate LoadBalancerConfigCommand and BumpUpPriorityCommand
         * [FIXME] Have a look at SetSourceNatConfigItem
         */
        s_logger.debug("Transforming " + cmd.getClass().getCanonicalName() + " to ConfigItems");

        final AbstractConfigItemFacade configItemFacade = AbstractConfigItemFacade.getInstance(cmd.getClass());

        return configItemFacade.generateConfig(cmd);
    }

    private Answer execute(AggregationControlCommand cmd) {
        Action action = cmd.getAction();
        String routerName = cmd.getAccessDetail(NetworkElementCommand.ROUTER_NAME);
        assert routerName != null;
        assert cmd.getRouterAccessIp() != null;

        if (action == Action.Start) {
            assert (!_vrAggregateCommandsSet.containsKey(routerName));

            Queue<NetworkElementCommand> queue = new LinkedBlockingQueue<>();
            _vrAggregateCommandsSet.put(routerName, queue);
            return new Answer(cmd, true, "Command aggregation started");
        } else if (action == Action.Finish) {
            Queue<NetworkElementCommand> queue = _vrAggregateCommandsSet.get(routerName);
            int answerCounts = 0;
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("#Apache CloudStack Virtual Router Config File\n");
                sb.append("<version>\n" + _cfgVersion + "\n</version>\n");
                for (NetworkElementCommand command : queue) {
                    answerCounts += command.getAnswersCount();
                    List<ConfigItem> cfg = generateCommandCfg(command);
                    if (cfg == null) {
                        s_logger.warn("Unknown commands for VirtualRoutingResource, but continue: " + cmd.toString());
                        continue;
                    }

                    for (ConfigItem c : cfg) {
                        sb.append(c.getAggregateCommand());
                    }
                }

                // TODO replace with applyConfig with a stop on fail
                String cfgFileName = "VR-"+ UUID.randomUUID().toString() + ".cfg";
                FileConfigItem fileConfigItem = new FileConfigItem(VRScripts.CONFIG_CACHE_LOCATION, cfgFileName, sb.toString());
                ScriptConfigItem scriptConfigItem = new ScriptConfigItem(VRScripts.VR_CFG, "-c " + VRScripts.CONFIG_CACHE_LOCATION + cfgFileName);
                // 120s is the minimal timeout
                int timeout = answerCounts * _eachTimeout;
                if (timeout < 120) {
                    timeout = 120;
                }

                ExecutionResult result = applyConfigToVR(cmd.getRouterAccessIp(), fileConfigItem);
                if (!result.isSuccess()) {
                    return new Answer(cmd, false, result.getDetails());
                }

                result = applyConfigToVR(cmd.getRouterAccessIp(), scriptConfigItem, timeout);
                if (!result.isSuccess()) {
                    return new Answer(cmd, false, result.getDetails());
                }

                return new Answer(cmd, true, "Command aggregation finished");
            } finally {
                queue.clear();
                _vrAggregateCommandsSet.remove(routerName);
            }
        }
        return new Answer(cmd, false, "Fail to recongize aggregation action " + action.toString());
    }

    //Andrew ling add
    private Answer execute(SetMultilineRouteCommand cmd){
//        String script = "route_rules.sh";
//        String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
        //The store format like :<ctcc,<10.204.120.1; 10.204.104.0/24,10.204.105.0/24>>
        //VRLabelToDefaultGateway format like : ctcc-10.204.104.1
        //one route rule like :ip route add default via 10.204.120.1 table ctcc; ip route add 10.204.104.0/24 via 10.204.119.1 table ctcc; there will be many rules in the map.
        //In this execute operation , if need delete first and then add rule or not? Not delete by now, but it must be remarked.
        String VRLabelToDefaultGateway = cmd.getVRLabelToDefaultGateway();
        if(VRLabelToDefaultGateway == "" || VRLabelToDefaultGateway == null){
            throw new InvalidParameterValueException("You must input the default gateway in the VR when using the multiline feature.");
        }
        String VRLableDefault = VRLabelToDefaultGateway.split("-")[0];
//        String VRDefaultGateway = VRLabelToDefaultGateway.split("-")[1];
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
//            final ExecutionResult result = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.VERSION, null);
            final ExecutionResult result = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.MULTILINE_ROUTE_RULES, mainTableToRouteRules);
//            String result = routerProxy(script, routerIp, mainTableToRouteRules);
            if (!result.isSuccess()){
                return new Answer( cmd, false, "SetMultilineRouteCommand failed besause can not create main route table rules.");
            }
        }

//        script = "none.sh";
        final ExecutionResult result = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.NONE_SCRIPTE, createRouteTableLableRulesCmd);
//        String result = routerProxy(script, routerIp, createRouteTableLableRulesCmd);
        if (!result.isSuccess()){
            return new Answer( cmd, false, "SetMultilineRouteCommand failed.besause can not create route tables.");
        }

        for(String tableLabelGroupToRouteRules : tableLabelGroupToRouteRulesCmdToArray){
            final ExecutionResult resultSecondary = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.NONE_SCRIPTE, tableLabelGroupToRouteRules);
//            result = routerProxy(script, routerIp, tableLabelGroupToRouteRules);
            if (!resultSecondary.isSuccess()){
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
//        tc qdisc add dev eth0 root handle 1: htb r2q 1 这一段在网卡创建的时候执行。
//        tc class add dev eth0 parent 1: classid 1:2 htb rate 1000kbit ceil 2000kbit prio 2
//        tc class add dev eth0 parent 1: classid 1:3 htb rate 1mbit ceil 2mbit prio 2
//        tc qdisc add dev eth0 parent 1:2 handle 2: sfq perturb 10
//        tc qdisc add dev eth0 parent 1:3 handle 3: sfq perturb 10
//        tc filter add dev eth0 protocol ip parent 1: prio 2 u32 match ip dst 192.168.0.2 match ip dport 80 0xffff flowid 1:2
//        tc filter add dev eth0 protocol ip parent 1: prio 2 u32 match ip dst 192.168.0.3 flowid 1:3
//         String routerIp = cmd.getAccessDetail(NetworkElementCommand.ROUTER_IP);
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
//            String script = "";
            if (rule.isKeepState()) {
                // no need to create class, just create or delete the filter rules.when delete the filter,must delete all the filters and create the still use filters.
                boolean  isDel = checkFiltersIncludeDel(rule.getBandwidthFilters());
                executeRules += buildFilterRules(rule.getBandwidthFilters(), isDel, false, rule.getType(), deviceId, prio, trafficRuleId);
                // if isDel is true, then need to execute the script, if not, then no need to execute the script.
                if(isDel){
                    String deleteAllFilterRule = " -D -c eth"+ deviceId +" -r "+trafficRuleId+" -p " + prio;
//                    script = "bandwidth_rule.sh";
                    ExecutionResult deleteResult = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.BANDWIDTH_RULE, deleteAllFilterRule);
//                    String deleteResult = routerProxy(script, routerIp, deleteAllFilterRule);
//                    String result = null;
                    ExecutionResult result = null;
                    if(!executeRules.isEmpty()){
//                        script = "none.sh";
                        result = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.NONE_SCRIPTE, executeRules);
//                        result = routerProxy(script, routerIp, executeRules);
                    }

                    if (!deleteResult.isSuccess() || (result != null && !result.isSuccess())) {
                        results[i++] = "Failed";
                        endResult = false;
                    } else {
                        results[i++] = null;
                    }
                } else {
//                    script = "none.sh";
                    ExecutionResult result = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.NONE_SCRIPTE, executeRules);
//                    String result = routerProxy(script, routerIp, executeRules);
                    if (!result.isSuccess()) {
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
//                    script = "bandwidth_rule.sh";
                    ExecutionResult deleteResult = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.BANDWIDTH_RULE, deleteAllFilterRule);
//                    String deleteResult = routerProxy(script, routerIp, deleteAllFilterRule);
                    // tc qdisc del dev eth0 parent 1:2 handle 2: sfq perturb 10
                    //tc class del dev eth0 parent 1: classid 1:2 htb rate 1000kbit ceil 2000kbit prio 2
                    executeRules +=  "tc qdisc del dev eth"+ deviceId + " parent 1:" + trafficRuleId+ " handle " + trafficRuleId + ": sfq perturb 10;"
                            + "tc class del dev eth" + deviceId+ " parent 1: classid 1:" + trafficRuleId+ " htb rate " + rate + "kbit ceil " + ceil
                            + "kbit prio " + prio + ";";
//                    script = "none.sh";
                    ExecutionResult result = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.NONE_SCRIPTE, executeRules);
//                    String result = routerProxy(script, routerIp, executeRules);
                    if (!deleteResult.isSuccess() || !result.isSuccess()) {
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
//                    script = "none.sh";
                    ExecutionResult result = _vrDeployer.executeInVR(cmd.getRouterAccessIp(), VRScripts.NONE_SCRIPTE, executeRules);
//                    String result = routerProxy(script, routerIp, executeRules);
                    if (!result.isSuccess()) {
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
        String portType = "";
        int protocolNum = _tcProtocolTcpNum;
        if (type.equals(BandwidthType.InTraffic)) {
            trafficType = "dst";
            portType = "dport";
        } else if (type.equals(BandwidthType.OutTraffic)) {
            trafficType = "src";
            portType = "sport";
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
//            tc filter add dev eth0 protocol ip parent 1: prio 2 u32 match ip dst 192.168.0.3 flowid 1:3
            filterRule += "tc filter add dev eth" + deviceId + " protocol ip parent 1: prio " + prio
                    + " u32 match ip " + trafficType + " " + ip + " flowid 1:"+ trafficRuleId + ";";
            return filterRule;
        }
        Map<Integer, String> portRangeParams = createBandwidthPortRangeParams(startPort, endPort);
        for (Map.Entry<Integer, String> entry : portRangeParams.entrySet()) {
            // tc filter add dev eth3 protocol ip prio 2 u32 match ip protocol 6 0xff match ip sport 8090 0xffff match ip src 122.13.159.230 flowid 1:4;
            filterRule += "tc filter add dev eth" + deviceId + " protocol ip prio " + prio
                    + " u32 match ip protocol " + protocolNum + " 0xff match ip " + portType + " " + entry.getKey() + " "+ entry.getValue()
                    + " match ip " + trafficType + " " + ip + " flowid 1:"+ trafficRuleId + ";" ;
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

}
