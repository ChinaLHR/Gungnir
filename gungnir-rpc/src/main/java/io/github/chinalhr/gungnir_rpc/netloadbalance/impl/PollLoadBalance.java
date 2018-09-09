package io.github.chinalhr.gungnir_rpc.netloadbalance.impl;

import io.github.chinalhr.gungnir_rpc.netloadbalance.ILoadBalance;
import io.github.chinalhr.gungnir_rpc.protocol.ProviderService;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:24 2018/5/11
 * @Email : 13435500980@163.com
 *
 * 软负载均衡：顺序轮询算法实现
 */
public class PollLoadBalance implements ILoadBalance {

    private int index = 0;

    private Lock lock = new ReentrantLock();

    @Override
    public ProviderService selectProviderService(List<ProviderService> providerServiceList) {
        ProviderService service = null;
        try {
            lock.tryLock(10, TimeUnit.MICROSECONDS);
            if (index>=providerServiceList.size())
                index = 0;
            service = providerServiceList.get(index);
            index++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

        if (service==null)
            service = providerServiceList.get(0);
        return service;
    }
}
