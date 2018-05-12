package io.github.chinalhr.gungnir.netloadbalance.impl;

import io.github.chinalhr.gungnir.netloadbalance.ILoadBalance;
import io.github.chinalhr.gungnir.protocol.ProviderService;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:37 2018/5/11
 * @Email : 13435500980@163.com
 *
 * 软负载均衡：随机算法实现
 */
public class RandomLoadBalance implements ILoadBalance {

    @Override
    public ProviderService selectProviderService(List<ProviderService> providerServiceList) {
        int length = providerServiceList.size();
        int index = ThreadLocalRandom.current().nextInt(0,length);
        return providerServiceList.get(index);
    }
}
