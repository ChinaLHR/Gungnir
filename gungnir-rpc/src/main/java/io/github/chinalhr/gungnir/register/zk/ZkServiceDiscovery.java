package io.github.chinalhr.gungnir.register.zk;

import io.github.chinalhr.gungnir.common.Constant;
import io.github.chinalhr.gungnir.register.IServiceDiscovery;
import io.github.chinalhr.gungnir.utils.CollectionUtil;
import io.netty.util.internal.ThreadLocalRandom;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:06 2018/3/19
 * @Email : 13435500980@163.com
 *
 * Zookeeper服务发现实现
 */
public class ZkServiceDiscovery implements IServiceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkServiceDiscovery.class);

    private String address;

    private int zkSession_TimeOut;

    private int zkConnection_TimeOut;

    public ZkServiceDiscovery(String address,int zkSession_TimeOut,int zkConnection_TimeOut) {
        this.address = address;
        this.zkSession_TimeOut = zkSession_TimeOut;
        this.zkConnection_TimeOut = zkConnection_TimeOut;
    }

    @Override
    public String discover(String serviceName) {
        ZkClient zkClient = new ZkClient(address, zkSession_TimeOut, zkConnection_TimeOut);
        String servicePath = Constant.ZK_REGISTRY_PATH+'/'+serviceName;

        //获取Service节点
        if (!zkClient.exists(servicePath))
            throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));

        //获取address节点
        List<String> addressList = zkClient.getChildren(servicePath);
        if (CollectionUtil.isEmpty(addressList))
            throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));

        String address;
        int size = addressList.size();
        //集群只有一个节点
        if (size == 1){
            address = addressList.get(0);
            LOGGER.debug("get only address node: {}", address);
        }
        else
        {
            //TODO 返回service的所有address
            address = addressList.get(ThreadLocalRandom.current().nextInt(size));
            LOGGER.debug("get random address node: {}", address);
        }
        //获取address节点的值
        String addressPath = servicePath + "/" + address;
        return zkClient.readData(addressPath);
    }
}
