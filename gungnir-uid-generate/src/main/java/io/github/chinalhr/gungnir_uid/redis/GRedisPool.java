package io.github.chinalhr.gungnir_uid.redis;

import io.github.chinalhr.gungnir_uid.exception.GungnirUidException;
import io.github.chinalhr.gungnir_uid.utils.PropertyConfigeUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:11 2018/7/30
 * @Email : 13435500980@163.com
 */
@Slf4j
public class GRedisPool {

    private static JedisPool jedisPool = null;

    private static ThreadLocal<Jedis> local = new ThreadLocal<>();

    private GRedisPool() {
    }

    public static void initPool() {
        try {
            // 创建jedis池配置实例
            JedisPoolConfig config = new JedisPoolConfig();
            // 设置池配置项值
            config.setMaxTotal(PropertyConfigeUtils.getRedisMaxActive());
            config.setMaxIdle(PropertyConfigeUtils.getRedisMaxIdle());
            config.setMaxWaitMillis(PropertyConfigeUtils.getRedisMaxWait());
            config.setTestOnBorrow(PropertyConfigeUtils.getRedisTestOnBorrow());
            config.setTestOnReturn(PropertyConfigeUtils.getRedisTestOnReturn());

            // 根据配置实例化jedis池
            jedisPool = new JedisPool(config, PropertyConfigeUtils.getRedisIp(),
                    PropertyConfigeUtils.getRedisPort(),
                    PropertyConfigeUtils.getRedisTimeout(),
                    PropertyConfigeUtils.getRedisPassword());
        } catch (Exception e) {
            log.error("Gungnir-uid Jedis Pool init Failure",e);
            throw new GungnirUidException(e);
        }
    }


    /**
     * 获得连接
     *
     * @return Jedis
     */
    public static Jedis getConnect() {
        //Redis对象
        Jedis jedis = local.get();
        if (jedis == null) {
            if (jedisPool == null) {
                initPool();
            }
            jedis = jedisPool.getResource();
            local.set(jedis);
        }
        return jedis;
    }

    /**
     * 关闭连接
     */
    public static void closeConnect() {
        //从本地线程中获取
        Jedis jedis = local.get();
        if (jedis != null) {
            jedis.close();
        }
        local.set(null);
    }

    /**
     * 关闭连接池
     */
    public static void closePool() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}


