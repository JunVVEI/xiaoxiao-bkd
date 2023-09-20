package com.xiaoxiao.taskCourier.model.dto;

import lombok.Data;

@Data
public class RewardTaskQueryDTO {

    /**
     * 当前页码
     */
    private Long currentPage;

    /**
     * 当前页大小
     */
    private Long pageSize;

    /**
     * 内容
     */
    private String content;

    /**
     * 赏金
     */
    private Double bounty;

    /**
     * 联系方式
     */
    private String contact;

    private Integer type;

    public static void checkIsValid(RewardTaskQueryDTO rewardTaskDTO) {
    }

}
