package io.github.chinalhr.consumer1;

import io.github.chinalhr.gungnir_rpc.netchannel.client.GungnirClientProxy;
import io.github.chinalhr.serviceapi.IDataService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:24 2018/5/25
 * @Email : 13435500980@163.com
 */
@Configuration
public class BeanConfig {


    @Bean(name = "IDataService")
    public GungnirClientProxy getGungnirClientProxy(){
        GungnirClientProxy iDataServicce = new GungnirClientProxy();
        iDataServicce.setSerializer("protostuff");
        iDataServicce.setIclass(IDataService.class);
        iDataServicce.setVersion("1.0.0");
        iDataServicce.setTimeoutMillis(5000);
        iDataServicce.setGroupName("dev");
        iDataServicce.setLoadBalance("random");
        return iDataServicce;
    }
}
