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
}
