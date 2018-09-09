package io.github.chinalhr.gungnir_rpc.serializer.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.github.chinalhr.gungnir_rpc.serializer.ISerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo序列化
 *
 * @Author : ChinaLHR
 * @Date : Create in 17:33 2018/4/27
 * @Email : 13435500980@163.com
 */
public class KryoSerializer implements ISerializer {

    @Override
    public <T> byte[] serialize(T t)  throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Output output = new Output(out);
        try {
            Kryo kryo = new Kryo();
            kryo.writeObject(output, t);
            return output.toBytes();
        }finally {
            out.close();
            output.close();
        }

    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Input input = new Input(in);
       try {
           Kryo kryo = new Kryo();
           return kryo.readObject(input, clazz);
       }finally {
           in.close();
           input.close();
       }
    }
}
