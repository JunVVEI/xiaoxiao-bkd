package com.xiaoxiao.baseservice.rpc.service;

import com.xiaoxiao.baseservice.rpc.model.response.CaptchaResponse;
import com.xiaoxiao.baseservice.rpc.model.response.GetWXAccessTokenResponse;
import com.xiaoxiao.baseservice.rpc.model.response.SendMailResponse;
import com.xiaoxiao.baseservice.rpc.model.resquest.CaptchaRequest;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendMailRequest;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendWeChatInformRequest;
import com.xiaoxiao.common.api.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * RpcBaseService
 * </p>
 *
 * @author Junwei
 * @since 2023/1/3
 */
@FeignClient(value = "xiaoxiao-baseservice")
public interface RpcBaseService {

    /**
     * 发送邮件rpc接口
     *
     * @param sendMailRequest 发送邮件请求实体
     * @return 发送结果
     */
    @PostMapping("/bs/rpc/mail")
    CommonResp<SendMailResponse> sendMail(@RequestBody SendMailRequest sendMailRequest);

    /**
     * 批量发送邮件rpc接口
     */
    @PostMapping("/bs/rpc/mailBatch")
    CommonResp<SendMailResponse> sendMailBatch(@RequestBody List<SendMailRequest> sendMailRequestList);

    /**
     * 返回验证码文本
     *
     * @param key
     * @return 在redis中对应的key和验证码中的文本内容
     */
    @GetMapping("/bs/rpc/code")
    CommonResp<CaptchaResponse> captchaCode(@RequestParam String key);

    /**
     * 校验验证码功能
     *
     * @param captchaRequest 校验验证码请求实体
     * @return 校验结果
     */
    @PostMapping("/bs/rpc/verify")
    CommonResp<CaptchaResponse> verify(@RequestBody CaptchaRequest captchaRequest);


    @PostMapping("/bs/rpc/wx_access_token")
    CommonResp<GetWXAccessTokenResponse> getServiceAccountAccessToken();

    @PostMapping("/bs/rpc/sendWeChatInform")
    boolean sendWeChatInform(@RequestBody SendWeChatInformRequest sendWeChatInformRequest);


    @PostMapping("/bs/rpc/getMiniAppAccessToken")
    CommonResp<GetWXAccessTokenResponse> getMiniAppAccessToken();
}
