package org.meteor.metty.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.meteor.metty.core.serialization.SerializationFactory;
import org.meteor.metty.core.common.RpcRequest;
import org.meteor.metty.core.common.RpcResponse;
import org.meteor.metty.core.constant.ProtocolConstants;
import org.meteor.metty.core.enums.MessageType;
import org.meteor.metty.core.enums.SerializationType;
import org.meteor.metty.core.protocol.MessageHeader;
import org.meteor.metty.core.protocol.RpcMessage;
import org.meteor.metty.core.serialization.Serialization;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: SharableRpcMessageCodec
 * @Created Time: 2024-04-06 20:46
 **/


@Sharable
public class SharableRpcMessageCodec extends MessageToMessageCodec<ByteBuf , RpcMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        MessageHeader header = msg.getHeader();

        // 4字节 魔数
        buf.writeBytes(header.getMagicNum());
        // 1字节 版本号
        buf.writeByte(header.getVersion());
        // 1字节 序列化算法
        buf.writeByte(header.getSerializerType());
        // 1字节 消息类型
        buf.writeByte(header.getMessageType());
        // 1字节 消息状态
        buf.writeByte(header.getMessageStatus());
        // 4字节 消息序列号
        buf.writeInt(header.getSequenceId());
        // 取出消息体
        Object body = msg.getBody();
        // 获取序列化算法
        Serialization serialization = SerializationFactory
                .getSerialization(SerializationType.parseByType(header.getSerializerType()));
        // 进行序列化
        byte[] bytes = serialization.serialize(body);
        // 设置消息体长度
        header.setLength(bytes.length);

        // 4字节 消息内容长度
        buf.writeInt(header.getLength());

        // 不固定字节 消息内容字节数组
        buf.writeBytes(bytes);

        // 传递到下一个出站处理器
        out.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        // 4字节 魔数
        int len = ProtocolConstants.MAGIC_NUM.length;
        byte[] magicNum = new byte[len];
        msg.readBytes(magicNum, 0, len);

        // 判断魔数是否正确，不正确表示非协议请求，不进行处理
        for (int i = 0; i < len; i++) {
            if (magicNum[i] != ProtocolConstants.MAGIC_NUM[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(magicNum));
            }
        }

        // 1字节 版本号
        byte version = msg.readByte();
        // 检查版本号是否一致
        if (version != ProtocolConstants.VERSION) {
            throw new IllegalArgumentException("The version isn't compatible " + version);
        }
        // 1字节 序列化算法
        byte serializeType = msg.readByte();
        // 1字节 消息类型
        byte messageType = msg.readByte();
        // 1字节 消息状态
        byte messageStatus = msg.readByte();
        // 4字节 消息序列号
        int sequenceId = msg.readInt();
        // 4字节 长度
        int length = msg.readInt();
        byte[] bytes = new byte[length];
        msg.readBytes(bytes, 0, length);

        // 构建协议头部信息
        MessageHeader header = MessageHeader.builder()
                .magicNum(magicNum)
                .version(version)
                .serializerType(serializeType)
                .messageType(messageType)
                .sequenceId(sequenceId)
                .messageStatus(messageStatus)
                .length(length).build();
        // 获取反序列化算法
        Serialization serialization = SerializationFactory
                .getSerialization(SerializationType.parseByType(serializeType));
        // 获取消息枚举类型
        MessageType type = MessageType.parseByType(messageType);
        RpcMessage protocol = new RpcMessage();
        protocol.setHeader(header);
        if (type == MessageType.REQUEST) {
            // 进行反序列化
            RpcRequest request = serialization.deserialize(RpcRequest.class, bytes);
            protocol.setBody(request);
        } else if (type == MessageType.RESPONSE) {
            // 进行反序列化
            RpcResponse response = serialization.deserialize(RpcResponse.class, bytes);
            protocol.setBody(response);
        } else if (type == MessageType.HEARTBEAT_REQUEST || type == MessageType.HEARTBEAT_RESPONSE) {
            String message = serialization.deserialize(String.class, bytes);
            protocol.setBody(message);
        }
        // 传递到下一个处理器
        out.add(protocol);
    }
}
