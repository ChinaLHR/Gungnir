package io.github.chinalhr.gungnir.netchannel.client.netty;

import io.github.chinalhr.gungnir.netchannel.client.IClient;
import io.github.chinalhr.gungnir.netchannel.client.pool.NettyClientPool;
import io.github.chinalhr.gungnir.netchannel.client.pool.NettyClientPoolProxy;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.github.chinalhr.gungnir.utils.RpcCallbackFuture;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : ChinaLHR
 * @Date : Create in 14:07 2018/4/26
 * @Email : 13435500980@163.com
 */
public class GungnirClient implements IClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(GungnirClient.class);

    @Override
    public GResponse send(String host, int port, GRequest request, ISerializer serializer, long timeoutMillis) throws Exception {
        GenericObjectPool<NettyClientPoolProxy> clientPool = NettyClientPool.getPool(host, port, serializer);

        //client proxy
        NettyClientPoolProxy clientPoolProxy = null;
        try {
            RpcCallbackFuture future = new RpcCallbackFuture(request);
            RpcCallbackFuture.futurePool.put(request.getRequestID(), future);

            // rpc invoke
            clientPoolProxy = clientPool.borrowObject();
            clientPoolProxy.send(request);

            //future get
            return future.get(timeoutMillis);
        }catch (Exception e){
            LOGGER.error("GungnirClient Send Exception：{}",e.getLocalizedMessage());
            throw e;
        }finally {
            RpcCallbackFuture.futurePool.remove(request.getRequestID());
            clientPool.returnObject(clientPoolProxy);
        }
    }

}
