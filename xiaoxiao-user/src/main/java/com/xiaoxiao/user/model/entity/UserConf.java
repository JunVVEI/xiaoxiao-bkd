package com.xiaoxiao.user.model.entity;

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
 * @author chenjunwei
 * @since 2023-08-17 10:57:05
 */
@Data
@TableName("ums_user_conf")
public class UserConf implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 设置id
     */
    @TableField("conf_key")
    private String confKey;

    /**
     * 设置value
     */
    @TableField("conf_value")
    private String confValue;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Timestamp updateTime;


}
