package com.xiaoxiao.bbs.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoxiao.bbs.model.entity.ActivityComment;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ActCommentVO {

    private Long id;

    /**
     * 活动id
     */
    private Long activityId;

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
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 媒体存储路径
     */
    private String mediaPath;

    /**
     * 是否被点赞
     */
    Boolean isLike;


    public ActCommentVO prepareActCommentVO(ActivityComment activityComment) {
        ActCommentVO actCommentVO = new ActCommentVO();
        BeanUtil.copyProperties(activityComment, actCommentVO);
        return actCommentVO;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }


}
