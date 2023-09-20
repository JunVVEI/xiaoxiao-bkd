package com.xiaoxiao.toolbag.model.bo.course;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
    教务系统的课表结构,基本单元
 */
@Data
public class EducationSystemCourse {

    //课程id: "10263108"
    @Alias("id")
    String courseId;
    //课程名:奇妙的动物
    @Alias("kc_name")
    String courseName;

    //课程班级或类型: 21软工R7-R8  或通识选修（公选课）
    @Alias("ktmc_name")
    String classInfo;

    //任课老师(可能多个):"黄玉妹,公晗"
    @Alias("teachernames")
    String teacherNames;

    //教室 :3402
    @Alias("js_name")
    String location;

    //上课时间: "19:30"
    @Alias("djkssj")
    String startTime;

    //下课时间:"21:40"
    @Alias("djjssj")
    String endTime;

    //排课周次: "1-11"
    @Alias("pkzc")
    String weeks;

    //排课周次明细:"1,2,3,4,5,6,7,8,9,10,11"
    @Alias("pkzcmx")
    String weeksDetail;

    //排课时间:"31315"
    @Alias("pksj")
    String courseTime;

    //排课时间:"周三 13-15节"
    @Alias("pksjshow")
    String courseTimeShow;
}
