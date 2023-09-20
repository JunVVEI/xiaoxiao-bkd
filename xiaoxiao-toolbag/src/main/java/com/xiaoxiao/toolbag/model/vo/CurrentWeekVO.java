package com.xiaoxiao.toolbag.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shkstart
 * @create 2023-02-13 15:59
 */
@Data
@AllArgsConstructor
public class CurrentWeekVO {
    /**
     * 学期开始日期
     */
    String termStartTime;
    /**
     * 当前周次
     */
    long currentWeek;

}
