package io.github.chinalhr.exampleserver.listener;

import io.github.chinalhr.api.IDataService;
import io.github.chinalhr.gungnir.annonation.GServiceListener;
import io.github.chinalhr.gungnir.listener.ProviderInvokerListener;
import io.github.chinalhr.gungnir.protocol.GRequest;

/**
 * @Author : ChinaLHR
 * @Date : Create in 13:45 2018/5/25
 * @Email : 13435500980@163.com
 */
@GServiceListener(
        value = IDataService.class,
        version = "1.0.0")
public class DataServiceListener implements ProviderInvokerListener{

    @Override
    public void beforeInvoker(GRequest request) {
        System.out.println("IDataService beforeInvoker");
    }

    @Override
    public void afterInvoker(GRequest request) {
        System.out.println("IDataService afterInvoker");
    }
}
