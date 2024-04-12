package org.meteor.metty.core.loadbalance;


import org.meteor.metty.core.common.RpcRequest;
import org.meteor.metty.core.common.ServiceInfo;

import java.util.List;

public interface LoadBalance {
    /**
     * 负载均衡，从传入的服务列表中按照指定的策略返回一个
     *
     * @param invokers 服务列表
     * @param request rpc请求
     * @return 按策略返回的服务信息对象
     */
    ServiceInfo select(List<ServiceInfo> invokers, RpcRequest request);
}
