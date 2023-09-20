package com.xiaoxiao.toolbag.service;

import com.xiaoxiao.toolbag.model.dto.course.EducationSystemLoginDTO;
import com.xiaoxiao.toolbag.model.entity.TermScore;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.toolbag.model.vo.ScoreVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaoyao
 * @since 2023-08-26 02:27:07
 */
public interface TermScoreService extends IService<TermScore> {

    ScoreVO pullTermScore(EducationSystemLoginDTO educationSystemLoginDTO);

}
