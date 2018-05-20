package io.github.chinalhr.gungnir.netloadbalance.impl;

import io.github.chinalhr.gungnir.exception.GRpcLoadBalanceException;
import io.github.chinalhr.gungnir.netloadbalance.ILoadBalance;
import io.github.chinalhr.gungnir.protocol.ProviderService;
import io.github.chinalhr.gungnir.utils.GeneralUtils;

import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
            int size = providerServiceList.size();
            String ip = GeneralUtils.getHostAddress();
            Integer hashCode = ip.hashCode();
            ProviderService service = providerServiceList.get(hashCode % size);
            return service;
        } catch (SocketException e) {
            throw new GRpcLoadBalanceException("HashLoadBalance get HostAddress Error");
        }
    }

}
