package io.github.chinalhr.gungnir_rpc.serializer.impl;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import io.github.chinalhr.gungnir_rpc.serializer.ISerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian序列化
 *
 * @Author : ChinaLHR
 * @Date : Create in 22:03 2018/1/5
 * @Email : 13435500980@163.com
 */
public class HessianSerializer implements ISerializer {

    @Override
    public <T> byte[] serialize(T t) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(out);
        try {
            ho.writeObject(t);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }finally {
            ho.close();
            out.close();
        }

    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        HessianInput hi = new HessianInput(in);
        try {
            return hi.readObject();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(),e);
        }finally {
            hi.close();
            in.close();
        }
    }
}
