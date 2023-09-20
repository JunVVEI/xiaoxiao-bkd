package com.xiaoxiao.baseservice.rpc.model.resquest;

import lombok.Data;

/**
 * <p>
 * SendMailRequest
 * </p>
 *
 * @author Junwei
 * @since 2023/1/3
 */
@Data
public class SendMailRequest {
    /**
     * 目标邮箱
     */
    private String targetMail;

    /**
     * 邮件内容
     */
    private String msg;

    /**
     * 邮件标题
     */
    private String title;
}
