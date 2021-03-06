package io.github.chinalhr.gungnir.annonation;

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

    /**
     * 服务版本号 默认""
     * @return
     */
    String version() default "";

    /**
     * 权重 默认1
     * @return
     */
    int weight() default 1;

    /**
     * 服务分组组名 默认default
     * @return
     */
    String groupName() default "default";

    /**
     * 每秒最高并发数量
     * @return
     */
    int maxConcurrent() default 0;

}
