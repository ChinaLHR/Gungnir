package io.github.chinalhr.gungnir.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:45 2018/5/3
 * @Email : 13435500980@163.com
 *
 * Property工具类
 */
public class PropertyConfigeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyConfigeUtils.class);

    private static final String PROPERTY_CLASSPATH = "/gungnir.properties";

    private static final Properties properties = new Properties();

    /**
     * zk
     */
    private static String zkAddress = "";//默认zookeeper配置
    private static Integer zkSession_TimeOut;//Zookeeper Session超时
    private static Integer zkConnection_TimeOut;//Zookeeper 连接超时

    /**
     * netty
     */
    private static Integer netChannelSize;//Netty连接数

    /**
     * redis
     */
    private static Integer redisMaxActive;//jedis的最大活跃连接数
    private static Integer redisMaxIdle;//jedis最大空闲连接数
    private static Long redisMaxWait;//jedis池没有连接对象返回时，等待可用连接的最大时间，单位毫秒
    private static Boolean redisTestOnBorrow;//从池中获取连接的时候，是否进行有效检查
    private static Boolean redisTestOnReturn;//归还连接的时候，是否进行有效检查
    private static String redisIp;
    private static String redisPassword;
    private static Integer redisPort;
    private static Integer redisTimeout;//与服务器建立连接的超时时间



    static {
        InputStream is = null;
        try {
            is = PropertyConfigeUtils.class.getResourceAsStream(PROPERTY_CLASSPATH);
            if (null==is){
                throw new IllegalArgumentException("gungnir.properties can not found in the classpath");
            }
            properties.load(is);
            zkAddress = properties.getProperty("zkAddress","127.0.0.1:2181");
            zkSession_TimeOut = Integer.parseInt(properties.getProperty("zkSession_TimeOut","5000"));
            zkConnection_TimeOut = Integer.parseInt(properties.getProperty("zkConnection_TimeOut","1000"));
            netChannelSize = Integer.parseInt(properties.getProperty("netChannelSize","10"));
            redisMaxActive = Integer.valueOf(properties.getProperty("redisMaxActive","100"));
            redisMaxIdle = Integer.valueOf(properties.getProperty("redisMaxIdle","50"));
            redisMaxWait = Long.valueOf(properties.getProperty("redisMaxWait","1500"));
            redisTestOnBorrow = Boolean.valueOf(properties.getProperty("redisTestOnBorrow","true"));
            redisTestOnReturn = Boolean.valueOf(properties.getProperty("redisTestOnReturn","true"));
            redisIp = properties.getProperty("redisIp","127.0.0.1");
            redisPassword = properties.getProperty("redisPassword","");
            redisPort = Integer.valueOf(properties.getProperty("redisPort","6379"));
            redisTimeout = Integer.valueOf(properties.getProperty("redisTimeout","3000"));


        }catch (Throwable t){
            LOGGER.error("load gungnir.properties failed",t);
            throw new RuntimeException(t);
        }finally {
            if (null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String getZkAddress() {
        return zkAddress;
    }

    public static int getZkSession_TimeOut() {
        return zkSession_TimeOut;
    }

    public static int getZkConnection_TimeOut() {
        return zkConnection_TimeOut;
    }

    public static int getNetChannelSize() {
        return netChannelSize;
    }

    public static Integer getRedisMaxActive() {
        return redisMaxActive;
    }

    public static Integer getRedisMaxIdle() {
        return redisMaxIdle;
    }

    public static Long getRedisMaxWait() {
        return redisMaxWait;
    }

    public static Boolean getRedisTestOnBorrow() {
        return redisTestOnBorrow;
    }

    public static Boolean getRedisTestOnReturn() {
        return redisTestOnReturn;
    }

    public static String getRedisIp() {
        return redisIp;
    }

    public static String getRedisPassword() {
        return redisPassword;
    }

    public static Integer getRedisPort() {
        return redisPort;
    }

    public static Integer getRedisTimeout() {
        return redisTimeout;
    }
}
