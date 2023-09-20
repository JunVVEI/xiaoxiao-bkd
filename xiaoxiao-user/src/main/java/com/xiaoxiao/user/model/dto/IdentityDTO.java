package com.xiaoxiao.user.model.dto;

import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

import java.util.Objects;

/**
 * @author 林嫄袁
 * @since 2023/2/22 21:43
 */
@Data
public class IdentityDTO {
    /**
     * 身份id
     */
    private Long identityId;

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    public static void checkIsValid(IdentityDTO identityDTO) {
        AssertUtil.isTrue(Objects.nonNull(identityDTO), StatusCode.VALIDATE_FAILED);
        AssertUtil.isTrue(Objects.nonNull(identityDTO.getIdentityId()), StatusCode.VALIDATE_FAILED);
        AssertUtil.isTrue(StrUtil.isNotBlank(identityDTO.getName()), "昵称为空");
        AssertUtil.isTrue(StrUtil.isNotBlank(identityDTO.getAvatar()), "头像为空");
    }

}
