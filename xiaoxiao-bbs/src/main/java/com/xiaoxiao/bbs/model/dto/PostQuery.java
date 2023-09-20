package com.xiaoxiao.bbs.model.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * <p>
 * PostQuery
 * </p>
 *
 * @author Junwei
 * @since 2022/10/28
 */
@Data
@ToString(callSuper = true)
@Slf4j
public class PostQuery {

    private Long postId;

    public static void checkIsValid(PostQuery postQuery) {
        Assert.isTrue(postQuery.getPostId() != null, "帖子id不能为空");
    }
}

