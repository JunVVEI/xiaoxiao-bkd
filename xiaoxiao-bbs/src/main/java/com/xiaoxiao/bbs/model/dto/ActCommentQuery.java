package com.xiaoxiao.bbs.model.dto;

import com.xiaoxiao.common.api.BasePageCondition;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActCommentQuery extends BasePageCondition {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 活动id
     */
    @NotNull(message = "活动id不能为空")
    @Min(value = 0, message = "活动id不能为负")
    private Long activityId;

    /**
     * 列表排序方式
     */
    @Range(min = 1, max = 2, message = "排序输入有误")
    private int order;

    public static void checkIsValid(ActCommentQuery actCommentQuery) {
        AssertUtil.isTrue(actCommentQuery.getActivityId() != null && actCommentQuery.getActivityId() > 0,
                "活动id不能为空或负数");
        AssertUtil.isTrue(actCommentQuery.getOrder() == 1 || actCommentQuery.getOrder() == 2,
                "排序规则只能为1或2");

    }

}