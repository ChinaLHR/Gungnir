package io.github.chinalhr.provider1.filter;

import io.github.chinalhr.gungnir.annonation.GFilter;
import io.github.chinalhr.gungnir.filter.FilterInvoker;
import io.github.chinalhr.gungnir.filter.ProviderFilter;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:34 2018/5/25
 * @Email : 13435500980@163.com
 */

//GFilter 服务提供者Filter order：顺序值
@GFilter(order = 10)
public class ProviderFilterOrder10 implements ProviderFilter {
    @Override
    public GResponse invoke(FilterInvoker filterInvoker, GRequest gRequest) {
        System.out.println("Filter======10=========");
        return filterInvoker.invoker(gRequest);
    }
}
