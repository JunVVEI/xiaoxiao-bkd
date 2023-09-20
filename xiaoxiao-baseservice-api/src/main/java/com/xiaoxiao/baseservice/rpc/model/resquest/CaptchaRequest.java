package com.xiaoxiao.baseservice.rpc.model.resquest;

import lombok.Data;

/**
 * @author zhengjianwei
 */
@Data
public class CaptchaRequest {

	/**
	 * redis中储存验证码的key
	 */
	private String key;

	/**
	 * 用户输入的验证码
	 */
	private String userCode;
}
