import org.meteor.server.transport.netty.NettyRpcServer;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: TestServer
 * @Created Time: 2024-04-06 21:53
 **/
public class TestServer {




    public static void main(String[] args) {
        NettyRpcServer server = new NettyRpcServer();
        server.start(8082);
    }
}
