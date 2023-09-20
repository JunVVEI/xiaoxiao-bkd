package com.xiaoxiao.bbs.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p>
 * 点赞的内容来源枚举类
 * </p>
 *
 * @author Junwei
 * @since 2023/2/19
 */
@AllArgsConstructor
@Getter
public enum LikeTypeEnum {

    POST(100, "帖子"),

    POST_COMMENT(101, "帖子评论"),

    POST_SUB_COMMENT(102, "帖子子评论"),

    ACTIVITY(200, "活动"),

    ACTIVITY_COMMENT(201, "活动评论"),

    ACTIVITY_SUB_COMMENT(202, "活动子评论");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 来源
     */
    private final String source;

    public static LikeTypeEnum getLikeTypeEnumByType(Integer type) {
        return Stream.of(LikeTypeEnum.values())
                .filter(likeTypeEnum -> Objects.equals(likeTypeEnum.getType(), type))
                .findFirst()
                .orElse(null);
    }
}
