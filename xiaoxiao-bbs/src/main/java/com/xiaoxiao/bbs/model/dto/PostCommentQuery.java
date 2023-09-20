package com.xiaoxiao.bbs.model.dto;

import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

@Data
public class PostCommentQuery {

    /**
     * 当前页码
     */
    private Long currentPage = 1L;

    /**
     * 页大小
     */
    private Long pageSize = 10L;

    private Long postId;

    /**
     * 1综合（点赞，时间），2时间
     */
    private Integer order = 1;


    public static void checkIsValid(PostCommentQuery postCommentQuery) {
        AssertUtil.isTrue(postCommentQuery.getPostId() != null, StatusCode.VALIDATE_FAILED);
    }
}
