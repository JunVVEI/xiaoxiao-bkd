package com.xiaoxiao.bbs.model.dto;

import cn.hutool.core.bean.BeanUtil;
import com.xiaoxiao.bbs.model.entity.CommentLike;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

import java.util.Objects;

@Data
public class CommentLikeDTO {

    private Long userId;

    private Long identityId;


    private Long entityId;


    private Long entityType;

    public static void checkIsValid(CommentLikeDTO CommentLikeDTO) {
        AssertUtil.isTrue(Objects.nonNull(CommentLikeDTO.getUserId()) && CommentLikeDTO.getUserId() > 0, "用户id为空或者为负数");
        AssertUtil.isTrue(Objects.nonNull(CommentLikeDTO.getEntityId()) && CommentLikeDTO.getUserId() > 0, "实体id为空或者为负数");
        AssertUtil.isTrue(CommentLikeDTO.getEntityType() == 1 || CommentLikeDTO.getEntityType() == 2, "实体类型参数不能为空且只能为1或者2");
    }

    public static CommentLike prepareCommentLike(CommentLikeDTO CommentLikeDTO) {
        CommentLike commentLike = new CommentLike();
        BeanUtil.copyProperties(CommentLikeDTO, commentLike);
        return commentLike;
    }
}
