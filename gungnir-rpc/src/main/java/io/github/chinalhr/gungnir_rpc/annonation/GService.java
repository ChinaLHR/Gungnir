package io.github.chinalhr.gungnir_rpc.annonation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:35 2018/1/5
 * @Email : 13435500980@163.com
 * GService 服务注册注解
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface GService {

    /**
     * Service接口
     * @return
     */
    Class<?> value();

    
}
