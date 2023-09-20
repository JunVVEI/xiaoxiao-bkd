package com.xiaoxiao.bbs.model.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yaoyao
 * @Description
 * @create 2023-05-23 14:33
 */
@Data
@Slf4j
public class ReportDTO {

    /**
     * 举报内容类别
     */
    private String reportType;

    /**
     * 举报目标内容的id
     */
    private Long targetId;

    /**
     * 举报时的内容快照
     */
    private String targetContent;

    /**
     * 举报理由
     */
    private String reason;

    /**
     * 举报图片
     */
    private String mediaPath;

}
