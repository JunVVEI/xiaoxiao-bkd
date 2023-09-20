package com.xiaoxiao.baseservice.config;

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
    @Value("${wechat.mini.appid}")
    private String miniAppid;

    /**
     * 小程序 appSecret
     */
    @Value("${wechat.mini.secret}")
    private String miniSecret;

    /**
     * 服务号 appId
     */
    @Value("${wechat.service-account.appid}")
    private String serviceAccountAppid;

    /**
     * 服务号 appSecret
     */
    @Value("${wechat.service-account.secret}")
    private String serviceAccountSecret;

    @Value("${wechat.service-account.subscribeMediaId:f2RCwpdz7Hosgku00vspv6M-M6H-mL2Or4CHG18V6YjuhxcnkxbVI-KfqjefR8V6}")
    private String subscribeMediaId;

}
