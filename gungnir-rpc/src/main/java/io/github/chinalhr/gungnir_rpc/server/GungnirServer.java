package io.github.chinalhr.gungnir_rpc.server;

import io.github.chinalhr.gungnir_rpc.serializer.ISerializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
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
    private ServerBootstrap bootstrap;

    public GungnirServer(int port,) {

    }

    @Override
    public void init() {
        LOGGER.info("GungnirServer init ");
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

        bootstrap = new ServerBootstrap();
    }

    @Override
    public void start(int port, ISerializer serializer) {

    }

    @Override
    public void destory() {

    }
}
