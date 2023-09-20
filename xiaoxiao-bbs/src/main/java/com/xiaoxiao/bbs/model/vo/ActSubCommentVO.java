package com.xiaoxiao.bbs.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoxiao.bbs.model.entity.ActivitySubComment;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ActSubCommentVO {

    private Long id;

    /**
     * 父评论id
     */
    private Long commentId;

    /**
     * 用户真实id
     */
    private Long userId;

    /**
     * 匿名身份id
     */
    private Long identityId;

    /**
     * 创建者用户名
     */
    private String creatorName;

    /**
     * 评论创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 回复对象用户名
     */
    private String replyName;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 媒体存储路径
     */
    private String mediaPath;

    /**
     * 是否被点赞
     */
    Boolean isLike;


    public ActSubCommentVO prepareActSubCommentVO(ActivitySubComment activitySubComment) {
        ActSubCommentVO actSubCommentVO = new ActSubCommentVO();
        BeanUtil.copyProperties(activitySubComment, actSubCommentVO);
        return actSubCommentVO;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }


}
