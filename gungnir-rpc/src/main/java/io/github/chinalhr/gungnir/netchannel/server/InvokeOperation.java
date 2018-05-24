package io.github.chinalhr.gungnir.netchannel.server;

import io.github.chinalhr.gungnir.protocol.GRequest;
import io.github.chinalhr.gungnir.protocol.GResponse;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static io.github.chinalhr.gungnir.netchannel.server.GungnirServerFactory.*;

/**
 * @Author : ChinaLHR
 * @Date : Create in 10:17 2018/3/19
 * @Email : 13435500980@163.com
 * <p>
 * Servicer操作类
 */
public class InvokeOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvokeOperation.class);

    /**
     * 通过CGLIB调用
     * @return
     */
    public static GResponse invokeService(GRequest request, Object serviceBean) {
        if (serviceBean == null) {
            if (request.getVersion()==""||request.getVersion()==null)
                serviceBean = serviceMap.get(request.getClassName());
            else
                serviceBean = serviceMap.get(request.getClassName() + '-' + request.getVersion());
        }
        GResponse response = new GResponse();
        response.setRequestId(request.getRequestID());
       try {
            Class<?> clazz = serviceBean.getClass();
            String methodName = request.getMethodName();
            Class<?>[] parameterTypes = request.getParameterTypes();
            Object[] parameters = request.getParameters();

            FastClass serviceFastClass = FastClass.create(clazz);
            FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);

            Object result = serviceFastMethod.invoke(serviceBean, parameters);
            response.setResult(result);

        } catch (Throwable e) {
            LOGGER.error("InvokeOperation Invoke Error :{}", e.fillInStackTrace());
            response.setError(e);
        }
        return response;
    }

}










