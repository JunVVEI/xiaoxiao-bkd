package com.xiaoxiao.taskCourier.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 *
 * </p>
 *
 * @author junwei
 * @since 2023-08-08 11:05:50
 */
@Data
@TableName("reward_task_comment")
public class RewardTaskComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父评论id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 悬赏任务id
     */
    @TableField("reward_task_id")
    private Long rewardTaskId;

    /**
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 匿名id
     */
    @TableField("identity_id")
    private Long identityId;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 是否删除
     */
    @TableField("is_delete")
    private Integer isDelete;

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


}
