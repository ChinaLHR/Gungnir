package io.github.chinalhr.gungnir_rpc.register;

/**
 * @Author : ChinaLHR
 * @Date : Create in 10:36 2018/5/18
 * @Email : 13435500980@163.com
 *
 * 注册中心观察者接口
 */
public interface RegisterCenterObserver {

    /**
     * 服务生产者发生变化
     */
    void updateProviderService();

}
