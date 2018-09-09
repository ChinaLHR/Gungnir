package io.github.chinalhr.gungnir_rpc.netloadbalance;

import io.github.chinalhr.gungnir_rpc.protocol.ProviderService;

import java.util.List;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:07 2018/5/11
 * @Email : 13435500980@163.com
 *
 * 负载均衡接口
 */
public interface ILoadBalance {

    /**
     * 服务提供者负载均衡接口
     * @return
     */
    ProviderService selectProviderService(List<ProviderService> providerServiceList);

}
