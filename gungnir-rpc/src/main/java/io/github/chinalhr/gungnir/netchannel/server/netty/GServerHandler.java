package io.github.chinalhr.gungnir.netchannel.server.netty;

import io.github.chinalhr.gungnir.filter.FilterInvoker;
import io.github.chinalhr.gungnir.netchannel.server.FilterOperation;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.netchannel.server.InvokeOperation;
import io.github.chinalhr.gungnir.threadpool.GungnirServerThreadPoolManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @Author : ChinaLHR
 * @Date : Create in 9:24 2018/3/19
 * @Email : 13435500980@163.com
 *
 * RPC Server Handler ：接受Request,对Service进行CGLIB调用，返回Request
 */
public class GServerHandler extends SimpleChannelInboundHandler<GRequest> implements FilterInvoker {

    private static final Logger LOGGER = LoggerFactory.getLogger(GServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GRequest request) throws Exception {
        ExecutorService handlerThreadPool = GungnirServerThreadPoolManager.getHandlerThreadPool();
        handlerThreadPool.execute(()->{
            FilterInvoker filterInvoker = FilterOperation.buildInvokerChain(this);
            GResponse response = filterInvoker.invoker(request);
            ctx.writeAndFlush(response);
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Gungnir Netty Server exceptionCaught : {} ",cause.fillInStackTrace());
        ctx.close();
    }

    @Override
    public GResponse invoker(GRequest request) {
        return InvokeOperation.invokeService(request, null);
    }
}
