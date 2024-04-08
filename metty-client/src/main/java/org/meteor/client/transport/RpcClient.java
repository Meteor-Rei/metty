package org.meteor.client.transport;

import org.meteor.client.common.RequestMetadata;
import org.metty.core.protocol.RpcMessage;
/**
* @Author: meteor
* @Version: 1.0
* @ClassName: RpcClient
* @Created Time: 2024-04-06 17:13
**/
public interface RpcClient {
    RpcMessage sendRpcRequest(RequestMetadata requestMetadata);
}
