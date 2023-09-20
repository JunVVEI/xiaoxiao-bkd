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
public class ActSubCommentQuery extends BasePageCondition {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 评论id
     */
    @NotNull(message = "评论id不能为空")
    @Min(value = 0, message = "评论id不能为负")
    private Long commentId;


    /**
     * 列表排序方式
     */
    @Range(min = 1, max = 2, message = "排序输入有误")
    private int order;

    public static void checkIsValid(ActSubCommentQuery actSubCommentQuery) {
        AssertUtil.isTrue(actSubCommentQuery.getCommentId() != null && actSubCommentQuery.getCommentId() > 0,
                "活动id不能为空或负数");
        AssertUtil.isTrue(actSubCommentQuery.getOrder() == 1 || actSubCommentQuery.getOrder() == 2,
                "排序规则只能为1或2");

    }

}