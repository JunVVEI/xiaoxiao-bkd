package com.xiaoxiao.toolbag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.toolbag.model.dto.course.EducationSystemLoginDTO;
import com.xiaoxiao.toolbag.model.dto.course.TermQueryDTO;
import com.xiaoxiao.toolbag.model.entity.CourseInfo;
import com.xiaoxiao.toolbag.model.vo.CourseVO;
import com.xiaoxiao.toolbag.model.vo.CurrentWeekVO;

import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zjh
 * @since 2022-11-22 01:16:43
 */
public interface CourseInfoService extends IService<CourseInfo> {

    /**
     * 拉取并保存课表数据
     *
     * @param educationSystemLoginDTO 用户教务系统信息
     */
    boolean pullAndSaveCourse(EducationSystemLoginDTO educationSystemLoginDTO);

    /**
     * @param idDTO 用户信息
     * @return 课表数据
     */
    CourseVO getCourse(TermQueryDTO idDTO);

    /**
     * @return 当前周次
     */
    CurrentWeekVO getWeek();

    /**
     * @return 获取学期数组
     */
    Set<String> getTerm(TermQueryDTO idDTO);

    /**
     * 查询用户uid, 判读数据库中是否有该uid的数据
     */
    boolean checkIsExist(String uid);
}
