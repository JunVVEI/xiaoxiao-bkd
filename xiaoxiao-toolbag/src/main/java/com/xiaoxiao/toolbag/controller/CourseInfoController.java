package com.xiaoxiao.toolbag.controller;

import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.toolbag.model.dto.course.EducationSystemLoginDTO;
import com.xiaoxiao.toolbag.model.dto.course.TermQueryDTO;
import com.xiaoxiao.toolbag.model.vo.CourseVO;
import com.xiaoxiao.toolbag.model.vo.CurrentWeekVO;
import com.xiaoxiao.toolbag.service.CourseInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zjh
 * @since 2022-11-22 01:16:43
 */
@RestController
@RequestMapping("tb/course")
public class CourseInfoController {

    @Resource
    private CourseInfoService courseInfoService;

    @PostMapping("/pullCourse")
    @XiaoXiaoResponseBody
    public Boolean pullCourse(@RequestBody EducationSystemLoginDTO user) {
        return courseInfoService.pullAndSaveCourse(user);
    }

    @PostMapping("/getCourse")
    @XiaoXiaoResponseBody
    public CourseVO getCourse(@RequestBody TermQueryDTO idDTO) {
        return courseInfoService.getCourse(idDTO);
    }

    @GetMapping("/getWeek")
    @XiaoXiaoResponseBody
    public CurrentWeekVO getWeek() {
        return courseInfoService.getWeek();
    }

    @PostMapping("/getTerm")
    @XiaoXiaoResponseBody
    public Set<String> getTerm(@RequestBody TermQueryDTO idDTO) {
        return courseInfoService.getTerm(idDTO);
    }
}