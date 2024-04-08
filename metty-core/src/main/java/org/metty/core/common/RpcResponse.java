package org.metty.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcResponse
 * @Created Time: 2024-04-02 20:51
 **/


@Data
public class RpcResponse implements Serializable {
    /**
     * 请求返回值
     */
    private Object returnValue;

    /**
     * 发生异常时的异常信息
     */
    private Exception exceptionValue;

}
