package com.xiaoxiao.user.controller;

import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.user.model.dto.LoginDTO;
import com.xiaoxiao.user.model.dto.UserRegistrationDTO;
import com.xiaoxiao.user.model.dto.WeChatLoginDTO;
import com.xiaoxiao.user.model.entity.User;
import com.xiaoxiao.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zjw
 * @since 2022-11-19 11:11:38
 */
@RestController
@RequestMapping("/user/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 微信登录，如果未注册返回false
     */
    @PostMapping("/weChatLogin")
    @XiaoXiaoResponseBody
    public boolean weChatLogin(@RequestBody WeChatLoginDTO weChatLoginDTO, HttpServletResponse response) {
        return userService.weChatLogin(weChatLoginDTO, response);
    }

    /**
     * 微信注册并登陆
     */
    @PostMapping("/weChatRegister")
    @XiaoXiaoResponseBody
    public boolean weChatRegister(@RequestBody WeChatLoginDTO weChatLoginDTO, HttpServletResponse response) {
        return userService.weChatRegister(weChatLoginDTO, response);
    }

    /**
     * @param loginDTO 登录实体
     * @param response 请求响应
     * @return 是否成功登陆
     */
    @PostMapping("/login")
    @XiaoXiaoResponseBody
    public boolean login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        return userService.login(loginDTO, response);
    }

    /**
     * 登出
     *
     * @return 是否成功
     */
    @GetMapping("/logout")
    @XiaoXiaoResponseBody
    public boolean logout() {
        return userService.logout();
    }

    /**
     * 发送邮箱验证码
     *
     * @param user 注册实体
     * @return 是否发送成功
     */
    @PostMapping("/sendEmailCode")
    @XiaoXiaoResponseBody
    public String sendEmailCode(@RequestBody User user) {
        return userService.sendEmailCode(user);
    }

    /**
     * 邮箱注册
     *
     * @param userRegistrationDTO 注册实体
     * @return 是否注册成功
     */
    @PostMapping("/register")
    @XiaoXiaoResponseBody
    public boolean register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        return userService.register(userRegistrationDTO);
    }

}

