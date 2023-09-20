package com.xiaoxiao.user.model.vo;

import lombok.Data;

/**
 * <p>
 * FollowUserVO
 * </p>
 *
 * @author Junwei
 * @since 2023/2/14
 */
@Data
public class FollowUserVO {

    /**
     * 关注表数据id
     */
    private Long id;

    /**
     * 标注id是属于用户id还是社区身份id 0：用户 1：社区
     */
    private Integer type;

    private Long userId;

    /**
     * 昵称
     */
    private String userName;

    /**
     * 头像
     */
    private String userAvatar;

    private Long identityId;

    /**
     * 昵称
     */
    private String identityName;

    /**
     * 头像
     */
    private String identityAvatar;

    /**
     * 关系类型
     */
    private Integer relType;

    /**
     * 关系名称
     */
    private String relName;

    /**
     * 当前用户是否已关注该用户
     */
    private Boolean isFollowing;

    /**
     * 是否是当前请求的用户本人
     */
    private Boolean isCurrentUserSelf;
}
