package io.github.chinalhr.gungnir.filter;

import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:41 2018/5/23
 * @Email : 13435500980@163.com
 *
 * 服务提供者 Filter接口
 */
@FunctionalInterface
public interface ProviderFilter {

    GResponse invoke(FilterInvoker nextInvoker, GRequest request);

}
