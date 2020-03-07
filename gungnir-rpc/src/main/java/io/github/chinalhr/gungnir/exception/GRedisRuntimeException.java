package io.github.chinalhr.gungnir.exception;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:15 2018/7/30
 * @Email : 13435500980@163.com
 */
public class GRedisRuntimeException extends RuntimeException {

    public GRedisRuntimeException() {
    }

    public GRedisRuntimeException(String message) {
        super(message);
    }

    public GRedisRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GRedisRuntimeException(Throwable cause) {
        super(cause);
    }
}

