package org.meteor.metty.client.transport;

import org.meteor.metty.client.common.RequestMetadata;
import org.meteor.metty.core.protocol.RpcMessage;
/**
* @Author: meteor
* @Version: 1.0
* @ClassName: RpcClient
* @Created Time: 2024-04-06 17:13
**/
public interface RpcClient {
    RpcMessage sendRpcRequest(RequestMetadata requestMetadata);
}
