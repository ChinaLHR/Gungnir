package io.github.chinalhr.gungnir.netchannel.client.pool;

import io.github.chinalhr.gungnir.register.zk.ZkServiceDiscovery;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author : ChinaLHR
 * @Date : Create in 14:46 2018/4/26
 * @Email : 13435500980@163.com
 */
public class NettyClientPool {

    private GenericObjectPool<NettyClientPoolProxy> pool;

    public NettyClientPool(String host, int port, ISerializer serializer){
        pool = new GenericObjectPool<NettyClientPoolProxy>(new NettyClientPoolFactory(host,port,serializer));
        pool.setTestOnBorrow(true);
        pool.setMaxTotal(2);
    }

    public GenericObjectPool<NettyClientPoolProxy> getPool(){
        return this.pool;
    }

    private static ConcurrentHashMap<String, NettyClientPool> clientPoolMap = new ConcurrentHashMap<>();
    public static GenericObjectPool<NettyClientPoolProxy> getPool(String host,int port, ISerializer serializer)
            throws Exception {
        String address = host+":"+port;
        // get from pool
        NettyClientPool clientPool = clientPoolMap.get(address);
        if (clientPool != null) {
            return clientPool.getPool();
        }
        clientPool = new NettyClientPool(host, port, serializer);
        clientPoolMap.put(address, clientPool);
        return clientPool.getPool();
    }
}
