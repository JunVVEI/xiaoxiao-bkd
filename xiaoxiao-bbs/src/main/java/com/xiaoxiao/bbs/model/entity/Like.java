package com.xiaoxiao.bbs.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * bbs点赞数据库实体
 * </p>
 *
 * @author chenjunwei
 * @since 2023-02-19 10:42:46
 */
@Data
@TableName("bbs_like")
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 点赞的内容id
     */
    @TableField("content_id")
    private Long contentId;

    /**
     * 内容类型，枚举表示
     */
    @TableField("type")
    private Integer type;

    /**
     * 标识：1点赞、2点踩、3删除
     */
    @TableField("flag")
    private Integer flag;

    @TableField(value = "create_time")
    private Timestamp createTime;

    @TableField(value = "update_time")
    private Timestamp updateTime;


}
