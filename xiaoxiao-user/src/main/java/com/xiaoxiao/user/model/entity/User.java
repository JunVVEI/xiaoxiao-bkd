package com.xiaoxiao.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 *
 * </p>
 *
 * @author zjw
 * @since 2022-11-19 11:11:38
 */
@Data
@TableName("ums_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户在开放平台的唯一标识符
     */
    @TableField("unionid")
    private String unionid;

    /**
     * 校校小程序openid
     */
    @TableField("openid")
    private String openid;


    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Timestamp createTime;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;

    /**
     * 数据更新版本
     */
    @TableField("version")
    private String version;

    /**
     * 邮箱地址
     */
    @TableField("email")
    private String email;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 学号
     */
    @TableField("school_no")
    private Integer schoolNo;

    /**
     * 服务号openId
     */
    @TableField("service_account_openid")
    private String serviceAccountOpenId;

}
