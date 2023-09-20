package com.xiaoxiao.baseservice.model.bo;

import lombok.Data;

@Data
public class WXInformResponse {
    private int errcode;
    private String errmsg;
    private long msgid;
}
