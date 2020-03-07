package io.github.chinalhr.gungnir.filter.impl;

import io.github.chinalhr.gungnir.filter.FilterHolder;
import io.github.chinalhr.gungnir.filter.FilterInvoker;
import io.github.chinalhr.gungnir.filter.ProviderFilter;
import io.github.chinalhr.gungnir.listener.ListenerHolder;
import io.github.chinalhr.gungnir.listener.ProviderInvokerListener;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;

import java.util.List;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:07 2018/5/24
 * @Email : 13435500980@163.com
 *
 *
 */
public class ProviderListenerFilter implements ProviderFilter{

    @Override
    public GResponse invoke(FilterInvoker nextInvoker, GRequest request) {
        String serviceName = FilterHolder.getServiceName(request);
        List<ProviderInvokerListener> listeners = ListenerHolder.getProviderInvokerListenerMap().get(serviceName);
        GResponse invoker;
        if (listeners!=null){
            listeners.forEach(listener->{
                listener.beforeInvoker(request);
            });
            invoker = nextInvoker.invoker(request);
            listeners.forEach(listener->{
                listener.afterInvoker(request);
            });
        }
        else{
            invoker = nextInvoker.invoker(request);
        }
        return invoker;
    }

    public static ProviderListenerFilter of(){
        return new ProviderListenerFilter();
    }
}
