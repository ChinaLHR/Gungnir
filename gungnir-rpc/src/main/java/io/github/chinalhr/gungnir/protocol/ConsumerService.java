package io.github.chinalhr.gungnir.protocol;

import java.io.Serializable;

/**
 * @Author : ChinaLHR
 * @Date : Create in 10:27 2018/5/9
 * @Email : 13435500980@163.com
 *
 * 消费者注册信息
 */
public class ConsumerService implements Serializable{

    private String serviceName;
    private String groupName;
    private String address;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
