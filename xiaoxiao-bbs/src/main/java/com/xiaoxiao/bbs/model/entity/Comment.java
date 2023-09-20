package com.xiaoxiao.bbs.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 *
 * </p>
 *
 * @author Junwei
 * @since 2022-10-29 04:18:48
 */
@Data
@TableName("bbs_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 帖子id
     */
    @TableField("post_id")
    private Long postId;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建者id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 身份id
     */
    @TableField("identity_id")
    private Long identityId;

    /**
     * 创建者名
     */
    @TableField("creator_name")
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Timestamp updateTime;

    /**
     * 修改者
     */
    @TableField("updater")
    private String updater;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 评论数
     */
    @TableField("sub_comment_count")
    private Integer subCommentCount;

    /**
     * 浏览量
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 踩数
     */
    @TableField("unlike_count")
    private Integer unlikeCount;

    /**
     * 是否被删除
     */
    @TableField("is_delete")
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;

    /**
     * 媒体存储路径
     */
    @TableField("media_path")
    private String mediaPath;

    /**
     * 父评论id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 根评论id
     */
    @TableField("root_comment_id")
    private Long rootCommentId;

}
