package io.github.chinalhr.gungnir.server;

import io.github.chinalhr.gungnir.annonation.GService;
import io.github.chinalhr.gungnir.common.SerializeEnum;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:24 2018/1/2
 * @Email : 13435500980@163.com
 *
 * Server配置Bean
 */
public class GungnirServerFactory implements ApplicationContextAware,InitializingBean,DisposableBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(GungnirServerFactory.class);

    /**
     * Service Map
     */
    private static Map<String, Object> serviceMap = new HashMap<>();

    /**
     * config
     */
    private int port = 8888;//默认Server端口8888
    private ISerializer serializer = SerializeEnum.PROTOSTUFF.serializer;//默认配置Protostuff
    private String zookeeper = "127.0.0.1:2181";//默认zookeeper配置

    public void setPort(int port) {
        this.port = port;
    }

    public void setSerializer(ISerializer serializer) {
        this.serializer = serializer;
    }

    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //TODO 启动服务器 Zookeeper注册Service，在获取Service的时候进行负载均衡
    }

    /**
     * 在构建Bean的时候对RPCService进行获取
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(GService.class);
        if(!MapUtils.isEmpty(serviceBeanMap)){
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(GService.class).value().getName();
                serviceMap.put(interfaceName,serviceBean);
            }
        }
    }
}
