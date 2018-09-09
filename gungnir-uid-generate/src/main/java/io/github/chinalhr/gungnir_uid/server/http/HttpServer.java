package io.github.chinalhr.gungnir_uid.server.http;

import io.github.chinalhr.gungnir_uid.config.ServerConfig;
import io.github.chinalhr.gungnir_uid.core.MachineSignGenerateIpStrategy;
import io.github.chinalhr.gungnir_uid.core.SnowFlakeUidGenerate;
import io.github.chinalhr.gungnir_uid.handler.HttpServerHandler;
import io.github.chinalhr.gungnir_uid.server.BaseServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:03 2018/9/9
 * @Email : 13435500980@163.com
 */
@Slf4j
public class HttpServer extends BaseServer {

    public HttpServer() {
        this.port = ServerConfig.HTTP_PORT;
    }

    @Override
    public void init() {
        super.init();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(defLoopGroup,
                                new HttpRequestDecoder(),
                                new HttpObjectAggregator(65536),//将多个消息转换成单一的消息对象
                                new HttpResponseEncoder(),
                                new HttpServerHandler()
                        );
                    }
                });
    }

    @Override
    public void start() {
        try {
            channelFuture = serverBootstrap.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) channelFuture.channel().localAddress();
            log.info("HttpServer Start Success, Port is:{}", addr.getPort());
        } catch (InterruptedException e) {
            log.error("HttpServer Start Failure", e);
        }
    }


}
