package com.xiaoxiao.user.model.enums;

import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserConfEnum {

    RECEIVE_COURSE(1, "RECEIVE_COURSE", "0", false, "明日课程推送");

    /**
     * id
     */
    private final Integer id;

    /**
     * 设置key
     */
    private final String key;

    /**
     * 设置key
     */
    private final String defaultValue;

    /**
     * 是否是后台类设置
     */
    private final Boolean isAdminConf;

    /**
     * 前端显示的文本
     */
    private final String showContent;

    public static String getDefaultValue(String key) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getKey(), key))
                .map(UserConfEnum::getDefaultValue)
                .findFirst()
                .orElseThrow(() -> new ApiException(StatusCode.VALIDATE_FAILED, "设置不存在"));
    }

    public static List<String> getKeyList() {
        return Arrays.stream(values())
                .map(UserConfEnum::getKey)
                .collect(Collectors.toList());
    }

    public static String getShowContent(String key) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getKey(), key))
                .map(UserConfEnum::getShowContent)
                .findFirst()
                .orElseThrow(() -> new ApiException(StatusCode.VALIDATE_FAILED, "设置不存在"));
    }
}
