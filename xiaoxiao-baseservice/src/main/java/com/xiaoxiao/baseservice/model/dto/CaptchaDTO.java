package com.xiaoxiao.baseservice.model.dto;

import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

/**
 * @author mayunfei
 */
@Data
public class CaptchaDTO {
	/**
	 * redis中储存验证码的key
	 */
	private String key;

	/**
	 * 用户输入的验证码
	 */
	private String userCode;

	public static void checkIsValid(CaptchaDTO captchaDTO) {
		AssertUtil.isTrue(StrUtil.isNotBlank(captchaDTO.getKey()), StatusCode.VALIDATE_FAILED);
		AssertUtil.isTrue(StrUtil.isNotBlank(captchaDTO.getUserCode()), StatusCode.VALIDATE_FAILED);
	}
}
