package com.xiaoxiao.bbs.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Junwei
 * @since 2022-10-29 04:11:42
 */
@Data
@TableName("bbs_topic")
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建者id
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 创建者名
     */
    @TableField("creator_name")
    private String creatorName;

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
     * 修改者
     */
    @TableField("modify_by")
    private String modifyBy;

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
     * 踩数
     */
    @TableField("unlike_count")
    private Integer unlikeCount;

    /**
     * 是否被删除
     */
    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;


}
