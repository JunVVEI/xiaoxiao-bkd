package com.xiaoxiao.toolbag.model.dto.freeroom;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;

/**
 * @author yaoyao
 * @Description
 * @create 2023-08-03 21:58
 */
@Data
public class FreeRoomQuery {
    /**
     * 周次
     */
    @NonNull
    @Size(min = 1, max = 20, message = "请输入正确的周次")
    Integer week;

    /**
     *星期
     */
    @NonNull
    @Size(min = 1, max = 7, message = "请输入正确的星期")
    Integer day;

    /**
     * 教学楼
     */
    @NonNull
    String buildingId;

    /**
     * type
     */
    Integer type;
}
