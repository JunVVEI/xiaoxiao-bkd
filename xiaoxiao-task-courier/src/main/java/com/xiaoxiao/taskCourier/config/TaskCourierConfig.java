package com.xiaoxiao.taskCourier.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@Data
@RefreshScope
public class TaskCourierConfig {
    @Value("${wechat.groups:test1}")
    private String groups;

}
