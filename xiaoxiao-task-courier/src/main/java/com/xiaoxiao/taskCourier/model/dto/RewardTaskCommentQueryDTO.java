package com.xiaoxiao.taskCourier.model.dto;

import lombok.Data;

@Data
public class RewardTaskCommentQueryDTO {
    /**
     * 当前页码
     */
    private Long currentPage;

    /**
     * 当前页大小
     */
    private Long pageSize;

    private Long taskId;
}
