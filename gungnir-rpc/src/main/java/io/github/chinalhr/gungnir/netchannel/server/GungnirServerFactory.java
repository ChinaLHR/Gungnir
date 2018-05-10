package io.github.chinalhr.gungnir.netchannel.server;

import io.github.chinalhr.gungnir.annonation.GService;
import io.github.chinalhr.gungnir.common.SerializeEnum;
import io.github.chinalhr.gungnir.protocol.ProviderService;
import io.github.chinalhr.gungnir.register.IServiceRegistry;
import io.github.chinalhr.gungnir.register.zk.RegisterCenter;
import io.github.chinalhr.gungnir.register.zk.ZKServiceRegistry;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.github.chinalhr.gungnir.netchannel.server.netty.GungnirServer;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:24 2018/1/2
 * @Email : 13435500980@163.com
 * <p>
 * Server配置Bean
 */
public class GungnirServerFactory implements ApplicationContextAware, InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(GungnirServerFactory.class);

    /**
     * Service Map
     */
    private static Map<String, Object> serviceMap = new HashMap<>();

    private static List<ProviderService> providerServices = new ArrayList<>();

    /**
     * config
     */
    private String ip = "127.0.0.1";//ip地址
    private int port = 8888;//默认Server端口8888
    private ISerializer serializer = SerializeEnum.PROTOSTUFF.serializer;//默认配置Protostuff

    public void setPort(int port) {
        this.port = port;
    }

    public void setSerializer(String serializer) {
        this.serializer = SerializeEnum.match(serializer, SerializeEnum.PROTOSTUFF).serializer;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public ISerializer getSerializer() {
        return serializer;
    }


    /**
     * field
     */
    private IServer server;
    private IServiceRegistry serviceRegistry;

    @Override
    public void destroy() throws Exception {
        if (server != null)
            server.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        server = new GungnirServer();
        server.init();
        server.start(ip, port, serializer);

    }


    /**
     * 在构建Bean的时候对RPCService进行获取,并载入配置
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(GService.class);
        if (!MapUtils.isEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                //build ServiceMap
                GService annotation = serviceBean.getClass().getAnnotation(GService.class);
                String serviceName = annotation.value().getName();
                String version = annotation.version();
                if (!StringUtils.isEmpty(version)) {
                    serviceName += "-" + version;
                }
                serviceMap.put(serviceName, serviceBean);

                //build ProviderServices
                ProviderService providerService = new ProviderService();
                providerService.setGroupName(annotation.groupName());
                providerService.setWeight(annotation.weight());
                providerService.setServiceName(serviceName);
                providerService.setVersion(annotation.version());
                providerService.setAddress(ip + ":" + port);
                RegisterCenter center = RegisterCenter.getInstance();
                providerServices.add(providerService);
                center.registerProvider(providerServices);
//                center.initProviderMap();
            }
        }
        InvokeOperation.setServiceMap(serviceMap);
    }


}
