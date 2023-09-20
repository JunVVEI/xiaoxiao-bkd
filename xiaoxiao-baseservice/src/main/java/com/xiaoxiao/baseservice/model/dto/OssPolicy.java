package com.xiaoxiao.baseservice.model.dto;

import lombok.Data;

@Data
public class OssPolicy {
    /**
     * 用户请求的accessid
     */
    private String accessKeyId;

    /**
     * 用户表单上传的策略（Policy），是经过Base64编码过的字符串。
     */
    private String policy;

    /**
     * 对Policy签名后的字符串
     */
    private String signature;

    /**
     * 上传文件夹路径前缀
     */
    private String dir;

    /**
     * 用户要往哪个域名发送上传请求
     */
    private String host;

    /**
     * 上传成功后的回调设置
     */
    private String callback;
}
