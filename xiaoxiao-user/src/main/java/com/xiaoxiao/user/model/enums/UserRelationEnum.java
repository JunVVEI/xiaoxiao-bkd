package com.xiaoxiao.user.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 林嫄袁
 * @since 2023/2/16 16:23
 */
@Getter
@AllArgsConstructor
public enum UserRelationEnum {

    /**
     * a b 均处于没有关注对方的状态
     */
    NONE(0, "关注", false),

    /**
     * b 关注 a
     */
    FOLLOWED(1, "被关注", false),

    /**
     * a 关注 b
     */
    FOLLOWING(2, "已关注", true),

    /**
     * a b 互相关注
     */
    MUTUAL_FOLLOWING(3, "已互关", true);


    private final Integer type;

    private final String name;

    private final Boolean isFollowing;

    public static String getUserRelNameByType(int type) {
        for (UserRelationEnum userRelName : UserRelationEnum.values()) {
            if (userRelName.getType() == type) {
                return userRelName.getName();
            }
        }
        return null;
    }
}
