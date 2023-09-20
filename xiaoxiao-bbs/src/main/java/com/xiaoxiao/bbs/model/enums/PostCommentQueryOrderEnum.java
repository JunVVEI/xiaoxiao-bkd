package com.xiaoxiao.bbs.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PostCommentQueryOrderEnum {

    ORDER_BY_LIKE(1),

    ORDER_BY_TIME(2),

    ;

    private final int order;
}
