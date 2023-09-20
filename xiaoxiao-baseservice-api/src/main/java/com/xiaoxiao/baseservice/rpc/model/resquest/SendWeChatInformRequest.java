package com.xiaoxiao.baseservice.rpc.model.resquest;

import lombok.Data;

@Data
public class SendWeChatInformRequest {

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 小程序打开的路径
     */
    private String pagePath;

    /**
     * 消息类型
     * WeChatInformTypeEnum
     */
    private int WeChatInformType;

    /**
     * 消息类型 String, 适配课表推送
     */
    private String weChatInformTypeString;

    /**
     * 内容
     */
    private String content;

}
