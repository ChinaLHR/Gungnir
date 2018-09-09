package io.github.chinalhr.gungnir_rpc.threadpool;

import io.github.chinalhr.gungnir_rpc.utils.GeneralUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:24 2018/5/20
 * @Email : 13435500980@163.com
 *
 * GungnirServer线程池管理器
 */
public class GungnirServerThreadPoolManager {

    private static ExecutorService handlerThreadPool = Executors.newFixedThreadPool(GeneralUtils.getThreadConfigNumberOfIO());

    public static ExecutorService getHandlerThreadPool(){
        return handlerThreadPool;
    }

}
