package io.github.chinalhr.gungnir.filter.impl;

import com.google.common.util.concurrent.RateLimiter;
import io.github.chinalhr.gungnir.filter.FilterInvoker;
import io.github.chinalhr.gungnir.filter.ProviderFilter;
import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.chinalhr.gungnir.filter.FilterHolder.getServiceName;
import static io.github.chinalhr.gungnir.netchannel.server.GungnirServerFactory.concurrentMap;

/**
 * @Author : ChinaLHR
 * @Date : Create in 11:08 2018/5/24
 * @Email : 13435500980@163.com
 *
 * Provider Service 限流Filter
 */
public class ProviderLimitFilter implements ProviderFilter{

    private final static Logger LOGGER = LoggerFactory.getLogger(ProviderLimitFilter.class);
    private final static Map<String,RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    @Override
    public GResponse invoke(FilterInvoker nextInvoker, GRequest request)
    {
        LOGGER.info("ProviderLimitFilter invoker");
        acquire(request);
        GResponse invoker = nextInvoker.invoker(request);
        return invoker;
    }

    public static void acquire(GRequest request){
        String serviceName = getServiceName(request);
        if (concurrentMap.containsKey(serviceName)){
            if (!rateLimiterMap.containsKey(serviceName)) {
                final RateLimiter rateLimiter = RateLimiter.create(concurrentMap.get(serviceName));
                rateLimiterMap.put(serviceName, rateLimiter);
                rateLimiter.acquire();
            }
            else {
                RateLimiter rateLimiter=rateLimiterMap.get(serviceName);
                rateLimiter.acquire();
            }
        }else {
            return;
        }
    }

    public static ProviderLimitFilter of(){
        return new ProviderLimitFilter();
    }
}
