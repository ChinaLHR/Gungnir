package io.github.chinalhr.gungnir.netchannel.keepalive;

import io.github.chinalhr.gungnir.enums.HearbeatEnum;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.protocol.GRpcHeartbeat;
import io.github.chinalhr.gungnir.utils.TimeStampUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : ChinaLHR
 * @Date : Create in 10:20 2018/5/21
 * @Email : 13435500980@163.com
 *
 * Gungnir Client心跳处理Handler
 */
public class ClientHearBeatHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHearBeatHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!(msg instanceof GResponse)){
            ctx.fireChannelRead(msg);
            return;
        }

        GResponse response = (GResponse) msg;

        if (null==response||null==response.getHeartbeat()){
            ctx.fireChannelRead(msg);
            return;
        }

        if (response.getHeartbeat().getType()== HearbeatEnum.PONG.getType()){
            LOGGER.info("ClientHearBeatHandler channelRead ,PONG data is:{}",TimeStampUtils.stampToDate(response.getHeartbeat().getTimeStamp(),"yyyy-MM-dd-HH-mm:ss:ms"));
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    this.handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    this.handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    this.handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleWriterIdle(ChannelHandlerContext ctx){
        LOGGER.info("ClientHeartbeatHandler.handleWriterIdle");
    }
    private void handleReaderIdle(ChannelHandlerContext ctx){
        LOGGER.info("ClientHeartbeatHandler.handleReaderIdle");
    }

    /**
     * 捕获读写超时，发送Ping
     * @param ctx
     */
    private void handleAllIdle(ChannelHandlerContext ctx){
        LOGGER.info("ClientHeartbeatHandler.handleAllIdle(读写超时): Send PING");
        this.sendPing(ctx);
    }

    private void sendPing(ChannelHandlerContext ctx){
        GRequest request = new GRequest();
        GRpcHeartbeat heartbeat = new GRpcHeartbeat();
        heartbeat.setType(HearbeatEnum.PING.getType());
        heartbeat.setTimeStamp(TimeStampUtils.getUnixTimeStampWithMS());
        request.setHeartbeat(heartbeat);

        ctx.writeAndFlush(request);
    }

}
