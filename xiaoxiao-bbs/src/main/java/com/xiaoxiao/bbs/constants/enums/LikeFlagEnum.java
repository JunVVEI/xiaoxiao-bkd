package com.xiaoxiao.bbs.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p>
 * 点赞状态枚类
 * </p>
 *
 * @author Junwei
 * @since 2023/2/19
 */
@AllArgsConstructor
@Getter
public enum LikeFlagEnum {

    LIKE(1, "点赞、支持"),

    DISLIKE(2, "踩、反对"),

    DELETED(3, "删除、无效");

    /**
     * 对应的值
     */
    private final Integer flag;

    /**
     * 说明
     */
    private final String desc;

    public static LikeFlagEnum getLikeFlagEnumByFlag(Integer flag) {
        return Stream.of(LikeFlagEnum.values())
                .filter(likeFlagEnum -> Objects.equals(likeFlagEnum.getFlag(), flag))
                .findFirst()
                .orElse(null);
    }
}
