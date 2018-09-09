package io.github.chinalhr.gungnir_rpc.lock.redis;

import com.google.common.collect.Maps;
import io.github.chinalhr.gungnir_rpc.lock.pool.GRedisPool;
import io.github.chinalhr.gungnir_rpc.utils.Func;
import io.github.chinalhr.gungnir_rpc.utils.PropertyConfigeUtils;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:14 2018/8/27
 * @Email : 13435500980@163.com
 * <p>
 * 分布式锁守护线程
 */
public class DistributedLockDaemon {

    private static ConcurrentMap<String,Thread> daemonMap = Maps.newConcurrentMap();

    private static final Long lockTimeoutCritical = 500L;

    private static Jedis jedis = GRedisPool.getConnect();

    public static void startDaemon(String lockId, String threadName, Func func) {
        Thread daemonThread = new Thread(() -> {
            Long lockTimeout = PropertyConfigeUtils.getDistributedLockTimeout();
            while (true) {
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(lockTimeout - lockTimeoutCritical));
                if (threadName.equals(jedis.get(lockId))) {
                    //续命
                    func.apply();
                }
            }
        });
        daemonThread.setDaemon(true);
        daemonThread.start();
        daemonMap.put(threadName, daemonThread);
    }

    public static void stopDaemon(String threadName){
        daemonMap.remove(threadName);
    }

}
