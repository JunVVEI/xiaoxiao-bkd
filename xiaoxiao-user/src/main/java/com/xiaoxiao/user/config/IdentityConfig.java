package com.xiaoxiao.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@RefreshScope
public class IdentityConfig {

    @Value("${identity.avatarLibrary:https://junweispicbed.oss-cn-guangzhou.aliyuncs.com/img/202031522292042080.jpg,https://junweispicbed.oss-cn-guangzhou.aliyuncs.com/img/160I612a21520-129440.jpg,https://junweispicbed.oss-cn-guangzhou.aliyuncs.com/img/160I612a21520-129440.jpg}")
    private List<String> identityAvatarLibrary;
}
