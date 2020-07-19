package cn.scu.rpc.codec;

import cn.scu.rpc.exception.RpcException;
import cn.scu.rpc.protocol.RpcMessage;
import cn.scu.rpc.util.ProtoStuffSerializeUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
编码器
 * Created by jiang on 2017/5/10.
 */
public class RpcEncoder extends MessageToByteEncoder<RpcMessage> {

    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, RpcMessage in, ByteBuf out) throws Exception {
        if(null==in){
            throw new RpcException("RpcMessage is null");
        }
        if (genericClass.isInstance(in.getMessageBody())) {
            byte[] data = ProtoStuffSerializeUtil.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}