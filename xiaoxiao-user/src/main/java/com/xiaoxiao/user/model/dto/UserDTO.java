package com.xiaoxiao.user.model.dto;

import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

import java.util.Objects;

/**
 * <p>
 * UserDTO
 * </p>
 *
 * @author Junwei
 * @since 2023/2/14
 */
@Data
public class UserDTO {

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 身份id，可为null
     */
    private Long identityId;

    /**
     * 类型： 1真实身份、2匿名身份
     */
    private Integer type;

    public static void checkIsValid(UserDTO userDTO) {
        AssertUtil.isTrue(Objects.nonNull(userDTO), StatusCode.VALIDATE_FAILED);
        AssertUtil.isTrue(
                Objects.nonNull(userDTO.getType()),
                StatusCode.VALIDATE_FAILED
        );
        AssertUtil.isTrue(
                Objects.equals(userDTO.getType(), XiaoXiaoConstEnum.USER.getVal())
                        || Objects.equals(userDTO.getType(), XiaoXiaoConstEnum.ANONYMOUS_IDENTITY.getVal()),
                StatusCode.VALIDATE_FAILED
        );
        AssertUtil.isTrue(
                Objects.nonNull(userDTO.getUid()) || Objects.nonNull(userDTO.getIdentityId()),
                StatusCode.VALIDATE_FAILED
        );
    }
}
