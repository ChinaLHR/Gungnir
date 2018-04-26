package io.github.chinalhr.gungnir.netchannel.client.pool;

import io.github.chinalhr.gungnir.serializer.ISerializer;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @Author : ChinaLHR
 * @Date : Create in 15:45 2018/4/26
 * @Email : 13435500980@163.com
 */
public class NettyClientPoolFactory extends BasePooledObjectFactory<NettyClientPoolProxy> {

    private String host;
    private int port;
    private ISerializer serializer;

    public NettyClientPoolFactory(String host, int port, ISerializer serializer) {
        this.host = host;
        this.port = port;
        this.serializer = serializer;
    }

    @Override
    public NettyClientPoolProxy create() throws Exception {
        NettyClientPoolProxy proxy = new NettyClientPoolProxy();
        proxy.createProxy(host, port, serializer);
        return proxy;
    }

    @Override
    public PooledObject<NettyClientPoolProxy> wrap(NettyClientPoolProxy nettyClientPoolProxy) {
        return new DefaultPooledObject<>(nettyClientPoolProxy);
    }

    @Override
    public void destroyObject(PooledObject<NettyClientPoolProxy> p) throws Exception {
        NettyClientPoolProxy proxy = p.getObject();
        proxy.close();
    }

    @Override
    public boolean validateObject(PooledObject<NettyClientPoolProxy> p) {
        NettyClientPoolProxy proxy = p.getObject();
        return proxy.isValidate();
    }
}
