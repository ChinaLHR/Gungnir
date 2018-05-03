package io.github.chinalhr.gungnir.common;

import io.github.chinalhr.gungnir.serializer.impl.HessianSerializer;
import io.github.chinalhr.gungnir.serializer.ISerializer;
import io.github.chinalhr.gungnir.serializer.impl.KryoSerializer;
import io.github.chinalhr.gungnir.serializer.impl.ProtostuffSerializer;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:49 2018/1/5
 * @Email : 13435500980@163.com
 *
 * 序列化方案
 */
public enum SerializeEnum {

    HESSIAN(new HessianSerializer()),
    PROTOSTUFF(new ProtostuffSerializer()),
    KRYO(new KryoSerializer());

    public final ISerializer serializer;

    private SerializeEnum(ISerializer serializer) {
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
