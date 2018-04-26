package io.github.chinalhr.gungnir.utils;

import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:14 2018/4/25
 * @Email : 13435500980@163.com
 */
public class RpcCallbackFuture {

    public static ConcurrentHashMap<String,RpcCallbackFuture> futurePool = new ConcurrentHashMap<>();

    private GResponse response;
    private GRequest request;

    private boolean isDone = false;
    private Object lock = new Object();

    public RpcCallbackFuture(GRequest request) {
        this.request = request;
        futurePool.put(request.getRequestID(),this);
    }

    public GResponse getResponse() {
        return response;
    }

    public void setResponse(GResponse response) {
        this.response = response;
        //notify future lock
        synchronized (lock){
            isDone = true;
            lock.notifyAll();
        }
    }

    public GResponse get(long timeoutMillis) throws InterruptedException, TimeoutException {
        if (!isDone) {
            synchronized (lock) {
                try {
                    lock.wait(timeoutMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        if (!isDone) {
            throw new TimeoutException(MessageFormat.format("GungnirClient request timeout at:{0}, request:{1}", System.currentTimeMillis(), request.toString()));
        }
        return response;
    }
}
