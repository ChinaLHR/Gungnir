package io.github.chinalhr.gungnir.netchannel.client.future;

import io.github.chinalhr.gungnir.protocol.GResponse;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:07 2018/5/18
 * @Email : 13435500980@163.com
 *
 * 异步调用返回结果包装类
 */
public class GResponseWrapper {

    //存储返回结果的阻塞队列
    private BlockingQueue<GResponse> responseQueue = new ArrayBlockingQueue<>(1);

    private long createTimeMillis;

    public BlockingQueue<GResponse> getResponseQueue() {
        return responseQueue;
    }

    public void setCreateTimeMillis(long createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public static GResponseWrapper build(){
        return new GResponseWrapper();
    }
}
