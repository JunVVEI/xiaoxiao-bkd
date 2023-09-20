package com.xiaoxiao.toolbag.service;


import com.xiaoxiao.baseservice.rpc.model.resquest.SendWeChatInformRequest;

import java.util.List;

/**
 * @author yaoyao
 * @Description
 * @create 2023-06-12 0:32
 */
public interface RpcService {
    /**
     * 发送openai接口错误信息
     *
     * @param message 发送邮件请求实体
     */
    void asyncSendOpenAiErrorMail(String message);

    void asyncSendErrorMail(String title, String message);

    boolean sendWeChatInform(SendWeChatInformRequest sendWeChatInformRequest);


    List<Long> getActivateConfKeyUserIds(String key);
}
