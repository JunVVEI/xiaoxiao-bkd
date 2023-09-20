package com.xiaoxiao.user.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yaoyao
 * @Description
 * @create 2023-09-01 0:06
 */
@AllArgsConstructor
@Getter
public enum EnergyCount {
    LIKE(1, 1.0, "点赞、支持");

    private final Integer type;
    private final Double count;
    private final String desc;
}