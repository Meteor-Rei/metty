package org.meteor.metty.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meteor.metty.core.constant.ProtocolConstants;
import org.meteor.metty.core.enums.MessageType;
import org.meteor.metty.core.enums.SerializationType;

/**
 *   --------------------------------------------------------------------
 *  | 魔数 (4byte) | 版本号 (1byte)  | 序列化算法 (1byte)  | 消息类型 (1byte) |
 *  *  -------------------------------------------------------------------
 *  |  状态类型 (1byte)  |     消息序列号 (4byte)   |     消息长度 (4byte)    |
 *  --------------------------------------------------------------------
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: MessageHeader
 * @Created Time: 2024-04-02 13:05
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageHeader {
    //魔数 4byte
    private byte[] magicNum;

    //版本号 1byte
    private byte version;

    //序列化算法 1byte
    private byte serializerType;

    //消息类型 1byte
    private byte messageType;

    //状态类型 1byte
    private byte messageStatus;

    //序列号 4byte
    private int sequenceId;

    //消息长度 4byte
    private int length;

    public static MessageHeader build(String serializeName) {
        return MessageHeader.builder()
                .magicNum(ProtocolConstants.MAGIC_NUM)
                .version(ProtocolConstants.VERSION)
                .serializerType(SerializationType.parseByName(serializeName).getType())
                .messageType(MessageType.REQUEST.getType())
                .sequenceId(ProtocolConstants.getSequenceId()) // 添加唯一 ID 生成
                .build();
    }

}
