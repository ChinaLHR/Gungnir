package io.github.chinalhr.gungnir.enums;

import io.github.chinalhr.gungnir.netloadbalance.ILoadBalance;
import io.github.chinalhr.gungnir.netloadbalance.LoadBalanceEngine;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:03 2018/5/11
 * @Email : 13435500980@163.com
 */
public enum LoadBalanceEnum {

    hash(LoadBalanceEngine.getBoadBalanceByName("hash")),
    poll(LoadBalanceEngine.getBoadBalanceByName("poll")),
    random(LoadBalanceEngine.getBoadBalanceByName("random")),
    weightPoll(LoadBalanceEngine.getBoadBalanceByName("weightPoll")),
    weightRandom(LoadBalanceEngine.getBoadBalanceByName("weightRandom"));

    public final ILoadBalance loadBalance;

    private LoadBalanceEnum(ILoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    public static LoadBalanceEnum match(String name, LoadBalanceEnum defaultLoadBalance) {
        for (LoadBalanceEnum item : LoadBalanceEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultLoadBalance;
    }


}
