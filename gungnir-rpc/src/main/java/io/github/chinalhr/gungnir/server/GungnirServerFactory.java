package io.github.chinalhr.gungnir.server;

import io.github.chinalhr.gungnir.annonation.GService;
import io.github.chinalhr.gungnir.common.SerializeEnum;
import io.github.chinalhr.gungnir.register.IServiceRegistry;
import io.github.chinalhr.gungnir.register.zk.ZKServiceRegistry;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.github.chinalhr.gungnir.server.netty.GungnirServer;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.imageio.spi.ServiceRegistry;
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
    private String ip = "127.0.0.1";//ip地址
    private int port = 8888;//默认Server端口8888
    private ISerializer serializer = SerializeEnum.PROTOSTUFF.serializer;//默认配置Protostuff
    private String zkAddress = "127.0.0.1:2181";//默认zookeeper配置
    private int zkSession_TimeOut = 5000;//Zookeeper Session超时
    private int zkConnection_TimeOut = 1000;//Zookeeper 连接超时

    public void setPort(int port) {
        this.port = port;
    }

    public void setSerializer(String serializer) {
        this.serializer = SerializeEnum.match(serializer,SerializeEnum.PROTOSTUFF).serializer;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public void setZkSession_TimeOut(int zkSession_TimeOut) {
        this.zkSession_TimeOut = zkSession_TimeOut;
    }

    public void setZkConnection_TimeOut(int zkConnection_TimeOut) {
        this.zkConnection_TimeOut = zkConnection_TimeOut;
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

    public String getZkAddress() {
        return zkAddress;
    }

    public int getZkSession_TimeOut() {
        return zkSession_TimeOut;
    }

    public int getZkConnection_TimeOut() {
        return zkConnection_TimeOut;
    }

    /**
     * field
     */
    private IServer server;
    private IServiceRegistry serviceRegistry;

    @Override
    public void destroy() throws Exception {
        if (server!=null)
        server.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        server = new GungnirServer();
        server.init();
        server.start(ip,port,serializer);

        String serviceAddress = ip+":"+port;
        serviceRegistry = new ZKServiceRegistry(zkAddress,zkSession_TimeOut,zkConnection_TimeOut);
        if (serviceRegistry!=null){
            for (String interfaceName:serviceMap.keySet()){
                serviceRegistry.register(interfaceName,serviceAddress);
                LOGGER.debug("registry service:{} ————> {}",interfaceName,serviceAddress);
            }
        }
    }

    /**
     * 在构建Bean的时候对RPCService进行获取,并载入配置
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(GService.class);
        if(!MapUtils.isEmpty(serviceBeanMap)){
            for (Object serviceBean : serviceBeanMap.values()) {
                GService annotation = serviceBean.getClass().getAnnotation(GService.class);
                String serviceName = annotation.value().getName()+'-'+annotation.verson();
                serviceMap.put(serviceName,serviceBean);
            }
        }
        InvokeOperation.setServiceMap(serviceMap);
    }



}
