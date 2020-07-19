package cn.scu.rpc.keepalive;

import cn.scu.rpc.common.RpcRequest;
import cn.scu.rpc.common.RpcResponse;
import cn.scu.rpc.constants.Constants;
import cn.scu.rpc.protocol.RpcMessage;
import cn.scu.rpc.protocol.RpcMessageHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * ClassName: AbstractHeartbeatHandler
 * Created By Kun Qiao Yang
 * At Si Chuan University
 * Created Date 2020/6/14 14:56
 * Version V1.0
 * Description:
 */

public abstract class AbstractHeartbeatHandler extends ChannelInboundHandlerAdapter {



    protected static final Logger logger= LoggerFactory.getLogger(AbstractHeartbeatHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {

        if(!(msg instanceof RpcMessage)){
            channelHandlerContext.fireChannelRead(msg);
            return;
        }
        RpcMessage message=(RpcMessage)msg;

        if(null==message||null==message.getMessageHeader()){
            channelHandlerContext.fireChannelRead(msg);
            return;
        }
        if(message.getMessageHeader().getType() == Constants.MESSAGE_TYPE_HEARTBEAT_PONG){
            logger.info("ClientHeartbeatHandler.channelRead0 ,pong data is:{}",message.getMessageBody());
        }
        else if(message.getMessageHeader().getType()== Constants.MESSAGE_TYPE_HEARTBEAT_PING){
            this.sendPong(channelHandlerContext);
        }
        else {
            channelHandlerContext.fireChannelRead(msg);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    this.handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    this.handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    this.handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    protected  void handleWriterIdle(ChannelHandlerContext ctx){
        logger.info("AbstractHeartbeatHandler.handleWriterIdle");
    }
    protected  void handleReaderIdle(ChannelHandlerContext ctx){
        logger.info("AbstractHeartbeatHandler.handleReaderIdle");
    }

    protected  void handleAllIdle(ChannelHandlerContext ctx){
        logger.info("AbstractHeartbeatHandler.handleAllIdle");
    }

    protected void sendPing(ChannelHandlerContext channelHandlerContext){
        RpcMessage message=new RpcMessage();

        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setRequestId("ping");

        RpcMessageHeader messageHeader=new RpcMessageHeader();
        messageHeader.setLength(1);
        messageHeader.setType(Constants.MESSAGE_TYPE_HEARTBEAT_PING);
        message.setMessageHeader(messageHeader);
        message.setMessageBody(rpcRequest);

        channelHandlerContext.writeAndFlush(message);
        logger.info("ping:{}",message.getMessageBody());
    }

    protected void sendPong(ChannelHandlerContext channelHandlerContext){

        RpcMessage message=new RpcMessage();
        String body = new Date().toString();

        RpcResponse rpcResponse=new RpcResponse();
        rpcResponse.setResult(body);

        RpcMessageHeader messageHeader=new RpcMessageHeader();
        messageHeader.setLength(body.length());
        messageHeader.setType(Constants.MESSAGE_TYPE_HEARTBEAT_PONG);
        message.setMessageHeader(messageHeader);
        message.setMessageBody(rpcResponse);

        channelHandlerContext.writeAndFlush(message);
    }
}
