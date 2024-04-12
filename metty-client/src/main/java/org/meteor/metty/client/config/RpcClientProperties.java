package org.meteor.metty.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcClientProperties
 * @Created Time: 2024-04-10 20:30
 **/

@Data
@ConfigurationProperties(prefix = "rpc.client")
public class RpcClientProperties {
    /**
     * Load balancing algorithm, candidate values include: (random, roundRobin, consistentHash), the default is random.
     */
    private String loadbalance;

    /**
     * Serialization algorithm, candidate values include: (JDK, JSON, HESSIAN, KRYO, PROTOSTUFF), default: HESSIAN
     */
    private String serialization;

    /**
     * Communication protocols, such as netty and http, are netty by default
     */
    private String transport;

    /**
     * Registration center, such as (zookeeper, nacos, etc.), defaults to: zookeeper
     */
    private String registry;

    /**
     * Service discovery (registry) address. The default is "127.0.0.1:2181"
     */
    private String registryAddr;

    /**
     * Connection timeout, default: 5000
     */
    private Integer timeout;

    public RpcClientProperties() {
        this.loadbalance = "random";
        this.serialization = "JSON";
        this.transport = "netty";
        this.registry = "zookeeper";
        this.registryAddr = "47.108.194.48:2181";
        this.timeout = 5000;
    }
}
