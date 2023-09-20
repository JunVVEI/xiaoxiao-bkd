package com.xiaoxiao.baseservice.service;

import com.xiaoxiao.baseservice.model.dto.CaptchaDTO;
import com.xiaoxiao.baseservice.model.vo.CaptchaVO;
import com.xiaoxiao.common.api.CommonResp;
import org.springframework.stereotype.Service;

/**
 * @author mayunfei
 */
@Service
public interface CaptchaService {
    /**
     * 返回验证码图片
     * @return 成功返回验证码base64和它在redis中的key
     */
    CaptchaVO captchaImg();

    /**
     * 返回验证码文本
     * @return 成功返回验证码文本和它在redis中的key
     */
    CaptchaVO captchaCode();

    /**
     * 返回验证码文本
     * @param key 指定key
     * @return
     */
    CaptchaVO captchaCode(String key);

    /**
     *
     * @param captchaDTO 参数
     * @return 验证结果
     */
    boolean verify(CaptchaDTO captchaDTO);
}
