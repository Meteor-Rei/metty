import org.junit.Test;
import org.meteor.metty.client.common.RequestMetadata;
import org.meteor.metty.client.transport.netty.NettyRpcClient;
import org.meteor.metty.core.protocol.MessageHeader;
import org.meteor.metty.core.protocol.RpcMessage;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: TestClient
 * @Created Time: 2024-04-06 21:59
 **/
public class TestClient {
    public static void main(String[] args) {
        NettyRpcClient client = new NettyRpcClient();
        RpcMessage msg = new RpcMessage();
        msg.setHeader(MessageHeader.build("json"));
        msg.setBody((Object) new TestService(1,1,"what can i say"));
        RequestMetadata requestMetadata = new RequestMetadata(msg, "192.168.19.1",8082,3000);
        client.sendRpcRequest(requestMetadata);
    }

    @Test
    public void startClient1(){
        NettyRpcClient client = new NettyRpcClient();
        RpcMessage msg = new RpcMessage();
        msg.setHeader(MessageHeader.build("json"));
        msg.setBody((Object) new TestService(1,1,"what can i say"));
        RequestMetadata requestMetadata = new RequestMetadata(msg, "192.168.19.1",8082,3000);
        client.sendRpcRequest(requestMetadata);
    }
}
