package com.xiaoxiao.common.exception;

import com.xiaoxiao.common.api.StatusCode;

/**
 * <p>
 *
 * </p>
 *
 * @author JunWei
 * @since 2022/10/21
 */
public class ApiException extends RuntimeException {

    private StatusCode statusCode;

    private String msg;

    public ApiException(StatusCode statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public ApiException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }

    public ApiException(String message) {
        super(message);
        this.msg = message;
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }
}
