package com.xiaoxiao.baseservice.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.xiaoxiao.baseservice.mapper.FileMapper;
import com.xiaoxiao.baseservice.model.FileEntity;
import com.xiaoxiao.baseservice.model.dto.FileQuery;
import com.xiaoxiao.baseservice.model.dto.OssPolicy;
import com.xiaoxiao.baseservice.service.FileService;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author caijiachun
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Resource
    private FileMapper fileMapper;

    @Resource
    OSSClient ossClient;

    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;

    @Value("${alibaba.cloud.oss.bucket}")
    private String bucket;

    @Value("${alibaba.cloud.oss.maxSize}")
    private int maxSize;

    @Value("${alibaba.cloud.accessKeyId}")
    private String accessId;

    @Value("${alibaba.cloud.oss.policy.expireTime}")
    private long expireTime;

    @Override
    @CrossOrigin
    public OssPolicy policy() {
        OssPolicy ossPolicy = new OssPolicy();

        String host = "https://" + bucket + "." + endpoint;

        //格式化一个当前的服务器时间
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // 用户上传文件时指定的前缀,我们希望以日期作为一个目录
        String dir = format + "/";

        long mSize = (long) maxSize * 1024 * 1024;

        try {
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, mSize);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(postPolicy);

            ossPolicy.setAccessKeyId(accessId);
            ossPolicy.setPolicy(policy);
            ossPolicy.setSignature(signature);
            ossPolicy.setDir(dir);
            ossPolicy.setHost(host);
        } catch (Exception e) {
            throw new ApiException("签名失败");
        } finally {
            ossClient.shutdown();
        }
        return ossPolicy;
    }

    @Override
    public int uploadFile(FileEntity fileEntity) {
        try{
            return fileMapper.uploadFile(fileEntity);
        }catch (Exception e){
            throw new ApiException("上传文件信息失败");
        }
    }

    @Override
    public int isDeleteFile(String fileName) {
        return fileMapper.isDeleteFile(fileName);
    }

    @Override
    public int deleteFile(String fileName) {
        return fileMapper.deleteFile(fileName);
    }

    @Override
    public int updateFile(FileEntity fileEntity) {
        return fileMapper.updateFile(fileEntity);
    }

    @Override
    public List<FileEntity> selectFile(FileQuery fileQuery) {
        return fileMapper.selectFile(fileQuery);
    }
}
