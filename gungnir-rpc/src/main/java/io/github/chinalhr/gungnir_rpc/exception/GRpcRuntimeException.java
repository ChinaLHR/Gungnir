package io.github.chinalhr.gungnir_rpc.exception;

/**
 * @Author : ChinaLHR
 * @Date : Create in 15:43 2018/4/23
 * @Email : 13435500980@163.com
 *
 * Gungnir RPC 运行时异常
 */
public class GRpcRuntimeException extends RuntimeException{

    public GRpcRuntimeException() {
    }

    public GRpcRuntimeException(String message) {
        super(message);
    }

    public GRpcRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GRpcRuntimeException(Throwable cause) {
        super(cause);
    }
}
