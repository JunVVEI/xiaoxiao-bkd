package com.xiaoxiao.baseservice.model.dto;

import lombok.Data;

/**
 * @author caijiachun
 */
@Data
public class FileQuery {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 文件地址
     */
    private String url;
    /**
     * 是否已删除状态：1为已删除，0为未删除
     */
    private int isDelete;
    /**
     * 查询日期开始时间
     */
    private String startTime;
    /**
     * 查询日期结束时间
     */
    private String endTime;
}
