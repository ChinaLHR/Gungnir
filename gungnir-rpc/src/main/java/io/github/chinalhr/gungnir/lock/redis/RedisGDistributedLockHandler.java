package io.github.chinalhr.gungnir.lock.redis;

import io.github.chinalhr.gungnir.lock.GDistributedLockHandler;
import io.github.chinalhr.gungnir.lock.pool.GRedisPool;
import io.github.chinalhr.gungnir.utils.GeneralUtils;
import io.github.chinalhr.gungnir.utils.PropertyConfigeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:48 2018/8/1
 * @Email : 13435500980@163.com
 */
public class RedisGDistributedLockHandler implements GDistributedLockHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisGDistributedLockHandler.class);

    /**
     * 重试等待时间
     */
    private int retryWaitTime = 300;

    @Override
    public boolean tryLock(String lockId, long timeout, TimeUnit unit) {
        final long startMillis = System.currentTimeMillis();
        final Long millisToWait = unit.toMillis(timeout);
        String lockValue = null;
        while (Objects.isNull(lockValue)) {
            Jedis connect = GRedisPool.getConnect();
            Thread thread = Thread.currentThread();
            Long lockTimeout = PropertyConfigeUtils.getDistributedLockTimeout();
            lockValue = connect.set(lockId, thread.getName(), "NX", "PX", lockTimeout);
            if (lockValue.equals(thread.getName())){
                //增加守护线程，快超时时候增加wait时间
                DistributedLockDaemon.startDaemon(lockId,thread.getName(),()->
                        connect.set(lockId, thread.getName(), "NX", "PX", lockTimeout));
                return true;
            }
            if (System.currentTimeMillis() - startMillis - retryWaitTime > millisToWait) {
                break;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(retryWaitTime));
        }
        return false;
    }

    @Override
    public boolean tryLock(String lockId) {
        String lockValue = null;
        while (Objects.isNull(lockValue)) {
            Jedis connect = GRedisPool.getConnect();
            Thread thread = Thread.currentThread();
            Long lockTimeout = PropertyConfigeUtils.getDistributedLockTimeout();
            lockValue = connect.set(lockId, thread.getName(), "NX", "PX", lockTimeout);
            if (lockValue.equals(thread.getName())){
                //使用守护线程进行续时
                return true;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(retryWaitTime));
        }
        return false;
    }

    @Override
    public void unlock() {
        //关闭守护线程
    }

}
