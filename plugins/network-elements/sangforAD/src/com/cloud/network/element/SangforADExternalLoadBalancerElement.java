package com.cloud.network.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.inject.Inject;

import org.apache.cloudstack.framework.config.dao.ConfigurationDao;
import org.apache.cloudstack.network.ExternalNetworkDeviceManager.NetworkDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloud.agent.api.to.LoadBalancerTO;
import com.cloud.api.ApiDBUtils;
import com.cloud.api.commands.AddSangforADLoadBalancerCmd;
import com.cloud.api.commands.ConfigureSangforADLoadBalancerCmd;
import com.cloud.api.commands.DeleteSangforADLoadBalancerCmd;
import com.cloud.api.commands.ListSangforADLoadBalancerNetworksCmd;
import com.cloud.api.commands.ListSangforADLoadBalancersCmd;
import com.cloud.api.response.SangforADLoadBalancerResponse;
import com.cloud.configuration.Config;
import com.cloud.configuration.ConfigurationManager;
import com.cloud.dc.DataCenter;
import com.cloud.dc.dao.DataCenterDao;
import com.cloud.deploy.DeployDestination;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.InsufficientNetworkCapacityException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.host.Host;
import com.cloud.host.dao.HostDao;
import com.cloud.host.dao.HostDetailsDao;
import com.cloud.network.ExternalLoadBalancerDeviceManager;
import com.cloud.network.ExternalLoadBalancerDeviceManagerImpl;
import com.cloud.network.Network;
import com.cloud.network.Network.Capability;
import com.cloud.network.Network.Provider;
import com.cloud.network.Network.Service;
import com.cloud.network.NetworkModel;
import com.cloud.network.Networks.TrafficType;
import com.cloud.network.PhysicalNetwork;
import com.cloud.network.PhysicalNetworkServiceProvider;
import com.cloud.network.PublicIpAddress;
import com.cloud.network.dao.ExternalLoadBalancerDeviceDao;
import com.cloud.network.dao.ExternalLoadBalancerDeviceVO;
import com.cloud.network.dao.ExternalLoadBalancerDeviceVO.LBDeviceState;
import com.cloud.network.dao.NetworkDao;
import com.cloud.network.dao.NetworkExternalLoadBalancerDao;
import com.cloud.network.dao.NetworkExternalLoadBalancerVO;
import com.cloud.network.dao.NetworkServiceMapDao;
import com.cloud.network.dao.NetworkVO;
import com.cloud.network.dao.PhysicalNetworkDao;
import com.cloud.network.dao.PhysicalNetworkVO;
import com.cloud.network.lb.LoadBalancingRule;
import com.cloud.network.resource.SangforADLoadBalancerResource;
import com.cloud.network.rules.LbStickinessMethod;
import com.cloud.network.rules.LbStickinessMethod.StickinessMethodType;
import com.cloud.network.rules.LoadBalancerContainer;
import com.cloud.offering.NetworkOffering;
import com.cloud.utils.NumbersUtil;
import com.cloud.utils.exception.CloudRuntimeException;
import com.cloud.vm.NicProfile;
import com.cloud.vm.ReservationContext;
import com.cloud.vm.VirtualMachineProfile;
import com.google.gson.Gson;
/**
 * @author root
 * @Date 2015/06/12
 * 新增外部网络设备的业务逻辑层
 * 1.负载均衡（LoadBalancer）业务规则
 * 2.端口转发（PortForward）业务规则
 * 3.地址转换（StaticNat，SourceNat）业务规则
 * 
 */
@Local(value = {NetworkElement.class, LoadBalancingServiceProvider.class,IpDeployer.class})
public class SangforADExternalLoadBalancerElement extends ExternalLoadBalancerDeviceManagerImpl implements LoadBalancingServiceProvider,IpDeployer, 
SangforADLoadBalancerElementService, ExternalLoadBalancerDeviceManager {
	
	private static final Logger s_logger = LoggerFactory.getLogger(SangforADExternalLoadBalancerElement.class);
	@Inject
	NetworkModel _networkManager;
	@Inject
	ConfigurationManager _config;
	@Inject
	NetworkServiceMapDao _ntwkSrvcDao;
	@Inject
	DataCenterDao _dcDao;
	@Inject
	PhysicalNetworkDao _physicalNetworkDao;
	@Inject
	HostDao _hostDao;
	@Inject
	ExternalLoadBalancerDeviceDao _lbDeviceDao;
	@Inject
	NetworkExternalLoadBalancerDao _networkLBDao;
	@Inject
	NetworkDao _networkDao;
	@Inject
	HostDetailsDao _detailsDao;
	@Inject
	ConfigurationDao _configDao;
	
    /**
     * 验证负载均衡规则可以处理
     * 1.验证网络类型（来宾网络流量类型，隔离，共享模式）
     * 2.验证负载均衡规则：是否包含public方案在设备能力范围内
     * 3.验证网络提供者（根据SangforADLoadBalancer提供者，网络ID 判断是否提供者网络，LB是否支持的网络）
     * 
     * @param  网络（提供网络类型，网络ID）
     * @param  负载均衡规则列表
     * @return 可以处理负载均衡规则
     */
	private boolean canHandle(Network config, List<LoadBalancingRule> rules){
		 if ((config.getGuestType() != Network.GuestType.Isolated && config.getGuestType() != Network.GuestType.Shared) || config.getTrafficType() != TrafficType.Guest) {
			s_logger.trace("Not handling network with Type  " + config.getGuestType() + " and traffic type " + config.getTrafficType());
			return false;
		}
		Map<Capability, String> lbCaps =  this.getCapabilities().get(Service.Lb);
		if (!lbCaps.isEmpty()) {
            String schemeCaps = lbCaps.get(Capability.LbSchemes);//方案public
            if (schemeCaps != null && rules != null && !rules.isEmpty()) {
                for (LoadBalancingRule rule : rules) {
                    if (!schemeCaps.contains(rule.getScheme().toString())) {
                        s_logger.debug("Scheme " + rules.get(0).getScheme() + " is not supported by the provider " + this.getName());
                        return false;
                    }
                }
            }
        }
		return (_networkManager.isProviderForNetwork(getProvider(), config.getId()) && _ntwkSrvcDao.canProviderSupportServiceInNetwork(config.getId(), Service.Lb, Network.Provider.SangforADLoadBalancer));
    }
	
	/**
	 * 重启，更新网络
	 * 1.验证是否可以处理
	 * 2.管理来宾网络下的外部网络设备负载均衡（交给父类处理：只支持来并网络，获取LB设备，为网络分配负载均衡器，将命令发送到外部负载均衡器实现或关闭客户网络）
	 * 
	 * @param 来宾网络信息
	 * @param 网络方案（这里没有使用，不需要关心）
	 * @param 部署目标（这里没有使用，不需要关心）
	 * @param ReservationContext（这里没有使用，不需要关心）
	 * @return boolean
	 * @throws ResourceUnavailableException, ConcurrentOperationException,InsufficientNetworkCapacityException
	 */
	@Override
	public boolean implement(Network guestConfig, NetworkOffering offering, DeployDestination dest, ReservationContext context) throws ResourceUnavailableException, ConcurrentOperationException,InsufficientNetworkCapacityException {
        if (!canHandle(guestConfig, null)) {
            return false;
        }
        try {
            return manageGuestNetworkWithExternalLoadBalancer(true, guestConfig);
        } catch (InsufficientCapacityException capacityException) {
            throw new ResourceUnavailableException("There are no SangforAD load balancer devices with the free capacity for implementing this network", DataCenter.class, guestConfig.getDataCenterId());
        }
    }
	
	/**
	 * 关闭网络
	 */
	@Override
	public boolean shutdown(Network guestConfig, ReservationContext context, boolean cleanup) throws ResourceUnavailableException, ConcurrentOperationException {
		if(!canHandle(guestConfig, null)){
			return false;
		}
		try{
			return manageGuestNetworkWithExternalLoadBalancer(false, guestConfig);
		} catch (InsufficientCapacityException capacityException) {
			return false;
		}
	}

	/**
	 * 准备
	 */
	@Override
	public boolean prepare(Network config, NicProfile nic, VirtualMachineProfile vm, DeployDestination dest, ReservationContext context) throws ConcurrentOperationException,
	InsufficientNetworkCapacityException, ResourceUnavailableException {
		return true;
    }

	/**
	 * 释放
	 */
	@Override
	public boolean release(Network config, NicProfile nic, VirtualMachineProfile vm, ReservationContext context) {
		return true;
	}
	
	/**
	 * 销毁
	 */
	@Override
	public boolean destroy(Network config, ReservationContext context) {
		return true;
	}
    
	/**
	 *验证负载均衡规则
	 *1.canHandle可以处理
	 *2.负载均衡规则算法为论寻或者最少连接
	 * @param 来宾网络
	 * @param 负载均衡规则
	 * @return 验证规则通过
	 */
	@Override
	public boolean validateLBRule(Network network, LoadBalancingRule rule) {
		if(canHandle(network, new ArrayList<LoadBalancingRule>(Arrays.asList(rule)))){
			String algo = rule.getAlgorithm();
			return (algo.equals("roundrobin") || algo.equals("leastconn"));
		}
		return true;
	}
	
	/**
	 * 应用负载均衡规则
	 * 1.验证可以处理规则
	 * 2.组装规则
	 * 3.发送规则到代理交给resource处理
	 * 4.resource发送给网络设备，配置生效
	 * @param 网络信息
	 * @param 负载均衡规则列表
	 * @return 是否完成负载均衡规则应用
	 * @throws InvalidParameterValueException
	 */
	@Override
	public boolean applyLBRules(Network config, List<LoadBalancingRule> rules) throws ResourceUnavailableException {
		if(!canHandle(config, rules)){
			return false;
		}
		return applyLoadBalancerRules(config, rules);
	}
	
	/**
	 * 获取网络设备的能力（在获取网络模式的时候会首先加载，查看设备网络）
	 * 1.添加负载均衡能力
	 * 2.设置负载均衡能力map（算法：论寻算法，最少链接算法，网络元素只能共享模式：共享，专用，协议：TCp，UDP，流量统计：
	 * 每个公共IP，负载平衡规则只能用公共ip，不是source nat，额外的，防火墙InlineMode模式，负载均衡方案：公共负载，支持粘性方法）
	 * LBCookieBased的cookie的粘的方法，只能用于http，保存时间（秒为单位）
	 * @return 负载均衡服务，能力键值对map
	 */
	@Override
	public Map<Service, Map<Capability, String>> getCapabilities() {
		Map<Service, Map<Capability, String>> capabilities = new HashMap<Service, Map<Capability, String>>();

		// Set capabilities for LB service
		Map<Capability, String> lbCapabilities = new HashMap<Capability, String>();

		// Specifies that the RoundRobin and Leastconn algorithms are supported
		// for load balancing rules
		lbCapabilities.put(Capability.SupportedLBAlgorithms,"roundrobin,leastconn");

		// specifies that F5 BIG IP network element can provide shared mode only
		lbCapabilities.put(Capability.SupportedLBIsolation, "dedicated, shared");

		// Specifies that load balancing rules can be made for either TCP or UDP
		// traffic
		lbCapabilities.put(Capability.SupportedProtocols, "tcp,udp");

		// Specifies that this element can measure network usage on a per public
		// IP basis
		lbCapabilities.put(Capability.TrafficStatistics, "per public ip");

		// Specifies that load balancing rules can only be made with public IPs
		// that aren't source NAT IPs
		lbCapabilities.put(Capability.LoadBalancingSupportedIps, "additional");

		// Support inline mode with firewall
		lbCapabilities.put(Capability.InlineMode, "true");

		// support only for public lb
		lbCapabilities.put(Capability.LbSchemes, LoadBalancerContainer.Scheme.Public.toString());

		List<LbStickinessMethod> methodList = new ArrayList<LbStickinessMethod>();
		LbStickinessMethod method;
		method = new LbStickinessMethod(StickinessMethodType.LBCookieBased, "This is cookie based sticky method, can be used only for http");
		method.addParam("cookie.type", false, "cookie type.", false);
		method.addParam("cookie.name", true, "cookie name.", false);
		method.addParam("cookie.domain",false, "cookie domain", false);
		method.addParam("cookie.path",false, "cookie path", false);
		method.addParam("cookie.timeout",false, "cookie timeout", false);
		method.addParam("priorToConnect",false, "cookie prior to Connect.", true);
		methodList.add(method);
		
		method = new LbStickinessMethod(StickinessMethodType.SourceBased,"This is source based sticky method");
		method.addParam("sourceip.maskv4", false, "source IPv4 mask.", false);
		method.addParam("sourceip.maskv6", false, "source IPv6 mask.", false);
		method.addParam("sourceip.timeout",false, "source ip timeout.", false);
		method.addParam("priorToConnect",false, "source prior to Connect.", true);
		methodList.add(method);
		
		Gson gson = new Gson();
		String stickyMethodList = gson.toJson(methodList);
		lbCapabilities.put(Capability.SupportedStickinessMethods, stickyMethodList);
		lbCapabilities.put(Capability.HealthCheckPolicy, "true");
		
		capabilities.put(Service.Lb, lbCapabilities);

		return capabilities;
	}
	
	@Override
	public Provider getProvider() {
		return Provider.SangforADLoadBalancer;
	}
	
	/**
	 * 验证负载均衡准备状态
	 * 1.获取负载均衡设备
	 * 2.验证外部负载均衡设备是否启用
	 * @param 物理网络服务提供者，根据id获取负载均衡设备的状态，判断设备是否启用
	 * @return 外部负载均衡设备已经准备就绪
	 */
	@Override
	public boolean isReady(PhysicalNetworkServiceProvider provider) {
		List<ExternalLoadBalancerDeviceVO> lbDevices = _lbDeviceDao.listByPhysicalNetworkAndProvider(provider.getPhysicalNetworkId(), Provider.SangforADLoadBalancer.getName());
        // true if at-least one SangforAD load balanver device is added in to physical network and is in configured (in enabled state) state
		if (lbDevices != null && !lbDevices.isEmpty()) {
			for (ExternalLoadBalancerDeviceVO lbDevice : lbDevices) {
				if (lbDevice.getState() == LBDeviceState.Enabled) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 关闭提供者实例
	 * 1.删除网络服务提供者调用
	 * @param 物理网络服务提供者，
	 * @param ReservationContext
	 * @return 关闭实例已经完成
	 * @throws ConcurrentOperationException,ResourceUnavailableException
	 */
	@Override
	public boolean shutdownProviderInstances(PhysicalNetworkServiceProvider provider, ReservationContext context)  throws ConcurrentOperationException,
	ResourceUnavailableException {
		//TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * 获取api入口类，ApiServer加载时找到入口
	 * 1.添加api入口类到列表
	 * @return api入口类集合列表
	 */
	@Override
	public List<Class<?>> getCommands() {
		List<Class<?>> cmdList = new ArrayList<Class<?>>();
		cmdList.add(AddSangforADLoadBalancerCmd.class);
		cmdList.add(ConfigureSangforADLoadBalancerCmd.class);
		cmdList.add(DeleteSangforADLoadBalancerCmd.class);
		cmdList.add(ListSangforADLoadBalancerNetworksCmd.class);
		cmdList.add(ListSangforADLoadBalancersCmd.class);
		return cmdList;
	}
	
	/**
	 * 添加SangforAD外部网络设备
	 * 1.获取设备类型:验证设备类型是否包含在网络设备类型里面（NetworkDevice.class）
	 * 2.添加设备到数据库: 调用外部负载均衡管理父类添加方法，进行相关验证和添加操作
	 * 3.父类添加操作：验证参数，到SangforADLoadBalancerResource配置设备信息，初始化设备
	 * @param  添加外部网络设备信息
	 * @return 外部负载均衡设备信息，添加设备信息
	 * @throws InvalidParameterValueException
	 */
	@Override
	public ExternalLoadBalancerDeviceVO addSangforADLoadBalancer(AddSangforADLoadBalancerCmd cmd) {
		String deviceName = cmd.getDeviceType();
		if(!deviceName.equalsIgnoreCase(NetworkDevice.SangforADLoadBalancer.getName())){
			throw new InvalidParameterValueException("Invalid SangforAD load balancer device type");
		}
		return addExternalLoadBalancer(cmd.getPhysicalNetworkId(), cmd.getUrl(), cmd.getUsername(), cmd.getPassword(),
                deviceName, new  SangforADLoadBalancerResource(), false, false, null, null);
	}
	
	/**
	 * 删除SangforAD外部网络设备
	 * 1.获取设备主机id
	 * 2.删除负载均衡设备
	 * @param  删除外部网络设备信息，获取设备ID
	 * @return 删除设备已经完成
	 * @throws InvalidParameterValueException
	 */
	@Override
	public boolean deleteSangforADLoadBalancer(DeleteSangforADLoadBalancerCmd cmd) {
		Long lbDeviceId = cmd.getLoadBalancerDeviceId();

		ExternalLoadBalancerDeviceVO lbDeviceVo = _lbDeviceDao.findById(lbDeviceId);
		if ((lbDeviceVo == null)|| !lbDeviceVo.getDeviceName().equalsIgnoreCase(NetworkDevice.SangforADLoadBalancer.getName())) {
			throw new InvalidParameterValueException("No SangforAD load balancer device found with ID: " + lbDeviceId);
		}

		return deleteExternalLoadBalancer(lbDeviceVo.getHostId());
	}
	
	/**
	 * 配置SangforAD外部网络设备
	 * 1.获取设备能力
	 * 2.启用设备状态
	 * 3.修改信息到数据库
	 * @param  配置设备信息
	 * @return 外部负载均衡设备信息
	 * @throws InvalidParameterValueException
	 */
	@Override
	public ExternalLoadBalancerDeviceVO configureSangforADLoadBalancer(ConfigureSangforADLoadBalancerCmd cmd) {
		Long lbDeviceId = cmd.getLoadBalancerDeviceId();
		Long capacity = cmd.getLoadBalancerCapacity();

		ExternalLoadBalancerDeviceVO lbDeviceVo = _lbDeviceDao.findById(lbDeviceId);
		if ((lbDeviceVo == null) || !lbDeviceVo.getDeviceName().equalsIgnoreCase( NetworkDevice.SangforADLoadBalancer.getName())) {
			throw new InvalidParameterValueException("No SangforAD load balancer device found with ID: " + lbDeviceId);
		}

		if (capacity != null) {
			// check if any networks are using this SangforAD device
			List<NetworkExternalLoadBalancerVO> networks = _networkLBDao.listByLoadBalancerDeviceId(lbDeviceId);
			if ((networks != null) && !networks.isEmpty()) {
				if (capacity < networks.size()) {
					throw new CloudRuntimeException("There are more number of networks already using this SangforAD device than configured capacity");
				}
			}
			if (capacity != null) {
				lbDeviceVo.setCapacity(capacity);
			}
		}

		lbDeviceVo.setState(LBDeviceState.Enabled);
		_lbDeviceDao.update(lbDeviceId, lbDeviceVo);
		return lbDeviceVo;
	}
	
	/**
	 * 获取SangforAD外部网络设备列表
	 * 1.根据外部网络设备ID 获取列表信息（也可以根据物理网络ID获取）
	 * @param  SangforAD设备信息，获取设备ID，物理网络ID
	 * @return 外部负载均衡设备列表
	 * @throws InvalidParameterValueException
	 */
	@Override
	public List<ExternalLoadBalancerDeviceVO> listSangforADLoadBalancers(ListSangforADLoadBalancersCmd cmd) {

		Long physcialNetworkId = cmd.getPhysicalNetworkId();
		Long lbDeviceId = cmd.getLoadBalancerDeviceId();
		PhysicalNetworkVO pNetwork = null;
		List<ExternalLoadBalancerDeviceVO> lbDevices = new ArrayList<ExternalLoadBalancerDeviceVO>();

		if (physcialNetworkId == null && lbDeviceId == null) {
			throw new InvalidParameterValueException("Either physical network Id or load balancer device Id must be specified");
		}

		if (lbDeviceId != null) {
			ExternalLoadBalancerDeviceVO lbDeviceVo = _lbDeviceDao.findById(lbDeviceId);
			if (lbDeviceVo == null || !lbDeviceVo.getDeviceName().equalsIgnoreCase(NetworkDevice.SangforADLoadBalancer.getName())) {
				throw new InvalidParameterValueException("Could not find SangforAD load balancer device with ID: " + lbDeviceId);
			}
			lbDevices.add(lbDeviceVo);
			return lbDevices;
		}

		if (physcialNetworkId != null) {
			pNetwork = _physicalNetworkDao.findById(physcialNetworkId);
			if (pNetwork == null) {
				throw new InvalidParameterValueException( "Could not find phyical network with ID: " + physcialNetworkId);
			}
			lbDevices = _lbDeviceDao.listByPhysicalNetworkAndProvider( physcialNetworkId, Provider.SangforADLoadBalancer.getName());
			return lbDevices;
		}

		return null;
	}
	
	/**
	 * 列出外部网络设备网络相关信息列表
	 * 1.获取外部网络设备
	 * 2.根据设备ID获取相关网络映射
	 * 3.获取设备相关网络
	 * @param  SangforAD设备负载均衡网络信息
	 * @return 设备网络相关信息列表
	 * @throws InvalidParameterValueException
	 */
	@Override
	public List<? extends Network> listNetworks(ListSangforADLoadBalancerNetworksCmd cmd) {
		Long lbDeviceId = cmd.getLoadBalancerDeviceId();
		List<NetworkVO> networks = new ArrayList<NetworkVO>();

		ExternalLoadBalancerDeviceVO lbDeviceVo = _lbDeviceDao.findById(lbDeviceId);
		if (lbDeviceVo == null || !lbDeviceVo.getDeviceName().equalsIgnoreCase( NetworkDevice.SangforADLoadBalancer.getName())) {
			throw new InvalidParameterValueException( "Could not find F5 load balancer device with ID " + lbDeviceId);
		}

		List<NetworkExternalLoadBalancerVO> networkLbMaps = _networkLBDao.listByLoadBalancerDeviceId(lbDeviceId);
		if (networkLbMaps != null && !networkLbMaps.isEmpty()) {
			for (NetworkExternalLoadBalancerVO networkLbMap : networkLbMaps) {
				NetworkVO network = _networkDao.findById(networkLbMap.getNetworkId());
				networks.add(network);
			}
		}
		return networks;
	}
	
	/**
	 * 添加SangforAD外部网络设备返回信息
	 * 1.获取负载主机信息
	 * 2.获取物理网络信息
	 * 3.添加设备信息，主机信息，网络信息到response对象
	 * @param  外部负载均衡设备VO对象
	 * @return SangforAD负载均衡设备返回信息
	 */
	@Override
	public SangforADLoadBalancerResponse createSangforADLoadBalancerResponse(ExternalLoadBalancerDeviceVO lbDeviceVO) {
		SangforADLoadBalancerResponse response = new SangforADLoadBalancerResponse();
		Host lbHost = _hostDao.findById(lbDeviceVO.getHostId());
		Map<String, String> lbDetails = _detailsDao.findDetails(lbDeviceVO.getHostId());

		response.setId(lbDeviceVO.getUuid());
		response.setIpAddress(lbHost.getPrivateIpAddress());
		PhysicalNetwork pnw = ApiDBUtils.findPhysicalNetworkById(lbDeviceVO.getPhysicalNetworkId());
		if (pnw != null) {
			response.setPhysicalNetworkId(pnw.getUuid());
		}
		response.setPublicInterface(lbDetails.get("publicInterface"));
		response.setPrivateInterface(lbDetails.get("privateInterface"));
		response.setDeviceName(lbDeviceVO.getDeviceName());
		if (lbDeviceVO.getCapacity() == 0) {
			long defaultLbCapacity = NumbersUtil.parseLong(_configDao.getValue(Config.DefaultExternalLoadBalancerCapacity.key()), 50);
			response.setDeviceCapacity(defaultLbCapacity);
		} else {
			response.setDeviceCapacity(lbDeviceVO.getCapacity());
		}
		response.setDedicatedLoadBalancer(lbDeviceVO.getIsDedicatedDevice());
		response.setProvider(lbDeviceVO.getProviderName());
		response.setDeviceState(lbDeviceVO.getState().name());
		response.setObjectName("sangforADloadbalancer");
		return response;
	}
	
	/**
	 * 可以使单个服务
	 */
	@Override
	public boolean canEnableIndividualServices() {
		return false;
	}

	/**
	 * 验证服务组合
	 */
	@Override
	public boolean verifyServicesCombination(Set<Service> services) {
		return true;
	}

	/**
	 * 获取负载均衡
	 */
	@Override
	public IpDeployer getIpDeployer(Network network) {
		ExternalLoadBalancerDeviceVO lbDevice = getExternalLoadBalancerForNetwork(network);
		if( lbDevice == null ){
			s_logger.error("cannot find external load balancer for network " + network.getName());
			s_logger.error("Make SangforAD load balancer as dummy ip deployer, since we likely met this when clean up resource after shutdown network");
	            return this;
		}
		if (_networkManager.isNetworkInlineMode(network)) {
            return getIpDeployerForInlineMode(network);
        }
        return this;
	}
	
	/**
	 * 应用IP关联负载均衡规则配置
	 */
	@Override
	public boolean applyIps(Network network, List<? extends PublicIpAddress> ipAddress, Set<Service> services) throws ResourceUnavailableException {
		// return true, as IP will be associated as part of LB rule configuration
		return true;
	}

	/**
	 * 更新健康检查
	 */
	@Override
	public List<LoadBalancerTO> updateHealthChecks(Network network,List<LoadBalancingRule> lbrules) {
		// TODO Auto-generated method stub
		return null;
	}
}