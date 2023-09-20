package com.xiaoxiao.bbs.model.dto;

import lombok.Data;

@Data
public class PostSubCommentQuery {

    /**
     * 当前页码
     */
    private Long currentPage = 1L;

    /**
     * 页大小
     */
    private Long pageSize = 10L;

    /**
     * 根评论id
     */
    private Long rootCommentId;
}
