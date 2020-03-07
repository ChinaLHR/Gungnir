package io.github.chinalhr.gungnir.netloadbalance.impl;

import io.github.chinalhr.gungnir.netloadbalance.ILoadBalance;
import io.github.chinalhr.gungnir.protocol.ProviderService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:52 2018/5/11
 * @Email : 13435500980@163.com
 *
 * 软负载均衡：加权随机算法
 */
public class WeightRandomLoadBalance implements ILoadBalance {
    @Override
    public ProviderService selectProviderService(List<ProviderService> providerServiceList) {
        List<ProviderService> tProviderServiceList = new ArrayList<>();
        for (ProviderService p:providerServiceList) {
            int weight = p.getWeight();
            for (int i = 0; i < weight; i++) {
                tProviderServiceList.add(p.clone());
            }
        }
        int length = tProviderServiceList.size();
        int index = ThreadLocalRandom.current().nextInt(0,length);
        return tProviderServiceList.get(index);
    }
}
