package io.github.chinalhr.gungnir.register.zk;

import io.github.chinalhr.gungnir.common.Constant;
import io.github.chinalhr.gungnir.register.IServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : ChinaLHR
 * @Date : Create in 14:02 2018/3/20
 * @Email : 13435500980@163.com
 *
 * Zookeeper 服务注册实现
 */
public class ZKServiceRegistry implements IServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKServiceRegistry.class);

    private final ZkClient zkClient;

    public ZKServiceRegistry(String zkAddress, int zkSession_TimeOut, int zkConnection_TimeOut) {
        //创建ZooKeeper客户端
        zkClient = new ZkClient(zkAddress, zkSession_TimeOut,zkConnection_TimeOut);
        LOGGER.debug("connect zookeeper");
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        //创建registry 节点（持久）
        String registryPath = Constant.ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            LOGGER.debug("create registry node: {}", registryPath);
        }
        //创建 service 节点（持久）
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            LOGGER.debug("create service node: {}", servicePath);
        }
        // 创建 address 节点（临时）
        String addressPath = servicePath + "/address";
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
        LOGGER.debug("create address node: {}", addressNode);
    }

}
