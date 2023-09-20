package com.xiaoxiao.bbs.model.dto;

import com.xiaoxiao.common.api.BasePageCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author mayunfei
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityQuery extends BasePageCondition {

    /**
     * 排序方式，0为时间，1为打气降序，默认 以时间排列
     */
    private Integer order;

}
