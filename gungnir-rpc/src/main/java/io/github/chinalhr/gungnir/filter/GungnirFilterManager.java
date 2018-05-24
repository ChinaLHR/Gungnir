package io.github.chinalhr.gungnir.filter;

import io.github.chinalhr.gungnir.filter.impl.ProviderLimitFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:41 2018/5/24
 * @Email : 13435500980@163.com
 */
public class GungnirFilterManager {
    private static List<ProviderFilter> gungnirFilterList = new ArrayList<>();

    static {
        gungnirFilterList.add(ProviderLimitFilter.of());
    }

    public static List<ProviderFilter> getGungnirFilterList() {
        return gungnirFilterList;
    }
}
