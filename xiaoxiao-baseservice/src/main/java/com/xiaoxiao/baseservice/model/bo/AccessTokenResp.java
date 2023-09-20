package com.xiaoxiao.baseservice.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccessTokenResp {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("errcode")
    private String errcode;

    @JsonProperty("errmsg")
    private String errmsg;

}
