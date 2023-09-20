package com.xiaoxiao.taskCourier.model.enums;

import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum RewardTaskTypeEnum {
    REWARD_TASK(1, "悬赏任务"),
    SECOND_HAND(2, "二手交易"),
    ;
    private final int type;
    private final String desc;

    public static RewardTaskTypeEnum getRewardTaskTypeEnum(int type) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getType(), type))
                .findFirst()
                .orElseThrow(() -> new ApiException(StatusCode.VALIDATE_FAILED));
    }
}
