package com.xiaoxiao.baseservice.service.impl;


import com.xiaoxiao.baseservice.service.RpcService;
import com.xiaoxiao.common.api.CommonResp;
import com.xiaoxiao.user.rpc.model.request.FillOpenIdRequest;
import com.xiaoxiao.user.rpc.model.request.GetUserInfoRequest;
import com.xiaoxiao.user.rpc.model.response.GetUserInfoResponse;
import com.xiaoxiao.user.rpc.service.RpcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Slf4j
@Service
public class RpcServiceImpl implements RpcService {
    @Resource
    private RpcUserService rpcUserService;

    @Override
    public void fillOpenidByUnionId(String unionId, String openId) {
        FillOpenIdRequest req = new FillOpenIdRequest();
        req.setUnionId(unionId);
        req.setOpenId(openId);
        rpcUserService.fillOpenIdByUnionId(req);
    }

    @Override
    public String getServiceAccountOpenId(String openId) {
        GetUserInfoRequest req = new GetUserInfoRequest();
        req.setOpenId(openId);
        CommonResp<GetUserInfoResponse> resp = rpcUserService.getUserInfo(req);
        return resp.getData().getServiceAccountOpenId();
    }

    public String getUserServiceAccountOpenId(Long uid) {
        return rpcUserService.getUserServiceAccountOpenId(uid);
    }
}
