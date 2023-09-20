package com.xiaoxiao.bbs.model.dto;

import com.xiaoxiao.common.api.BasePageCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * <p>
 * 帖子列表请求参数对象
 * </p>
 *
 * @author Zeng Xiangcheng
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PostListQuery extends BasePageCondition {

    /**
     * 当前页码
     */
    private Long currentPage;

    /**
     * 当前页大小
     */
    private Long pageSize;

    /**
     * 排序标志,1按点赞，2按时间
     */
    private Integer orderFlag;

    /**
     * 类型，查询我的帖子时使用，1所有，2仅真实，3匿名
     */
    private Integer type = 1;

    /**
     * 指定的用户id
     */
    private Long userId;

    /**
     * 指定的匿名id
     */
    private Long identityId;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 请求参数校验
     *
     * @param postListQuery 查询帖子列表请求参数
     */
    public static void checkIsValid(PostListQuery postListQuery) {
        Assert.isTrue(postListQuery.getOrderFlag() != null, "参数异常");
        Assert.isTrue(postListQuery.getOrderFlag() == 1 || postListQuery.getOrderFlag() == 2, "参数异常");
    }
}
