package io.github.chinalhr.gungnir.netchannel.config;

import io.github.chinalhr.gungnir.enums.LoadBalanceEnum;
import io.github.chinalhr.gungnir.enums.SerializeEnum;
import io.github.chinalhr.gungnir.netloadbalance.ILoadBalance;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : ChinaLHR
 * @Date : Create in 14:42 2018/5/17
 * @Email : 13435500980@163.com
 */
public abstract class GungnirClientConfig {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GungnirClientConfig.class);

    protected Class<?> iclass;
    protected String version;
    protected ISerializer serializer = SerializeEnum.protostuff.serializer;//默认配置Protostuff
    protected ILoadBalance loadBalance = LoadBalanceEnum.random.loadBalance;//默认配置随机负载均衡算法
    protected long timeoutMillis = 5000;//请求超时时间
    protected String groupName = "default";//路由分组名

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

    public Class<?> getIclass() {
        return iclass;
    }

    public String getVersion() {
        return version;
    }

    public ISerializer getSerializer() {
        return serializer;
    }

    public ILoadBalance getLoadBalance() {
        return loadBalance;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public String getGroupName() {
        return groupName;
    }
}
