package com.xiaoxiao.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户关注表
 */
@Data
@TableName("ums_follow")
public class Follow implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("self_id")
    private Long selfId;

    @TableField("follow_id")
    private Long followId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Timestamp createTime;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 0:关注、1：取消关注
     */
    @TableField(value = "status", fill = FieldFill.INSERT)
    @TableLogic
    private Integer status;

    /**
     * 用于标识follow表中的followId 属于用户id还是身份id
     */
    @TableField("type")
    private Integer type;

}
