package io.github.chinalhr.provider1;

import io.github.chinalhr.gungnir.netchannel.server.GungnirServerFactory;
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
     * 配置服务提供者
     * @return
     */
    @Bean
    public GungnirServerFactory getGungnirServerFactory(){
        GungnirServerFactory serverFactory = new GungnirServerFactory();
        //配置server address
        serverFactory.setIp("127.0.0.1");
        serverFactory.setPort(8000);
        //配置序列化协议
        serverFactory.setSerializer("protostuff");
        return serverFactory;
    }

}
