package com.xiaoxiao.bbs.model.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.bbs.model.entity.ActivityComment;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.sql.Timestamp;


@Data
public class ActCommentDTO {

    private Long id;

    /**
     * 活动id
     */
    @NotNull(message = "活动id不能为空")
    @Min(value = 0, message = "活动id不能为负")
    private Long activityId;

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
     * 媒体存储路径
     */
    private String mediaPath;

    public static void checkIsValid(ActCommentDTO actCommentDTO) {
        AssertUtil.isTrue(actCommentDTO.getActivityId() != null && actCommentDTO.getActivityId() > 0,
                "活动id不能为空或负数");
        AssertUtil.isTrue(actCommentDTO.getIdentityId() > 0,
                "匿名身份id不能为负数");
        AssertUtil.isTrue(StrUtil.isNotBlank(actCommentDTO.getContent()) && actCommentDTO.getContent().length() <= 100,
                "非法评论内容");

    }

    public static ActivityComment prepareActComment(ActCommentDTO actCommentDTO) {
        ActivityComment activityComment = new ActivityComment();
        BeanUtil.copyProperties(actCommentDTO, activityComment);
        activityComment.setCommentCount((long) 0);
        activityComment.setLikeCount((long) 0);
        activityComment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        activityComment.setIsDelete(false);
        return activityComment;
    }

}
