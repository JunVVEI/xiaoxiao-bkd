package com.xiaoxiao.bbs.model.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.bbs.model.entity.ActivitySubComment;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import java.sql.Timestamp;
import java.util.Objects;

@Data
public class ActSubCommentDTO {

    private Long id;

    /**
     * 父评论id
     */
    @NotNull(message = "父评论id不能为空")
    @Min(value = 1, message = "父评论id不能为负")
    private Long commentId;

    /**
     * 匿名身份id
     */
    @Min(value = 1, message = "匿名身份id不能为负")
    private Long identityId;


    /**
     * 评论内容
     */
    @NotEmpty(message = "评论内容不能为空")
    @Length(max = 100, message = "评论内容不能超过100个字")
    private String content;


    /**
     * 回复对象用户名
     */
    @Length(max = 20, message = "用户名不能超过20个字")
    private String replyName;


    /**
     * 媒体存储路径
     */
    private String mediaPath;

    public static void checkIsValid(ActSubCommentDTO actSubCommentDTO) {
        AssertUtil.isTrue(Objects.nonNull(actSubCommentDTO.getCommentId()) && actSubCommentDTO.getCommentId() > 0,
                "父评论id不能为空或负数");
        AssertUtil.isTrue(actSubCommentDTO.getIdentityId() > 0,
                "匿名身份id不能为负数");
        AssertUtil.isTrue(StrUtil.isNotBlank(actSubCommentDTO.getContent()) && actSubCommentDTO.getContent().length() <= 100,
                "非法评论内容");
        AssertUtil.isTrue(actSubCommentDTO.getReplyName().length() <= 20,
                "回复用户名不能超过20个字");

    }

    public static ActivitySubComment prepareActSubComment(ActSubCommentDTO actSubCommentDTO) {
        ActivitySubComment activitySubComment = new ActivitySubComment();
        BeanUtil.copyProperties(actSubCommentDTO, activitySubComment);
        activitySubComment.setLikeCount((long) 0);
        activitySubComment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        activitySubComment.setIsDelete(false);
        return activitySubComment;
    }
}
