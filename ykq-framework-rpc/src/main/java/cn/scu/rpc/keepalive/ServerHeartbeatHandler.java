package cn.scu.rpc.keepalive;

import io.netty.channel.ChannelHandlerContext;

/**
 * ClassName: ServerHeartbeatHandler
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 14:55
 * Version V1.0
 * Description:
 */

public class ServerHeartbeatHandler extends AbstractHeartbeatHandler{

    @Override
    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        logger.info("ServerHeartbeatHandler.handleReaderIdle reader timeout ,close channel");
        ctx.close();
    }

}
