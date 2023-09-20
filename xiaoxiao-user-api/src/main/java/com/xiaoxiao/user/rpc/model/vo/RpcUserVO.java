package com.xiaoxiao.user.rpc.model.vo;

import lombok.Data;

/**
 * <p>
 * RpcFollowUserVO
 * </p>
 *
 * @author Junwei
 * @since 2023/2/24
 */
@Data
public class RpcUserVO {

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

}
