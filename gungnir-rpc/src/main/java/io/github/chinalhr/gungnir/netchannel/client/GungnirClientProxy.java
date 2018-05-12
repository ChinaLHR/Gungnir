package io.github.chinalhr.gungnir.netchannel.client;

import io.github.chinalhr.gungnir.enums.LoadBalanceEnum;
import io.github.chinalhr.gungnir.enums.SerializeEnum;
import io.github.chinalhr.gungnir.exception.GRpcRuntimeException;
import io.github.chinalhr.gungnir.netchannel.client.netty.GungnirClient;
import io.github.chinalhr.gungnir.netloadbalance.ILoadBalance;
import io.github.chinalhr.gungnir.protocol.ConsumerService;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.protocol.ProviderService;
import io.github.chinalhr.gungnir.register.IRegisterCenter;
import io.github.chinalhr.gungnir.register.zk.RegisterCenter;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.github.chinalhr.gungnir.utils.GeneralUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * @Author : ChinaLHR
 * @Date : Create in 14:13 2018/4/26
 * @Email : 13435500980@163.com
 */
public class GungnirClientProxy implements FactoryBean<Object>, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(GungnirClientProxy.class);

    /**
     * config
     */
    private Class<?> iclass;
    private String version;
    private ISerializer serializer = SerializeEnum.protostuff.serializer;//默认配置Protostuff
    private ILoadBalance loadBalance = LoadBalanceEnum.random.loadBalance;//默认配置随机负载均衡算法
    private long timeoutMillis = 5000;//请求超时时间
    private String groupName = "default";//路由分组名

    public void setIclass(Class<?> iclass) {
        this.iclass = iclass;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setSerializer(String serializer) {
        this.serializer = SerializeEnum.match(serializer, SerializeEnum.protostuff).serializer;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = LoadBalanceEnum.match(loadBalance,LoadBalanceEnum.random).loadBalance;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * field
     */
    private IClient client;
    private IRegisterCenter registerCenter;
    private String serviceAddress;

    public GungnirClientProxy() {
    }

    public GungnirClientProxy(Class<?> iclass, String version, ISerializer serializer, long timeoutMillis,String groupName) {
        this.iclass = iclass;
        this.version = version;
        this.serializer = serializer;
        this.timeoutMillis = timeoutMillis;
        this.groupName = groupName;
        try {
            afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public Object getObject() throws Exception {

        return Proxy.newProxyInstance(iclass.getClassLoader(), new Class[]{iclass}, (proxy, method, args) -> {
            //封装GRequest
            GRequest request = new GRequest();
            request.setClassName(method.getDeclaringClass().getName());
            request.setCreateMillisTime(System.currentTimeMillis());
            request.setRequestID(GeneralUtils.getUUID());
            request.setMethodName(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setParameters(args);
            request.setVersion(version);
            request.setGroupName(groupName);

            registerCenter.initProviderMap();
            if (registerCenter != null) {
                String serviceName = iclass.getName();
                if (!StringUtils.isEmpty(version)) {
                    serviceName += "-" + version;
                }
                Map<String, Map<String, List<ProviderService>>> providerServiceMap = registerCenter.getProviderServiceMap();
                List<ProviderService> providerServices = providerServiceMap.get(groupName).get(serviceName);

                ProviderService providerService = loadBalance.selectProviderService(providerServices);
                serviceAddress = providerService.getAddress();
                LOGGER.debug("GungnirClientProxy discover serviceAddress={}", serviceAddress);
            } else {
                LOGGER.error("GungnirClientProxy serviceDiscovery is null");
                throw new GRpcRuntimeException("GungnirClientProxy serviceDiscovery is empty");
            }

            //获取RPC Server的host:ip
            String[] split = StringUtils.split(serviceAddress, ":");
            String host = split[0];
            int port = Integer.parseInt(split[1]);

            //发送request接受response
            GResponse response = client.send(host, port, request, serializer, timeoutMillis);

            if (response == null) {
                LOGGER.error("GungnirClientProxy Get GResponse Is Null");
                throw new GRpcRuntimeException("GungnirClientProxy Get GResponse Is Null");
            }

            if (response.getError() != null) {
                LOGGER.error("GungnirClientProxy Get GResponse Error:{}", response.getError().getLocalizedMessage());
                throw response.getError();
            } else {
                return response.getResult();
            }
        });
    }

    @Override
    public Class<?> getObjectType() {
        return iclass;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registerCenter = RegisterCenter.getInstance();
        client = new GungnirClient();
        //进行消费者注册
        String serviceName = iclass.getName();
        if (!StringUtils.isEmpty(version)) {
            serviceName += "-" + version;
        }

        ConsumerService consumerService = new ConsumerService();
        consumerService.setIp(GeneralUtils.getHostAddress());
        consumerService.setServiceName(serviceName);
        consumerService.setGroupName(groupName);
        registerCenter.registerConsumer(consumerService);
    }

}
