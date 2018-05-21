package io.github.chinalhr.gungnir.protocol;

import java.io.Serializable;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:42 2018/5/20
 * @Email : 13435500980@163.com
 */
public class GRpcHeartbeat implements Serializable{

    private byte type;

    private String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
