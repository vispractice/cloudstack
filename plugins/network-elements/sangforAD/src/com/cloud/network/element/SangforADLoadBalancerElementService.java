package com.cloud.network.element;

import java.util.List;

import com.cloud.api.commands.AddSangforADLoadBalancerCmd;
import com.cloud.api.commands.ConfigureSangforADLoadBalancerCmd;
import com.cloud.api.commands.DeleteSangforADLoadBalancerCmd;
import com.cloud.api.commands.ListSangforADLoadBalancerNetworksCmd;
import com.cloud.api.commands.ListSangforADLoadBalancersCmd;
import com.cloud.api.response.SangforADLoadBalancerResponse;
import com.cloud.network.Network;
import com.cloud.network.dao.ExternalLoadBalancerDeviceVO;
import com.cloud.utils.component.PluggableService;
/**
 * @author root
 * @Date 2015/06/12
 * 外部网络设备（SangforAD）的操作接口
 * 1.外部网络设备的增、删、查、改（配置）
 * 
 */
public interface SangforADLoadBalancerElementService   extends PluggableService {
	
	/**
     * 添加SangforAD外部网络设备
     * @param AddSangforADLoadBalancerCmd 
     * @return ExternalLoadBalancerDeviceVO object for the load balancer added
     */
    public ExternalLoadBalancerDeviceVO addSangforADLoadBalancer(AddSangforADLoadBalancerCmd cmd);

    /**
     * 删除SangforAD外部网络设备
     * @param DeleteSangforADLoadBalancerCmd 
     * @return true if load balancer device successfully deleted
     */
    public boolean deleteSangforADLoadBalancer(DeleteSangforADLoadBalancerCmd cmd);

    /**
     * 配置SangforAD外部设备
     * @param ConfigureSangforADLoadBalancerCmd
     * @return ExternalFirewallDeviceVO for the device configured
     */
    public ExternalLoadBalancerDeviceVO configureSangforADLoadBalancer(ConfigureSangforADLoadBalancerCmd cmd);

    /**
     * 列出SangforAD外部设备信息列表
     * @param ListSangforADLoadBalancersCmd
     * @return list of ExternalFirewallDeviceVO for the devices in the physical network.
     */
    public List<ExternalLoadBalancerDeviceVO> listSangforADLoadBalancers(ListSangforADLoadBalancersCmd cmd);

    /**
     * 列出SangforAD负载平衡器设备来宾网络相关信息
     * @param ListSangforADLoadBalancerNetworksCmd
     * @return list of the guest networks that are using this SangforAD load balancer
     */
    public List<? extends Network> listNetworks(ListSangforADLoadBalancerNetworksCmd cmd);

    /**
     * 组装SangforAD外部网络设备返回信息
     * @param ExternalLoadBalancerDeviceVO
     * @return SangforADLoadBalancerResponse 
     */
    public SangforADLoadBalancerResponse createSangforADLoadBalancerResponse(ExternalLoadBalancerDeviceVO lbDeviceVO);
	
}
