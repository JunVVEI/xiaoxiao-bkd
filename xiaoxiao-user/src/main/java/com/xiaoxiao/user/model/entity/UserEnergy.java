package com.xiaoxiao.user.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yaoyao
 * @since 2023-09-03 01:58:36
 */
@Data
@TableName("user_energy")
public class UserEnergy implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("userid")
    private Long userid;

    @TableField("type")
    private Integer type;

    @TableField("energy_count")
    private Double energyCount;

    @TableField("energy_sum")
    private Double energySum;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Timestamp createTime;

    @TableField("description")
    private String description;

    @TableField("relate_id")
    private Long relateId;


}
