package com.xiaoxiao.bbs.model.dto;

import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.bbs.model.entity.Comment;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Data
public class PostCommentDTO {

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 根评论id，为空表示是一个根评论
     */
    private Long rootCommentId;

    /**
     * 父评论id，为空表示是一个根评论
     */
    private Long parentId;

    private Long identityId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 图片路径
     */
    private String mediaPath;


    public static void checkIsValid(PostCommentDTO postCommentDTO) {
        AssertUtil.isTrue(Objects.nonNull(postCommentDTO.getPostId()) && postCommentDTO.getPostId() > 0, "帖子id为空或者为负数");
        AssertUtil.isTrue(StrUtil.isNotBlank(postCommentDTO.getContent()), "评论内容为空");
    }

}
