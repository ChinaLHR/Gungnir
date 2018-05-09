package io.github.chinalhr.gungnir.register;

import io.github.chinalhr.gungnir.protocol.ConsumerService;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * @Author : ChinaLHR
 * @Date : Create in 14:27 2018/5/8
 * @Email : 13435500980@163.com
 *
 * 注册中心接口
 */
public interface IRegisterCenter {

    String ROOT_PATH="/gungnir";
    String PROVIDER_TYPE = "provider";
    String CONSUMER_TYPE = "consumer";

    /**
     * 进行服务注册
     * @param providerServices
     */
    void registerProvider(List<ProviderService> providerServices);

    /**
     * 获取服务提供者Map
     * Key:服务提供者接口  value:服务提供者服务方法列表
     * @return
     */
    Map<String,List<ProviderService>> getProviderServiceMap();

    /**
     * 初始化服务提供者信息
     * @param groupName
     */
    void initProviderMap(String groupName);

    /**
     * 消费者注册
     * @param service
     */
    void registerConsumer(ConsumerService service);

    /**
     * 获取服务提供者元信息，自动更新
     * @return
     */
    Map<String,List<ProviderService>> getServiceMetaDataMap();

}
