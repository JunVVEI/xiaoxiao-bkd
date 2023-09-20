package com.xiaoxiao.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 用户身份表
 * </p>
 *
 * @author junwei
 * @since 2022-11-20 03:36:59
 */
@Data
@TableName("ums_identity")
public class Identity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 名称
     */
    @TableField("name")
    private String name;


    /**
     * 0实名、1匿名
     */
    @TableField("type")
    private Integer type;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;

    /**
     * 0未删除、1已删除
     */
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;

    /**
     * 评论数
     */
    @TableField("comment_count")
    private Long commentCount;

    /**
     * 被关注数
     */
    @TableField("followed_count")
    private Long followedCount;

    /**
     * 发帖数
     */
    @TableField("post_count")
    private Long postCount;

    /**
     * 被赞数
     */
    @TableField("like_count")
    private Long likeCount;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 用于记录修改昵称的时间
     */
    @TableField(value = "change_time")
    private Timestamp changeTime;
}
