package org.meteor.metty.server.config;

import org.meteor.metty.server.spring.RpcServerBeanPostProcessor;
import org.meteor.metty.server.transport.RpcServer;
import org.meteor.metty.server.transport.netty.NettyRpcServer;
import org.meteor.metty.core.registry.ServiceRegistry;
import org.meteor.metty.core.registry.zookeeper.ZookeeperServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcServerAutoConfiguration
 * @Created Time: 2024-04-08 20:57
 **/


@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcServerAutoConfiguration {
    @Autowired
    RpcServerProperties properties;


    /**
     * 创建 ServiceRegistry 实例 bean，当没有配置时默认使用 zookeeper 作为配置中心
     */
    @Bean(name = "serviceRegistry")
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.server", name = "registry", havingValue = "zookeeper", matchIfMissing = true)
    public ServiceRegistry zookeeperServiceRegistry() {
        return new ZookeeperServiceRegistry(properties.getRegistryAddr());
    }


    //之后再说吧
    //    @Bean(name = "serviceRegistry")
    //    @ConditionalOnMissingBean
    //    @ConditionalOnProperty(prefix = "rpc.server", name = "registry", havingValue = "nacos")
    //    public ServiceRegistry nacosServiceRegistry() {
    //        return new NacosServiceRegistry(properties.getRegistryAddr());
    //    }

    // 当没有配置通信协议属性时，默认使用 netty 作为通讯协议
    @Bean(name = "rpcServer")
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.server", name = "transport", havingValue = "netty", matchIfMissing = true)
    public RpcServer nettyRpcServer() {
        return new NettyRpcServer();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({ServiceRegistry.class, RpcServer.class})
    public RpcServerBeanPostProcessor rpcServerBeanPostProcessor(@Autowired ServiceRegistry serviceRegistry,
                                                                 @Autowired RpcServer rpcServer,
                                                                 @Autowired RpcServerProperties properties) {

        return new RpcServerBeanPostProcessor(serviceRegistry, rpcServer, properties);
    }
}
