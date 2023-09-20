package com.xiaoxiao.baseservice.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author caijiachun
 */
@Configuration
public class OssConfig {
    @Value("${alibaba.cloud.accessKeyId}")
    private String ALIYUN_OSS_ACCESSKEYID;

    @Value("${alibaba.cloud.accessKeySecret}")
    private String ALIYUN_OSS_ACCESSKEYSECRET;

    @Value("${alibaba.cloud.oss.endpoint}")
    private String ALIYUN_OSS_ENDPOINT;

    @Bean
    public OSSClient ossClient() {
        return new OSSClient(ALIYUN_OSS_ENDPOINT, ALIYUN_OSS_ACCESSKEYID, ALIYUN_OSS_ACCESSKEYSECRET);
    }
}
