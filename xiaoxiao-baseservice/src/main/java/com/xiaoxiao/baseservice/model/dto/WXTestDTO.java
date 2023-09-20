package com.xiaoxiao.baseservice.model.dto;

import lombok.Data;

@Data
public class WXTestDTO {
    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;
}
