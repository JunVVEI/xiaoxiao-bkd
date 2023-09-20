package com.xiaoxiao.bbs.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@Data
@RefreshScope
public class WeChatConfig {

    /**
     * 小程序 appId
     */
    @Value("${wechat.appid}")
    private String appid;

    /**
     * 小程序 appSecret
     */
    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.groups:}")
    private String groups;

}