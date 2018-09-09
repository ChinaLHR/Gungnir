package io.github.chinalhr.gungnir_uid.handler;

import io.github.chinalhr.gungnir_uid.config.ServerConfig;
import io.github.chinalhr.gungnir_uid.core.SnowFlakeUidGenerate;
import io.github.chinalhr.gungnir_uid.utils.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:42 2018/9/9
 * @Email : 13435500980@163.com
 * <p>
 * Http响应Handler
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * Semaphore 控制TPS
     */
    private Semaphore semaphore = new Semaphore(ServerConfig.HTTP_TPS);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        String uri = getUriNoSprit(msg);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        if (ServerConfig.HTTP_REQUEST_MAPPING.equals(uri)){
            if (semaphore.tryAcquire(ServerConfig.ACQUIRE_TIMEOUTMILLIS, TimeUnit.MILLISECONDS)) {
                long uid = SnowFlakeUidGenerate.nextId();
                response.content().writeBytes((String.valueOf(uid)).getBytes());
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }else {
                String info = String.format("HttpServerHandler tryAcquire semaphore timeout, %dms, waiting thread " +
                                "nums: %d availablePermit: %d",
                        ServerConfig.ACQUIRE_TIMEOUTMILLIS,
                        this.semaphore.getQueueLength(),
                        this.semaphore.availablePermits());
                log.warn(info);
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        log.error("HttpServerHandler channel [{}] error and will be closed", NettyUtils.parseRemoteAddr(channel), cause);
        NettyUtils.closeChannel(channel);
    }

    private String getUriNoSprit(FullHttpRequest request) {
        String uri = request.uri();
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        return uri;
    }
}
