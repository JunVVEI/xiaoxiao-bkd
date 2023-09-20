package com.xiaoxiao.user.model.vo;

import lombok.Data;

/**
 * <p>
 * UserPublicInfoVO
 * </p>
 *
 * @author Junwei
 * @since 2023/2/14
 */
@Data
public class UserPublicInfoVO {

    /**
     * 名称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户被关注的 数量
     */
    private Long followerCount;

    /**
     * 用户关注的人 数量
     */
    private Long followingCount;

    /**
     * 关系类型
     */
    private Integer relType;

    /**
     * 关系名称
     */
    private String relName;

    /**
     * 关系名称 -前端显示，
     */
    private Boolean isFollowing;
}
