package com.xiaoxiao.toolbag.model.bo.course;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 教务系统的多课表数据(结构优化后)
 */
@Data
@AllArgsConstructor
public class EducationSystemTermsCoursesParse {
    /**
     * 学期数，如2022-2023-1，则weeksCourses为该学期的课程数据
     */
    String term;
    List<EducationSystemWeeksCourses> weeksCourses;
    /**
     * 每学期的课表备注
     */
    String[] notes;
}
