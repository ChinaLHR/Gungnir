package io.github.chinalhr.gungnir.server;

import io.github.chinalhr.gungnir.serializer.ISerializer;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:14 2018/1/5
 * @Email : 13435500980@163.com
 */
public interface IServer {

    void init();

    void start(String ip,int port, ISerializer serializer);

    void stop();

}
