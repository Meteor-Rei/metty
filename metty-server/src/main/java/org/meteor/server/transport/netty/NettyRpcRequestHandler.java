package org.meteor.server.transport.netty;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.meteor.server.handler.RpcRequestHandler;
import org.metty.core.common.RpcRequest;
import org.metty.core.common.RpcResponse;
import org.metty.core.constant.ProtocolConstants;
import org.metty.core.enums.MessageStatus;
import org.metty.core.enums.MessageType;
import org.metty.core.exception.RpcException;
import org.metty.core.factory.SingletonFactory;
import org.metty.core.protocol.MessageHeader;
import org.metty.core.protocol.RpcMessage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: NettyRpcRequestHandler
 * @Created Time: 2024-04-06 21:33
 **/


@Slf4j
public class NettyRpcRequestHandler extends SimpleChannelInboundHandler<RpcMessage> {


    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

    private final RpcRequestHandler rpcRequestHandler;

    public NettyRpcRequestHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        threadPool.submit(() -> {
            try {
                RpcMessage responseRpcMessage = new RpcMessage();
                MessageHeader header = msg.getHeader();
                MessageType type = MessageType.parseByType(header.getMessageType());
                log.debug("The message received by the server is: {}", msg.getBody());
                // 如果是心跳检测请求信息
                if (type == MessageType.HEARTBEAT_REQUEST) {
                    header.setMessageType(MessageType.HEARTBEAT_RESPONSE.getType());
                    header.setMessageStatus(MessageStatus.SUCCESS.getCode());
                    // 设置响应头部信息
                    responseRpcMessage.setHeader(header);
                    responseRpcMessage.setBody(ProtocolConstants.PONG);
                } else { // 处理 Rpc 请求信息
                    RpcRequest request = (RpcRequest) msg.getBody();
                    RpcResponse response = new RpcResponse();
                    // 设置头部消息类型
                    header.setMessageType(MessageType.RESPONSE.getType());
                    // 反射调用
                    try {
                        // 获取本地反射调用结果
                        Object result = rpcRequestHandler.handleRpcRequest(request);
                        response.setReturnValue(result);
                        header.setMessageStatus(MessageStatus.SUCCESS.getCode());
                    } catch (Exception e) {
                        log.error("The service [{}], the method [{}] invoke failed!", request.getServiceName(), request.getMethod());
                        // 若不设置，堆栈信息过多，导致报错
                        response.setExceptionValue(new RpcException("Error in remote procedure call, " + e.getMessage()));
                        header.setMessageStatus(MessageStatus.FAIL.getCode());
                    }
                    // 设置响应头部信息
                    responseRpcMessage.setHeader(header);
                    responseRpcMessage.setBody(response);
                }
                log.debug("responseRpcMessage: {}.", responseRpcMessage);
                // 将结果写入，传递到下一个处理器
                ctx.writeAndFlush(responseRpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } finally {
                // 确保 ByteBuf 被释放，防止发生内存泄露
                ReferenceCountUtil.release(msg);
            }
        });
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.warn("idle check happen, so close the connection.");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
