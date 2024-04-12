package org.meteor.metty.core.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcFrameDecoder
 * @Created Time: 2024-03-31 23:51
 **/
public class RpcFrameDecoder extends LengthFieldBasedFrameDecoder {

    public RpcFrameDecoder() {
        this(1024, 12, 4);
    }
    /**
     * 构造方法
     *
     * @param maxFrameLength    数据帧的最大长度
     * @param lengthFieldOffset 长度域的偏移字节数
     * @param lengthFieldLength 长度域所占的字节数
     */
    public RpcFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
    }
}
