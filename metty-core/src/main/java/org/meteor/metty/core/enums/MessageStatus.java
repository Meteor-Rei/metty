package org.meteor.metty.core.enums;

import lombok.Getter;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: MessageStatus
 * @Created Time: 2024-04-06 21:38
 **/
public enum MessageStatus {
    /**
     * 成功
     */
    SUCCESS((byte) 0),

    /**
     * 失败
     */
    FAIL((byte) 1);

    @Getter
    private final byte code;

    MessageStatus(byte code) {
        this.code = code;
    }

    public static boolean isSuccess(byte code) {
        return MessageStatus.SUCCESS.code == code;
    }
}
