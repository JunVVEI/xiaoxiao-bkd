package com.xiaoxiao.taskCourier.service.serviceImpl;

import cn.hutool.core.collection.CollectionUtil;
import com.xiaoxiao.baseservice.rpc.model.response.GetWXAccessTokenResponse;
import com.xiaoxiao.baseservice.rpc.model.response.SendMailResponse;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendMailRequest;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendWeChatInformRequest;
import com.xiaoxiao.baseservice.rpc.service.RpcBaseService;
import com.xiaoxiao.common.api.CommonResp;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.taskCourier.service.RpcService;
import com.xiaoxiao.user.rpc.model.response.IdentityInfoResponse;
import com.xiaoxiao.user.rpc.model.response.UserFollowerResponse;
import com.xiaoxiao.user.rpc.model.response.UserFollowingResponse;
import com.xiaoxiao.user.rpc.model.response.UserInfoResponse;
import com.xiaoxiao.user.rpc.model.vo.RpcUserVO;
import com.xiaoxiao.user.rpc.service.RpcUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RpcServiceImpl implements RpcService {

    @Resource
    private RpcUserService rpcUserService;
    @Resource
    private RpcBaseService rpcBaseService;

    @Override
    public UserFollowingResponse getUserFollowing(Long uid) {
        try {
            UserFollowingResponse data = rpcUserService.getUserFollowing(uid).getData();
            if (Objects.isNull(data)) {
                data = new UserFollowingResponse();
                data.setRpcUserVOList(new ArrayList<>());
            }
            return data;
        } catch (Exception e) {
            UserFollowingResponse userFollowingResponse = new UserFollowingResponse();
            userFollowingResponse.setRpcUserVOList(new ArrayList<>());
            return userFollowingResponse;
        }
    }

    @Override
    public List<UserInfoResponse> getUserNameAndAvatar(List<Long> uid) {
        try {
            if (CollectionUtil.isEmpty(uid)) {
                return new ArrayList<>();
            }
            List<UserInfoResponse> data = rpcUserService.getUserNameAndAvatar(uid).getData();
            if (CollectionUtil.isEmpty(data)) {
                data = new ArrayList<>();
            }
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    @Override
    public List<IdentityInfoResponse> getAnonymousNameAndAvatar(List<Long> identityId) {
        try {
            if (CollectionUtil.isEmpty(identityId)) {
                return new ArrayList<>();
            }
            List<IdentityInfoResponse> data = rpcUserService.getAnonymousNameAndAvatar(identityId).getData();
            if (CollectionUtil.isEmpty(data)) {
                data = new ArrayList<>();
            }
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public SendMailResponse sendMail(SendMailRequest sendMailRequest) {
        try {
            // todo 增加邮箱接口的批量发送 防止rpc调用太多
            CommonResp<SendMailResponse> sendMailResponseCommonResp = rpcBaseService.sendMail(sendMailRequest);
            if (CommonResp.isSuccess(sendMailResponseCommonResp)) {
                return sendMailResponseCommonResp.getData();
            }
            return new SendMailResponse(false, "发送失败");
        } catch (Exception e) {
            log.error("邮件发生异常", e);
            return new SendMailResponse(false, "发送失败");
        }
    }

    @Override
    public boolean sendWeChatInform(SendWeChatInformRequest sendWeChatInformRequest) {
        return rpcBaseService.sendWeChatInform(sendWeChatInformRequest);
    }

    @Override
    public Map<Long, UserInfoResponse> getUserNameAndAvatarMap(List<Long> uid) {
        if (CollectionUtil.isEmpty(uid)) {
            return new HashMap<>();
        }
        List<UserInfoResponse> userNameAndAvatar = getUserNameAndAvatar(uid);
        return userNameAndAvatar.stream()
                .collect(
                        Collectors.toMap(
                                UserInfoResponse::getUid,
                                Function.identity(), (a, b) -> a
                        )
                );
    }

    @Override
    public Map<Long, IdentityInfoResponse> getAnonymousNameAndAvatarMap(List<Long> identityId) {
        if (CollectionUtil.isEmpty(identityId)) {
            return new HashMap<>();
        }
        List<IdentityInfoResponse> anonymousNameAndAvatar = getAnonymousNameAndAvatar(identityId);
        return anonymousNameAndAvatar.stream()
                .collect(
                        Collectors.toMap(
                                IdentityInfoResponse::getIdentityId, Function.identity(),
                                (a, b) -> a
                        )
                );

    }

    @Override
    public List<RpcUserVO> getUserFollowers(Long uid) {
        try {
            UserFollowerResponse data = rpcUserService.getUserFollowers(uid).getData();
            if (Objects.isNull(data)) {
                data = new UserFollowerResponse();
                data.setRpcUserVOList(new ArrayList<>());
            }
            return data.getRpcUserVOList();
        } catch (Exception e) {
            UserFollowingResponse userFollowingResponse = new UserFollowingResponse();
            userFollowingResponse.setRpcUserVOList(new ArrayList<>());
            return userFollowingResponse.getRpcUserVOList();
        }
    }

    @Override
    public String getUserCreateBy(Long uid) {
        if (Objects.isNull(uid)) {
            return null;
        }
        try {
            String createBy = rpcUserService.getUserCreateBy(uid);
            return createBy;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getMiniAppAccessToken() {
        CommonResp<GetWXAccessTokenResponse> miniAppAccessToken = rpcBaseService.getMiniAppAccessToken();
        String accessToken = miniAppAccessToken.getData().getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            throw new ApiException(StatusCode.BIZ_ERROR);
        }
        return miniAppAccessToken.getData().getAccessToken();
    }
}
