package io.github.chinalhr.gungnir.netchannel.client.future;

import io.github.chinalhr.gungnir.netchannel.client.pool.NettyChannelPoolFactory;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:58 2018/5/18
 * @Email : 13435500980@163.com
 * <p>
 * CallBack线程调用
 */
public class GResponseCallback implements Callable<GResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GResponseCallback.class);

    private String address;
    private ISerializer serializer;
    private long timeoutMillis;
    private GRequest request;
    private Channel channel;

    public GResponseCallback(String address, ISerializer serializer, long timeoutMillis, GRequest request) {
        this.address = address;
        this.serializer = serializer;
        this.timeoutMillis = timeoutMillis;
        this.request = request;
    }

    public static GResponseCallback build(String address, ISerializer serializer, long timeoutMillis, GRequest request) {
        return new GResponseCallback(address, serializer, timeoutMillis, request);
    }

    @Override
    public GResponse call() throws Exception {
        try {
            //初始化异步返回容器
            GResponseHolder.initGResponse(request.getRequestID());
            channel = NettyChannelPoolFactory.getInstance().getChannelByPool(address, serializer, timeoutMillis);
            ChannelFuture future = channel.writeAndFlush(request);
            future.syncUninterruptibly();
            return GResponseHolder.getResponse(request.getRequestID(),timeoutMillis);
        }catch (Exception e){
            LOGGER.error("GungnirInvokeCallback get response error：{}",e.getLocalizedMessage());
        }finally {
            NettyChannelPoolFactory.getInstance().recycleChannel(address,serializer,channel);
        }
        return null;
    }
}
