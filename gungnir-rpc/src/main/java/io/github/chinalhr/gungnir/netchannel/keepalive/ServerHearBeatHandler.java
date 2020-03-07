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
 * @Date : Create in 22:49 2018/5/20
 * @Email : 13435500980@163.com
 *
 * GungnirServer心跳处理Handler
 */
public class ServerHearBeatHandler extends ChannelInboundHandlerAdapter{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHearBeatHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!(msg instanceof GRequest)){
            ctx.fireChannelRead(msg);
            return;
        }
        GRequest request = (GRequest) msg;
        if (null==request||null==request.getHeartbeat()){
            ctx.fireChannelRead(msg);
            return;
        }

        if (request.getHeartbeat().getType()== HearbeatEnum.PING.getType()){
            LOGGER.info("ServerHearBeatHandler channelRead ,Read the PING data is:{}",TimeStampUtils.stampWithMSToDate(request.getHeartbeat().getTimeStamp(),"yyyy-MM-dd HH:mm:ss:ms"));
            this.sendPong(ctx);
        } else {
            ctx.fireChannelRead(msg);
        }

    }

    private void sendPong(ChannelHandlerContext ctx){
        LOGGER.info("ServerHearBeatHandler send Pong ");
        GResponse response = new GResponse();
        GRpcHeartbeat heartbeat = new GRpcHeartbeat();
        heartbeat.setTimeStamp(TimeStampUtils.getUnixTimeStampWithMS());
        heartbeat.setType(HearbeatEnum.PONG.getType());
        response.setHeartbeat(heartbeat);
        ctx.writeAndFlush(response);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent e = (IdleStateEvent)evt;
            switch (e.state()){
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
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
        LOGGER.info("ServerHearBeatHandler.handleWriterIdle");
    }

    /**
     * 捕获读超时，关闭Channel
     * @param ctx
     */
    private void handleReaderIdle(ChannelHandlerContext ctx){
        LOGGER.info("ServerHearBeatHandler.handleReaderIdle(读超时) ：close channel");
        ctx.close();
    }

    private void handleAllIdle(ChannelHandlerContext ctx){
        LOGGER.info("ServerHearBeatHandler.handleAllIdle");
    }

}
