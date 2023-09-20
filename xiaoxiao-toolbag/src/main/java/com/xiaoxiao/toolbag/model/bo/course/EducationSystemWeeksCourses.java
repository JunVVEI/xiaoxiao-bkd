package com.xiaoxiao.toolbag.model.bo.course;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 教务系统的多周课表数据(为结构优化使用)
 */

@Data
@AllArgsConstructor
public class EducationSystemWeeksCourses {

    /**
     * 当前周数，如第一周，则daysCourses为第一周的课程数据
     */
    private String week;

    private Map<String, List<EducationSystemCourse>> daysCourses;
}