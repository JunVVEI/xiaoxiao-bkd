package com.xiaoxiao.toolbag.model.vo;

import com.xiaoxiao.toolbag.model.bo.score.ScoreDetail;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author yaoyao
 * @Description
 * @create 2023-08-26 14:38
 */
@Data
public class ScoreVO {
    Map<String, List<ScoreDetail>> scoreMap;
}
