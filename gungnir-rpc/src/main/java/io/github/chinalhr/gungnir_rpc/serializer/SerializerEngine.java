package io.github.chinalhr.gungnir_rpc.serializer;

import io.github.chinalhr.gungnir_rpc.serializer.impl.HessianSerializer;
import io.github.chinalhr.gungnir_rpc.serializer.impl.KryoSerializer;
import io.github.chinalhr.gungnir_rpc.serializer.impl.ProtostuffSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:49 2018/5/11
 * @Email : 13435500980@163.com
 */
public class SerializerEngine {

    private static final Map<String,ISerializer> serializerMap = new ConcurrentHashMap<>();


    static {
        serializerMap.put("hessian",new HessianSerializer());
        serializerMap.put("protostuff",new ProtostuffSerializer());
        serializerMap.put("kryo",new KryoSerializer());
    }

    public static ISerializer getSerializerByName(String serializerName){
        return serializerMap.get(serializerName);
    }

}
