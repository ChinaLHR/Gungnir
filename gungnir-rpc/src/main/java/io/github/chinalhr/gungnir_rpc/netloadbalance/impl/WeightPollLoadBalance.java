package io.github.chinalhr.gungnir_rpc.netloadbalance.impl;

import io.github.chinalhr.gungnir_rpc.netloadbalance.ILoadBalance;
import io.github.chinalhr.gungnir_rpc.protocol.ProviderService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:43 2018/5/11
 * @Email : 13435500980@163.com
 *
 * 软负载均衡：顺序加权轮询算法实现
 */
public class WeightPollLoadBalance implements ILoadBalance {

    private volatile int index = 0;

    private Lock lock = new ReentrantLock();

    @Override
    public ProviderService selectProviderService(List<ProviderService> providerServiceList) {
        ProviderService service = null;
        try {
            lock.tryLock(10, TimeUnit.MICROSECONDS);
            List<ProviderService> tProviderServiceList = new ArrayList<>();
            for (ProviderService p:providerServiceList) {
                int weight = p.getWeight();
                for (int i = 0; i < weight; i++) {
                    tProviderServiceList.add(p.clone());
                }
            }

            if (index>=tProviderServiceList.size())
                index = 0;
            service = tProviderServiceList.get(index);
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
