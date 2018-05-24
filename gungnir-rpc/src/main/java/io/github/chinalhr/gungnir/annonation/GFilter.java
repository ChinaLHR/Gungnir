package io.github.chinalhr.gungnir.annonation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author : ChinaLHR
 * @Date : Create in 15:39 2018/5/23
 * @Email : 13435500980@163.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface GFilter {

    /**
     * Filter顺序指定
     * @return
     */
    int order() default 999999999;
}
