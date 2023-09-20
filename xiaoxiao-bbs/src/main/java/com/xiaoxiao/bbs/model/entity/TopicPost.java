package com.xiaoxiao.bbs.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Junwei
 * @since 2022-10-29 04:18:49
 */
@Data
@TableName("bbs_topic_post")
public class TopicPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("topic_id")
    private Long topicId;

    @TableField("post_id")
    private Long postId;


}
