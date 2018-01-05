package io.github.chinalhr.gungnir_rpc.serializer;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:52 2018/1/5
 * @Email : 13435500980@163.com
 */
public interface ISerializer {

    <T>byte[] serialize(T t);
    <T>Object deserialize(byte[] bytes,Class<T> clazz);

}
