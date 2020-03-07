package io.github.chinalhr.gungnir.netchannel.server;

import io.github.chinalhr.gungnir.serializer.ISerializer;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:14 2018/1/5
 * @Email : 13435500980@163.com
 */
public interface IServer {

    /**
     * 初始化Server
     */
    void init();

    /**
     * 开启Server
     * @param ip
     * @param port
     * @param serializer
     */
    void start(String ip,int port, ISerializer serializer);

    /**
     * 停止Server
     */
    void stop();

}
