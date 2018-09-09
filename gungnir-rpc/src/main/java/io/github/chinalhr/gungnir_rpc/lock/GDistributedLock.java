package io.github.chinalhr.gungnir_rpc.lock;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:33 2018/8/1
 * @Email : 13435500980@163.com
 */
public interface GDistributedLock {

    /**
     * 分布式锁执行(非阻塞)
     * @param lockId
     * @param timeout
     * @param unBlockCallback
     * @return
     */
    Object execute(String lockId, int timeout, UnBlockCallback unBlockCallback);

    /**
     * 分布式锁执行(阻塞)
     * @param lockId
     * @param unBlockCallback
     * @return
     */
    Object execute(String lockId, UnBlockCallback unBlockCallback);
}
