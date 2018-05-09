package io.github.chinalhr.gungnir.register.zk;

import io.github.chinalhr.gungnir.common.Constant;
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
 *
 * zookeeper path:gungnir/groupName/serviceName/type/address:weight:workerThreads:groupName
 */
public class RegisterCenter implements IRegisterCenter{
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCenter.class);

    /**
     * 本地缓存服务提供者列表 key：serviceName value：服务提供者注册信息列表
     */
    private Map<String,List<ProviderService>> providerServiceMap = new ConcurrentHashMap<>();

    private String address;
    private int zkSession_TimeOut;
    private int zkConnection_TimeOut;
    private final ZkClient zkClient;
    @Override
    public void registerProvider(List<ProviderService> serviceMetaDatas) {
        if (CollectionUtil.isEmpty(serviceMetaDatas)) return;
        //创建registry根节点（持久）
        String registryPath = ROOT_PATH;
        if (!zkClient.exists(ROOT_PATH)) {
            zkClient.createPersistent(registryPath,true);
            LOGGER.debug("create registry node: {}", registryPath);
        }
        for (ProviderService providerService:serviceMetaDatas) {
            //创建registry service节点（持久）
            String servicePath = registryPath + "/"+providerService.getGroupName()+"/" + providerService.getServiceName() +"/"+PROVIDER_TYPE;
            if (!zkClient.exists(servicePath)){
               zkClient.createPersistent(servicePath,true);
                LOGGER.debug("create service node: {}", servicePath);
            }
            //创建registry config节点（临时）
            String address = providerService.getAddress();
            int weight = providerService.getWeight();
            int workerThreads = providerService.getWorkerThreads();

            String configPath = servicePath+"/"+address+":"+weight+":"+workerThreads;
            boolean exist = zkClient.exists(configPath);
            if (!exist){
                zkClient.createEphemeral(configPath);
            }

            //TODO 监听注册服务的变化更新本地缓存
            zkClient.subscribeChildChanges(servicePath,(parentPath,strings)->{
                System.out.println(strings);
            });
        }
    }

    @Override
    public Map<String, List<ProviderService>> getProviderServiceMap() {
        return null;
    }

    @Override
    public void initProviderMap(String groupName) {

    }

    @Override
    public void registerConsumer(ConsumerService service) {

    }

    @Override
    public Map<String, List<ProviderService>> getServiceMetaDataMap() {
        return null;
    }

    /**
     * singleton
     */
    private static class RegisterCenterHolder{
        private static final RegisterCenter INSTANCE = new RegisterCenter();
    }

    private RegisterCenter() {
        this.address = PropertyConfigeUtils.getZkAddress();
        this.zkSession_TimeOut = PropertyConfigeUtils.getZkSession_TimeOut();
        this.zkConnection_TimeOut = PropertyConfigeUtils.getZkConnection_TimeOut();
        zkClient = new ZkClient(address, zkSession_TimeOut,zkConnection_TimeOut);
        LOGGER.debug("connect zookeeper");
    }

    public static final RegisterCenter getInstance(){
        return RegisterCenterHolder.INSTANCE;
    }

}
