package com.xiaoxiao.baseservice.mapper;

import com.xiaoxiao.baseservice.model.FileEntity;
import com.xiaoxiao.baseservice.model.dto.FileQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author caijiachun
 */
@Mapper
public interface FileMapper {

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
     * 从数据库中删除文件
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
     * 查找文件
     *
     * @param fileQuery
     * @return
     */
    List<FileEntity> selectFile(FileQuery fileQuery);
}
