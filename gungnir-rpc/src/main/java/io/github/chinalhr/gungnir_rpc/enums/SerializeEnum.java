package io.github.chinalhr.gungnir_rpc.enums;

import io.github.chinalhr.gungnir_rpc.serializer.SerializerEngine;
import io.github.chinalhr.gungnir_rpc.serializer.ISerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:49 2018/1/5
 * @Email : 13435500980@163.com
 *
 */
public enum SerializeEnum {

    hession(SerializerEngine.getSerializerByName("hessian")),
    protostuff(SerializerEngine.getSerializerByName("protostuff")),
    kryo(SerializerEngine.getSerializerByName("kryo"));

    public final ISerializer serializer;

    SerializeEnum(ISerializer serializer) {
        this.serializer = serializer;
    }

    public static SerializeEnum match(String name, SerializeEnum defaultSerializer) {
        for (SerializeEnum item : SerializeEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultSerializer;
    }
}
