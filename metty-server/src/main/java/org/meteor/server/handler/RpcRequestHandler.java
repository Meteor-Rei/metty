package org.meteor.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.meteor.server.store.LocalServiceCache;
import org.metty.core.common.RpcRequest;
import org.metty.core.exception.RpcException;

import java.lang.reflect.Method;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcRequestHandler
 * @Created Time: 2024-04-06 21:24
 **/

@Slf4j
public class RpcRequestHandler {
    public Object handleRpcRequest(RpcRequest request) throws Exception {
        // 反射调用 RpcRequest 请求指定的方法
        // 获取请求服务实例
        Object service = LocalServiceCache.getService(request.getServiceName());
        if (service == null) {
            log.error("The service [{}] is not exist!", request.getServiceName());
            throw new RpcException(String.format("The service [%s] is not exist!", request.getServiceName()));
        }
        // 获取指定的方法
        Method method = service.getClass().getMethod(request.getMethod(), request.getParameterTypes());
        // 调用方法并返回结果
        return method.invoke(service, request.getParameterValues());
    }
}
