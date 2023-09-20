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
 * @since 2022-10-27 11:23:00
 */
@Data
@TableName("bbs_post")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

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
     * 用户匿名id
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
    @TableField("modifier_name")
    private String modifierName;

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
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 浏览量
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 分享量
     */
    @TableField("share_count")
    private Integer shareCount;

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
     * 标签id
     */
    // 1：考研 2：求职 3：新生
    @TableField("tag_id")
    private Long tagId;

    public double getHotScore() {
        double w = (this.getLikeCount() * 20 +
                this.getViewCount() * 10 +
                this.getCommentCount() * 35 +
                this.getShareCount() * 35) + 100;
        long t = (System.currentTimeMillis() - this.getCreateTime().getTime()) / (120 * 60 * 1000);

        return w / Math.pow(t + 2, 1.8);
    }

}
