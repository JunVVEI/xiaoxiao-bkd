package com.xiaoxiao.toolbag.model.bo.score;

import lombok.Data;

/**
 * @author yaoyao
 * @Description
 * @create 2023-08-26 14:33
 */
@Data
public class ScoreDetail {
    /**
     * 学期
     */
    String term;
    /**
     * 课程名
     */
    String courseName;

    /**
     * 总成绩
     */
    Double finishScore;

    /**
     * 考试成绩
     */
    Double examScore;

    /**
     * 平时成绩
     */
    Double usualScore;

    /**
     * 学分
     */
    Double courseScore;

    /**
     * 绩点
     */
    Double GPA;
}
