package io.github.chinalhr.gungnir.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

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
    public <T> byte[] serialize(T t) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(out);
        try {
            ho.writeObject(t);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return out.toByteArray();
    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        HessianInput hi = new HessianInput(in);
        try {
            return hi.readObject();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(),e);
        }
    }
}
