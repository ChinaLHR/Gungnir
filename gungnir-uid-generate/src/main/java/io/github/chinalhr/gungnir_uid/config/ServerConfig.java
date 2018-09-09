package io.github.chinalhr.gungnir_uid.config;

/**
 * @Author : ChinaLHR
 * @Date : Create in 20:42 2018/9/9
 * @Email : 13435500980@163.com
 */
public interface ServerConfig {


    /**
     * HTTP端口
     */
    int HTTP_PORT = 11000;

    /**
     * HTTP 请求路径
     */
    String HTTP_REQUEST_MAPPING = "geiUid";

    /**
     * 最大机器标识
     */
    long MAX_MACHINES_SING = 1024L;

    /**
     * 最小机器标识
     */
    long MIN_MACHINES_SING = 0L;

    /**
     * Acquire TPS 控制
     */
    int HTTP_TPS = 10000;
    int ACQUIRE_TIMEOUTMILLIS = 5000;
}
