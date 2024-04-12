package org.meteor.metty.client.spring;

import lombok.extern.slf4j.Slf4j;
import org.meteor.metty.core.discovery.ServiceDiscovery;
import org.springframework.beans.factory.DisposableBean;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcClientExitDisposableBean
 * @Created Time: 2024-04-11 21:03
 **/


@Slf4j
public class RpcClientExitDisposableBean implements DisposableBean {
    /**
     * 服务发现中心
     */
    private final ServiceDiscovery serviceDiscovery;

    public RpcClientExitDisposableBean(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }


    @Override
    public void destroy() throws Exception {
        try {
            if (serviceDiscovery != null) {
                serviceDiscovery.destroy();
            }
            log.info("Rpc client resource release completed and exited successfully.");
        } catch (Exception e) {
            log.warn("An exception occurred while executing the destroy operation when the rpc client exited, {}.",
                    e.getMessage());
        }
    }
}
