package org.meteor.metty.provider;

import org.meteor.metty.server.annotation.RpcComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: ProviderApplication
 * @Created Time: 2024-04-09 16:07
 **/

@SpringBootApplication
@RpcComponentScan(basePackages = {"org.meteor.metty.provider"})
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
