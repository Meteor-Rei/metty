package org.meteor.metty.core.loadbalance.impl;

import org.meteor.metty.core.common.RpcRequest;
import org.meteor.metty.core.common.ServiceInfo;
import org.meteor.metty.core.loadbalance.AbstractLoadBalance;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: ConsistentHashLoadBalance
 * @Created Time: 2024-04-10 22:32
 **/
public class ConsistentHashLoadBalance extends AbstractLoadBalance {

    private final Map<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();
    @Override
    protected ServiceInfo doSelect(List<ServiceInfo> invokers, RpcRequest request) {
        return null;
    }


    private final static class ConsistentHashSelector {

        /**
         * 使用 TreeMap 存储虚拟节点（virtualInvokers 需要提供高效的查询操作，因此选用 TreeMap 作为存储结构）
         */
        private final TreeMap<Long, ServiceInfo> virtualInvokers;

        /**
         * invokers 的原始哈希码
         */
        private final int identityHashCode;

        /**
         * 构建一个 ConsistentHashSelector 对象
         *
         * @param invokers         存储虚拟节点
         * @param replicaNumber    虚拟节点数，默认为 160
         * @param identityHashCode invokers 的原始哈希码
         */
        public ConsistentHashSelector(List<ServiceInfo> invokers, int replicaNumber, int identityHashCode) {
            this.virtualInvokers = new TreeMap<>();
            this.identityHashCode = identityHashCode;

            for (ServiceInfo invoker : invokers) {
                String address = invoker.getAddress();
                for (int i = 0; i < replicaNumber / 4; i++) {
                    // 对 address + i 进行 md5 运算，得到一个长度为16的字节数组
                    byte[] digest = md5(address + i);
                    // 对 digest 部分字节进行4次 hash 运算，得到四个不同的 long 型正整数
                    for (int h = 0; h < 4; h++) {
                        // h = 0 时，取 digest 中下标为 0 ~ 3 的4个字节进行位运算
                        // h = 1 时，取 digest 中下标为 4 ~ 7 的4个字节进行位运算
                        // h = 2, h = 3 时过程同上
                        long m = hash(digest, h);
                        // 将 hash 到 invoker 的映射关系存储到 virtualInvokers 中
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }


        /**
         * 进行 md5 运算，返回摘要字节数组
         *
         * @param key 编码字符串 key
         * @return 编码后的摘要内容，长度为 16 的字节数组
         */
        private byte[] md5(String key) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
                md.update(bytes);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            return md.digest();
        }


        /**
         * 根据摘要生成 hash 值
         *
         * @param digest md5摘要内容
         * @param number 当前索引数
         * @return hash 值
         */
        private long hash(byte[] digest, int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                    | (digest[number * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }

        public ServiceInfo select(String key) {
            // 对参数 key 进行 md5 运算
            byte[] digest = md5(key);
            // 取 digest 数组的前四个字节进行 hash 运算，再将 hash 值传给 selectForKey 方法，
            // 寻找合适的 Invoker
            return selectForKey(hash(digest, 0));
        }

        /**
         * 得到第一个大于等于 hash 值的服务信息，若没有则返回第一个
         *
         * @param hash 哈希值
         * @return 服务信息
         */
        private ServiceInfo selectForKey(long hash) {
            // 找到 TreeMap 中查找第一个节点值大于或等于当前 hash 的 Invoker
            Map.Entry<Long, ServiceInfo> entry = virtualInvokers.ceilingEntry(hash);
            // 如果 hash 大于 Invoker 在圆环上最大的位置，此时 entry = null，需要将 TreeMap 的头节点赋值给 entry
            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }
            return entry.getValue();
        }
    }
}
