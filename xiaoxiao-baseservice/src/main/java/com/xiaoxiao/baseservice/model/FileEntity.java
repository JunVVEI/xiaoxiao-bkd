package com.xiaoxiao.baseservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;


/**
 * @author caijiachun
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {
    /**
     * id
     */
    private int id;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件类型
     */
    private String url;
    /**
     * 文件大小
     */
    private double fileSize;
    /**
     * 文件状态
     */
    private String state;
    /**
     * 文件哈希码
     */
    private String fileHash;
    /**
     * 文件创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 更新者
     */
    private String updater;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 是否为删除状态，1为已删除，0为未删除
     */
    private int isDelete;
}
