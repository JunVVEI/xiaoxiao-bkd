package com.xiaoxiao.toolbag.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yaoyao
 * @since 2023-08-26 02:27:07
 */
@Data
@TableName("term_score")
public class TermScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("userid")
    private Long userid;

    @TableField("score_json")
    private String scoreJson;

    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;


}
