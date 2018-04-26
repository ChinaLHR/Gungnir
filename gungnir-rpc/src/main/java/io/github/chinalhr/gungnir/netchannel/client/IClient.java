package io.github.chinalhr.gungnir.netchannel.client;

import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.serializer.ISerializer;

/**
 * @Author : ChinaLHR
 * @Date : Create in 13:56 2018/4/26
 * @Email : 13435500980@163.com
 */
public interface IClient {

    /**
     * 发送Reques返回Response
     * @param request
     * @return
     * @throws Exception
     */
    GResponse send(String host, int port, GRequest request, ISerializer serializer,long timeoutMillis) throws Exception;

}
