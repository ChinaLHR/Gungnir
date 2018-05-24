package io.github.chinalhr.gungnir.common;

/**
 * @Author : ChinaLHR
 * @Date : Create in 9:27 2018/3/20
 * @Email : 13435500980@163.com
 */
public interface Constant {

    /**
     * RegisterCenter Config
     */
    String ROOT_PATH="/gungnir";
    String PROVIDER_TYPE = "provider";
    String CONSUMER_TYPE = "consumer";

    /**
     * Netty Config
     */
    int SERVER_READERIDLE_TIMESECONDS = 10;
    int CLIENT_ALLIDLE_TIMESECONDS = 5;
}
