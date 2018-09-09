package io.github.chinalhr.gungnir_rpc.protocol;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:18 2018/3/16
 * @Email : 13435500980@163.com
 */
public class GRequest implements Serializable{

    private GRpcHeartbeat heartbeat;

    private static final long serialVersionUID = 1L;

    private String requestID;

    private long createMillisTime;

    private String className;

    private String version;

    private String groupName;

    private String methodName;

    private int maxConcurrent;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

    public int getMaxConcurrent() {
        return maxConcurrent;
    }

    public void setMaxConcurrent(int maxConcurrent) {
        this.maxConcurrent = maxConcurrent;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public long getCreateMillisTime() {
        return createMillisTime;
    }

    public void setCreateMillisTime(long createMillisTime) {
        this.createMillisTime = createMillisTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public GRpcHeartbeat getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(GRpcHeartbeat heartbeat) {
        this.heartbeat = heartbeat;
    }

    @Override
    public String toString() {
        return "GRequest{" +
                "requestID='" + requestID + '\'' +
                ", createMillisTime=" + createMillisTime +
                ", className='" + className + '\'' +
                ", version='" + version + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}