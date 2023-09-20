package com.xiaoxiao.baseservice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author chenhaowen
 */
@TableName("base_mail_log")
@Data
public class MailLog {

    /**
     * 发送的邮件的id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 目标邮箱地址
     */
    @TableField("target_address")
    private String targetAddress;

    /**
     * 发送内容
     */
    @TableField("send_content")
    private String sendContent;

    /**
     * 发送原因
     */
    @TableField("send_reason")
    private String sendReason;

    /**
     * 发送时间
     */
    @TableField("send_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp sendTime;

    /**
     * 发送结果（0失败、1成功）
     */
    @TableField("send_result")
    private Integer sendResult;

    public static void check(MailLog mailLog) {
        // TODO: 参数校验
    }

}
