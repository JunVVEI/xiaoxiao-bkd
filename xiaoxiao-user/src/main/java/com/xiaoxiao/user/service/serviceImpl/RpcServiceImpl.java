package com.xiaoxiao.user.service.serviceImpl;

import cn.hutool.json.JSONUtil;
import com.xiaoxiao.baseservice.rpc.model.response.CaptchaResponse;
import com.xiaoxiao.baseservice.rpc.model.response.SendMailResponse;
import com.xiaoxiao.baseservice.rpc.model.resquest.CaptchaRequest;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendMailRequest;
import com.xiaoxiao.baseservice.rpc.service.RpcBaseService;
import com.xiaoxiao.bbs.rpc.service.RpcBbsService;
import com.xiaoxiao.common.api.CommonResp;
import com.xiaoxiao.user.service.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenjunwei
 */
@Service
@Slf4j
public class RpcServiceImpl implements RpcService {

    @Resource
    private RpcBaseService rpcBaseService;

    @Resource
    private RpcBbsService rpcBbsService;

    public SendMailResponse sendMail(SendMailRequest sendMailRequest) {
        try {
            CommonResp<SendMailResponse> sendMailResponseCommonResp = rpcBaseService.sendMail(sendMailRequest);
            log.info("发送邮件 请求 {}, 响应 {}", JSONUtil.toJsonStr(sendMailRequest), JSONUtil.toJsonStr(sendMailResponseCommonResp));
            if (CommonResp.isSuccess(sendMailResponseCommonResp)) {
                return sendMailResponseCommonResp.getData();
            }
        } catch (Exception e) {
            log.error("发送邮件异常 sendMailRequest {}", sendMailRequest, e);
        }
        return new SendMailResponse(false, "发送失败");
    }


    public CaptchaResponse captchaCode(String key) {
        try {
            CommonResp<CaptchaResponse> captchaResponseCommonResp = rpcBaseService.captchaCode(key);
            log.info("获取验证码响应 {}", JSONUtil.toJsonStr(captchaResponseCommonResp));
            if (CommonResp.isSuccess(captchaResponseCommonResp)) {
                return captchaResponseCommonResp.getData();
            }
        } catch (Exception e) {
            log.error("获取验证码异常: ", e);
        }
        return null;
    }


    public CaptchaResponse verify(CaptchaRequest captchaRequest) {
        try {
            CommonResp<CaptchaResponse> verify = rpcBaseService.verify(captchaRequest);
            log.info("校验验证码 请求 {}, 响应 {}", JSONUtil.toJsonStr(captchaRequest), JSONUtil.toJsonStr(verify));
            if (CommonResp.isSuccess(verify)) {
                return verify.getData();
            }
        } catch (Exception e) {
            log.error("校验验证码异常 {}", captchaRequest, e);
        }
        return null;
    }

    @Override
    public Integer getUserPostCount(Long uid) {
        try {
            return rpcBbsService.getUserPostCount(uid).getData();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Integer getUserLikeCount(Long uid) {
        try {
            return rpcBbsService.getUserPostLikeSum(uid).getData();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Integer getUserIdentityPostCount(Long identityId) {
        try {
            return rpcBbsService.getIdentityPostCount(identityId).getData();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Integer getUserIdentityLikeCount(Long identityId) {
        return 0;
    }
}
