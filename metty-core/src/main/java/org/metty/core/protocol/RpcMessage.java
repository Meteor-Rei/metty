package org.metty.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcMessage
 * @Created Time: 2024-04-02 13:23
 **/


@Data
public class RpcMessage {
    /**
     * 请求头 - 协议信息
     */
    private MessageHeader header;

    /**
     * 消息体
     */
    private Object body;
}
