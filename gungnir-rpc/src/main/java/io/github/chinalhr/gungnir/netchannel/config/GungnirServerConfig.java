package io.github.chinalhr.gungnir.netchannel.config;

import io.github.chinalhr.gungnir.enums.SerializeEnum;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:06 2018/5/17
 * @Email : 13435500980@163.com
 */
public abstract class GungnirServerConfig {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GungnirServerConfig.class);

    private String ip = "127.0.0.1";//ip地址
    private int port = 8888;//默认Server端口8888
    private ISerializer serializer = SerializeEnum.protostuff.serializer;//默认配置Protostuff

    public void setPort(int port) {
        this.port = port;
    }

    public void setSerializer(String serializer) {
        this.serializer = SerializeEnum.match(serializer, SerializeEnum.protostuff).serializer;
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

}
