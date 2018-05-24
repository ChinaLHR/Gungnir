package io.github.chinalhr.gungnir.netchannel.client.pool;

import io.github.chinalhr.gungnir.netchannel.client.netty.GungnirClientHandler;
import io.github.chinalhr.gungnir.netchannel.keepalive.ClientHearBeatHandler;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.github.chinalhr.gungnir.serializer.netty.GDecoder;
import io.github.chinalhr.gungnir.serializer.netty.GEncoder;
import io.github.chinalhr.gungnir.utils.GeneralUtils;
import io.github.chinalhr.gungnir.utils.PropertyConfigeUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.github.chinalhr.gungnir.common.Constant.*;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:05 2018/5/17
 * @Email : 13435500980@163.com
 * <p>
 * NettyChannel构造器
 */
public class NettyChannelPoolFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyChannelPoolFactory.class);

    //Key:address - Value：[ISerializer,ChannelQueue]
    private static final Map<String, Map<ISerializer, ArrayBlockingQueue<Channel>>> channelPoolMap = new ConcurrentHashMap<>();

    private static final int netChannelSize = PropertyConfigeUtils.getNetChannelSize();

    /**
     * 根据address获取Netty Channel阻塞队列
     *
     * @param address
     * @param serializer
     * @return
     */
    private ArrayBlockingQueue<Channel> getBlockingQueue(String address, ISerializer serializer) {
        if (null == channelPoolMap.get(address)) {
            Map<ISerializer, ArrayBlockingQueue<Channel>> channelMap = new ConcurrentHashMap<>();
            channelPoolMap.put(address, channelMap);
        }
        if (null == channelPoolMap.get(address).get(serializer)) {
            //计数器
            int channelSize = 0;
            ArrayBlockingQueue<Channel> channelArrayBlockingQueue = new ArrayBlockingQueue<>(netChannelSize);
            while (channelSize < netChannelSize) {
                Channel channel = getNetChannel(address, serializer);
                channelSize++;
                //将新注册的Channel存入阻塞队列channelArrayBlockingQueue
                channelArrayBlockingQueue.offer(channel);
            }
            channelPoolMap.get(address).put(serializer, channelArrayBlockingQueue);
            return channelArrayBlockingQueue;
        } else {
            return channelPoolMap.get(address).get(serializer);
        }
    }

    /**
     * 从NettyPool中获取Channel
     * @param address
     * @param serializer
     * @param timeoutMillis
     * @return
     */
    public Channel getChannelByPool(String address, ISerializer serializer, long timeoutMillis) {
        ArrayBlockingQueue<Channel> queue = getBlockingQueue(address, serializer);
        Channel channel = null;
        try {
            channel = queue.poll(timeoutMillis, TimeUnit.MILLISECONDS);
            if (!channel.isOpen() || !channel.isActive() || !channel.isWritable()) {
                LOGGER.debug("NettyChannelPoolFactory getChannelByPool Channel Failure");
                channel = getNetChannel(address, serializer);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channel;
    }

    /**
     * Channel使用完毕，回收到ArrayBlockingQueue中
     * @param address
     * @param serializer
     * @param channel
     */
    public void recycleChannel(String address, ISerializer serializer,Channel channel) {
        ArrayBlockingQueue<Channel> queue = getBlockingQueue(address, serializer);
        if (!channel.isActive() || !channel.isOpen() || !channel.isWritable()) {
            if (channel != null) {
                //等待future完成
                channel.deregister().syncUninterruptibly().awaitUninterruptibly();
                channel.closeFuture().syncUninterruptibly().awaitUninterruptibly();
            }else{
                channel = getNetChannel(address,serializer);
            }

            getBlockingQueue(address,serializer).offer(channel);
            return;
        }
        getBlockingQueue(address,serializer).offer(channel);
    }

    /**
     * 根据address与serializer获取channel
     *
     * @param address
     * @return
     */
    public Channel getNetChannel(String address, ISerializer serializer) {
        try {
            String[] split = StringUtils.split(address, ":");
            String host = split[0];
            int port = Integer.parseInt(split[1]);
            NioEventLoopGroup group = new NioEventLoopGroup(GeneralUtils.getThreadConfigNumberOfIO());

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new GEncoder(GRequest.class, serializer));
                            pipeline.addLast(new GDecoder(GResponse.class, serializer));
                            pipeline.addLast(new IdleStateHandler(0,0,CLIENT_ALLIDLE_TIMESECONDS));
                            pipeline.addLast(new ClientHearBeatHandler());
                            pipeline.addLast(new GungnirClientHandler());
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            final Channel channel = channelFuture.channel();
            final CountDownLatch connectedLatch = new CountDownLatch(1);
            final Boolean[] isSuccess = {false};
            //监听Channel是否建立成功
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        isSuccess[0] = true;
                    } else {
                        //若Channel建立失败,保存建立失败的标记
                        future.cause().printStackTrace();
                        isSuccess[0] = false;
                    }
                    connectedLatch.countDown();
                }
            });

            connectedLatch.await();
            if (isSuccess[0]) {
                return channel;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 当zookeeper节点相应的Group发生变化时，清除ChannelPool缓存
     */
    public void cleanChannelPoolMap(){
        channelPoolMap.forEach((address,map)->{
            map.forEach((s,queue)->{
                while (queue.poll()!=null){
                    queue.poll().close();//关闭channel
                }
            });
        });
        channelPoolMap.clear();
    }

    /**
     * Singleton
     */
    private static class NettyChannelPoolHolder {
        private static final NettyChannelPoolFactory INSTANCE = new NettyChannelPoolFactory();
    }

    private NettyChannelPoolFactory() {
    }

    public static final NettyChannelPoolFactory getInstance() {
        return NettyChannelPoolHolder.INSTANCE;
    }


}
