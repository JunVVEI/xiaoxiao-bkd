package com.xiaoxiao.user.model.dto;


import lombok.Data;

/**
 * 微信code2Session接口响应
 *
 * @author zjw
 */
@Data
public class WeChatSession {
    /**
     * 用户唯一标识
     */
    private String openid;
    /**
     * 用户在开放平台的唯一标识符
     */
    private String unionid;
    /**
     * 会话密钥
     */
    private String session_key;
    /**
     * 错误码
     */
    private String errcode;
    /**
     * 错误信息
     */
    private String errmsg;
}
