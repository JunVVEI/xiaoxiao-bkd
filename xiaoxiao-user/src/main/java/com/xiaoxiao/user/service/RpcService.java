package com.xiaoxiao.user.service;

import com.xiaoxiao.baseservice.rpc.model.response.CaptchaResponse;
import com.xiaoxiao.baseservice.rpc.model.response.SendMailResponse;
import com.xiaoxiao.baseservice.rpc.model.resquest.CaptchaRequest;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendMailRequest;

/**
 * 将使用的rpc接口都在这里在做一层处理，如远程调用出现异常的兜底处理可以在这里做
 * 以应对以一些异常情况，项目较小，可以都写到这里
 *
 * @author chenjunwei
 */
public interface RpcService {

    /**
     * 发送邮件rpc接口
     *
     * @param sendMailRequest 发送邮件请求实体
     * @return 发送结果
     */
    SendMailResponse sendMail(SendMailRequest sendMailRequest);

    /**
     * 返回验证码文本
     *
     * @return 在redis中对应的key和验证码中的文本内容
     */
    CaptchaResponse captchaCode(String key);

    /**
     * 校验验证码功能
     *
     * @param captchaRequest 校验验证码请求实体
     * @return 校验结果
     */
    CaptchaResponse verify(CaptchaRequest captchaRequest);

    /**
     * 获取用户的发帖量
     */
    Integer getUserPostCount(Long uid);

    /**
     * 获取用户帖子获得的点赞量
     */
    Integer getUserLikeCount(Long uid);

    /**
     * 获取用户的发帖量
     */
    Integer getUserIdentityPostCount(Long identityId);

    /**
     * 获取用户帖子获得的点赞量
     */
    Integer getUserIdentityLikeCount(Long identityId);
}
