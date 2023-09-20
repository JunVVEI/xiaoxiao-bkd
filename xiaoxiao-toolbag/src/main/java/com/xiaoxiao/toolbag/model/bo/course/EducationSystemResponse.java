package com.xiaoxiao.toolbag.model.bo.course;

import lombok.Data;

/**
 * 教务系统统一返回格式
 * @author zjh
 */
@Data
public class EducationSystemResponse<T> {
    T data;
    String errorMessage;
}
