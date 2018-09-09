package io.github.chinalhr.provider2;

import io.github.chinalhr.gungnir_rpc.netchannel.server.GungnirServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:24 2018/5/25
 * @Email : 13435500980@163.com
 */
@Configuration
public class BeanConfig {

    /**
     * 配置服务提供者工厂
     * @return
     */
    @Bean
    public GungnirServerFactory getGungnirServerFactory(){
        GungnirServerFactory serverFactory = new GungnirServerFactory();
        //配置server address
        serverFactory.setIp("127.0.0.1");
        serverFactory.setPort(9000);
        //配置序列化协议
        serverFactory.setSerializer("protostuff");
        return serverFactory;
    }

}
