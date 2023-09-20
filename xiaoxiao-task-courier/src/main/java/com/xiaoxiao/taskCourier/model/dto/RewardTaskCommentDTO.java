package com.xiaoxiao.taskCourier.model.dto;

import lombok.Data;

@Data
public class RewardTaskCommentDTO {

    /**
     * 父评论id
     */
    private Long parentId;

    /**
     * 悬赏任务id
     */
    private Long rewardTaskId;

    /**
     * 匿名id
     */
    private Long identityId;

    /**
     * 内容
     */
    private String content;

    public static void checkIdValid(RewardTaskCommentDTO rewardTaskCommentDTO) {

    }

}
