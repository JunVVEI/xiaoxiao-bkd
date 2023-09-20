package com.xiaoxiao.user.model.dto;

import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.common.util.AssertUtil;
import com.xiaoxiao.user.model.entity.User;
import com.xiaoxiao.user.utils.PasswordUtils;
import lombok.Data;

/**
 * 用户注册请求实体类
 *
 * @author zhengjianwei
 */
@Data
public class UserRegistrationDTO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String userCode;

    public static void checkIsValid(UserRegistrationDTO userRegistrationDTO) {
        AssertUtil.isTrue(StrUtil.isNotBlank(userRegistrationDTO.getUserName()), "参数校验失败");
        AssertUtil.isTrue(StrUtil.isNotBlank(userRegistrationDTO.getEmail()), "参数校验失败");
        AssertUtil.isTrue(StrUtil.isNotBlank(userRegistrationDTO.getPassword()), "参数校验失败");
        AssertUtil.isTrue(StrUtil.isNotBlank(userRegistrationDTO.getUserCode()), "参数校验失败");
    }


    public static User prepareXiaoXiaoUser(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setUserName(userRegistrationDTO.getUserName());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(PasswordUtils.encode(userRegistrationDTO.getPassword()));
        return user;
    }

}
