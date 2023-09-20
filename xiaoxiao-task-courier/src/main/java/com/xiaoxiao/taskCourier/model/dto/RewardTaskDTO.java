package com.xiaoxiao.taskCourier.model.dto;

import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.util.AssertUtil;
import com.xiaoxiao.taskCourier.model.enums.RewardTaskTypeEnum;
import lombok.Data;

import java.util.Objects;

@Data
public class RewardTaskDTO {
    /**
     * 内容
     */
    private String content;

    /**
     * 媒体资源路径逗号分隔
     */
    private String mediaPath;

    /**
     * 赏金
     */
    private Double bounty;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 类型
     */
    private Integer type;

    public static void checkIsValid(RewardTaskDTO rewardTaskDTO) {
        AssertUtil.isTrue(StrUtil.isNotBlank(rewardTaskDTO.getContact()), StatusCode.VALIDATE_FAILED);
        AssertUtil.isTrue(
                Objects.nonNull(rewardTaskDTO.getBounty()) && 0 <= rewardTaskDTO.getBounty(),
                StatusCode.VALIDATE_FAILED
        );
        AssertUtil.isTrue(StrUtil.isNotBlank(rewardTaskDTO.getContact()), StatusCode.VALIDATE_FAILED);
//        AssertUtil.isTrue(
//                Objects.nonNull(RewardTaskTypeEnum.getRewardTaskTypeEnum(rewardTaskDTO.getType())),
//                StatusCode.VALIDATE_FAILED
//        );
    }

}
