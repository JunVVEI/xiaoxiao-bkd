package com.xiaoxiao.baseservice.rpc.model.response;

import lombok.Data;

/**
 * @author zhengjianwei
 */
@Data
public class CaptchaResponse {
	/**
	 * redis中储存验证码条目的key
	 */
	private String key;
	/**
	 * 验证码图片base64数据流
	 */
	private String verCode64;
	/**
	 * 验证码图中的文字内容字符串
	 */
	private String verCode;

	private boolean isSuccess;

	private String message;
}
