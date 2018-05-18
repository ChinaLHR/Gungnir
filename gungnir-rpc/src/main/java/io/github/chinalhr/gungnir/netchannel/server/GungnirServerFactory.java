package io.github.chinalhr.gungnir.netchannel.server;

import io.github.chinalhr.gungnir.annonation.GService;
import io.github.chinalhr.gungnir.netchannel.config.GungnirServerConfig;
import io.github.chinalhr.gungnir.protocol.ProviderService;
import io.github.chinalhr.gungnir.register.zk.RegisterCenter;
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
public class GungnirServerFactory extends GungnirServerConfig implements ApplicationContextAware, InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(GungnirServerFactory.class);

    /**
     * Service Map
     */
    private static Map<String, Object> serviceMap = new HashMap<>();

    private static List<ProviderService> providerServices = new ArrayList<>();
    /**
     * field
     */
    private IServer server;

    @Override
    public void destroy() throws Exception {
        if (server != null)
            server.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        server = new GungnirServer();
        server.init();
        server.start(getIp(), getPort(), getSerializer());

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
        RegisterCenter center = RegisterCenter.getInstance();
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
                ProviderService providerService = buildProviderService(serviceName, annotation);
                providerServices.add(providerService);
                center.registerProvider(providerServices);
            }
        }
        InvokeOperation.setServiceMap(serviceMap);
    }

    /**
     * 根据注解源数据于serviceName构建ProviderService
     * @param serviceName
     * @param metaDate
     * @return
     */
    private ProviderService buildProviderService(String serviceName,GService metaDate){
        ProviderService providerService = new ProviderService();
        providerService.setGroupName(metaDate.groupName());
        providerService.setWeight(metaDate.weight());
        providerService.setServiceName(serviceName);
        providerService.setVersion(metaDate.version());
        providerService.setAddress(getIp() + ":" + getPort());
        return providerService;
    }

}
