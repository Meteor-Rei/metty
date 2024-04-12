package org.meteor.metty.provider.service.impl;

import org.meteor.metty.api.service.HelloService;
import org.meteor.metty.server.annotation.RpcService;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: HelloServiceImpl
 * @Created Time: 2024-04-09 16:12
 **/


@RpcService(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
