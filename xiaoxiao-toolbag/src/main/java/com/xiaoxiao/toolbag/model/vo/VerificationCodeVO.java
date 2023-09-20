package com.xiaoxiao.toolbag.model.vo;

import lombok.Data;

/**
 * 返回base64和教务系统设置的cookie
 *
 * @author zjh
 */
@Data
public class VerificationCodeVO {

    /**
     * 验证码接口返回的cookie
     */
    String session;

    /**
     * 验证码图片
     */
    String base64;

    public VerificationCodeVO(String session, String base64) {
        this.session = session;
        this.base64 = base64;
    }
}
