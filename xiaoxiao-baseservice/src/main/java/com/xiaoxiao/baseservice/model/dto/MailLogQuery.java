package com.xiaoxiao.baseservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoxiao.common.api.BasePageCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @author chenhaowen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MailLogQuery extends BasePageCondition {

    private Long id;

    /**
     * 目标邮箱地址
     */
    private String targetAddress;

    /**
     * 发送内容
     */
    private String sendContent;

    /**
     * 发送原因
     */
    private String sendReason;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp sendTime;

    /**
     * 发送结果（0失败、1成功）
     */
    private Integer sendResult;

    /**
     * 时间区间查询的起点
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startTime;

    /**
     * 时间区间查询的终点
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp endTime;

}
