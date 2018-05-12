package io.github.chinalhr.gungnir.netloadbalance.impl;

import io.github.chinalhr.gungnir.exception.GRpcLoadBalanceException;
import io.github.chinalhr.gungnir.netloadbalance.ILoadBalance;
import io.github.chinalhr.gungnir.protocol.ProviderService;
import io.github.chinalhr.gungnir.utils.GeneralUtils;

import java.net.SocketException;
import java.util.List;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:49 2018/5/11
 * @Email : 13435500980@163.com
 * <p>
 * 软负载均衡：一致性hash算法(利用调用方IP hash值与服务列表大小取模获得服务索引值)
 */
public class HashLoadBalance implements ILoadBalance {
    @Override
    public ProviderService selectProviderService(List<ProviderService> providerServiceList) {
        try {
            String ip = GeneralUtils.getHostAddress();
            int hashCode = ip.hashCode();

            int size = providerServiceList.size();
            return providerServiceList.get(hashCode % size);
        } catch (SocketException e) {
            throw new GRpcLoadBalanceException("LoadBalance get HostAddress Error");
        }
    }
}
