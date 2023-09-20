package com.xiaoxiao.bbs.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yaoyao
 * @Description
 * @create 2023-09-02 0:39
 */
@AllArgsConstructor
@Getter
public enum EnergyEnum {
    LIKE(1, "点赞");
    /**
     * 类型
     */
    private final Integer type;

    /**
     * 来源
     */
    private final String source;
}
