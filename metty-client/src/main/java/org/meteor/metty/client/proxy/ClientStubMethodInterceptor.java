package org.meteor.metty.client.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.meteor.metty.client.transport.RpcClient;
import org.meteor.metty.client.config.RpcClientProperties;
import org.meteor.metty.core.discovery.ServiceDiscovery;

import java.lang.reflect.Method;

/**
 * 基于 Cglib 动态代理的客户端方法调用处理器类
 *
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: ClientStubMethodInterceptor
 * @Created Time: 2024-04-10 23:07
 **/
public class ClientStubMethodInterceptor implements MethodInterceptor {

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

    public ClientStubMethodInterceptor(ServiceDiscovery serviceDiscovery, RpcClient rpcClient, RpcClientProperties properties, String serviceName) {
        this.serviceDiscovery = serviceDiscovery;
        this.rpcClient = rpcClient;
        this.properties = properties;
        this.serviceName = serviceName;
    }


    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 执行远程方法调用
        return RemoteMethodCall.remoteCall(serviceDiscovery, rpcClient, serviceName, properties, method, args);
    }
}
