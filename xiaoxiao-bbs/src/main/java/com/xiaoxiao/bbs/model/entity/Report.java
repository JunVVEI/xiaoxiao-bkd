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
 * 投诉举报
 * </p>
 *
 * @author yaoyao
 * @since 2023-05-23 02:11:22
 */
@Data
@TableName("bbs_report")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建该举报的用户的id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 举报类别
     */
    @TableField("report_type")
    private String reportType;

    /**
     * 举报目标内容的id
     */
    @TableField("target_content_id")
    private Long targetContentId;

    /**
     * 举报目标的用户id
     */
    @TableField("target_uid")
    private Long targetUid;

    /**
     * 举报时的内容快照
     */
    @TableField("target_content")
    private String targetContent;

    /**
     * 举报理由
     */
    @TableField("reason")
    private String reason;

    /**
     * 举报图片
     */
    @TableField("media_path")
    private String mediaPath;

    /**
     * 举报时间
     */
    @TableField("create_time")
    private Timestamp createTime;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

}
