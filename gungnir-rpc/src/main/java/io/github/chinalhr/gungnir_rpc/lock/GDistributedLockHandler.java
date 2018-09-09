package io.github.chinalhr.gungnir_rpc.lock;

import java.util.concurrent.TimeUnit;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:39 2018/8/1
 * @Email : 13435500980@163.com
 */
public interface GDistributedLockHandler {

    boolean tryLock(String lockId,long timeout, TimeUnit unit);

    boolean tryLock(String lockId);

    void unlock(String lockId);

}
