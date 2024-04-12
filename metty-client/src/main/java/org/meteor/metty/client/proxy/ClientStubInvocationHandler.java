package org.meteor.metty.client.proxy;

import org.meteor.metty.client.transport.RpcClient;
import org.meteor.metty.client.config.RpcClientProperties;
import org.meteor.metty.core.discovery.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *基于 JDK 动态代理的客户端方法调用处理器类
 *
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: ClientStubInvocationHandler
 * @Created Time: 2024-04-10 23:04
 **/
public class ClientStubInvocationHandler implements InvocationHandler {
    /**
     * 服务发现中心
     */
    private final ServiceDiscovery serviceDiscovery;

    /**
     * Rpc客户端
     */
    private final RpcClient rpcClient;

    /**
     * Rpc 客户端配置属性
     */
    private final RpcClientProperties properties;

    /**
     * 服务名称：接口-版本
     */
    private final String serviceName;

    public ClientStubInvocationHandler(ServiceDiscovery serviceDiscovery, RpcClient rpcClient, RpcClientProperties properties, String serviceName) {
        this.serviceDiscovery = serviceDiscovery;
        this.rpcClient = rpcClient;
        this.properties = properties;
        this.serviceName = serviceName;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return RemoteMethodCall.remoteCall(serviceDiscovery, rpcClient, serviceName, properties, method, args);
    }
}
