package io.github.chinalhr.gungnir_rpc.exception;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:52 2018/5/11
 * @Email : 13435500980@163.com
 *
 * Gungnir RPC 负载均衡异常
 */
public class GRpcLoadBalanceException extends RuntimeException {

    public GRpcLoadBalanceException() {
    }

    public GRpcLoadBalanceException(String message) {
        super(message);
    }

    public GRpcLoadBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public GRpcLoadBalanceException(Throwable cause) {
        super(cause);
    }
}
