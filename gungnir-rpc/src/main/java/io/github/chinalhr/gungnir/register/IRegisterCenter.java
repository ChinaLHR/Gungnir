package io.github.chinalhr.gungnir.register;

import io.github.chinalhr.gungnir.protocol.ConsumerService;
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
     * @param consumerService
     */
    void registerConsumer(ConsumerService consumerService);

    /**
     * 从Zookeeper中初始化服务消费者信息
     */
    void initConsumerMap();

    /**
     * TODO 取消注册服务提供者
     * @return
     */
    boolean unRegisterProvider(String groupName,String serviceName);

    /**
     * TODO 取消注册服务生产者
     * @return
     */
    boolean unRegisterConsumer(String groupName,String serviceName);

    /**
     * 注册中心Subject：注册
     * @param groupName
     * @param observer
     */
    void attach(String groupName,RegisterCenterObserver observer);

    /**
     * 注册中心Subject：注销
     * @param groupName
     * @param observer
     */
    void detach(String groupName,RegisterCenterObserver observer);

    /**
     * 注册中心Subject：通知
     * @param parentPath
     */
    void notificationObserver(String parentPath);
}
