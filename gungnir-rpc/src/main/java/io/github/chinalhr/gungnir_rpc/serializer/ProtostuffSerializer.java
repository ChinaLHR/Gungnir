package io.github.chinalhr.gungnir_rpc.serializer;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:15 2018/1/5
 * @Email : 13435500980@163.com
 */
public class ProtostuffSerializer implements ISerializer{

    private static Objenesis objenesis = new ObjenesisStd(true);
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
    private static <T> Schema<T> getSchema(Class<T> cls){
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    @Override
    public <T> byte[] serialize(T t) {
        Class<T> cls = (Class<T>) t.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(t, schema, buffer);
        }catch (Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }finally {
            buffer.clear();
        }

    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        try {
            T message = (T) objenesis.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        }catch (Exception e){
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
