package com.xiaoxiao.user.model.dto;

import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

/**
 * @author chenjunwei
 */
@Data
public class LoginDTO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;


    public static void checkIsValid(LoginDTO loginDto) {
        AssertUtil.isTrue(StrUtil.isNotBlank(loginDto.getUserName()), "用户名为空");
        AssertUtil.isTrue(StrUtil.isNotBlank(loginDto.getPassword()), "密码为空");
    }
}
