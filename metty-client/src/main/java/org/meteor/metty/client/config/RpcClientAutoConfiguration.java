package org.meteor.metty.client.config;

import org.meteor.metty.client.spring.RpcClientBeanPostProcessor;
import org.meteor.metty.client.proxy.ClientStubProxyFactory;
import org.meteor.metty.client.spring.RpcClientExitDisposableBean;
import org.meteor.metty.client.transport.RpcClient;
import org.meteor.metty.client.transport.netty.NettyRpcClient;
import org.meteor.metty.core.discovery.ServiceDiscovery;
import org.meteor.metty.core.discovery.zookeeper.ZookeeperServiceDiscovery;
import org.meteor.metty.core.loadbalance.LoadBalance;
import org.meteor.metty.core.loadbalance.impl.ConsistentHashLoadBalance;
import org.meteor.metty.core.loadbalance.impl.RandomLoadBalance;
import org.meteor.metty.core.loadbalance.impl.RoundRobinLoadBalance;
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
 * @ClassName: RpcClientAutoConfiguration
 * @Created Time: 2024-04-10 20:32
 **/

@Configuration
@EnableConfigurationProperties(RpcClientProperties.class)
public class RpcClientAutoConfiguration {

    @Autowired
    RpcClientProperties rpcClientProperties;

    @Bean(name = "loadBalance")
    @Primary
    @ConditionalOnMissingBean // 不指定 value 则值默认为当前创建的类
    @ConditionalOnProperty(prefix = "rpc.client", name = "loadbalance", havingValue = "random", matchIfMissing = true)
    public LoadBalance randomLoadBalance() {
        return new RandomLoadBalance();
    }

    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client", name = "loadbalance", havingValue = "roundRobin")
    public LoadBalance roundRobinLoadBalance() {
        return new RoundRobinLoadBalance();
    }

    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client", name = "loadbalance", havingValue = "consistentHash")
    public LoadBalance consistentHashLoadBalance() {
        return new ConsistentHashLoadBalance();
    }



    @Bean(name = "serviceDiscovery")
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnBean(LoadBalance.class)
    @ConditionalOnProperty(prefix = "rpc.client", name = "registry", havingValue = "zookeeper", matchIfMissing = true)
    public ServiceDiscovery zookeeperServiceDiscovery(@Autowired LoadBalance loadBalance) {
        return new ZookeeperServiceDiscovery(rpcClientProperties.getRegistryAddr(), loadBalance);
    }

    @Bean(name = "rpcClient")
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client", name = "transport", havingValue = "netty", matchIfMissing = true)
    public RpcClient nettyRpcClient() {
        return new NettyRpcClient();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({ServiceDiscovery.class, RpcClient.class})
    public ClientStubProxyFactory clientStubProxyFactory(@Autowired ServiceDiscovery serviceDiscovery,
                                                         @Autowired RpcClient rpcClient,
                                                         @Autowired RpcClientProperties rpcClientProperties) {
        return new ClientStubProxyFactory(serviceDiscovery, rpcClient, rpcClientProperties);
    }

    //下面是什么东东 不是很懂
    @Bean
    @ConditionalOnMissingBean
    public RpcClientBeanPostProcessor rpcClientBeanPostProcessor(@Autowired ClientStubProxyFactory clientStubProxyFactory) {
        return new RpcClientBeanPostProcessor(clientStubProxyFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcClientExitDisposableBean rpcClientExitDisposableBean(@Autowired ServiceDiscovery serviceDiscovery) {
        return new RpcClientExitDisposableBean(serviceDiscovery);
    }
}
