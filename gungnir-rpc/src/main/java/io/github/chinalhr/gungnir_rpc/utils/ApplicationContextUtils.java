package io.github.chinalhr.gungnir_rpc.utils;

import org.springframework.context.ApplicationContext;

/**
 * @Author : ChinaLHR
 * @Date : Create in 20:25 2018/5/23
 * @Email : 13435500980@163.com
 */
public class ApplicationContextUtils {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextUtils.applicationContext = applicationContext;
    }
}
