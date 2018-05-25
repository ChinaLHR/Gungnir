package io.github.chinalhr.provider1.listener;

import io.github.chinalhr.gungnir.annonation.GServiceListener;
import io.github.chinalhr.gungnir.listener.ProviderInvokerListener;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.serviceapi.IDataService;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:49 2018/5/25
 * @Email : 13435500980@163.com
 */
@GServiceListener(
        value = IDataService.class,
        version = "1.0.0")
public class DataServiceListener implements ProviderInvokerListener {

    @Override
    public void beforeInvoker(GRequest request) {
        System.out.println("IDataService beforeInvoker");
    }

    @Override
    public void afterInvoker(GRequest request) {
        System.out.println("IDataService afterInvoker");
    }
}
