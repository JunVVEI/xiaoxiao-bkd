package com.xiaoxiao.taskCourier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("reward_task")
public class RewardTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 媒体资源路径逗号分隔
     */
    @TableField("media_path")
    private String mediaPath;

    /**
     * 赏金
     */
    @TableField("bounty")
    private Double bounty;

    /**
     * 联系方式
     */
    @TableField("contact")
    private String contact;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

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
     * 状态
     */
    @TableField("type")
    private Integer type;
}
