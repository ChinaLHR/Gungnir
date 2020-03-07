package io.github.chinalhr.gungnir.threadpool;

import io.github.chinalhr.gungnir.utils.GeneralUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:27 2018/5/18
 * @Email : 13435500980@163.com
 *
 * GungnirClient线程池管理器
 */
public class GungnirClientThreadPoolManager {

    private static ExecutorService responseCallThreadPool = Executors.newFixedThreadPool(GeneralUtils.getThreadConfigNumberOfIO());

    public static ExecutorService getResponseCallThreadPool(){
        return responseCallThreadPool;
    }

}
