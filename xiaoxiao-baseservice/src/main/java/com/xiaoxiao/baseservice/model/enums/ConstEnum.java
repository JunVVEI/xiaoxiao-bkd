package com.xiaoxiao.baseservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * ConstEnum
 * </p>
 *
 * @author Junwei
 * @since 2022/12/3
 */
@Getter
@AllArgsConstructor
public enum ConstEnum {

    /**
     * 失败
     */
    FAILED(0, "失败"),

    /**
     * 成功
     */
    SUCCESS(1, "成功");


    private final Integer val;
    private final String desc;
}
