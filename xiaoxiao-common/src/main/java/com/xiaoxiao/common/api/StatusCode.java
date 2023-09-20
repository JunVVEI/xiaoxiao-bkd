package com.xiaoxiao.common.api;

import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author JunWEI
 * @since 2022/10/21
 */
@Getter
public enum StatusCode {

    SUCCESS("操作成功"),

    // TODO: 此状态过于宽泛，后面有必要进行一定的细分
    FAILED("操作失败"),

    VALIDATE_FAILED("参数检验失败"),

    BIZ_ERROR("业务异常"),

    UNAUTHORIZED("暂未登录或token已经过期"),

    FORBIDDEN("没有相关权限"),

    SERVER_BUSY("服务繁忙");


    private final String message;

    StatusCode(String message) {
        this.message = message;
    }
}
