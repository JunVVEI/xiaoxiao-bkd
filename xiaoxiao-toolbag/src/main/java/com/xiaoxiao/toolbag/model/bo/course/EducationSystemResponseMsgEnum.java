package com.xiaoxiao.toolbag.model.bo.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * 枚举类,抛出异常登录结果
 *
 * @author zjh
 */
@Getter
@AllArgsConstructor
@Slf4j
public enum EducationSystemResponseMsgEnum {

    SUCCESS("success", "登录成功"),

    KAPTCHA_INCORRECT("kaptcha.incorrect", "输入验证码错误!"),

    KAPTCHA_EXPIRED("kaptcha.expired", "验证码已过期!"),

    INPUT_ERROR("security.httpstatu.401.1001", "请输入正确的账号/密码!"),

    UNKNOWN_ERROR("", "拉取课表失败");

    private final String value;
    private final String message;

    public static EducationSystemResponseMsgEnum getByValue(String value) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getValue(), value))
                .findFirst()
                .orElse(UNKNOWN_ERROR);
    }
}
