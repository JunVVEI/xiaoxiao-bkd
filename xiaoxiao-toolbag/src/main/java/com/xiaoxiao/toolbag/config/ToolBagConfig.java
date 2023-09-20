package com.xiaoxiao.toolbag.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author shkstart
 * @create 2023-01-10 20:17
 * <p>
 * 从Nacos获取数据
 */
@Configuration
@Data
@RefreshScope
public class ToolBagConfig {

    /**
     * 课程开始时间
     */
    @Value("${course-config.term-start-time:2023-02-20}")
    private String termStartTime;

    /**
     * 学期+1，寒暑假的时候教务系统学期数更新没那么块，可以手动加1
     */
    @Value("${course-config.currentTerm:2023-2024-1}")
    private String currentTerm;

    /**
     * 空教室同步任务开关
     */
    @Value("${switch.freeRoomTaskSwitch:1}")
    private Integer freeRoomTaskSwitch;

    /**
     * 明日课表推送通知任务开关
     */
    @Value("${switch.courseNoticeTaskSwitch:1}")
    private Integer courseNoticeTaskSwitch;

}
