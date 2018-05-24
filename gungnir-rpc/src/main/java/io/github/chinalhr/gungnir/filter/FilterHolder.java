package io.github.chinalhr.gungnir.filter;

import io.github.chinalhr.gungnir.annonation.GFilter;
import io.github.chinalhr.gungnir.exception.GRpcRuntimeException;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.stream.Collectors;

import static io.github.chinalhr.gungnir.utils.ApplicationContextUtils.getApplicationContext;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:58 2018/5/23
 * @Email : 13435500980@163.com
 *
 * Filter构建Utils
 */
public class FilterHolder {

    /**
     * 获取Filter List(从Order大到小)
     * @return
     */
    public static List<Object> getFilterList(){
        ArrayList<Object> filterList = new ArrayList<>();
        if (null==getApplicationContext()) throw new GRpcRuntimeException("GFilterHolder getApplicationContext is Null");
        Map<String, Object> filterBeanMap = getApplicationContext().getBeansWithAnnotation(GFilter.class);
        if (!MapUtils.isEmpty(filterBeanMap)){
            filterList.addAll(filterBeanMap.values());
            Collections.sort(filterList,(o1,o2)->{
                GFilter pf1 = o1.getClass().getAnnotation(GFilter.class);
                GFilter pf2 = o2.getClass().getAnnotation(GFilter.class);
                return pf1.order()>pf2.order()?1:-1;
            });
        }
        return filterList;
    }

    /**
     * 获取FilterMap(key-className，value-bean)
     * @return
     */
    public static Map<String,ProviderFilter> getFilterMap(){
        List<Object> gFilterList = getFilterList();
        HashMap<String, ProviderFilter> filterMap = new LinkedHashMap<>();
        gFilterList.forEach(filterBean->{
            Class<?>[] interfaces = filterBean.getClass().getInterfaces();
            for (Class<?> clazz : interfaces){
                if (clazz.isAssignableFrom(ProviderFilter.class)){
                    filterMap.put(filterBean.getClass().getName(), (ProviderFilter) filterBean);
                }
            }
        });
        return filterMap;
    }

}
