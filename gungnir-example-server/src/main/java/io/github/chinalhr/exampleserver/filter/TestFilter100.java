package io.github.chinalhr.exampleserver.filter;

import io.github.chinalhr.gungnir.annonation.GFilter;
import io.github.chinalhr.gungnir.filter.FilterInvoker;
import io.github.chinalhr.gungnir.filter.ProviderFilter;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;


/**
 * @Author : ChinaLHR
 * @Date : Create in 22:17 2018/5/23
 * @Email : 13435500980@163.com
 */
@GFilter
public class TestFilter100 implements ProviderFilter {
    @Override
    public GResponse invoke(FilterInvoker parentInvoker, GRequest request) {
        System.out.println("Filter======100=========");
        return parentInvoker.invoker(request);
    }
}
