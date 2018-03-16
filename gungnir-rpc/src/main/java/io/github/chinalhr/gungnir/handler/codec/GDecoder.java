package io.github.chinalhr.gungnir.handler.codec;

import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:41 2018/3/16
 * @Email : 13435500980@163.com
 *
 * RPC编码器
 */
public class GDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;
    private ISerializer iserializer;

    public GDecoder(Class<?> genericClass,ISerializer serializer){
        this.genericClass = genericClass;
        this.iserializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes()<4) return;

        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }

        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = iserializer.deserialize(data, genericClass);
        out.add(obj);

    }
}
