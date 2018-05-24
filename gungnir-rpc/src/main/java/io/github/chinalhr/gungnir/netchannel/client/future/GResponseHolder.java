package io.github.chinalhr.gungnir.netchannel.client.future;

import io.github.chinalhr.gungnir.protocol.GResponse;
import io.github.chinalhr.gungnir.utils.GeneralUtils;
import sun.misc.ThreadGroupUtils;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:34 2018/5/18
 * @Email : 13435500980@163.com
 *
 * 异步返回容器
 */
public class GResponseHolder {

    //Map requestID——ResponseWrapper
    public static final Map<String,GResponseWrapper> responseMap = new ConcurrentHashMap<>();



    /**
     * 构建RequestID与GResponse的对应关系
     * @param requestID
     */
    public static void initGResponse(String requestID){
        responseMap.put(requestID,GResponseWrapper.build());
    }

    /**
     * 将异步调用返回结果放入阻塞队列中
     * @param response
     */
    public static void putGResponse(GResponse response){
        GResponseWrapper responseWrapper = responseMap.get(response.getRequestId());
        responseWrapper.getResponseQueue().add(response);
        responseWrapper.setCreateTimeMillis(System.currentTimeMillis());
        responseMap.put(response.getRequestId(),responseWrapper);
    }


    /**
     * 从阻塞队列中获取异步返回结果值
     * @param requestID
     * @param timeoutMillis
     * @return
     */
    public static GResponse getResponse(String requestID,long timeoutMillis){
        GResponseWrapper responseWrapper = responseMap.get(requestID);
        try {
            return responseWrapper.getResponseQueue().poll(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            responseMap.remove(requestID);
        }
        return null;
    }
}
