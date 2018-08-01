package io.github.chinalhr.gungnir.lock;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:33 2018/8/1
 * @Email : 13435500980@163.com
 */
public interface GDistributedLock {

    /**
     * 分布式锁执行
     * @param lockId
     * @param timeout
     * @param callback
     * @return
     */
    Object execute(String lockId,int timeout,Callback callback);

}
