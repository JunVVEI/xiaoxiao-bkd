package com.xiaoxiao.user.rpc.model.request;

import lombok.Data;

@Data
public class FillOpenIdRequest {
    private String unionId;
    private String openId;
}
