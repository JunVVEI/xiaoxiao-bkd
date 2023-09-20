package com.xiaoxiao.baseservice.model.vo;

import lombok.Data;

@Data
public class WXAccessTokenVO {
    private String accessToken;

    public WXAccessTokenVO(String accessToken) {
        this.accessToken = accessToken;
    }
}
