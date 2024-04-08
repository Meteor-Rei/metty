package org.metty.core.constant;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: ProtocolConstants
 * @Created Time: 2024-04-02 13:14
 **/
public class ProtocolConstants {
    private static final AtomicInteger ai = new AtomicInteger();

    /**
     * 魔数，用来第一时间判断是否无效数据包
     */
    public static final byte[] MAGIC_NUM = new byte[]{(byte) 'm', (byte) 'r', (byte) 'p', (byte) 'c'};

    public static final byte VERSION = 1;

    public static final String PING = "ping";

    public static final String PONG = "pong";

    public static int getSequenceId() {
        // todo: 实现原子操作
        return ai.getAndIncrement();
    }

}
