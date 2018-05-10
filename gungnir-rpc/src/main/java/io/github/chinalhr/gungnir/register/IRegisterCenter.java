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
     * 进行服务提供者注册
     * @param providerServices
     */
    void registerProvider(List<ProviderService> providerServices);

    /**
     * 获取服务提供者缓存Map
     * Key:服务提供者接口  value:服务提供者服务方法列表
     * @return
     */
    Map<String,Map<String,List<ProviderService>>> getProviderServiceMap();

    /**
     * 获取服务消费者缓存Map
     * Key:服务提供者接口  value:服务消费者服务方法列表
     * @return
     */
    Map<String,Map<String,List<ConsumerService>>> getConsumerServiceMap();

    /**
     * 从zookeeper中初始化服务提供者信息
     */
    void initProviderMap();

    /**
     * 进行服务消费者注册
     * @param service
     */
    void registerConsumer(ConsumerService service);

    /**
     * 从Zookeeper中初始化服务消费者信息
     */
    void initConsumerMap();

}
