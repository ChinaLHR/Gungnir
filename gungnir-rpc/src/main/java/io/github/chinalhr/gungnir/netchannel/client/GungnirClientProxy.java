package io.github.chinalhr.gungnir.netchannel.client;

import io.github.chinalhr.gungnir.common.SerializeEnum;
import io.github.chinalhr.gungnir.exception.GRpcRuntimeException;
import io.github.chinalhr.gungnir.netchannel.client.netty.GungnirClient;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.register.IServiceDiscovery;
import io.github.chinalhr.gungnir.register.zk.ZkServiceDiscovery;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.github.chinalhr.gungnir.utils.GeneralUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;

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
    private ISerializer serializer = SerializeEnum.PROTOSTUFF.serializer;//默认配置Protostuff
//    private String zkAddress = "127.0.0.1:2181";//默认zookeeper配置
//    private int zkSession_TimeOut = 5000;//Zookeeper Session超时
//    private int zkConnection_TimeOut = 1000;//Zookeeper 连接超时
    private long timeoutMillis = 5000;//请求超时时间

    public void setIclass(Class<?> iclass) {
        this.iclass = iclass;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setSerializer(String serializer) {
        this.serializer = SerializeEnum.match(serializer, SerializeEnum.PROTOSTUFF).serializer;
    }

//    public void setZkAddress(String zkAddress) {
//        this.zkAddress = zkAddress;
//    }
//
//    public void setZkSession_TimeOut(int zkSession_TimeOut) {
//        this.zkSession_TimeOut = zkSession_TimeOut;
//    }
//
//    public void setZkConnection_TimeOut(int zkConnection_TimeOut) {
//        this.zkConnection_TimeOut = zkConnection_TimeOut;
//    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }


    /**
     * field
     */
    private IClient client;
    private IServiceDiscovery serviceDiscovery;
    private String serviceAddress;

    public GungnirClientProxy() {
    }

    public GungnirClientProxy(Class<?> iclass, String version, ISerializer serializer, String zkAddress, int zkSession_TimeOut, int zkConnection_TimeOut, long timeoutMillis) {
        this.iclass = iclass;
        this.version = version;
        this.serializer = serializer;
//        this.zkAddress = zkAddress;
//        this.zkSession_TimeOut = zkSession_TimeOut;
//        this.zkConnection_TimeOut = zkConnection_TimeOut;
        this.timeoutMillis = timeoutMillis;
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

            serviceDiscovery = new ZkServiceDiscovery();
            if (serviceDiscovery != null) {
                String serviceName = iclass.getName();
                if (!StringUtils.isEmpty(version)) {
                    serviceName += "-" + version;
                }
                serviceAddress = serviceDiscovery.discover(serviceName);
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
                throw new Exception("GungnirClientProxy Get GResponse Is Null");
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
        client = new GungnirClient();
    }
}
