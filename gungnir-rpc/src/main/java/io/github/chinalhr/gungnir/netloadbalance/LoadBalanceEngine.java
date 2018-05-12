package io.github.chinalhr.gungnir.netloadbalance;

import io.github.chinalhr.gungnir.netloadbalance.impl.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:34 2018/5/11
 * @Email : 13435500980@163.com
 */
public class LoadBalanceEngine {

    private static final Map<String,ILoadBalance> loadbalanceMap = new ConcurrentHashMap<>();

    static {
        loadbalanceMap.put("hash",new HashLoadBalance());
        loadbalanceMap.put("poll",new PollLoadBalance());
        loadbalanceMap.put("random",new RandomLoadBalance());
        loadbalanceMap.put("weightPoll",new WeightPollLoadBalance());
        loadbalanceMap.put("weightRandom",new WeightRandomLoadBalance());
    }

    public static ILoadBalance getBoadBalanceByName(String boadBalanceName){
        return loadbalanceMap.get(boadBalanceName);
    }


}
