package io.github.chinalhr.gungnir.server;

import io.github.chinalhr.gungnir.handler.GServerHandler;
import io.github.chinalhr.gungnir.handler.codec.GDecoder;
import io.github.chinalhr.gungnir.handler.codec.GEncoder;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.serializer.ISerializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:16 2018/1/5
 * @Email : 13435500980@163.com
 */
public class GungnirServer implements IServer{

    private static final Logger LOGGER = LoggerFactory.getLogger(GungnirServer.class);
    private DefaultEventLoopGroup defaultGroup;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workGroup;
    private ServerBootstrap bootStrap;
    private ChannelFuture future;
    private ScheduledExecutorService executorService;

    @Override
    public void init() {
        LOGGER.info("GungnirServer Init");
        int cpu = Runtime.getRuntime().availableProcessors();

        defaultGroup = new DefaultEventLoopGroup(8,(r)->{
            AtomicInteger integer = new AtomicInteger(0);
            return new Thread(r,"DEFAULTGROUP" + integer.incrementAndGet());
        });

        bossGroup = new NioEventLoopGroup(cpu,(r)->{
            AtomicInteger integer = new AtomicInteger(0);
            return new Thread(r,"BOSSGROUP"+integer.incrementAndGet());
        });

        workGroup = new NioEventLoopGroup(cpu,(r)->{
            AtomicInteger integer = new AtomicInteger(0);
            return new Thread(r,"WORKGROUP"+integer.incrementAndGet());
        });

        bootStrap = new ServerBootstrap();
        executorService = Executors.newScheduledThreadPool(2);
    }

    @Override
    public void start(int port, ISerializer serializer) {
        LOGGER.info("GungnirServer start");
        bootStrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)//连接保活
                .option(ChannelOption.TCP_NODELAY,true)//禁用Nagle算法
                .option(ChannelOption.SO_BACKLOG,1024)//设置队列长度
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(defaultGroup,
                                        new GDecoder(GRequest.class,serializer),
                                        new GEncoder(GResponse.class,serializer),
                                        new GServerHandler()
                                );

                    }
                });

        //TODO 进行心跳检测Ping与Channel清理工作

    }

    @Override
    public void destory() {

    }
}
