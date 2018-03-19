package io.github.chinalhr.gungnir.register.zk;

import io.github.chinalhr.gungnir.register.ServiceDiscovery;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:06 2018/3/19
 * @Email : 13435500980@163.com
 *
 * Zookeeper服务注册实现
 */
public class ZkServiceDiscovery implements ServiceDiscovery{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkServiceDiscovery.class);

    private String zkAddress;

    public ZkServiceDiscovery(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    @Override
    public String discover(String serviceName) {
//        new ZkClient();
        return null;
    }
}
