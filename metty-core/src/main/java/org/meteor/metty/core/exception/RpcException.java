package org.meteor.metty.core.exception;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcException
 * @Created Time: 2024-04-06 21:09
 **/
public class RpcException extends RuntimeException{

    private static final long serialVersionUID = 3365624081242234231L;

    public RpcException() {
        super();
    }

    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}
