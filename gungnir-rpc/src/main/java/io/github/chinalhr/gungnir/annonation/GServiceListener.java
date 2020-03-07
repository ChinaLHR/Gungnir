package io.github.chinalhr.gungnir.annonation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:09 2018/5/24
 * @Email : 13435500980@163.com
 *
 * Service调用invoker
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface GServiceListener {
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

}
