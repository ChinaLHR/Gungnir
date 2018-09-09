package io.github.chinalhr.gungnir_uid.exception;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:09 2018/9/9
 * @Email : 13435500980@163.com
 */
public class GungnirUidException extends RuntimeException{

    public GungnirUidException(String message) {
        super(message);
    }

    public GungnirUidException(String message, Throwable cause) {
        super(message, cause);
    }

    public GungnirUidException(Throwable cause) {
        super(cause);
    }
}
