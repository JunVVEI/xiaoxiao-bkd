package com.xiaoxiao.baseservice.rpc;

import com.xiaoxiao.baseservice.model.dto.CaptchaDTO;
import com.xiaoxiao.baseservice.model.dto.MailDTO;
import com.xiaoxiao.baseservice.model.vo.CaptchaVO;
import com.xiaoxiao.baseservice.rpc.model.response.CaptchaResponse;
import com.xiaoxiao.baseservice.rpc.model.response.GetWXAccessTokenResponse;
import com.xiaoxiao.baseservice.rpc.model.response.SendMailResponse;
import com.xiaoxiao.baseservice.rpc.model.resquest.CaptchaRequest;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendMailRequest;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendWeChatInformRequest;
import com.xiaoxiao.baseservice.rpc.service.RpcBaseService;
import com.xiaoxiao.baseservice.service.CaptchaService;
import com.xiaoxiao.baseservice.service.MailService;
import com.xiaoxiao.baseservice.service.WXService;
import com.xiaoxiao.common.api.CommonResp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * RpcMailController
 * </p>
 *
 * @author Junwei
 * @since 2023/1/3
 */
@RestController
public class RpcBaseServiceImpl implements RpcBaseService {

    @Resource
    private MailService mailService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private WXService wxService;

    /**
     * 发送邮件
     */
    @PostMapping("/bs/rpc/mail")
    public CommonResp<SendMailResponse> sendMail(@RequestBody SendMailRequest sendMailRequest) {
        MailDTO mailDTO = MailDTO.prepareMailDTO(sendMailRequest);
        boolean isSuccess = mailService.sendMail(mailDTO);
        SendMailResponse sendMailResponse = new SendMailResponse();
        sendMailResponse.setIsSuccess(isSuccess);
        sendMailResponse.setMessage("发送成功");
        return CommonResp.success(sendMailResponse);
    }

    @Override
    @PostMapping("/bs/rpc/mailBatch")
    public CommonResp<SendMailResponse> sendMailBatch(List<SendMailRequest> sendMailRequestList) {
        for (SendMailRequest sendMailRequest : sendMailRequestList) {
            MailDTO mailDTO = MailDTO.prepareMailDTO(sendMailRequest);
            mailService.sendMail(mailDTO);
        }
        SendMailResponse sendMailResponse = new SendMailResponse();
        sendMailResponse.setIsSuccess(true);
        sendMailResponse.setMessage("发送成功");
        return CommonResp.success(sendMailResponse);
    }

    /**
     * 返回验证码文本
     *
     * @return 在redis中对应的key和验证码中的文本内容
     */
    @Override
    @RequestMapping("/bs/rpc/code")
    public CommonResp<CaptchaResponse> captchaCode(@RequestParam String key) {
        CaptchaVO captchaVO = captchaService.captchaCode(key);
        CaptchaResponse captchaResponse = new CaptchaResponse();
        captchaResponse.setSuccess(true);
        captchaResponse.setKey(captchaVO.getKey());
        captchaResponse.setVerCode(captchaVO.getVerCode());
        return CommonResp.success(captchaResponse);
    }

    /**
     * 校验验证码功能
     *
     * @param captchaRequest
     */
    @Override
    @PostMapping("/bs/rpc/verify")
    public CommonResp<CaptchaResponse> verify(@RequestBody CaptchaRequest captchaRequest) {
        CaptchaDTO captchaDTO = new CaptchaDTO();
        captchaDTO.setKey(captchaRequest.getKey());
        captchaDTO.setUserCode(captchaRequest.getUserCode());
        boolean verify = captchaService.verify(captchaDTO);
        CaptchaResponse captchaResponse = new CaptchaResponse();
        captchaResponse.setSuccess(verify);
        return CommonResp.success(captchaResponse);
    }

    @Override
    @PostMapping("/bs/rpc/wx_access_token")
    public CommonResp<GetWXAccessTokenResponse> getServiceAccountAccessToken() {
        String accessToken = wxService.getServiceAccountAccessToken();
        GetWXAccessTokenResponse getWXAccessTokenResponse = new GetWXAccessTokenResponse();
        getWXAccessTokenResponse.setAccessToken(accessToken);
        return CommonResp.success(getWXAccessTokenResponse);
    }

    @Override
    @PostMapping("/bs/rpc/sendWeChatInform")
    public boolean sendWeChatInform(@RequestBody SendWeChatInformRequest sendWeChatInformRequest) {
        return wxService.sendWeChatInform(sendWeChatInformRequest);
    }

    @Override
    @PostMapping("/bs/rpc/getMiniAppAccessToken")
    public CommonResp<GetWXAccessTokenResponse> getMiniAppAccessToken() {
        String accessToken = wxService.getMiniAppAccessToken();
        GetWXAccessTokenResponse getWXAccessTokenResponse = new GetWXAccessTokenResponse();
        getWXAccessTokenResponse.setAccessToken(accessToken);
        return CommonResp.success(getWXAccessTokenResponse);
    }

//    @Override
//    @PostMapping("/bs/rpc/getMiniAppAccessToken")
//    public CommonResp<GetWXAccessTokenResponse> getMiniAppAccessToken() {
//        String accessToken = wxService.getMiniAppAccessToken();
//        GetWXAccessTokenResponse getWXAccessTokenResponse = new GetWXAccessTokenResponse();
//        getWXAccessTokenResponse.setAccessToken(accessToken);
//        return CommonResp.success(getWXAccessTokenResponse);
//    }

}
