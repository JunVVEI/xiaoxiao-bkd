package com.xiaoxiao.baseservice.model.dto;

import com.xiaoxiao.baseservice.rpc.model.resquest.SendMailRequest;
import lombok.Data;

/**
 * @author luohaobin
 */
@Data
public class MailDTO {
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

    public static MailDTO prepareMailDTO(SendMailRequest sendMailRequest) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTargetMail(sendMailRequest.getTargetMail());
        mailDTO.setMsg(sendMailRequest.getMsg());
        mailDTO.setTitle(sendMailRequest.getTitle());
        return mailDTO;
    }
}
