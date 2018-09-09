package io.github.chinalhr.gungnir_rpc.netchannel.server;

import io.github.chinalhr.gungnir_rpc.filter.FilterHolder;
import io.github.chinalhr.gungnir_rpc.filter.FilterInvoker;
import io.github.chinalhr.gungnir_rpc.filter.GungnirFilterManager;
import io.github.chinalhr.gungnir_rpc.filter.ProviderFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:39 2018/5/23
 * @Email : 13435500980@163.com
 *
 * Filter操作类
 */
public class FilterOperation {

    public static FilterInvoker buildInvokerChain(final FilterInvoker invoker) {
        FilterInvoker last = invoker;
        List<ProviderFilter> filterList = new ArrayList<>(FilterHolder.getFilterMap().values());
        //add Gungnir Filter
        filterList.addAll(GungnirFilterManager.getGungnirFilterList());

        if (filterList.size() > 0) {
            for (int i = filterList.size() - 1; i >= 0; i--) {
                final ProviderFilter filter = filterList.get(i);
                final FilterInvoker next = last;
                last = (req -> filter.invoke(next, req));
            }
        }
        return last;
    }

}
