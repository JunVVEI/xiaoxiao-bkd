package com.xiaoxiao.user.model.vo;

import com.xiaoxiao.user.model.entity.Identity;
import lombok.Data;

/**
 * @author 林嫄袁
 * @since 2023/2/22 22:50
 */
@Data
public class IdentityVO {
    /**
     * 社区身份id
     */
    private Long identityId;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 发帖数
     */
    private Integer postCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 被赞数
     */
    private Integer likeCount;

    /**
     * 被关注人数
     */
    private Integer followerCount;

    public static IdentityVO prepareIdentityVo(Identity identity) {
        IdentityVO identityVO = new IdentityVO();
        identityVO.setIdentityId(identity.getId());
        identityVO.setUid(identity.getUid());
        identityVO.setName(identity.getName());
        identityVO.setAvatar(identity.getAvatar());
        return identityVO;
    }
}
