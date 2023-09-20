package com.xiaoxiao.bbs.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("bbs_comment_like")
public class CommentLike implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论id
     */
    @TableField("entity_id")
    private Long entityId;


    /**
     * 创建者id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 身份id
     */
    @TableField("identity_id")
    private Long identityId;

    /**
     * type
     */
    @TableField("entity_type")
    private int entityType;

    /**
     * status
     */
    @TableField("status")
    private int status;
}
