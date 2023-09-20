package com.xiaoxiao.toolbag.model.vo;

import com.xiaoxiao.toolbag.model.bo.course.EducationSystemWeeksCourses;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author shkstart
 * @create 2023-02-16 23:34
 */
@Data
@AllArgsConstructor
public class CourseVO{
    /**
     * 每学期的课程
     */
    List<EducationSystemWeeksCourses> termCourses;
    /**
     * 课表备注
     */
    String [] notes;
}