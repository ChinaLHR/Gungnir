package io.github.chinalhr.gungnir.listener;

import io.github.chinalhr.gungnir.annonation.GServiceListener;
import io.github.chinalhr.gungnir.exception.GRpcRuntimeException;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.github.chinalhr.gungnir.utils.ApplicationContextUtils.*;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:00 2018/5/24
 * @Email : 13435500980@163.com
 *
 * Lister构建Holder
 */
public class ListenerHolder {

    private static Map<String,List<ProviderInvokerListener>> listenerMap;

    public static Map<String,List<ProviderInvokerListener>> getProviderInvokerListenerMap(){
        if (MapUtils.isEmpty(listenerMap)){

            if (null==getApplicationContext()) throw new GRpcRuntimeException("ListenerHolder getApplicationContext is Null");

            Map<String, Object> annotationMap = getApplicationContext().getBeansWithAnnotation(GServiceListener.class);
            listenerMap = new HashMap<>();
            if (!MapUtils.isEmpty(annotationMap)){
                annotationMap.values().forEach(listenerBean->{
                    GServiceListener annotation = listenerBean.getClass().getAnnotation(GServiceListener.class);
                    String serviceName = annotation.value().getName();
                    String version = annotation.version();
                    if (!StringUtils.isEmpty(version)) {
                        serviceName += "-" + version;
                    }
                    //为每个serviceName增加
                    if (null==listenerMap.get(serviceName)) {
                        ArrayList<ProviderInvokerListener> list = new ArrayList<>();
                        Class<?>[] interfaces = listenerBean.getClass().getInterfaces();
                        for (Class<?> clazz:interfaces){
                            if (clazz.isAssignableFrom(ProviderInvokerListener.class)){
                                list.add((ProviderInvokerListener) listenerBean);
                            }
                        }
                        listenerMap.put(serviceName,list);
                    }else{
                        Class<?>[] interfaces = listenerBean.getClass().getInterfaces();
                        for (Class<?> clazz:interfaces){
                            if (clazz.isAssignableFrom(ProviderInvokerListener.class)){
                                listenerMap.get(serviceName).add((ProviderInvokerListener) listenerBean);
                            }
                        }
                    }
                });
            }
        }
        return listenerMap;
    }


}
