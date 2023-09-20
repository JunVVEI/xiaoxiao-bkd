package com.xiaoxiao.taskCourier.model.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RewardTaskCommentVO {
    private Long commentId;

    private Long uid;

    private Long identityId;

    private String userName;

    private String userAvatar;

    private Boolean isCreator;

    /**
     * 父评论id
     */
    private Long parentId;

    /**
     * 悬赏任务id
     */
    private Long rewardTaskId;

    /**
     * 内容
     */
    private String content;

    private Timestamp createTime;

    private RewardTaskCommentVO reply;

}
