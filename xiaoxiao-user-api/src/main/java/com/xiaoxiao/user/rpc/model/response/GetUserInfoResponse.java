package com.xiaoxiao.user.rpc.model.response;

import lombok.Data;

@Data
public class GetUserInfoResponse {
    private String serviceAccountOpenId;
    public GetUserInfoResponse(){
    }
    public GetUserInfoResponse(String serviceAccountOpenId){
        this.serviceAccountOpenId = serviceAccountOpenId;
    }
}
