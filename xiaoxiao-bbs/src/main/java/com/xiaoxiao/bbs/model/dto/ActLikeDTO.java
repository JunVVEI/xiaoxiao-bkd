package com.xiaoxiao.bbs.model.dto;

import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Objects;


@Data
public class ActLikeDTO {

    /**
     * 评论id
     */
    @NotNull(message = "评论id不能为空")
    @Min(value=0,message = "评论id不能为负")
    private Long commentId;

    public static void checkIsValid(ActLikeDTO actLikeDTO) {
        AssertUtil.isTrue(Objects.nonNull(actLikeDTO.getCommentId())&&actLikeDTO.getCommentId()>0,
                "评论id不能为空或负数");
    }
}
