package com.xiaoxiao.baseservice.model.bo;

import lombok.Data;

@Data
public class WXInformRequest {
    private String touser;
    private String template_id;
    private String url;
    private WXInformMiniProgram miniprogram;
    private String client_msg_id;
    private WXInformData data;
}
