package com;

import com.cloud.network.resource.SangforADLoadBalancerResource;

public  class TestSangforADLoadBalancer extends SangforADLoadBalancerResource{
	
	public static void main(String[] args) {
	    try {
	 	    TestSangforADLoadBalancer t = new TestSangforADLoadBalancer();
	 	    t.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
     }
	
	@SuppressWarnings("unused")
	public void execute() throws Exception{
		/*testInit("admin","yxtech123");
		ObjectVO objectVO = new ObjectVO();
		LbProtocol lbProtocol = LbProtocol.tcp;
		String srcIp ="192.168.2.5";
		int srcPort = 25;
		String destIp = "192.168.2.3";
		int destPor = 21;
		Integer toPort = null;
		String vsServerName = genObjectName(LBPrefixName.cs_vs,lbProtocol, srcIp, srcPort);
		String ipGroupName = genObjectName(LBPrefixName.cs_group,lbProtocol, srcIp, srcPort);
		String poolNodeName = genObjectName(LBPrefixName.cs_pool ,lbProtocol, srcIp, srcPort);
		String serverName = genObjectName(LBPrefixName.cs_service,lbProtocol, srcPort, toPort);
		String nodeMonitorName = null;
		String persistName = "none";
		NodeLbMethodType lbAlgorithm = NodeLbMethodType.NODE_LB_LEAST_CONN;
		
		//1,stickiness healthcheck
		if(true){
			StickinessPolicyTO[] stickyPolicies = new StickinessPolicyTO[]{new StickinessPolicyTO(StickinessMethodType.SourceBased.getName(), null)};
			if (stickyPolicies != null && stickyPolicies.length > 0 && stickyPolicies[0] != null){
				persistName =  genObjectName(LBPrefixName.cs_persist,lbProtocol, srcPort, toPort);
				createOrUpdateStickinessPolicy(stickyPolicies,objectVO,persistName,poolNodeName);
	        }
			HealthCheckPolicyTO health = new HealthCheckPolicyTO("/","",4,4,2,2,false);
			HealthCheckPolicyTO[] healthCheckPolicys = new HealthCheckPolicyTO[]{health};
			if (healthCheckPolicys != null && healthCheckPolicys.length > 0 && healthCheckPolicys[0] != null){
				nodeMonitorName =  genObjectName(LBPrefixName.cs_monitor,lbProtocol, srcPort, toPort);
				createOrUpdateHealthCheckPolicy(healthCheckPolicys,objectVO,nodeMonitorName,poolNodeName);
	        }
		}
		
		//2,node pool group
		if (true) {
			String vsExistServerName = existService(srcPort, toPort);
			if(vsExistServerName.equals("") || vsExistServerName == null){
				List<PortScopeType> portScopes = new ArrayList<PortScopeType>();
				PortScopeType portScope = objectVO.ServPortVO(srcPort,toPort);
				portScopes.add(portScope);
				ServiceInfoType serverInfo = objectVO.ServiceInfoVO(serverName,portScopes);
				createServ(serverInfo,serverName);
			} else {
				serverName = vsExistServerName;
			}
			
			List<NodeInfoType> nodeInfos = new ArrayList<NodeInfoType>();
			NodeInfoType _node = objectVO.NodeVO(destIp, destPor);
			nodeInfos.add(_node);
			
			NodePoolCreateInfoType nodePool = objectVO.NodePoolVO(poolNodeName,persistName,nodeMonitorName,lbAlgorithm,nodeInfos);
			createOrUpdateNodePoolInfo(nodePool,poolNodeName);
			
			IpInfoType ipInfo = objectVO.IpGroupIpVO((List<String>)objectToList(srcIp));
			List<IpInfoType> ipInfos = (List<IpInfoType>)objectToList(ipInfo);
			IpGroupInfoType ipGroup = objectVO.IpGroupVO(ipGroupName,ipInfos);
			createIpGroup(ipGroup,ipGroupName);
			
			VsCreateInfoType vs = objectVO.VsVO(vsServerName,serverName,ipGroupName,poolNodeName);
			createVs(vs,vsServerName);
		}
		
		//3,删除
		if(false){
			//若存在删除虚拟服务
			deleteVs(vsServerName);
			//若存在删除组
			deleteIpGroup(ipGroupName);
			//若存在删除节点
			deleteNodePoolInfo(poolNodeName);
			//删除会话保持
			deletePersist(genObjectName(LBPrefixName.cs_persist,lbProtocol, srcPort, toPort));
			//删除节点监视器
			deleteNodeMonitor(genObjectName(LBPrefixName.cs_monitor,lbProtocol, srcPort, toPort));
			//NodeKeyType nodeKey = objectVO.NodeKeyVO(destIp, poolNodeName, destPor);
			//deleteNode(nodeKey);
		}
		*/
	}

}
