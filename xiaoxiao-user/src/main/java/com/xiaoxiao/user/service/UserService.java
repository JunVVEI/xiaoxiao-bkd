package com.xiaoxiao.user.service;

import com.xiaoxiao.user.model.dto.LoginDTO;
import com.xiaoxiao.user.model.dto.UserRegistrationDTO;
import com.xiaoxiao.user.model.dto.WeChatLoginDTO;
import com.xiaoxiao.user.model.entity.User;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zjw
 * @since 2022-11-19 11:11:38
 */
public interface UserService {

    /**
     * 登陆
     *
     * @param loginDTO 登陆实体
     * @param response 请求响应
     * @return 是否成功登陆
     */
    boolean login(LoginDTO loginDTO, HttpServletResponse response);

    /**
     * 登出
     *
     * @return 是否成功
     */
    boolean logout();

    /**
     * 更新账号版本，用于服务端强制下线
     */
    void renewUserVersion(Long uid, String version);

    /**
     * 发送邮箱验证码
     *
     * @param user 注册实体
     * @return 是否发送成功
     */
    String sendEmailCode(User user);

    /**
     * 邮箱注册
     *
     * @param userRegistrationDTO 注册实体
     * @return 是否注册成功
     */
    boolean register(UserRegistrationDTO userRegistrationDTO);

    /**
     * 微信登录
     */
    boolean weChatLogin(WeChatLoginDTO weChatLoginDTO, HttpServletResponse response);

    /**
     * 微信登录
     */
    boolean weChatRegister(WeChatLoginDTO weChatLoginDTO, HttpServletResponse response);

    /**
     * 是用户的token失效
     */
    boolean invalidateJWT(Long uid);
}
