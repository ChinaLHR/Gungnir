package io.github.chinalhr.gungnir.lock.redis;

import io.github.chinalhr.gungnir.lock.UnBlockCallback;
import io.github.chinalhr.gungnir.lock.GDistributedLock;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:09 2018/7/30
 * @Email : 13435500980@163.com
 *
 * 分布式锁Redis实现
 */
public class RedisGDistributedLock implements GDistributedLock {

    @Override
    public Object execute(String lockId, int timeout, UnBlockCallback unBlockCallback) {
        return null;
    }

    @Override
    public Object execute(String lockId, UnBlockCallback unBlockCallback) {
        return null;
    }


}
