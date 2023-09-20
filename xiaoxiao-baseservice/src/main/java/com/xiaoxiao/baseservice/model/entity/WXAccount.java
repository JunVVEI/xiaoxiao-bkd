package com.xiaoxiao.baseservice.model.entity;

import lombok.Data;

@Data
public class WXAccount {
    private String appid;
    private String secret;
    private String accessToken;

    public WXAccount(String appid, String secret) {
        this.appid = appid;
        this.secret = secret;
    }
}
