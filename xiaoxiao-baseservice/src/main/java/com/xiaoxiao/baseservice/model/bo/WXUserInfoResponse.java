package com.xiaoxiao.baseservice.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WXUserInfoResponse {
    @JsonProperty("unionid")
    private String unionId;
}
