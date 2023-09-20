package com.xiaoxiao.bbs.model.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostSubCommentVO {

    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 父评论id，为空表示是一个根评论
     */
    private Long parentId;

    /**
     * 父评论的创建人名称
     */
    private String parentCreatorName;

    /**
     * 内容
     */
    private String content;

    /**
     * 如果为实名，用户id
     */
    private Long uid;

    /**
     * 如果为匿名，身份id
     */
    private Long identityId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 用户是否点赞
     */
    private Boolean isLiked;

    /**
     * 当前用户是否为创建人
     */
    private Boolean isCreator;

    /**
     * 图片列表
     */
    private String mediaPath;

}
