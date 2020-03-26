package io.github.chinalhr.gungnir.netchannel.client.netty;

import io.github.chinalhr.gungnir.netchannel.client.future.GResponseHolder;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : ChinaLHR
 * @Date : Create in 15:12 2018/4/26
 * @Email : 13435500980@163.com
 */
public class GungnirClientHandler extends SimpleChannelInboundHandler<GResponse>{

    private static final Logger LOGGER = LoggerFactory.getLogger(GungnirClientHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Gungnir Client Netty caught exception",cause);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, GResponse msg) throws Exception {
        GResponseHolder.putGResponse(msg);
    }
}
