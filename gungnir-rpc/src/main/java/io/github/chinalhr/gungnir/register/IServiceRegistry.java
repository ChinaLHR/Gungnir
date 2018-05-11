package io.github.chinalhr.gungnir.register;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:47 2018/3/19
 * @Email : 13435500980@163.com
 *
 * 服务注册接口
 */
@Deprecated
public interface IServiceRegistry {

    void register(String serviceName,String serviceAddress);

}
