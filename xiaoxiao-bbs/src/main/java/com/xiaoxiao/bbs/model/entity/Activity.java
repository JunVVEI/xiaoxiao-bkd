package com.xiaoxiao.bbs.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author mayunfei
 * @since 2023-02-04 09:40:02
 */

@Data
@TableName("activity")
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建活动的用户的id
     */
    @TableField("user_id")
    private long userId;

    /**
     * 创建活动的用户的昵称
     */
    @TableField("creator_name")
    private String creatorName;

    /**
     * 打气计数
     */
    @TableField("cheer")
    private Integer cheer;

    /**
     * 活动标题
     */
    @TableField("activity_topic")
    private String activityTopic;

    /**
     * 活动形式
     */
    @TableField("activity_form")
    private Integer activityForm;

    /**
     * 活动举行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("activity_time")
    private Timestamp activityTime;

    /**
     * 活动举办地点
     */
    @TableField("activity_local")
    private String activityLocal;

    /**
     * 活动人数
     */
    @TableField("activity_NumberOfPeople")
    private Integer activityNumberOfPeople;

    /**
     * 活动须知
     */
    @TableField("activity_notice")
    private String activityNotice;

    /**
     * 活动内容
     */
    @TableField("activity_content")
    private String activityContent;

    /**
     * 媒体路径
     */
    @TableField("media_path")
    private String mediaPath;

    /**
     * 是否被删除
     */
    @TableField("is_delete")
    @TableLogic
    private boolean isDelete;

    /**
     * 匿名id
     */
    @TableField("identity_id")
    private Long identityId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;


}
