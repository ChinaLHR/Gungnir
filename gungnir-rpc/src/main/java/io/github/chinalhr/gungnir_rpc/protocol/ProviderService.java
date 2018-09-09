package io.github.chinalhr.gungnir_rpc.protocol;

import java.io.Serializable;

/**
 * @Author : ChinaLHR
 * @Date : Create in 15:05 2018/5/3
 * @Email : 13435500980@163.com
 *
 * 提供者注册信息
 */
public class ProviderService implements Serializable{

    private String serviceName;

    private String version;

    private int weight;

    private String groupName;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ProviderService clone(){
        ProviderService providerClone = new ProviderService();
        providerClone.setServiceName(this.serviceName);
        providerClone.setGroupName(this.groupName);
        providerClone.setWeight(this.weight);
        providerClone.setAddress(this.address);
        providerClone.setVersion(this.version);
        return providerClone;
    }
}

