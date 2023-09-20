package com.xiaoxiao.baseservice.controller;

import com.xiaoxiao.baseservice.model.dto.CaptchaDTO;
import com.xiaoxiao.baseservice.model.vo.CaptchaVO;
import com.xiaoxiao.baseservice.service.CaptchaService;
import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author mayunfei
 */
@RestController
@RequestMapping("/bs/captcha")
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    /**
     * 返回验证码图片
     *
     * @return 在redis中对应的key以及base64图片流字符串
     */
    @ResponseBody
    @GetMapping("/img")
    @XiaoXiaoResponseBody
    public CaptchaVO captchaImg() {
        return captchaService.captchaImg();
    }

    /**
     * 返回验证码文本
     *
     * @return 在redis中对应的key和验证码中的文本内容
     */
    @RequestMapping("/code")
    @XiaoXiaoResponseBody
    public CaptchaVO captchaCode() {
        return captchaService.captchaCode();
    }

    /**
     * 校验验证码功能
     */
    @PostMapping("/verify")
    @XiaoXiaoResponseBody
    public boolean verify(@RequestBody CaptchaDTO captchaDTO) {
        return captchaService.verify(captchaDTO);
    }

}


