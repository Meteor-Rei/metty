package org.meteor.metty.core.loadbalance.impl;

import org.meteor.metty.core.common.RpcRequest;
import org.meteor.metty.core.common.ServiceInfo;
import org.meteor.metty.core.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RandomLoadBalance
 * @Created Time: 2024-04-10 21:09
 **/
public class RandomLoadBalance extends AbstractLoadBalance {
    final Random random =new Random();


    @Override
    protected ServiceInfo doSelect(List<ServiceInfo> invokers, RpcRequest request) {
        return invokers.get(random.nextInt(invokers.size()));
    }
}
