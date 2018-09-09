package io.github.chinalhr.gungnir_rpc.listener;

import io.github.chinalhr.gungnir_rpc.protocol.GRequest;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:39 2018/5/24
 * @Email : 13435500980@163.com
 *
 * Provider调用监听器
 */
public interface ProviderInvokerListener {

    void beforeInvoker(GRequest request);

    void afterInvoker(GRequest request);

}
