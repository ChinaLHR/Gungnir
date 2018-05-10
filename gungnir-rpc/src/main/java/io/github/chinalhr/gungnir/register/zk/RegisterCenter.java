package io.github.chinalhr.gungnir.register.zk;

import io.github.chinalhr.gungnir.common.Constant;
import io.github.chinalhr.gungnir.exception.GRpcRuntimeException;
import io.github.chinalhr.gungnir.protocol.ConsumerService;
import io.github.chinalhr.gungnir.protocol.ProviderService;
import io.github.chinalhr.gungnir.register.IRegisterCenter;
import io.github.chinalhr.gungnir.utils.CollectionUtil;
import io.github.chinalhr.gungnir.utils.PropertyConfigeUtils;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author : ChinaLHR
 * @Date : Create in 11:55 2018/5/9
 * @Email : 13435500980@163.com
 * <p>
 * zookeeper path:gungnir/groupName/serviceName-version/type/address-weight
 */
public class RegisterCenter implements IRegisterCenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCenter.class);

    /**
     * 本地缓存服务提供者列表 key：serviceName value：服务提供者注册信息列表
     * key：groupName —— value：[key:serviceName value:List<ProviderService>]
     */
    private Map<String, Map<String, List<ProviderService>>> providerServiceMap = new ConcurrentHashMap<>();

    /**
     * 本地缓存服务消费者列表 key：serviceName value：服务消费者列表
     * key：groupName —— value：[key:serviceName value:List<ComsumerService>]
     */
    private Map<String, Map<String, List<ConsumerService>>> consumerServiceMap = new ConcurrentHashMap<>();

    private String address;
    private int zkSession_TimeOut;
    private int zkConnection_TimeOut;
    private final ZkClient zkClient;

    @Override
    public void registerProvider(List<ProviderService> providerServices) {
        if (CollectionUtil.isEmpty(providerServices)) return;
        //创建registry根节点（持久）
        String registryPath = ROOT_PATH;
        if (!zkClient.exists(ROOT_PATH)) {
            zkClient.createPersistent(registryPath, true);
            LOGGER.debug("create registry node: {}", registryPath);
        }
        for (ProviderService providerService : providerServices) {
            //创建registry service节点（持久）
            String servicePath = registryPath + "/" + providerService.getGroupName() + "/" + providerService.getServiceName() + "/" + PROVIDER_TYPE;
            if (!zkClient.exists(servicePath)) {
                zkClient.createPersistent(servicePath, true);
                LOGGER.debug("create service node: {}", servicePath);
            }
            //创建registry config节点（临时）
            String address = providerService.getAddress();
            int weight = providerService.getWeight();

            String configPath = servicePath + "/" + address + "-" + weight;
            boolean exist = zkClient.exists(configPath);
            if (!exist) {
                zkClient.createEphemeral(configPath);
            }

        }
    }

    @Override
    public Map<String, Map<String, List<ProviderService>>> getProviderServiceMap() {

        return providerServiceMap;
    }

    @Override
    public Map<String, Map<String, List<ConsumerService>>> getConsumerServiceMap() {
        return consumerServiceMap;
    }

    //TODO 使用Stream代替外循环
    @Override
    public void initProviderMap() {
        String registryPath = ROOT_PATH;
        if (!zkClient.exists(registryPath))
            throw new GRpcRuntimeException(String.format("RegisterCenter can not find any service node on path:%s", registryPath));
        List<String> groupNames = zkClient.getChildren(registryPath);//groupName
        for (String groupName : groupNames) {
            List<ProviderService> providerServiceList = new ArrayList<>();
            Map<String, List<ProviderService>> tMap = new ConcurrentHashMap<>();
            String groupPath = registryPath + "/" + groupName;
            if (!zkClient.exists(registryPath))
                throw new GRpcRuntimeException(String.format("RegisterCenter can not find any service node on path:%s", groupPath));
            List<String> serviceNames = zkClient.getChildren(groupPath);
            for (String serviceName : serviceNames) {
                String servicePath = groupPath + "/" + serviceName + "/" + PROVIDER_TYPE;
                List<String> configs = zkClient.getChildren(servicePath);
                for (String config : configs) {
                    ProviderService providerService = buildProviderService(servicePath, config);
                    providerServiceList.add(providerService);
                }

                if (providerServiceList.size() > 0) {
                    tMap.put(serviceName, providerServiceList);
                    //监听注册服务的变化更新本地缓存
                    zkClient.subscribeChildChanges(servicePath, (parentPath, children) -> {
                        RefreshProviderServiceMap(parentPath, children);
                        System.out.println();
                    });
                }
            }
            providerServiceMap.put(groupName, tMap);
            System.out.println();
        }

    }


    @Override
    public void registerConsumer(ConsumerService service) {

    }

    @Override
    public void initConsumerMap() {

    }


    /**
     * singleton
     */
    private static class RegisterCenterHolder {
        private static final RegisterCenter INSTANCE = new RegisterCenter();
    }

    private RegisterCenter() {
        this.address = PropertyConfigeUtils.getZkAddress();
        this.zkSession_TimeOut = PropertyConfigeUtils.getZkSession_TimeOut();
        this.zkConnection_TimeOut = PropertyConfigeUtils.getZkConnection_TimeOut();
        zkClient = new ZkClient(address, zkSession_TimeOut, zkConnection_TimeOut);
        LOGGER.debug("connect zookeeper");
    }

    public static final RegisterCenter getInstance() {
        return RegisterCenterHolder.INSTANCE;
    }

    /**
     * 刷新ProviderServiceMap缓存
     *
     * @param parentPath
     * @param children
     */
    private void RefreshProviderServiceMap(String parentPath, List<String> children) {
        if (children.size() > 0) {
            String[] paths = parentPath.split("/");
            providerServiceMap.get(paths[2]).get(paths[3]).clear();

            for (String config : children) {
                ProviderService providerService = buildProviderService(parentPath, config);
                providerServiceMap.get(paths[2]).get(paths[3]).add(providerService);
            }
        }
    }

    /**
     * 构造ProviderService
     *
     * @param servicePath
     * @param config
     * @return
     */
    private ProviderService buildProviderService(String servicePath, String config) {
        ProviderService providerService = new ProviderService();
        String[] configArray = config.split("-");
        String[] serviceArray = servicePath.split("/");
        providerService.setAddress(configArray[0]);
        providerService.setWeight(Integer.parseInt(configArray[1]));
        providerService.setGroupName(serviceArray[1]);
        providerService.setServiceName(serviceArray[2]);
        return providerService;
    }
}
