package io.github.chinalhr.gungnir.lock;

import java.util.concurrent.TimeUnit;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:39 2018/8/1
 * @Email : 13435500980@163.com
 */
public interface GDistributedLockHandler {

    boolean tryLock(long timeout, TimeUnit unit);

    void unlock();

}
