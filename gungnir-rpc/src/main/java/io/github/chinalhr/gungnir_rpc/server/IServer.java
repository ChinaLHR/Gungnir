package io.github.chinalhr.gungnir_rpc.server;

import io.github.chinalhr.gungnir_rpc.common.SerializeEnum;
import io.github.chinalhr.gungnir_rpc.serializer.ISerializer;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:14 2018/1/5
 * @Email : 13435500980@163.com
 */
public interface IServer {

    void init();

    void start(int port, ISerializer serializer);

    void destory();

}
