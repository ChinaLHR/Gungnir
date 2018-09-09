package io.github.chinalhr.gungnir_rpc.lock;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:44 2018/8/2
 * @Email : 13435500980@163.com
 */
public interface BlockCallback {

    /**
     * 获取到锁
     * @return
     */
    Object getlock();

}
