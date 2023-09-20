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
 * @author chenjunwei
 * @since 2023-07-16 06:57:53
 */
@Data
@TableName("bbs_association")
public class Association implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * json内容
     */
    @TableField("json_data")
    private String jsonData;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField("is_delete")
    private Integer isDelete;

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
     * 更新时间
     */
    @TableField(value = "uid")
    private Long uid;

}
