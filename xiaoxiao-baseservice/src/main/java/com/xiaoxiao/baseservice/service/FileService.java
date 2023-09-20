package com.xiaoxiao.baseservice.service;

import com.xiaoxiao.baseservice.model.FileEntity;
import com.xiaoxiao.baseservice.model.dto.FileQuery;
import com.xiaoxiao.baseservice.model.dto.OssPolicy;


import java.util.List;


/**
 * @author caijiachun
 */
public interface FileService {

    /**
     * 获取上传文件所需参数
     *
     * @return
     */
    OssPolicy policy();

    /**
     * 上传文件信息
     *
     * @param fileEntity
     * @return 上传成功返回1
     */
    int uploadFile(FileEntity fileEntity);

    /**
     * 软删除文件
     *
     * @param fileName
     * @return 删除成功返回1
     */
    int isDeleteFile(String fileName);

    /**
     * 彻底删除文件信息
     *
     * @param fileName
     * @return 删除成功返回1
     */
    int deleteFile(String fileName);

    /**
     * 更新文件信息
     *
     * @param fileEntity
     * @return 更新成功返回1
     */
    int updateFile(FileEntity fileEntity);

    /**
     * 查询文件信息
     *
     * @param fileQuery
     * @return
     */
    List<FileEntity> selectFile(FileQuery fileQuery);
}
