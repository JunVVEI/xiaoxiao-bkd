package com.xiaoxiao.bbs.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author chenhaowen
 */
@Data
@TableName("activity_sub_comment")
public class ActivitySubComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父级评论id
     */
    @TableField("comment_id")
    private Long commentId;

    /**
     * 用户真实id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 匿名身份id
     */
    @TableField("identity_id")
    private Long identityId;

    /**
     * 创建者用户名
     */
    @TableField("creator_name")
    private String creatorName;

    /**
     * 评论创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;


    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 回复对象用户名
     */
    @TableField("reply_name")
    private String replyName;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Long likeCount;

    /**
     * 媒体存储路径
     */
    @TableField("media_path")
    private String mediaPath;

    /**
     * 是否被删除
     */
    @TableField("is_delete")
    @TableLogic
    Boolean isDelete;

}
