package com.xiaoxiao.bbs.rpc.service;

import com.xiaoxiao.common.api.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "xiaoxiao-bbs")
public interface RpcBbsService {

    /**
     * 获取用户的发帖量
     */
    @PostMapping("/bbs/rpc/getUserPostCount")
    CommonResp<Integer> getUserPostCount(@RequestParam Long uid);

    /**
     * 获取用户获得的点赞量
     */
    @PostMapping("/bbs/rpc/getUserLikeCount")
    CommonResp<Integer> getUserPostLikeSum(@RequestParam Long uid);

    /**
     * 获取匿名用户的发帖量
     */
    @PostMapping("/bbs/rpc/getIdentityPostCount")
    CommonResp<Integer> getIdentityPostCount(@RequestParam Long identityId);
}
