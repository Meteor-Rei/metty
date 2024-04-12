package org.meteor.metty.client.transport.netty;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: ChannelProvider
 * @Created Time: 2024-04-06 17:06
 **/
public class ChannelProvider {
    private final Map<String, Channel> channels = new ConcurrentHashMap<>();

    public Channel get(String hostname, Integer port) {
        String key = hostname + ":" + port;
        // 如果之前对应的 ip port 已经建立了 channel
        if (channels.containsKey(key)) {
            // 取出 channel
            Channel channel = channels.get(key);
            // 如果 channel 不为 null，并且处于活跃状态（连接状态）
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                // 为 null 或者已经关闭连接，从 map 中移除
                channels.remove(key);
            }
        }
        return null;
    }

    public Channel get(InetSocketAddress inetSocketAddress) {
        return get(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
    }

    public void set(String hostname, Integer port, Channel channel) {
        String key = hostname + ":" + port;
        channels.put(key, channel);
    }

    public void set(InetSocketAddress inetSocketAddress, Channel channel) {
        this.set(inetSocketAddress.getHostName(), inetSocketAddress.getPort(), channel);
    }
}
