package com.xiaoxiao.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author chenjunwei
 */
@Component
@Data
@RefreshScope
public class GatewayConfig {

    /**
     * 秘钥明文
     */
    @Value("${jwt.key:123qwqer}")
    private String jwtKey;
}
