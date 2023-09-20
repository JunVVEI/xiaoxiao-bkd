package com.xiaoxiao.bbs.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TagEnum {

    A(1L, "考研"),

    B(2L, "求职"),

    C(3L, "闲聊"),

    ;

    /**
     * tagId
     */
    private final Long tagId;

    /**
     * 标签名称
     */
    private final String name;

}
