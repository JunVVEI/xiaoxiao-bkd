package com.xiaoxiao.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 微信小程序信息配置
 *
 * @author zjw
 */
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

    /**
     * code2Session接口url
     */
    @Value("${wechat.jscode2sessionUrl}")
    private String jscode2sessionUrl;
}