package io.github.chinalhr.gungnir.netchannel.server.netty;

import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.serializer.ISerializer;

import io.github.chinalhr.gungnir.netchannel.server.IServer;
import io.github.chinalhr.gungnir.serializer.netty.GDecoder;
import io.github.chinalhr.gungnir.serializer.netty.GEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:16 2018/1/5
 * @Email : 13435500980@163.com
 */
public class GungnirServer implements IServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GungnirServer.class);
    private DefaultEventLoopGroup defaultGroup;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workGroup;
    private ServerBootstrap bootStrap;
    private ScheduledExecutorService executorService;

    @Override
    public void init() {
        LOGGER.info("GungnirServer Init");
        int cpu = Runtime.getRuntime().availableProcessors();

        defaultGroup = new DefaultEventLoopGroup(8, (r) -> {
            AtomicInteger integer = new AtomicInteger(0);
            return new Thread(r, "ServerDefaultGroup" + integer.incrementAndGet());
        });

        bossGroup = new NioEventLoopGroup(cpu, (r) -> {
            AtomicInteger integer = new AtomicInteger(0);
            return new Thread(r, "ServerBossGroup" + integer.incrementAndGet());
        });

        workGroup = new NioEventLoopGroup(cpu, (r) -> {
            AtomicInteger integer = new AtomicInteger(0);
            return new Thread(r, "ServerWorkGroup" + integer.incrementAndGet());
        });

        bootStrap = new ServerBootstrap();
        executorService = Executors.newScheduledThreadPool(2);
    }

    @Override
    public void start(String ip, int port, ISerializer serializer) {
        LOGGER.info("GungnirServer start");
        bootStrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)//连接保活
                .option(ChannelOption.TCP_NODELAY,true)//禁用Nagle算法
                .option(ChannelOption.SO_BACKLOG, 1024)//设置队列长度
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(defaultGroup,
                                        new GEncoder(GResponse.class, serializer),
                                        new GDecoder(GRequest.class, serializer),
                                        new GServerHandler()
                                );
                    }
                });

        try {
            bootStrap.bind(ip, port).sync();
            LOGGER.info("GungnirServer start success ,host is :" + ip + ",port is:" + port);

        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error("GungnirServer Start Error:{}", e.getLocalizedMessage());
        }


    }

    @Override
    public void stop() {
        if (defaultGroup != null) defaultGroup.shutdownGracefully();
        if (executorService != null) executorService.shutdown();

        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
