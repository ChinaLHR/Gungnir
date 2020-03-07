package io.github.chinalhr.gungnir.filter;

import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;


/**
 * @Author : ChinaLHR
 * @Date : Create in 17:49 2018/5/23
 * @Email : 13435500980@163.com
 */
public interface FilterInvoker {

    GResponse invoker(GRequest request);

}
