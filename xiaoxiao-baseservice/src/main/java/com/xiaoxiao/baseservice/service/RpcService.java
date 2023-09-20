package com.xiaoxiao.baseservice.service;


public interface RpcService {
    void fillOpenidByUnionId(String unionId, String openId);

    String getServiceAccountOpenId(String openId);

    String getUserServiceAccountOpenId(Long uid);
}
