package org.meteor.metty.provider.service.impl;

import org.meteor.metty.api.service.AbstractService;
import org.meteor.metty.server.annotation.RpcService;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: AbstractServiceImpl
 * @Created Time: 2024-04-09 16:11
 **/

@RpcService(interfaceClass = AbstractService.class)
public class AbstractServiceImpl extends AbstractService{
    @Override
    public String abstractHello(String name) {
        return "abstract hello " + name;
    }
}


