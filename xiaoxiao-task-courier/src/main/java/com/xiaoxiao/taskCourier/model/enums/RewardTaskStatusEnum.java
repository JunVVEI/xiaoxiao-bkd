package com.xiaoxiao.taskCourier.model.enums;

import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum RewardTaskStatusEnum {
    WAITING_FOR_ACCEPTANCE(1, "未接单", "未出售"),
    ORDER_ACCEPTED(2, "已接单", "已出售"),
    ORDER_COMPLETED(3, "已完成", "已出售"),
    DELETED(4, "已删除", "已删除"),
    ;
    private final int status;
    private final String statusName;
    private final String secondHandStatusName;

    public static RewardTaskStatusEnum getRewardTaskStatusEnum(int status) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getStatus(), status))
                .findFirst()
                .orElseThrow(() -> new ApiException(StatusCode.VALIDATE_FAILED));
    }
}
