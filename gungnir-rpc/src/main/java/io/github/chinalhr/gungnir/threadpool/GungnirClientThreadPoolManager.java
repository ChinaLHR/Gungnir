package io.github.chinalhr.gungnir.threadpool;

import io.github.chinalhr.gungnir.utils.GeneralUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:27 2018/5/18
 * @Email : 13435500980@163.com
 */
public class GungnirClientThreadPoolManager {

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(GeneralUtils.getThreadConfigNumberOfCPU());

    public static ExecutorService getFixedThreadPool(){
        return fixedThreadPool;
    }

//    /**
//     * Singleton
//     */
//    private GungnirClientThreadPoolManager(){}
//
//    private static class ClientThreadPoolHolder{
//        private static GungnirClientThreadPoolManager INSTANCE = new GungnirClientThreadPoolManager();
//    }
//
//    public static final GungnirClientThreadPoolManager getInstance(){
//        return ClientThreadPoolHolder.INSTANCE;
//    }
}
