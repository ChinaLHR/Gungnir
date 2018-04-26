package io.github.chinalhr.gungnir.netchannel.client.pool;

import io.github.chinalhr.gungnir.netchannel.client.netty.GungnirClientHandler;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.github.chinalhr.gungnir.serializer.netty.GDecoder;
import io.github.chinalhr.gungnir.serializer.netty.GEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : ChinaLHR
 * @Date : Create in 14:46 2018/4/26
 * @Email : 13435500980@163.com
 */
public class NettyClientPoolProxy {

    private static transient Logger LOGGER = LoggerFactory.getLogger(NettyClientPoolProxy.class);

    private Channel channel;

    public void createProxy(String host, int port, ISerializer serializer) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new GEncoder(GRequest.class, serializer));
                        pipeline.addLast(new GDecoder(GResponse.class,serializer));
                        pipeline.addLast(new GungnirClientHandler());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true);
        this.channel = bootstrap.connect(host,port).sync().channel();
    }


    public Channel getChannel() {
        return this.channel;
    }

    public boolean isValidate() {
        if (this.channel != null) {
            return this.channel.isActive();
        }
        return false;
    }

    public void close() {
        if (this.channel != null) {
            if (this.channel.isOpen()) {
                this.channel.close();
            }
        }
        LOGGER.info("GungnirClient NettyClientPoolProxy channel close");

    }

    public void send(GRequest request) throws Exception {
        this.channel.writeAndFlush(request).sync();
    }

}
