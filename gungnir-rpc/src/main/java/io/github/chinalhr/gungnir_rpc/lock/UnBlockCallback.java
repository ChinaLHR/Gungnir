package io.github.chinalhr.gungnir_rpc.lock;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:36 2018/8/1
 * @Email : 13435500980@163.com
 */
public interface UnBlockCallback {

    /**
     * 获取到锁
     * @return
     */
    Object getlock();

    /**
     * 锁超时
     * @return
     */
    Object getTimeout();

}
