package io.github.chinalhr.gungnir.protocol;

import java.io.Serializable;

/**
 * @Author : ChinaLHR
 * @Date : Create in 15:50 2018/3/16
 * @Email : 13435500980@163.com
 */
public class GResponse implements Serializable{

    private static final long serialVersionUID = 1L;

    private String requestID;

    private Throwable error;

    private Object result;

    public String getRequestId() {
        return requestID;
    }

    public void setRequestId(String requestId) {
        this.requestID = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GResponse{" +
                "requestId='" + requestID + '\'' +
                ", error=" + error +
                ", result=" + result +
                '}';
    }
}
