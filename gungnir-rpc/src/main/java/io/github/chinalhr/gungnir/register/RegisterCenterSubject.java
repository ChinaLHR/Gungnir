package io.github.chinalhr.gungnir.register;

import io.github.chinalhr.gungnir.exception.GRpcRuntimeException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:02 2018/5/17
 * @Email : 13435500980@163.com
 *
 * 注册中心Subject：注册，注销，通知
 */
public abstract class RegisterCenterSubject {

    protected List<RegisterCenterObserver> registerCenterObservers = new ArrayList<>();

    public void attach(RegisterCenterObserver observer){
        if (observer==null) throw new GRpcRuntimeException("RegisterCenterSubject attach error : RegisterCenterObserver is null");
        if (!registerCenterObservers.contains(observer))
            registerCenterObservers.add(observer);
    }

    public void detach(RegisterCenterObserver observer){
        if (observer==null) throw new GRpcRuntimeException("RegisterCenterSubject detach error : RegisterCenterObserver is null");
        registerCenterObservers.remove(observer);
    }

    protected abstract void notificationUpdateProviderService();
}
