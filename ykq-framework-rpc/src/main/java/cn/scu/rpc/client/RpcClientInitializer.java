package cn.scu.rpc.client;

import cn.scu.rpc.common.RpcFilter;
import cn.scu.rpc.codec.RpcDecoder;
import cn.scu.rpc.codec.RpcEncoder;
import cn.scu.rpc.common.RpcRequest;
import cn.scu.rpc.common.RpcResponse;
import cn.scu.rpc.constants.Constants;
import cn.scu.rpc.keepalive.ClientHeartbeatHandler;
import cn.scu.rpc.util.ActiveFilterUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {

    private final static Logger logger = LoggerFactory.getLogger(RpcClientInitializer.class);

    private Map<String, RpcFilter> getFilterMap(){
        logger.info("RpcClientInitializer.setApplicationContext");

        return ActiveFilterUtil.getFilterMap(false);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();

        cp.addLast(new RpcEncoder(RpcRequest.class));
        cp.addLast(new RpcDecoder(RpcResponse.class));
        cp.addLast(new IdleStateHandler(0, 0, Constants.ALLIDLE_TIME_SECONDS));
        cp.addLast(new ClientHeartbeatHandler());
        cp.addLast(new RpcClientInvoker(this.getFilterMap()));

    }
}