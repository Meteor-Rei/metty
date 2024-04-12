package org.meteor.metty.server.spring;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.meteor.metty.server.annotation.RpcService;
import org.meteor.metty.server.config.RpcServerProperties;
import org.meteor.metty.server.store.LocalServiceCache;
import org.meteor.metty.server.transport.RpcServer;
import org.meteor.metty.core.common.ServiceInfo;
import org.meteor.metty.core.registry.ServiceRegistry;
import org.meteor.metty.core.util.ServiceUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcServerBeanPostProcessor
 * @Created Time: 2024-04-08 23:08
 **/

@Slf4j
public class RpcServerBeanPostProcessor implements BeanPostProcessor, CommandLineRunner {

    private final ServiceRegistry serviceRegistry;

    private final RpcServer rpcServer;

    private final RpcServerProperties properties;

    public RpcServerBeanPostProcessor(ServiceRegistry serviceRegistry, RpcServer rpcServer, RpcServerProperties properties) {
        this.serviceRegistry = serviceRegistry;
        this.rpcServer = rpcServer;
        this.properties = properties;
    }


    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> rpcServer.start(properties.getPort())).start();
        log.info("Rpc server [{}] start, the appName is {}, the port is {}",
                rpcServer, properties.getAppName(), properties.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // 当服务关闭之后，将服务从 注册中心 上清除（关闭连接）
                serviceRegistry.destroy();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    @SneakyThrows
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 判断当前 bean 是否被 @RpcService 注解标注
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with [{}].", bean.getClass().getName(), RpcService.class.getCanonicalName());
            // 获取到该类的 @RpcService 注解
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            String interfaceName;
            if ("".equals(rpcService.interfaceName())) {
                interfaceName = rpcService.interfaceClass().getName();
            } else {
                interfaceName = rpcService.interfaceName();
            }
            String version = rpcService.version();
            String serviceName = ServiceUtil.serviceKey(interfaceName, version);
            // 构建 ServiceInfo 对象
            ServiceInfo serviceInfo = ServiceInfo.builder()
                    .appName(properties.getAppName())
                    .serviceName(serviceName)
                    .version(version)
                    .address(properties.getAddress())
                    .port(properties.getPort())
                    .build();
            // 进行远程服务注册
            serviceRegistry.register(serviceInfo);
            // 进行本地服务缓存注册
            LocalServiceCache.addService(serviceName, bean);
        }
        return bean;
    }
}
