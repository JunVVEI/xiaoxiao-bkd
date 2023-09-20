package com.xiaoxiao.toolbag.controller;

import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.toolbag.model.dto.course.EducationSystemLoginDTO;
import com.xiaoxiao.toolbag.model.vo.ScoreVO;
import com.xiaoxiao.toolbag.service.TermScoreService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yaoyao
 * @since 2023-08-26 02:27:07
 */
@RestController
@RequestMapping("/tb/termScore")
public class TermScoreController {
    @Resource
    private TermScoreService termScoreService;

    @PostMapping("/pullTermScore")
    @XiaoXiaoResponseBody
    public ScoreVO pullTermScore(@RequestBody EducationSystemLoginDTO educationSystemLoginDTO){
        return termScoreService.pullTermScore(educationSystemLoginDTO);
    }
}

