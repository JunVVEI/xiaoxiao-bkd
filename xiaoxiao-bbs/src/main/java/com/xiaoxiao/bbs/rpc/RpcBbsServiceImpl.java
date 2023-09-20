package com.xiaoxiao.bbs.rpc;

import com.xiaoxiao.bbs.rpc.service.RpcBbsService;
import com.xiaoxiao.bbs.service.PostService;
import com.xiaoxiao.common.api.CommonResp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RpcBbsServiceImpl implements RpcBbsService {

    @Resource
    private PostService postService;

    @Override
    @PostMapping("/bbs/rpc/getUserPostCount")
    public CommonResp<Integer> getUserPostCount(Long uid) {
        return CommonResp.success(postService.getUserPostCount(uid));
    }

    @Override
    @PostMapping("/bbs/rpc/getUserLikeCount")
    public CommonResp<Integer> getUserPostLikeSum(Long uid) {
        return CommonResp.success(postService.getUserPostLikedSum(uid));
    }

    @Override
    @PostMapping("/bbs/rpc/getIdentityPostCount")
    public CommonResp<Integer> getIdentityPostCount(Long identityId) {
        return CommonResp.success(postService.getIdentityPostCount(identityId));
    }
}
