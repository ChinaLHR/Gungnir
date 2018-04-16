package io.github.chinalhr.gungnir.server.netty.codec;

import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:01 2018/3/16
 * @Email : 13435500980@163.com
 *
 * RPC解码器
 */
public class GEncoder extends MessageToByteEncoder{

    private Class<?> genericClass;
    private ISerializer iserializer;

    public GEncoder(Class<?> genericClass,ISerializer serializer){
        this.genericClass = genericClass;
        this.iserializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (genericClass.isInstance(msg)){
            byte[] data = iserializer.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }

    }
}
