package org.meteor.client.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;


import lombok.extern.slf4j.Slf4j;
import org.metty.core.common.RpcResponse;
import org.metty.core.constant.ProtocolConstants;
import org.metty.core.enums.MessageType;
import org.metty.core.enums.SerializationType;
import org.metty.core.protocol.MessageHeader;
import org.metty.core.protocol.RpcMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcResponseHandler
 * @Created Time: 2024-04-02 20:37
 **/

@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcMessage> {


    public static final Map<Integer, Promise<RpcMessage>> UNPROCESSED_RPC_RESPONSES = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {
        try {
            MessageType type = MessageType.parseByType(rpcMessage.getHeader().getMessageType());
            // 如果是 RpcRequest 请求
            if (type == MessageType.RESPONSE) {
                int sequenceId = rpcMessage.getHeader().getSequenceId();
                // 拿到还未执行完成的 promise 对象
                Promise<RpcMessage> promise = UNPROCESSED_RPC_RESPONSES.remove(sequenceId);
                if (promise != null) {
                    Exception exception = ((RpcResponse) rpcMessage.getBody()).getExceptionValue();
                    if (exception == null) {
                        promise.setSuccess(rpcMessage);
                    } else {
                        promise.setFailure(exception);
                    }
                }
            } else if (type == MessageType.HEARTBEAT_RESPONSE) { // 如果是心跳检查请求
                log.debug("Heartbeat info {}.", rpcMessage.getBody());
            }
        } finally {
            // 释放内存，防止内存泄漏
            ReferenceCountUtil.release(rpcMessage);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
                log.warn("Write idle happen [{}].", ctx.channel().remoteAddress());
                // 构造 心跳检查 RpcMessage
                RpcMessage rpcMessage = new RpcMessage();
                MessageHeader header = MessageHeader.build(SerializationType.KRYO.name());
                header.setMessageType(MessageType.HEARTBEAT_REQUEST.getType());
                rpcMessage.setHeader(header);
                rpcMessage.setBody(ProtocolConstants.PING);
                // 发送心跳检测请求
                ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
