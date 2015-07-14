package com.cloud.network.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.ConfigurationException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloud.adapi.ADAuthType;
import com.cloud.adapi.Adapi;
import com.cloud.adapi.AdapiPortType;
import com.cloud.adapi.CookieType;
import com.cloud.adapi.ErrType;
import com.cloud.adapi.ErrorOpr;
import com.cloud.adapi.HttpSchedModeType;
import com.cloud.adapi.IpGroupInfoType;
import com.cloud.adapi.IpInfoListType;
import com.cloud.adapi.IpInfoType;
import com.cloud.adapi.MonitorType;
import com.cloud.adapi.NodeConfStatusType;
import com.cloud.adapi.NodeHashType;
import com.cloud.adapi.NodeInfoType;
import com.cloud.adapi.NodeKeyExtType;
import com.cloud.adapi.NodeKeyType;
import com.cloud.adapi.NodeLbMethodType;
import com.cloud.adapi.NodeMonitorBaseInfoType;
import com.cloud.adapi.NodeMonitorInfoType;
import com.cloud.adapi.NodeMonitorUpdateInfoType;
import com.cloud.adapi.NodePoolCreateInfoType;
import com.cloud.adapi.NodePoolViewInfoType;
import com.cloud.adapi.PersistCookieType;
import com.cloud.adapi.PersistInfoType;
import com.cloud.adapi.PersistSourceIpType;
import com.cloud.adapi.PersistType;
import com.cloud.adapi.PersistUpdateInfoType;
import com.cloud.adapi.PortListType;
import com.cloud.adapi.PortScopeType;
import com.cloud.adapi.SSLAttrType;
import com.cloud.adapi.SchedMethodType;
import com.cloud.adapi.ServiceBaseInfoType;
import com.cloud.adapi.ServiceInfoListType;
import com.cloud.adapi.ServiceInfoType;
import com.cloud.adapi.ServiceType;
import com.cloud.adapi.VsCreateInfoType;
import com.cloud.adapi.VsModeType;
import com.cloud.adapi.VsViewInfoType;
import com.cloud.agent.IAgentControl;
import com.cloud.agent.api.Answer;
import com.cloud.agent.api.Command;
import com.cloud.agent.api.ExternalNetworkResourceUsageAnswer;
import com.cloud.agent.api.ExternalNetworkResourceUsageCommand;
import com.cloud.agent.api.MaintainAnswer;
import com.cloud.agent.api.MaintainCommand;
import com.cloud.agent.api.PingCommand;
import com.cloud.agent.api.ReadyAnswer;
import com.cloud.agent.api.ReadyCommand;
import com.cloud.agent.api.StartupCommand;
import com.cloud.agent.api.StartupExternalLoadBalancerCommand;
import com.cloud.agent.api.routing.IpAssocAnswer;
import com.cloud.agent.api.routing.IpAssocCommand;
import com.cloud.agent.api.routing.LoadBalancerConfigCommand;
import com.cloud.agent.api.routing.NetworkElementCommand;
import com.cloud.agent.api.to.IpAddressTO;
import com.cloud.agent.api.to.LoadBalancerTO;
import com.cloud.agent.api.to.LoadBalancerTO.DestinationTO;
import com.cloud.agent.api.to.LoadBalancerTO.HealthCheckPolicyTO;
import com.cloud.agent.api.to.LoadBalancerTO.StickinessPolicyTO;
import com.cloud.host.Host;
import com.cloud.host.Host.Type;
import com.cloud.network.rules.LbStickinessMethod.StickinessMethodType;
import com.cloud.resource.ServerResource;
import com.cloud.utils.NumbersUtil;
import com.cloud.utils.Pair;
import com.cloud.utils.exception.ExecutionException;
import com.cloud.utils.script.Script;
import com.google.gson.Gson;
/**
 * @author root
 * @Date 2015/06/12
 * 管控外部网络设备的操作功能实现
 * 1.连接外部网络设备
 * 2.获取UI接口参数
 * 3.调用外部外部网络设备接口（发送规则到设备）
 */
public class SangforADLoadBalancerResource implements ServerResource {
	
	private static final Logger s_logger = LoggerFactory.getLogger(SangforADLoadBalancerResource.class);
	
	private final int SUCCESS_OK = 0;
	
	private Adapi _adapi;
	private static AdapiPortType _api;
	private static ADAuthType _login;
	private Properties _configProps;
	private ServiceInfoListType _serviceInfoListType;
	
	private static String _username;
	private static String _password;
	private String _name;
	private String _zoneId;
	private String _ip;
	private Integer _numRetries; 
	private String _guid;
	private String _objectNamePathSep = "_";
	private String _routeDomainIdentifier = "%";
	
	//虚拟服务
	private VsModeType _vsModeType;
	private HttpSchedModeType _vsHttpSchedModeType;
	private boolean _vsEnable;
	private boolean _vsTcpCacheStreamEnable;//是否开启TCP流
	private BigInteger _vsTcpCacheNum;//TCP缓冲流大小（0~4096）
	private String  _vsEndStr;
	private String  _vsPreRule;//前置调度策略
	private boolean _vsAutoSnatEnable;
	private boolean _vsThreeTransferEnbale;//是否开启三角传输
	private String  _vsSnatAddress;
	private ServiceType _serServiceType;
	//ssl策略
	private boolean _sslHttpRedirEnable;
	private BigInteger _sslHttpRedirPort;
	private String _sslProfile;
	//节点
	private Integer _nodeRatio;
	private NodeConfStatusType _nodeStatus;
	private BigInteger _nodeMaxConnects;
	private BigInteger _nodeNewConnects;
	private BigInteger _nodeMaxRequest;
	private String _nodeRsVal1;
	private NodeHashType _nodeHashType;
	//节点池
	private String _poolPersist2Name;
	private String _poolMonitors;//节点监视器名称，使用base64编码,可配置多个(,号分割)
	private BigInteger _poolMinMonitors;//节点有效条件(0表示全部监视器有效)
	private int _poolRecoverInterval;//恢复时间（0~300s）
	private int _poolStepperInterval;//温暖时间（0~300s）
	private SchedMethodType _poolSchedMethod;//节点池繁忙处理策略
	private boolean _poolConnStatAllEnable;//连接数统计
	private BigInteger _poolQueueLength;//队列长度（1~100000）
	private BigInteger _poolQueueTimeout;//超时时间（1~60）
	//组
	private String _groupNetifName;
	//监视器
	private boolean _nodeMonitorDebugEnable;
	private MonitorType _nodeMonitorType;
	private int _nodePersistDefaultTimeout;
	
	/**
	 * 负载均衡协议枚举
	 */
	private enum LbProtocol {
		tcp,
		udp;
	}
	
	/**
	 * 负载均衡算法枚举
	 */
	private enum LBAlgorithm {
		LB_Roundrobin("roundrobin"),
		LB_leastconn("leastconn"),
		LB_source("source");
	    private String value;

	    LBAlgorithm(String v) {
	        value = v;
	    }
	    private  static LBAlgorithm fromValue(String v) {
	        for (LBAlgorithm c: LBAlgorithm.values()) {
	            if (c.value.equals(v)) {
	                return c;
	            }
	        }
	        throw new IllegalArgumentException(v);
	    }
	}
	
	/**
	 * 名称前缀枚举
	 */
	private enum LBPrefixName {
		cs_vs,
		cs_pool,
		cs_node,
		cs_group,
		cs_service,
		cs_persist,
		cs_monitor;
	}
	
	/**
	 * ad配置文件名
	 */
	 private enum fileName {
		    AD_API_WSDL("adapi.wsdl"),
		    AD_PROPERTIES("sangforAD.properties"),
		    AD_KEY_STORE("ad.keystore");
	        
		    private final String scriptsDir = "scripts/network/sangforad";
	        private final String path;
	        
	        private fileName(String filename) {
	        	path = Script.findScript(scriptsDir, filename);
	        }
	        
	        public String getPath() {
	            return path;
	        }
	 }
	/**
	 * cookie类型枚举
	 */
	private enum LBCookieType {
		//插入
		LB_COOKIE_TYPE_INSERT("insert"),
		//被动
		LB_COOKIE_TYPE_STUDY("study"),
		//改写
		LB_COOKIE_TYPE_REWRITE("rewrite");
		
	    private String value;

	    LBCookieType(String v) {
	        value = v;
	    }
	    private  static LBCookieType fromValue(String v) {
	        for (LBCookieType c: LBCookieType.values()) {
	            if (c.value.equals(v)) {
	                return c;
	            }
	        }
	        throw new IllegalArgumentException(v);
	    }
	}
	
	/**
	 * 会话sourceip类型枚举
	 *
	 */
	private enum LBSourceBased {
		LB_NAME("name"),
		LB_SOURCE_IP_MASKV4("sourceip.maskv4"),
		LB_SOURCE_IP_MASKV6("sourceip.maskv6"),
		LB_SOURCE_IP_TIMEOUT("sourceip.timeout"),
		LB_PRIORT_TO_CONNECT("priorToConnect");
	    private String value;

	    LBSourceBased(String v) {
	        value = v;
	    }
	    private  static LBSourceBased fromValue(String v) {
	        for (LBSourceBased c: LBSourceBased.values()) {
	            if (c.value.equals(v)) {
	                return c;
	            }
	        }
	        throw new IllegalArgumentException(v);
	    }
	}
	
	/**
	 * 会话cookie类型枚举
	 * @author root
	 *
	 */
	private enum LBCookieBased {
		LB_NAME("name"),
		LB_COOKIE_NAME("cookie.name"),
		LB_COOKIE_TYPE("cookie.type"),
		LB_COOKIE_DOMAIN("cookie.domain"),
		LB_COOKIE_PATH("cookie.path"),
		LB_COOKIE_TIMEOUT("cookie.timeout"),
		LB_PRIORT_TO_CONNECT("priorToConnect");
	    private String value;

	    LBCookieBased(String v) {
	        value = v;
	    }
	    private  static LBCookieBased fromValue(String v) {
	        for (LBCookieBased c: LBCookieBased.values()) {
	            if (c.value.equals(v)) {
	                return c;
	            }
	        }
	        throw new IllegalArgumentException(v);
	    }
	}
	
	/**
	 * 配置设备信息
	 * 1.在添加网络设备的时候获取属性
	 * 2.注入到当前类的属性，并验证属性可用
	 * 3.初始化登录设备
	 * @param name  主机名
	 * @param params 添加设备参数信息
	 * @return 完成配置
	 * @throws ErrorOpr 
	 */
	@Override
	public boolean configure(String name, Map<String, Object> params) throws ConfigurationException {
		try {
			_username = (String) params.get("username");
    		if (_username == null) {
    			throw new ConfigurationException("Unable to find username");
    		}
    		_password = (String) params.get("password");
    		if (_password == null) {
    			throw new ConfigurationException("Unable to find password");
    		}   
			_name = (String) params.get("name");
    		if (_name == null) {
    			throw new ConfigurationException("Unable to find name");
    		}
    		
    		_zoneId = (String) params.get("zoneId");
    		if (_zoneId == null) {
    			throw new ConfigurationException("Unable to find zone");
    		}
    		
    		_ip = (String) params.get("ip");
    		if (_ip == null) {
    			throw new ConfigurationException("Unable to find IP");
    		}
    		
    		_numRetries = NumbersUtil.parseInt((String) params.get("numretries"), 1);
			    		
    		_guid = (String)params.get("guid");
            if (_guid == null) {
                throw new ConfigurationException("Unable to find the guid");
            }
            
            initLogin();
            
    		return Boolean.TRUE;
    		
    	}catch (Exception e) {
    		s_logger.error("Faild to configure . " + e.getMessage());
    		throw new ConfigurationException(e.getMessage());
    	}
	}
	
	/**
	 * 初始化启动命令
	 * 1.设置cmd初始化参数（添加网络设备会获取主机明细，获取DataCenter）
	 */
	@Override
	public StartupCommand[] initialize() {
		StartupExternalLoadBalancerCommand cmd = new StartupExternalLoadBalancerCommand();
		cmd.setName(_name);
		cmd.setDataCenter(_zoneId);
		cmd.setPod("");
    	cmd.setPrivateIpAddress(_ip);
    	cmd.setStorageIpAddress("");
    	cmd.setVersion(SangforADLoadBalancerResource.class.getPackage().getImplementationVersion());
    	cmd.setGuid(_guid);
    	return new StartupCommand[]{cmd};
	}
	
	/**
	 *  初始化登录
     * 1.登录设备，设置认证参数，用户名，密码
     * 2.获取api实例
     * 3.https认证证书配置
     * 4.验证登录：获取服务器列表验证登录可以正常调用
     * 5.获取sagnforad默认配置参数
	 * @throws ErrorOpr
	 * @throws IllegalStateException
	 * @throws MalformedURLException 
	 */
    private void initLogin() throws ErrorOpr,IllegalStateException, MalformedURLException{
    	if(_api == null){
    		if(_adapi == null){
    			_adapi = new  Adapi(new URL("file:"+fileName.AD_API_WSDL.getPath()));
        	}
    		_api = _adapi.getAdapi();
    	}
    	if(_login == null){
    		_login = new ADAuthType();
    		_login.setUsername(getByets(_username));
        	_login.setPasswd(getByets(_password));
    	}
    	if(_serviceInfoListType == null ){
    		configure();
    		_serviceInfoListType = getServInfoList();
    	}
    	if(_configProps == null){
    		getSangforAdConfigResource();
    	}
    }
	
    /**
     * SSL认证证书导入配置
     * @throws ErrorOpr
     * @throws IllegalStateException
     */
    private void configure() throws ErrorOpr,IllegalStateException {
		System.setProperty("javax.net.ssl.trustStoreType", "jks");
		System.setProperty("javax.net.ssl.trustStore",fileName.AD_KEY_STORE.getPath());
		System.setProperty("javax.net.ssl.trustStorePassword", "password");
	}
    
    /**
     * 获取sangforAd config配置文件资源
     * @return
     * @throws IllegalStateException
     */
    private void getSangforAdConfigResource() throws IllegalStateException{
    	 InputStream is = null;
    	 try {
    		 File props = new File(fileName.AD_PROPERTIES.getPath());
    		 if ( props != null && props.exists() ) {
                 is = new FileInputStream(props);
             }
             if (is != null ) {
            	 _configProps = new Properties();
             	 _configProps.load(is);
             }
             if(_configProps == null || _configProps.getProperty("sangforad.version") == null ){
             	s_logger.error("Failed to get sangforAD config properties" + _configProps);
             	throw new IllegalStateException("Failed to load sangforad.properties");
             }
             getSangforAdProperties();
             s_logger.info("successfully init .....");
		} catch (IOException e ) {
			s_logger.error("Failed to load sangforad.properties ");
            throw new IllegalStateException("Failed to load sangforad.properties ", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
    
	/**
	 * 获取sangforAd config配置属性
	 * @throws IllegalStateException
	 */
	private void getSangforAdProperties() throws IllegalStateException{
		String vsModeType = _configProps.getProperty("vs.mode.type");
        if(!stringIsNull(vsModeType)){
        	_vsModeType = VsModeType.fromValue(vsModeType);
        }
        String vsHttpSchedModeType = _configProps.getProperty("vs.http.sched.mode");
        if(!stringIsNull(vsHttpSchedModeType)){
        	 _vsHttpSchedModeType = HttpSchedModeType.fromValue(vsHttpSchedModeType);
        }
        _vsEnable = StringToBoolean(_configProps.getProperty("vs.enable"));
        _vsTcpCacheStreamEnable = StringToBoolean(_configProps.getProperty("vs.tcp.cache.stream.enable"));
        _vsTcpCacheNum = stringTOBigInteger(_configProps.getProperty("vs.tcp.cache.num"));
        _vsEndStr = _configProps.getProperty("vs.end.str");
        _vsPreRule = _configProps.getProperty("vs.pre.rule");
        _vsAutoSnatEnable = StringToBoolean(_configProps.getProperty("vs.auto.snat.enable"));
        _vsThreeTransferEnbale = StringToBoolean(_configProps.getProperty("vs.three.transfer.enable"));
        _vsSnatAddress = _configProps.getProperty("vs.snat.pool");
        String serServiceType = _configProps.getProperty("ser.service.type");
        if(!stringIsNull(serServiceType)){
        	 _serServiceType = ServiceType.fromValue(serServiceType);
        }
        _sslHttpRedirEnable = StringToBoolean(_configProps.getProperty("ssl.http.redir.enable"));
        _sslHttpRedirPort = stringTOBigInteger(_configProps.getProperty("ssl.http.redir.port"));
        _sslProfile = _configProps.getProperty("ssl.profile");
        String nodeRatio = _configProps.getProperty("node.ratio");
        if(!stringIsNull(nodeRatio)){
        	 _nodeRatio = Integer.valueOf(nodeRatio);
        }
        String nodeStatus = _configProps.getProperty("node.status");
        if(!stringIsNull(nodeStatus)){
        	 _nodeStatus = NodeConfStatusType.fromValue(nodeStatus);
        }
        _nodeMaxConnects = stringTOBigInteger(_configProps.getProperty("node.max.connects"));
        _nodeNewConnects = stringTOBigInteger(_configProps.getProperty("node.new.connects"));
        _nodeMaxRequest = stringTOBigInteger(_configProps.getProperty("node.max.request"));
        _nodeRsVal1 = _configProps.getProperty("node.rs.val1");
        String nodeHashType = _configProps.getProperty("node.hash.type");
        if(!stringIsNull(nodeHashType)){
        	_nodeHashType = NodeHashType.fromValue(nodeHashType);
        }	        
        _poolPersist2Name = _configProps.getProperty("pool.persist2.name");
        _poolMonitors = _configProps.getProperty("pool.monitors");
        _poolMinMonitors = stringTOBigInteger(_configProps.getProperty("pool.min.monitors"));
        String poolRecoverInterval = _configProps.getProperty("pool.recover.interval");
        if(!stringIsNull(poolRecoverInterval)){
        	 _poolRecoverInterval = Integer.valueOf(poolRecoverInterval);
        }
        String poolStepperInterval = _configProps.getProperty("pool.stepper.interval");
        if(!stringIsNull(poolStepperInterval)){
        	_poolStepperInterval = Integer.valueOf(poolStepperInterval);
        }
        String poolSchedMethod = _configProps.getProperty("pool.sched.method");
        if(!stringIsNull(poolSchedMethod)){
        	  _poolSchedMethod = SchedMethodType.fromValue(poolSchedMethod);
        }
        _poolConnStatAllEnable = StringToBoolean(_configProps.getProperty("pool.conn.stat.all.enable"));
        _poolQueueLength = stringTOBigInteger(_configProps.getProperty("pool.queue.length"));
        _poolQueueTimeout = stringTOBigInteger(_configProps.getProperty("pool.queue.timeout"));
        
        _groupNetifName = _configProps.getProperty("group.netif.name");
        
        _nodeMonitorDebugEnable = StringToBoolean(_configProps.getProperty("node.monitor.debug.enable"));
        String nodeMonitorType = _configProps.getProperty("node.monitor.type");
        if(!stringIsNull(nodeMonitorType)){
        	 _nodeMonitorType = MonitorType.fromValue(nodeMonitorType);
        }
        String nodePersistDefaultTimeout = _configProps.getProperty("node.persist.default.timeout");
        if(!stringIsNull(nodePersistDefaultTimeout)){
        	_nodePersistDefaultTimeout = Integer.valueOf(nodePersistDefaultTimeout);
        }
    }
	
	/**
	 * 执行请求
	 * @param Command子类实例
	 */
	@Override
	public Answer executeRequest(Command cmd) {
		return executeRequest(cmd, _numRetries);
	}
	
	/**
	 * 执行请求
	 * 1.获取Command子类实例
	 * 2.若为ReadyCommand实例，执行准备操作，直接返回ReadyAnswer
	 * 3.若为MaintainCommand实例，执行维护操作，直接返回MaintainAnswer
	 * 4.若为IpAssocCommand实例，执行Ip操作
	 * 5.若为LoadBalancerConfigCommand实例，执行负载均衡器配置
	 * 6.其他返回不支持Answer
	 * @param Command子类实例
	 * @param 请求执行次数
	 * @return Answer
	 */
	private Answer executeRequest(Command cmd, int numRetries) {
		   if (cmd instanceof ReadyCommand) {
	            return execute((ReadyCommand) cmd);
	        } else if (cmd instanceof MaintainCommand) {
	            return execute((MaintainCommand) cmd);
	        } else if (cmd instanceof IpAssocCommand) {
				return execute((IpAssocCommand) cmd);
			}  else if (cmd instanceof LoadBalancerConfigCommand) {
				return execute((LoadBalancerConfigCommand) cmd,numRetries);
			} else if (cmd instanceof ExternalNetworkResourceUsageCommand) {
				return execute((ExternalNetworkResourceUsageCommand) cmd);
			}else {
	            return Answer.createUnsupportedCommandAnswer(cmd);
			}
	}
	
	/**
	 * 执行准备操作
	 * 1.准备正常直接放回Boolean.TRUE
	 * 2.准备失败，返回Boolean.FALSE，失败的异常明细（例如：nullException）
	 * @param cmd
	 * @return
	 */
	private Answer execute(ReadyCommand cmd) {
	     return new ReadyAnswer(cmd);
	}
	
	/**
	 * 执行维护操作
	 * 1.是否迁移
	 * @param cmd
	 * @return
	 */
	private Answer execute(MaintainCommand cmd) {
	     return new MaintainAnswer(cmd);
	}
	
	/**
	 * 执行IP处理操作
	 * 来宾网络业务处理（创建网络，重启网络，修改网络）
	 * 1.获取Ips，tag，inline，vlanSelfIp，vlan网络掩码
	 * 2.删除任何已经存在来宾VLAN
	 * 3.添加一个新的来斌blan
	 * 4.组装PublicIp数组
	 * @param  
	 * @return
	 */
	private synchronized Answer execute(IpAssocCommand cmd) {
		 String[] results = new String[cmd.getIpAddresses().length];
         int i = 0;
         try {
             IpAddressTO[] ips = cmd.getIpAddresses();
             for (IpAddressTO ip : ips) {
                 results[i++] = ip.getPublicIp() + " - success";
             }
         } catch (Exception e) {
             s_logger.error("Failed to execute IPAssocCommand due to " + e);
         }
         return new IpAssocAnswer(cmd, results);
	}
    
	/**
	 * 外部网络资源使用情况
	 * 主要功能：设置来宾vlan，设在ip
	 * @param cmd
	 * @return
	 */
	private synchronized ExternalNetworkResourceUsageAnswer execute(ExternalNetworkResourceUsageCommand cmd) {
		return new ExternalNetworkResourceUsageAnswer(cmd);
	}
	  
	/**
	 * 执行负载均衡规则业务处理操作
	 * 1.获取负载均衡配置参数:VlanID,LoadBalancerTO(负载均衡协议添加到虚拟服务,负载均衡算法添加到池，获取虚拟服务名)，源IP，源端口
	 * 2.删除该虚拟服务协议,源IP,和源端口
	 * 3.添加服务，pool,节点，虚拟服务
	 * 4.调用sagnforAD的api接口，参数发送到sangforAD的api
	 * @param 负载均衡规则（数组封装规则）
	 * @param 规则执行次数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized Answer execute(LoadBalancerConfigCommand cmd,int numRetries) {
		try {
			long starttime = System.currentTimeMillis();
			long guestVlanTag = Long.parseLong(cmd.getAccessDetail(NetworkElementCommand.GUEST_VLAN_TAG));
			LoadBalancerTO[] loadBalancers = cmd.getLoadBalancers();
			for (LoadBalancerTO loadBalancer : loadBalancers) {
				//负载均衡协议
				LbProtocol lbProtocol = getlbProtocol(loadBalancer);
				//负载均衡算法
				NodeLbMethodType lbAlgorithm = getlbAlgorithm(loadBalancer);
				
				//获取服务名
				boolean inline = loadBalancer.isInline();
				String srcIp = inline ? tagAddressWithRouteDomain(loadBalancer.getSrcIp(), guestVlanTag) : loadBalancer.getSrcIp();
				int srcPort = loadBalancer.getSrcPort();
				Integer toPort = null;
				String vsServerName = genObjectName(LBPrefixName.cs_vs,lbProtocol, srcIp, srcPort);
				String ipGroupName = genObjectName(LBPrefixName.cs_group,lbProtocol, srcIp, srcPort);
				String poolNodeName = genObjectName(LBPrefixName.cs_pool ,lbProtocol, srcIp, srcPort);
				String serverName = genObjectName(LBPrefixName.cs_service,lbProtocol, srcPort, toPort);
				String nodeMonitorName = null;
				String persistName = "none";
				
				//是否为添加
				boolean destinationsToAdd = Boolean.FALSE;
				for (DestinationTO destination : loadBalancer.getDestinations()) {
					if (!destination.isRevoked()) {
						destinationsToAdd = Boolean.TRUE;
						break;
					}
				}
				
				initLogin();
				
				if (!loadBalancer.isRevoked() && destinationsToAdd) {
					ObjectVO objectVO = new ObjectVO();
					//若不存在添加服务
					String vsExistServerName = existService(srcPort, toPort);
					if(stringIsNull(vsExistServerName)){
						List<PortScopeType> portScopes = new ArrayList<PortScopeType>();
						PortScopeType portScope = objectVO.ServPortVO(srcPort,toPort);
						portScopes.add(portScope);
						ServiceInfoType serverInfo = objectVO.ServiceInfoVO(serverName,portScopes);
						createServ(serverInfo,serverName);
					} else {
						serverName = vsExistServerName;
					}
					
					//创建，修改粘性(会话保持)
					StickinessPolicyTO[] stickyPolicies = loadBalancer.getStickinessPolicies();
					if (stickyPolicies != null && stickyPolicies.length > 0 && stickyPolicies[0] != null){
						persistName = genObjectName(LBPrefixName.cs_persist,lbProtocol, srcPort, toPort);
						createOrUpdateStickinessPolicy(stickyPolicies,objectVO,persistName,poolNodeName);
			        }
					//创建，修改,删除健康检查(节点监视器)
					HealthCheckPolicyTO[] healthCheckPolicys = loadBalancer.getHealthCheckPolicies();
					if(healthCheckPolicys !=null && healthCheckPolicys.length > 0 && healthCheckPolicys[0] !=null){
						nodeMonitorName =  genObjectName(LBPrefixName.cs_monitor ,lbProtocol, srcPort, toPort);
						createOrUpdateHealthCheckPolicy(healthCheckPolicys,objectVO,nodeMonitorName,poolNodeName);
					}
					
					//添加,删除节点
					List<NodeInfoType> nodeInfos = new ArrayList<NodeInfoType>();
					for (DestinationTO destination : loadBalancer.getDestinations()) {
						String destIp = destination.getDestIp();
						int destPor = destination.getDestPort();
						if (!destination.isRevoked()) {
							NodeInfoType _node = objectVO.NodeVO(destIp, destPor);
							nodeInfos.add(_node);
						} else {
							NodeKeyType nodeKey = objectVO.NodeKeyVO(destIp, poolNodeName, destPor);
							deleteNode(nodeKey);
						}
					}
					
					//若不存在创建节点池,若存在则修改
					NodePoolCreateInfoType nodePool = objectVO.NodePoolVO(poolNodeName,persistName,nodeMonitorName,lbAlgorithm,nodeInfos);
					createOrUpdateNodePoolInfo(nodePool,poolNodeName);
					
					//若不存在创建Ip组
					IpInfoType ipInfo = objectVO.IpGroupIpVO((List<String>)objectToList(srcIp));
					List<IpInfoType> ipInfos = (List<IpInfoType>)objectToList(ipInfo);
					IpGroupInfoType ipGroup = objectVO.IpGroupVO(ipGroupName,ipInfos);
					createIpGroup(ipGroup,ipGroupName);
					
					//若不存在创建虚拟服务
					VsCreateInfoType vs = objectVO.VsVO(vsServerName,serverName,ipGroupName,poolNodeName);
					createVs(vs,vsServerName);
					
				} else {
					//若存在删除虚拟服务
					deleteVs(vsServerName);
					//若存在删除组
					deleteIpGroup(ipGroupName);
					//若存在删除节点
					deleteNodePoolInfo(poolNodeName);
					//删除会话保持
					deletePersist(genObjectName(LBPrefixName.cs_persist,lbProtocol, srcPort, toPort),poolNodeName);
					//删除节点监视器
					deleteNodeMonitor(genObjectName(LBPrefixName.cs_monitor,lbProtocol, srcPort, toPort),poolNodeName);
				}
			}
			long endtime = System.currentTimeMillis();
			s_logger.info("successful execute SangforADLoadBalancerResource ,The running time(ms) : " + (endtime -starttime));
			return new Answer(cmd);
		}catch (ErrorOpr e) {
			s_logger.error("Failed to Invok WebService due to " + e);
			if (shouldRetry(numRetries)) {
				return retry(cmd, numRetries);
			} else {
				return new Answer(cmd, e);
			}
		} catch (Exception e) {
			s_logger.error("Failed to execute LoadBalancerConfigCommand due to " + e);
			if (shouldRetry(numRetries)) {
				return retry(cmd, numRetries);
			} else {
				return new Answer(cmd, e);
			}
		}
	}
	
	/**
	 * 获取负载均衡协议
	 * @param loadBalancer
	 * @return
	 */
	private LbProtocol getlbProtocol(LoadBalancerTO loadBalancer) {
		LbProtocol lbProtocol = LbProtocol.tcp;
		try {
			if (loadBalancer.getProtocol() != null) {
				lbProtocol = LbProtocol.valueOf(loadBalancer.getProtocol());
			}
		} catch (IllegalArgumentException e) {
			s_logger.error("Got invalid protocol:"+ loadBalancer.getProtocol());
		}
		return lbProtocol;
	}
	
	/**
	 * 获取负载均衡算法
	 * @param loadBalancer
	 * @return
	 * @throws ExecutionException
	 */
	private  NodeLbMethodType getlbAlgorithm(LoadBalancerTO loadBalancer){
		NodeLbMethodType lbAlgorithm = NodeLbMethodType.NODE_LB_RR;
		try {
			LBAlgorithm lAlgorithm = LBAlgorithm.fromValue(loadBalancer.getAlgorithm());
			if (lAlgorithm == LBAlgorithm.LB_Roundrobin) {
				lbAlgorithm = NodeLbMethodType.NODE_LB_RR;
			} else if (lAlgorithm == LBAlgorithm.LB_leastconn) {
				lbAlgorithm = NodeLbMethodType.NODE_LB_LEAST_CONN;
			} else if (lAlgorithm==LBAlgorithm.LB_source) {
				lbAlgorithm = NodeLbMethodType.NODE_LB_HASH;
			}
		} catch (IllegalArgumentException e) {
			s_logger.error("Got invalid algorithm: " + loadBalancer.getAlgorithm());
		}
		return lbAlgorithm;
	}
	
	/**
	 * 粘性处理
	 * @param stickyPolicies
	 * @param objectVO
	 * @param persistName
	 * @throws ErrorOpr
	 * @throws ExecutionException
	 * @throws UnsupportedEncodingException 
	 */
	private void createOrUpdateStickinessPolicy(StickinessPolicyTO[] stickyPolicies,ObjectVO objectVO,String persistName,String nodePoolName) throws ErrorOpr, ExecutionException, UnsupportedEncodingException{
		StickinessPolicyTO stickinessPolicy = stickyPolicies[0];
        List<Pair<String, String>> paramsList = stickinessPolicy.getParams();
        
        Boolean priorToConnect = Boolean.FALSE;
        Integer holdtime = _nodePersistDefaultTimeout;
        if (StickinessMethodType.LBCookieBased.getName().equalsIgnoreCase(stickinessPolicy.getMethodName())) {
            String cookieType = "insert";
            String cookieName = "cookie-name";
            String cookieDomain = null;
            String cookiePath = "/";
            CookieType cookieTypes = CookieType.PERSIST_COOKIE_INSERT;
            if(paramsList != null && paramsList.size()>0 ){
            	 for(Pair<String,String> p : paramsList) {
                 	 String key = p.first();
                     String value = p.second();
                     LBCookieBased lCookieBased = LBCookieBased.fromValue(key);
                     if (lCookieBased == LBCookieBased.LB_COOKIE_TYPE) cookieType = value;
                     if (lCookieBased == LBCookieBased.LB_COOKIE_NAME) cookieName = value;
                     if (lCookieBased == LBCookieBased.LB_COOKIE_DOMAIN) cookieDomain = value;
                     if (lCookieBased == LBCookieBased.LB_COOKIE_PATH) cookiePath = value;
                     if (lCookieBased == LBCookieBased.LB_COOKIE_TIMEOUT) holdtime = Integer.parseInt(value);
                     if (lCookieBased == LBCookieBased.LB_PRIORT_TO_CONNECT) priorToConnect = Boolean.TRUE;
                 }
            	 if(cookieType != null){
     	            LBCookieType lbcCookieType = LBCookieType.fromValue(cookieType);
     	            if(lbcCookieType == LBCookieType.LB_COOKIE_TYPE_INSERT){
     	            	cookieTypes = CookieType.PERSIST_COOKIE_INSERT;
     	            } else if(lbcCookieType == LBCookieType.LB_COOKIE_TYPE_STUDY){
     	            	cookieTypes = CookieType.PERSIST_COOKIE_STUDY;
     	            } else if(lbcCookieType == LBCookieType.LB_COOKIE_TYPE_REWRITE){
     	            	cookieTypes = CookieType.PERSIST_COOKIE_REWRITE;
     	            }
                 }
            }
            PersistCookieType persistCookie = objectVO.PersistCookieVO(cookieTypes, cookieName, cookieDomain, cookiePath, holdtime);
            PersistInfoType persistInfo = objectVO.PersistInfoVO(PersistType.PERSIST_COOKIE, persistName, priorToConnect, null, persistCookie);
            createOrUpdatePersist(persistName,nodePoolName, persistInfo);
        } else if (StickinessMethodType.SourceBased.getName().equalsIgnoreCase(stickinessPolicy.getMethodName())) {
        	 String maskv4 = null;
             String maskv6 = null;
             if(paramsList != null && paramsList.size()>0 ){
            	 for(Pair<String,String> p : paramsList) {
                 	String key = p.first();
                     String value = p.second();
                     LBSourceBased lbSourceBased = LBSourceBased.fromValue(key);
                     if (lbSourceBased == LBSourceBased.LB_SOURCE_IP_MASKV4) maskv4 = value;
                     if (lbSourceBased == LBSourceBased.LB_SOURCE_IP_MASKV6) maskv6 = value;
                     if (lbSourceBased == LBSourceBased.LB_SOURCE_IP_TIMEOUT) holdtime = Integer.parseInt(value);
                     if (lbSourceBased == LBSourceBased.LB_PRIORT_TO_CONNECT) priorToConnect = Boolean.TRUE;
             	 }
             }
             PersistSourceIpType persistSourceIp = objectVO.PersistSourceIpVO(maskv4, maskv6, holdtime);
             PersistInfoType persistInfo = objectVO.PersistInfoVO(PersistType.PERSIST_SOURCE_IP, persistName, priorToConnect, persistSourceIp, null);
             createOrUpdatePersist(persistName,nodePoolName, persistInfo);
        } else {
        	throw new ExecutionException("Failed to get Stickiness method type = "+ stickinessPolicy.getMethodName());
        }
	}
	
	/**
	 * 创建,修改健康检查
	 * @param loadBalancer
	 * @param objectVO
	 * @param nodeMonitorName
	 * @throws IllegalStateException
	 * @throws ErrorOpr
	 * @throws MalformedURLException 
	 * @throws UnsupportedEncodingException 
	 */
	private void createOrUpdateHealthCheckPolicy(HealthCheckPolicyTO[] healthCheckPolicys,ObjectVO objectVO,String nodeMonitorName,String poolNodeName) throws ErrorOpr, MalformedURLException, UnsupportedEncodingException{
		HealthCheckPolicyTO health = healthCheckPolicys[0];
		boolean isRevoked = health.isRevoked();
		String  pingPath = health.getpingPath();
		int responseTime = health.getResponseTime();
		int interval = health.getHealthcheckInterval();
		int tryout = health.getUnhealthThresshold();//阈值
		String[] strArr = getIpAndPort(pingPath);
		String ip = strArr[1];
		String port = strArr[0];
		NodeMonitorBaseInfoType nodeMonitorBaseInfo = objectVO.nodeMonitorBaseInfoVO(interval, responseTime, tryout, ip, port);
		NodeMonitorInfoType nodeMonitorInfo = objectVO.nodeMonitorVO(nodeMonitorName,nodeMonitorBaseInfo);
		if(isRevoked){
			deleteNodeMonitor(nodeMonitorName,poolNodeName);
		}else {
			createOrUpdateNodeMonitor(nodeMonitorName,nodeMonitorInfo);
		}
	}
	
	 //以下为sangforAD接口操作
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//服务 
	
	/**
	 * 获取服务配置列表
	 * @return
	 * @throws ErrorOpr
	 */
	private ServiceInfoListType getServInfoList()  throws ErrorOpr {
		return _api.getServInfoList(_login);
	}
	
	/**
	 * 创建服务
	 * @param serverInfo
	 * @param serverName
	 * @throws ErrorOpr
	 */
	private void createServ(ServiceInfoType serverInfo,String serverName) throws ErrorOpr {
		ErrType _return = _api.createServ(serverInfo, _login);
        if(_return.getErrCode().intValue() != SUCCESS_OK){
        	 s_logger.error("Failed to  create service , return = " + _return);
        	 throw new ErrorOpr("Failed to  create service ,return = " + _return);
        }
		if (!existServPortInfo(serverName)) {
			s_logger.error("Failed to  create service , return = " + _return);
			throw new ErrorOpr("Failed to  create service =" + serverName);
		}
	}
	
	/**
	 * 验证服务端口已经存在（目的，重用服务）
	 * @param serverName
	 * @param srcPort
	 * @param toPort
	 * @return
	 * @throws ErrorOpr
	 * @throws UnsupportedEncodingException
	 */
	private String existService(int srcPort,Integer toPort)  throws ErrorOpr, UnsupportedEncodingException {
		ServiceInfoListType serviceInfoLis = getServInfoList();
		if(serviceInfoLis != null &&  serviceInfoLis.getServiceInfo() != null ){
			List<ServiceBaseInfoType> serviceList = serviceInfoLis.getServiceInfo();
			for (ServiceBaseInfoType  servicebaseInfo : serviceList) {
				if(servicebaseInfo.getType() == _serServiceType){
					byte[] bytes=servicebaseInfo.getName();
					String serName = new String(bytes,"UTF-8");
					PortListType portListType = getServPortInfo(serName);
					List<PortScopeType> ports = portListType.getPort();
					for (PortScopeType prot : ports) {
						if(prot.getFrom() == srcPort && prot.getTo() == toPort){
							return serName;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取服务信息
	 * @param _serverName
	 * @return
	 * @throws ErrorOpr
	 */
	private PortListType getServPortInfo(String serverName) throws ErrorOpr {
	   if(stringIsNull(serverName)){
		   return null;
	   }
       return _api.getServPortInfo(_login, getByets(serverName));
	}
	
	/**
	 * 存在服务
	 * @param serverName
	 * @return
	 * @throws ErrorOpr
	 */
	private boolean existServPortInfo(String serverName) {
		try {
			PortListType portListType = getServPortInfo(serverName);
			if(portListType == null) return Boolean.FALSE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
        return Boolean.TRUE;
	}
	
	//ip组
	
	/**
	 * 创建IP组
	 * @param ipGroup
	 * @param ipGroupName
	 * @throws ErrorOpr
	 */
	private void createIpGroup(IpGroupInfoType ipGroup,String ipGroupName)  throws ErrorOpr {
		if (!existIpInfoList(ipGroupName)) {
			ErrType _return = _api.createIpGroup(ipGroup,_login);
	        if(_return.getErrCode().intValue() != SUCCESS_OK){
	        	 s_logger.error("Failed to  create Ip Group , result=" + _return);
	        	 throw new ErrorOpr("Failed to  create ip group , result =" + _return);
	        }
			if (!existIpInfoList(ipGroupName)) {
				 s_logger.error("Failed to  create Ip Group , result=" + _return);
				 throw new ErrorOpr("Failed to  create ip group = " + ipGroupName);
			}
		}
	}
	
	/**
	 * 删除IP组
	 * @param ipGroupName
	 * @throws ErrorOpr
	 */
	private void deleteIpGroup(String ipGroupName)  throws ErrorOpr {
		if(existIpInfoList(ipGroupName)){
			 ErrType _return = _api.deleteIpGroup(getByets(ipGroupName), _login);
		     if(_return.getErrCode().intValue() != SUCCESS_OK){
	    		s_logger.error("Failed to  deleted IpGroup , result=" + _return);
			   	throw new ErrorOpr("Failed to  deleted ip group , result=" + _return);
		     }
		}
	}

	/**
	 * ip组
	 * @param ipGroupName
	 * @return
	 * @throws ErrorOpr
	 */
	private IpInfoListType getIpInfoList(String ipGroupName) throws ErrorOpr{
		  if(stringIsNull(ipGroupName))  return null;
		  return _api.getIpInfoList(_login,getByets(ipGroupName));
	}
	
	/**
	 * ip组是否存在
	 * @param ipGroupName
	 * @return
	 */
	private boolean existIpInfoList(String ipGroupName){
		try {
			IpInfoListType ipInfoListType = getIpInfoList(ipGroupName);
			if(ipInfoListType == null || ipInfoListType.getIpList()==null){
				return Boolean.FALSE;
			}
		} catch (ErrorOpr e) {
			return Boolean.FALSE;
		}
		 return Boolean.TRUE;
	}
	
	
	//节点池
	
	/**
	 * 创建节点池
	 * 1.判断节点池是否存在
	 * 2.创建节点池：调用sangforAD接口创建节点池
	 * 3.验证节点池是否创建成功
	 * @param _nodePool
	 * @param _poolNodeName
	 * @throws ErrorOpr
	 */
	private void createOrUpdateNodePoolInfo(NodePoolCreateInfoType nodePool,String poolNodeName) throws ErrorOpr {
		s_logger.info("Invoking createNodePoolInfo...");
		if(existNodePoolInfo(poolNodeName)){
			ObjectVO object = new ObjectVO();
			NodePoolViewInfoType nodePoolInfo = object.NodePoolViewVO(nodePool);
			updateNodePoolInfo(nodePoolInfo,poolNodeName);
			for (NodeInfoType nodeInfo : nodePool.getNodeInfo()) {
				NodeKeyType nodeKey = object.NodeKeyVO(nodeInfo.getAddr(), poolNodeName, nodeInfo.getPort());
				createNode(poolNodeName, nodeInfo,nodeKey);
			}
		}else{
			ErrType _return = _api.createNodePoolInfo(nodePool, _login);
	        if(_return.getErrCode().intValue() != SUCCESS_OK){
	        	s_logger.error("Failed to  create Node Pool ,result=" + _return);
	        	throw new ErrorOpr("Failed to  create Node Pool ,result=" + _return);
	        }
			if (!existNodePoolInfo(poolNodeName)) {
				s_logger.error("Failed to  create Node Pool ,result=" + _return);
				throw new ErrorOpr("Failed to  create Node Pool = " + poolNodeName);
			}
		}
	}
	
	/**
	 * 修改节点池（目前主要针对负载均衡算法）
	 * @param nodePoolInfo
	 * @param poolNodeName
	 * @throws ErrorOpr
	 */
	private void updateNodePoolInfo(NodePoolViewInfoType nodePoolInfo,String poolNodeName) throws ErrorOpr {
		 s_logger.info("Invoking updateNodePoolInfo...");
	     ErrType _return = _api.updateNodePoolInfo(getByets(poolNodeName), nodePoolInfo, _login);
	     if(_return.getErrCode().intValue() != SUCCESS_OK){
        	s_logger.error("Failed to  update Node Pool ,result=" + _return);
        	throw new ErrorOpr("Failed to  update Node Pool ,result=" + _return);
	     }
	}
	
	/**
	 * 删除节点池
	 * @param poolNodeName
	 * @throws ErrorOpr
	 */
	private void deleteNodePoolInfo(String poolNodeName) throws ErrorOpr {
		if(existNodePoolInfo(poolNodeName)){
			ErrType _return = _api.deleteNodePoolInfo(getByets(poolNodeName), _login);
	        if(_return.getErrCode().intValue() != SUCCESS_OK ){
	        	s_logger.error("Failed to  deleted Node Pool ,result=" + _return);
	        	throw new ErrorOpr("Failed to  deleted Node Pool ,result=" + _return);
	        }
		}
	}
	
	/**
	 * 获取节点池详细信息
	 * @param _nodePoolName
	 * @return
	 * @throws ErrorOpr
	 */
	private NodePoolViewInfoType  getNodePoolInfo(String nodePoolName) throws ErrorOpr {
		if(stringIsNull(nodePoolName)) return null;
		return _api.getNodePoolInfo(_login,getByets(nodePoolName));
	}
	
	/**
	 * 节点池是否存在
	 * @param _nodePoolName
	 * @return
	 */
	private boolean  existNodePoolInfo(String nodePoolName) {
		try {
			NodePoolViewInfoType nodePoolViewInfoType = getNodePoolInfo (nodePoolName);
			if(nodePoolViewInfoType == null) return Boolean.FALSE;
		} catch (ErrorOpr e) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	/**
	 * 创建节点
	 * @param nodePoolName
	 * @param nodeInfo
	 * @return
	 */
	private void  createNode(String poolNodeName ,NodeInfoType nodeInfo,NodeKeyType nodeKey) {
		try {
			if(!exitNodeInfo(nodeKey)){
		         ErrType _return = _api.createNode(getByets(poolNodeName), nodeInfo, _login);
		         if(_return.getErrCode().intValue() != SUCCESS_OK ){
		        	s_logger.error("Failed to  create Node  ,result=" + _return);
		        	throw new ErrorOpr("Failed to  create Node ,result=" + _return);
		         }
			}
		} catch (ErrorOpr e) {
			s_logger.error("This node pool name does not exist .");
		}
	}
	
	/**
	 * 删除节点
	 * @param nodeKey
	 * @throws ErrorOpr
	 */
	private void deleteNode(NodeKeyType nodeKey) throws ErrorOpr{
		if(exitNodeInfo(nodeKey)){
			ObjectVO objectVO = new ObjectVO();
			NodeKeyExtType nodeKeyExt = objectVO.nodeKeyExtVO(nodeKey.getIp(),nodeKey.getNodePoolName(), nodeKey.getPort().toString());
			ErrType _return = _api.deleteNode(nodeKeyExt, _login);
		    if(_return.getErrCode().intValue() != SUCCESS_OK ){
		        s_logger.error("Failed to  delete Node  ,result=" + _return);
		        throw new ErrorOpr("delete Node.result=" + _return);
	        }
		}
	}
	
	/**
	 * 获取节点池的节点信息
	 * @param _nodeKey
	 * @return
	 * @throws ErrorOpr
	 */
	private NodeInfoType getNodeInfo(NodeKeyType nodeKey) throws ErrorOpr {
	      return  _api.getNodeInfo(_login, nodeKey);
	}
	
	/**
	 *  存在节点
	 * @param nodeKey
	 * @return
	 */
	private boolean exitNodeInfo(NodeKeyType nodeKey){
		try {
			NodeInfoType nodeInfoType = getNodeInfo(nodeKey);
			if(nodeInfoType == null) return Boolean.FALSE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	    return Boolean.TRUE;
	}
	
	//虚拟服务
	
	/**
	 * 创建虚拟服务
	 * 1.验证虚拟服务是否存在
	 * 2.创建虚拟服务
	 * 3.验证虚拟服务创建成功
	 */
	private void createVs(VsCreateInfoType vs,String vsName) throws ErrorOpr {
		if (!existVsInfo(vsName)) {
		 	ErrType _return= _api.createVs(vs, _login);
			if(_return.getErrCode().intValue() != SUCCESS_OK){
				s_logger.error("Failed to  virtual service ,result=" + _return);
	             throw new ErrorOpr("Failed to  virtual service ,result=" + _return);
	        }
			if (!existVsInfo(vsName)) {
				s_logger.error("Failed to  virtual service ,result=" + _return);
				throw new ErrorOpr("Failed to  virtual service = " + vsName);
			}							
		}
	}
	
	/**
	 * 获取虚拟服务详细信息
	 * @param api
	 * @param _login
	 * @param _vsName
	 * @return
	 * @throws ErrorOpr
	 */
	private VsViewInfoType getVsInfo(String vsName) throws ErrorOpr {
		if(stringIsNull(vsName)) return null;
		return _api.getVsInfo(_login,getByets(vsName));
	}
	
	/**
	 * 虚拟服务是否存在
	 * @param vsName
	 * @return
	 */
	private boolean existVsInfo(String vsName)  {
		try {
			VsViewInfoType vsViewInfoType = getVsInfo(vsName);
			if(vsViewInfoType == null) return Boolean.FALSE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	/**
	 * 删除虚拟服务
	 * @param vsName
	 * @throws ErrorOpr
	 */
	private void deleteVs(String vsName) throws ErrorOpr {
		 if(existVsInfo(vsName)){
			 ErrType _return = _api.deleteVs(getByets(vsName), _login);
		     if(_return.getErrCode().intValue() != SUCCESS_OK){
		    	s_logger.error("Failed to  deleted virtual service , result=" + _return);
				throw new ErrorOpr("Failed to  deleted virtual service , result=" + _return);
		     }
		 }
	}
	
	//节点监视器
	
	/**
	 * 创建节点监视器
	 * @param monitorInfo
	 * @throws ErrorOpr
	 */
	private void createOrUpdateNodeMonitor(String monitorName,NodeMonitorInfoType monitorInfo) throws ErrorOpr{
		if(existNodeMonitorInfo(monitorName)){
	    	ObjectVO objectVO = new ObjectVO();
	    	NodeMonitorUpdateInfoType nodeMonitorUpdateInfo = objectVO.nodeMonitorUpdateInfoVO(monitorInfo);
	    	updateNodeMonitor(monitorName, nodeMonitorUpdateInfo);
	    } else {
	    	ErrType _return = _api.createNodeMonitor(monitorInfo, _login);
	        if(_return.getErrCode().intValue() != SUCCESS_OK){
		    	s_logger.error("Failed to  create  node monitor , result=" + _return);
				throw new ErrorOpr("Failed to  create node monitor , result=" + _return);
		   }
	    } 
	}
	
	/**
	 * 删除节点监视器
	 * @param monitorName
	 * @throws ErrorOpr
	 */
	private void  deleteNodeMonitor(String monitorName) throws ErrorOpr{
		 if(existNodeMonitorInfo(monitorName)){
			 ErrType _return = _api.deleteNodeMonitor(getByets(monitorName), _login);
			 if(_return.getErrCode().intValue() != SUCCESS_OK){
			     s_logger.error("Failed to  deleted  node monitor , result=" + _return);
				 throw new ErrorOpr("Failed to  deleted node monitor , result=" + _return);
			 }
		 }
	}
	
	/**
	 * 删除节点监视器(删除前更新节点池)
	 * @param monitorName
	 * @throws ErrorOpr
	 * @throws UnsupportedEncodingException 
	 */
	private void deleteNodeMonitor(String monitorName,String nodePoolName) throws ErrorOpr, UnsupportedEncodingException{
		 if(existNodeMonitorInfo(monitorName)){
			 if(existNodePoolInfo(nodePoolName)){
				 boolean isExistMonitor = Boolean.FALSE;
				  NodePoolViewInfoType nodePoolViewInfo = getNodePoolInfo(nodePoolName);
				  List<byte[]>  newMonitors = null;
				  if(nodePoolViewInfo !=null && nodePoolViewInfo.getMonitors() !=null){
					  List<byte[]> monitors = nodePoolViewInfo.getMonitors();
					  for (byte[] monitor : monitors) {
						  if(monitorName.equals(new String(monitor, "UTF-8"))){
							  isExistMonitor = Boolean.TRUE;
						  } else {
							  newMonitors = new ArrayList<byte[]>();
							  newMonitors.add(monitor);
						  }
					  }
				  }
				  if(isExistMonitor){
					  ObjectVO objectVO = new ObjectVO();
					  nodePoolViewInfo.setMonitors(newMonitors);
					  NodePoolCreateInfoType nodePool = objectVO.NodePoolCreateInfoVO(nodePoolViewInfo);
					  createOrUpdateNodePoolInfo(nodePool,nodePoolName);
				  }
			 }
			 deleteNodeMonitor(monitorName);
		 }
	}
	
	/**
	 * 获取节点监视器信息
	 * @param monitorName
	 * @return
	 * @throws ErrorOpr
	 */
	private NodeMonitorInfoType getNodeMonitorInfo(String monitorName) throws ErrorOpr{
		if(stringIsNull(monitorName)) return null;
		return _api.getNodeMonitorInfo(_login, getByets(monitorName));
	}
	
	/**
	 * 更新节点监视器
	 * @param monitorName
	 * @param monitorInfo
	 * @throws ErrorOpr
	 */
	private void updateNodeMonitor(String monitorName,NodeMonitorUpdateInfoType monitorInfo) throws ErrorOpr{
	     ErrType _return = _api.updateNodeMonitor(getByets(monitorName),monitorInfo, _login);
	     if(_return.getErrCode().intValue() != SUCCESS_OK){
		    	s_logger.error("Failed to  update  node monitor , result=" + _return);
				throw new ErrorOpr("Failed to  update node monitor , result=" + _return);
		  }
	}
	
	/**
	 * 存在节点监视器
	 * @param monitorName
	 * @return
	 */
	private boolean existNodeMonitorInfo(String monitorName){
		try {
			NodeMonitorInfoType nodeMonitorInfoType = getNodeMonitorInfo(monitorName);
			if(nodeMonitorInfoType == null) return Boolean.FALSE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
		return  Boolean.TRUE;
	}
	
	//会话保持
	
	/**
	 * 创建，修改会话保持
	 * @param persistName
	 * @param persistInfo
	 * @throws ErrorOpr
	 * @throws UnsupportedEncodingException 
	 */
	private void createOrUpdatePersist(String persistName,String nodePoolName,PersistInfoType persistInfo) throws ErrorOpr, UnsupportedEncodingException{
		if(!existPersistInfo(persistName)){
			 ErrType _return = _api.createPersist(persistInfo, _login);
	         if(_return.getErrCode().intValue() != SUCCESS_OK){
		    	s_logger.error("Failed to  create Persist , result=" + _return);
				throw new ErrorOpr("Failed to  create Persist , result=" + _return);
		     }
	         if(!existPersistInfo(persistName)){
		    	s_logger.error("Failed to  create Persist , result=" + _return);
				throw new ErrorOpr("Failed to  create Persist , result=" + _return);
			}
		} else {
			ObjectVO object = new ObjectVO();
			PersistInfoType  oldPersistInfo = getPersistInfo(persistName);
			if(!persistInfo.getPType().equals(oldPersistInfo.getPType())){
				deletePersist(persistName,nodePoolName);
				createOrUpdatePersist(persistName,nodePoolName,persistInfo);
			} else {
				PersistUpdateInfoType persistUpdateInfo = object.PersistUpdateInfoVO(persistInfo,oldPersistInfo);
				updatePersist(persistName, persistUpdateInfo);
			}
		}
	}
	
	/**
	 * 修改会话
	 * @param persistName
	 * @param persistInfo
	 * @throws ErrorOpr
	 */
	private void updatePersist(String persistName,PersistUpdateInfoType persistInfo) throws ErrorOpr{
	       ErrType _return = _api.updatePersist(getByets(persistName), persistInfo, _login);
	       if(_return.getErrCode().intValue() != SUCCESS_OK){
		    	s_logger.error("Failed to  update  Persist , result=" + _return);
				throw new ErrorOpr("Failed to  update Persist , result=" + _return);
		   }
	}
	
	/**
	 * 删除会话保持
	 * @param persistName
	 * @throws ErrorOpr
	 * @throws UnsupportedEncodingException 
	 */
	private void deletePersist(String persistName,String nodePoolName) throws ErrorOpr, UnsupportedEncodingException{
		if(existPersistInfo(persistName)){
			  if(existNodePoolInfo(nodePoolName)){
				  NodePoolViewInfoType nodePoolViewInfo = getNodePoolInfo(nodePoolName);
				  if(nodePoolViewInfo!=null && nodePoolViewInfo.getPersist1Name() !=null ){
					  String oldPersistName = new String(nodePoolViewInfo.getPersist1Name(), "UTF-8");
					  if(oldPersistName.equals(persistName)){
						  ObjectVO objectVO = new ObjectVO();
						  nodePoolViewInfo.setPersist1Name(getByets("none"));
						  NodePoolCreateInfoType nodePool = objectVO.NodePoolCreateInfoVO(nodePoolViewInfo);
						  createOrUpdateNodePoolInfo(nodePool,nodePoolName);
					  }
				  }
			  }
			  deletePersist(persistName);
		}
	}
	
	/**
	 * 删除节点会话保持
	 * @param persistName
	 * @throws ErrorOpr
	 */
	private void deletePersist(String persistName) throws ErrorOpr{
		 if(existPersistInfo(persistName)){
			 ErrType _return = _api.deletePersist(getByets(persistName), _login);
		     if(_return.getErrCode().intValue() != SUCCESS_OK){
		    	s_logger.error("Failed to  deleted  Persist , result=" + _return);
				throw new ErrorOpr("Failed to  deleted Persist , result=" + _return);
			 }
		 }
	}
	/**
	 * 获取会话保持信息
	 * @param persistName
	 * @return
	 * @throws ErrorOpr
	 */
	private PersistInfoType getPersistInfo(String persistName) throws ErrorOpr{
		  if(stringIsNull(persistName) || persistName.equals("none"))return null;
	      return _api.getPersistInfo(_login,getByets(persistName));
	}
	
	/**
	 * 存在会话保持
	 * @param persistName
	 * @return
	 * @throws ErrorOpr
	 */
	private boolean existPersistInfo(String persistName){
		try {
			PersistInfoType persistInfoType = getPersistInfo(persistName);
			if(persistInfoType == null ) return Boolean.FALSE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	    return Boolean.TRUE;
	}
	
	
	//object VO	
	
	private  class ObjectVO{
		
		//更新节点监视器update VO
		private NodeMonitorUpdateInfoType nodeMonitorUpdateInfoVO(NodeMonitorInfoType nodeMonitor){
			Gson gson = new Gson();
			return gson.fromJson(gson.toJson(nodeMonitor), NodeMonitorUpdateInfoType.class);  
		}
		
		//节点监视器base VO
		private NodeMonitorBaseInfoType nodeMonitorBaseInfoVO(int interval,int timeout,int tryout,String addr,String port){
			NodeMonitorBaseInfoType nodeMonitorBaseInfo = new NodeMonitorBaseInfoType();
		    nodeMonitorBaseInfo.setType(_nodeMonitorType);
		    nodeMonitorBaseInfo.setInterval(interval);
		    nodeMonitorBaseInfo.setTimeout(timeout);
		    nodeMonitorBaseInfo.setTryout(tryout);
		    nodeMonitorBaseInfo.setAddr(addr);
		    nodeMonitorBaseInfo.setPort(port);
		    nodeMonitorBaseInfo.setDebug(_nodeMonitorDebugEnable);
			return nodeMonitorBaseInfo;
		}
		
		//节点监视器node VO
		private NodeMonitorInfoType nodeMonitorVO(String monitorName,NodeMonitorBaseInfoType nodeMonitorBaseInfo){
			NodeMonitorInfoType nodeMonitorInfo = new NodeMonitorInfoType();
			nodeMonitorInfo.setMonitorName(getByets(monitorName));
			nodeMonitorInfo.setBaseInfo(nodeMonitorBaseInfo);
			nodeMonitorInfo.setConnectExt(null);
			nodeMonitorInfo.setSnmpExt(null);
			nodeMonitorInfo.setDnsExt(null);
			nodeMonitorInfo.setRadiusExt(null);
			nodeMonitorInfo.setFtpExt(null);
			nodeMonitorInfo.setTcphalfExt(null);
			return nodeMonitorInfo;
		}
		
		//会话保持cookie VO
		private PersistCookieType PersistCookieVO(CookieType cookieType,String cookIeName,String cookieDomain,String cookiePath,Integer cookieTimeout){
			PersistCookieType _persistCookie = new PersistCookieType();
			//保持方式
			_persistCookie.setCookieType(cookieType);
			_persistCookie.setCookieName(getByets(cookIeName));
			//域名（选择插入方式时填写）
			_persistCookie.setCookieDomain(cookieDomain);
			_persistCookie.setCookiePath(cookiePath);
			//超时时间（单位：s）
			_persistCookie.setCookieTimeout(cookieTimeout);
			return _persistCookie;
		}
		
		//会话保持source VO
		private PersistSourceIpType PersistSourceIpVO(String maskv4,String maskv6,Integer sourceipTimeout){
			PersistSourceIpType _persistSourceIp = new PersistSourceIpType();
			//ipv4掩码
			_persistSourceIp.setMaskV4(maskv4);
			//ipv6掩码
			_persistSourceIp.setMaskV6(maskv6);
			//超时时间（单位：s）
			_persistSourceIp.setSourceipTimeout(sourceipTimeout);
			return _persistSourceIp;
		}
		
		//会话保持VO
		private PersistInfoType PersistInfoVO(PersistType persistType,String persistName,Boolean priorToConnect,PersistSourceIpType persistSourceIp,PersistCookieType persistCookie){
			PersistInfoType _persistInfo = new PersistInfoType();
			_persistInfo.setPersistName(getByets(persistName));
			//会话保持类型
			_persistInfo.setPType(persistType);
			//优先于连接（启用/禁用）
			_persistInfo.setPriorToConnect(priorToConnect);
			_persistInfo.setSourceIp(persistSourceIp);
			_persistInfo.setCookie(persistCookie);
			return _persistInfo;
		}
		
		//修改会话保持update VO
		private  PersistUpdateInfoType PersistUpdateInfoVO(PersistInfoType persistInfo,PersistInfoType  oldPersistInfo) {
			Gson gson = new Gson();
			PersistSourceIpType oldPersistSourceIp = oldPersistInfo.getSourceIp();
			if(persistInfo.getPType() == PersistType.PERSIST_SOURCE_IP && oldPersistSourceIp != null){
				PersistSourceIpType persistSourceIp = persistInfo.getSourceIp();
				if(stringIsNull(persistSourceIp.getMaskV4())){
					persistSourceIp.setMaskV4(oldPersistSourceIp.getMaskV4());
				}
				if(stringIsNull(persistSourceIp.getMaskV6())){
					persistSourceIp.setMaskV6(oldPersistSourceIp.getMaskV6());
				}
				if(persistSourceIp.getSourceipTimeout() == null){
					persistSourceIp.setSourceipTimeout(oldPersistSourceIp.getSourceipTimeout());
				}
				persistInfo.setSourceIp(persistSourceIp);
			}
			PersistCookieType oldPersistCookie = oldPersistInfo.getCookie();
			if(persistInfo.getPType() == PersistType.PERSIST_COOKIE && oldPersistCookie != null ){
				PersistCookieType persistCookie = persistInfo.getCookie();
				if(persistCookie.getCookieName() == null){
					persistCookie.setCookieName(oldPersistCookie.getCookieName());
				}
				if(stringIsNull(persistCookie.getCookiePath())){
					persistCookie.setCookiePath(oldPersistCookie.getCookiePath());
				}
				if(stringIsNull(persistCookie.getCookieDomain())){
					persistCookie.setCookieDomain(oldPersistCookie.getCookieDomain());
				}
				if(persistCookie.getCookieTimeout() == null ){
					persistCookie.setCookieTimeout(oldPersistCookie.getCookieTimeout());
				}
				if(persistCookie.getCookieType() == null){
					persistCookie.setCookieType(oldPersistCookie.getCookieType());
				}
				persistInfo.setCookie(persistCookie);
			}
			return gson.fromJson(gson.toJson(persistInfo), PersistUpdateInfoType.class);  
		}
		
		//添加服务端口
		private  PortScopeType  ServPortVO(int fromPor,Integer toPor){
			PortScopeType _portScopeType = new PortScopeType();
			_portScopeType.setFrom(fromPor);
			_portScopeType.setTo(toPor);
			return _portScopeType;
		}
		
		//创建服务VO
		private  ServiceInfoType  ServiceInfoVO(String serviceName,List<PortScopeType> portScopes){
			ServiceInfoType _serInfo = new ServiceInfoType();
			_serInfo.setName(getByets(serviceName));
			_serInfo.setType(_serServiceType);
			_serInfo.setPortScope(portScopes);
			return _serInfo;
		}
		
		//创建虚拟服务VO
		private  VsCreateInfoType VsVO(String vsServerName,String serviceName,String ipGroupName ,String nodePoolName){
			VsCreateInfoType _vs = new VsCreateInfoType();
	        _vs.setName(getByets(vsServerName));
	        _vs.setEnable(_vsEnable);
	        _vs.setMode(_vsModeType);
	        //http调度方式
	        if(_serServiceType == ServiceType.SRV_HTTP || _serServiceType == ServiceType.SRV_HTTPS){
	        	_vs.setHttpSchedMode(_vsHttpSchedModeType);
	        }
	        //是否开启TCP流
	        _vs.setTcpCacheStream(_vsTcpCacheStreamEnable);
	        //TCP缓冲流大小
	        _vs.setTcpCacheNum(_vsTcpCacheNum);
	        //结束字符
	        if(!stringIsNull(_vsEndStr)){
	        	 _vs.setEndStr(_vsEndStr);
	        }
	        //前置调度策略,只能引用与虚拟服务相同服务类型的前置调度策略
	        _vs.setPreRule(getListByte(_vsPreRule));
	        //服务名称
	        _vs.setServiceName(getByets(serviceName));
	        _vs.setIpGroupName(getByets(ipGroupName));
	        _vs.setNodePoolName(getByets(nodePoolName));
	        //自动snat
	        _vs.setAutoSnat(_vsAutoSnatEnable);
	        if(_vsAutoSnatEnable == Boolean.FALSE){
	        	 //snat地址集名称
	            _vs.setSnatPool(getByets(_vsSnatAddress));
	        }
	        //是否开启三角传输  注：4层虚拟服务有效
	        _vs.setThreeTransfer(_vsThreeTransferEnbale);
	        //ssl策略， 是否启用自动HTTPS跳转（Boolean.TRUE/Boolean.FALSE）  HTTP跳转端口
	        if(!stringIsNull(_sslProfile)){
	        	 _vs.setSsl(SSLAttrVO());
	        }
	        return _vs;
		}
		
		//ssl策略
		private  SSLAttrType SSLAttrVO(){
			//ssl策略，是否启用自动HTTPS跳转（Boolean.TRUE/Boolean.FALSE）  HTTP跳转端口
			SSLAttrType _sslAttrType = new SSLAttrType();
	        _sslAttrType.setHttpRedirEnable(_sslHttpRedirEnable);
	        _sslAttrType.setPort(_sslHttpRedirPort);
	        _sslAttrType.setSslProfile(getByets(_sslProfile));
	        return _sslAttrType;
		}
		
		//创建IP组Ip的VO
		private  IpInfoType IpGroupIpVO(List<String> ipAddrs){
			IpInfoType _ipInfo = new IpInfoType();
			_ipInfo.setNetifName(getByets(_groupNetifName));
			_ipInfo.setIpAddr(ipAddrs);
			return _ipInfo;
		}
				
		//创建IP组VO
		private  IpGroupInfoType IpGroupVO(String ipGroupName,List<IpInfoType> ipInfos){
	        IpGroupInfoType _ipGroup = new IpGroupInfoType();
	        _ipGroup.setName(getByets(ipGroupName));
		    _ipGroup.setIpInfo(ipInfos);
	        return _ipGroup;
		}
		
		//创建节点VO
		private  NodeInfoType  NodeVO(String srcIp,int srcPort){
			NodeInfoType _node = new NodeInfoType();
			_node.setStatus(_nodeStatus);//状态启用
			_node.setRatio(_nodeRatio);//权重
			_node.setMaxConnects(_nodeMaxConnects);
			_node.setNewConnects(_nodeNewConnects);
			_node.setMaxRequest(_nodeMaxRequest);
			_node.setRsVal1(getByets(_nodeRsVal1));
			_node.setAddr(srcIp);
			_node.setPort(srcPort);
	        return _node;
		}
				
		//创建节点池VO
		private  NodePoolCreateInfoType NodePoolVO(String poolNodeName,String poolPersist1Name,String nodeMonitorName,NodeLbMethodType lbAlgorithm,List<NodeInfoType> nodeInfos){
			NodePoolCreateInfoType _nodePool = new NodePoolCreateInfoType();
	        _nodePool.setNodePoolName(getByets(poolNodeName));
	        _nodePool.setNodeLbMethod(lbAlgorithm);
	        _nodePool.setHashType(_nodeHashType);
	        if(!existPersistInfo(poolPersist1Name)){
	        	poolPersist1Name = "none";
	        }
	        _nodePool.setPersist1Name(getByets(poolPersist1Name));
	        _nodePool.setPersist2Name(getByets(_poolPersist2Name));
	        //节点监视器名称
	        if(!stringIsNull(_poolMonitors)){
	        	if(!stringIsNull(nodeMonitorName) && existNodeMonitorInfo(nodeMonitorName)){
	        		nodeMonitorName +=","+ _poolMonitors;
	        	} else {
	        		nodeMonitorName = _poolMonitors;
	        	}
	        }
	        _nodePool.setMonitors(getListByte(nodeMonitorName));
	        _nodePool.setMinMonitors(_poolMinMonitors);
	        _nodePool.setRecoverInterval(_poolRecoverInterval);
	        _nodePool.setStepperInterval(_poolStepperInterval);
	        _nodePool.setSchedMethod(_poolSchedMethod);
	        _nodePool.setConnStatAll(_poolConnStatAllEnable);
	        _nodePool.setQueueLength(_poolQueueLength);
	        _nodePool.setQueueTimeout(_poolQueueTimeout);
	        _nodePool.setNodeInfo(nodeInfos);
	        return _nodePool;
		}
		
		//节点池key
		private NodeKeyExtType nodeKeyExtVO(String ip,byte[] poolName,String port){
			NodeKeyExtType _nodeKeyExt = new NodeKeyExtType();
			_nodeKeyExt.setIp(ip);
			_nodeKeyExt.setNodePoolName(poolName);
			_nodeKeyExt.setPort(port);
			return _nodeKeyExt;
		}
		
		//节点池key
		private NodeKeyType NodeKeyVO(String ip,String poolName,Integer port){
			NodeKeyType _nodeKey = new NodeKeyType();
			_nodeKey.setIp(ip);
			_nodeKey.setNodePoolName(getByets(poolName));
			_nodeKey.setPort(port);
			return _nodeKey;
		}
		
		//修改节点池VO
		private  NodePoolViewInfoType NodePoolViewVO(NodePoolCreateInfoType nodePool) {
			Gson gson = new Gson();
			return gson.fromJson(gson.toJson(nodePool), NodePoolViewInfoType.class);  
		}
		
		//显示节点池VO
		private  NodePoolCreateInfoType NodePoolCreateInfoVO( NodePoolViewInfoType nodePoolViewInfo) {
			Gson gson = new Gson();
			return gson.fromJson(gson.toJson(nodePoolViewInfo), NodePoolCreateInfoType.class);  
		}
		
	}
	
	//////////////////////////////utils//////////////////////////////
	/**
	 * 重操作发送命令
	 * 执行一次重新操作命令，总重试数量减1
	 * @param cmd
	 * @param numRetries
	 * @return
	 */
	private Answer retry(Command cmd,int numRetries) {
		int numRetriesRemaining = numRetries - 1;
		s_logger.error("Retrying " + cmd.getClass().getSimpleName() + ". Number of retries remaining: " + numRetriesRemaining);
		return executeRequest(cmd,numRetriesRemaining);	
	}
	
	/**
	 * 验证命令发送失败是否重新操作
	 * @param numRetries
	 * @return
	 */
	private boolean shouldRetry(int numRetries) {
        if (numRetries > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
	}
	
	/**
	 * object转list
	 * @param args
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  List<?>  objectToList(Object... args) {
		List lists=new ArrayList();
		for (int i = 0; i < args.length; i++) {
			lists.add(args[i]);
		}
		return lists;
	}
	
	/**
	 * 获取多个监视器的名称列表
	 * @param strArry
	 * @return
	 */
	private List<byte[]> getListByte(String strArry){
		List<byte[]> bytes = new ArrayList<byte[]>();
        if(!stringIsNull(strArry)){
        	 String[] strArr= strArry.split(",");
        	 for (int i = 0; i < strArr.length; i++) {
        		 bytes.add(getByets(strArr[i]));
			}
        }
        if(bytes !=null && bytes.size()>0){
        	return bytes;
        }
        return null;
	}
	
	/**
	 * 获取ip，port
	 * @param s
	 * @return
	 */
	private String[] getIpAndPort(String s){
		if(stringIsNull(s) || s.equals("/")){
			return new String[]{null,"*"};
		}
		try {
			URL url= new URL(s);
			int port = url.getPort();
			if(port > 0){
				return new String[]{String.valueOf(port),url.getHost()};
			}
		}catch (Exception e) {
			return new String[]{null,s};
		}
		return new String[]{null,"*"};
	}
	
	/**
	 * 获取对象名称
	 * @param args
	 * @return 例如：vs-tcp-192.168.2.5-80
	 */
	private String genObjectName(Object... args) {
		String objectName = "";
		for (int i = 0; i < args.length; i++) {
			if(args[i] != null){
				objectName += args[i];
			}
			if ( i < args.length-1 && args[i+1] != null) {
				objectName += _objectNamePathSep;
			}
		}
		return objectName;
	}
	
	/**
	 * 组装IP和vlanTag
	 * @param address
	 * @param vlanTag
	 * @return address+%+vlanTag
	 */
	private String tagAddressWithRouteDomain(String address, long vlanTag) {
		return address + _routeDomainIdentifier + vlanTag;
	}
	
	/**
	 * 获取byte数组
	 * @param s
	 * @return
	 */
	private  byte[] getByets(String s) {
		if(stringIsNull(s)){
			return null;
		}
		return  s.getBytes();
	}
	
	/**
	 * string转为BigInteger
	 * @param s
	 * @return
	 */
	private BigInteger stringTOBigInteger(String s){
		if(stringIsNull(s)){
			return null;
		}else {
			return new BigInteger(s);
		}
	}
	
	/**
	 * string转为boolean
	 * @param s
	 * @return
	 */
	private  boolean StringToBoolean(String s) {
		if(stringIsNull(s)){
			return Boolean.FALSE;
		}
		if(s.equalsIgnoreCase("true")){
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	/**
	 * 判断字符是否为null
	 * @param boolean
	 * @return
	 */
	private boolean stringIsNull(String s) {
		if(s == null || s.equals("") || s.trim().equals("")){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
   /**
    * 测试用例入口
    * @param username
    * @param password
    * @throws IllegalStateException
    * @throws ErrorOpr
 * @throws MalformedURLException 
    */
   public void testInit(String username,String password) throws IllegalStateException, ErrorOpr, MalformedURLException{
	   _username = username;
   	   _password = password;
	   initLogin();
   }
	
	@Override
	public String getName() {
		return _name;
	}

	@Override
	public boolean start() {
		return Boolean.TRUE;
	}

	@Override
	public boolean stop() {
		return Boolean.TRUE;
	}

	@Override
	public Type getType() {
		return Host.Type.ExternalLoadBalancer;
	}
	
	@Override
	public PingCommand getCurrentStatus(long id) {
		return new PingCommand(Host.Type.ExternalLoadBalancer, id);
	}

	@Override
	public void disconnected() {
		return;
	}

	@Override
	public IAgentControl getAgentControl() {
		return null;
	}

	@Override
	public void setAgentControl(IAgentControl agentControl) {
		return;
	}
	
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
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
	public int getRunLevel() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setRunLevel(int level) {
		// TODO Auto-generated method stub
	}
}