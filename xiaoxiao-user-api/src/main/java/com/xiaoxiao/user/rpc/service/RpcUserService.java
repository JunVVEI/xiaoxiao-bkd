package com.xiaoxiao.user.rpc.service;

import com.xiaoxiao.common.api.CommonResp;
import com.xiaoxiao.user.rpc.model.request.EnergyRequest;
import com.xiaoxiao.user.rpc.model.request.FillOpenIdRequest;
import com.xiaoxiao.user.rpc.model.request.GetUserInfoRequest;
import com.xiaoxiao.user.rpc.model.request.UserSearchRequest;
import com.xiaoxiao.user.rpc.model.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * service
 * </p>
 *
 * @author Junwei
 * @since 2023/2/24
 */
@FeignClient(value = "xiaoxiao-user")
public interface RpcUserService {

    /**
     * 获取用户关注的人列表
     *
     * @param uid 用户id
     * @return 关注的人
     */
    @PostMapping("/user/rpc/biz/getUserFollowing")
    CommonResp<UserFollowingResponse> getUserFollowing(@RequestParam Long uid);

    /**
     * 根据关键词搜索用户
     *
     * @param userSearchRequest 用户搜索请求封装
     * @return 搜索分页结果
     */
    @GetMapping(value = "/user/rpc/biz/search")
    CommonResp<UserSearchResponse> searchUserByKeyword(@SpringQueryMap(true) UserSearchRequest userSearchRequest);

    /**
     * 批量根据id查询用户名称与头像
     */
    @PostMapping("/user/rpc/getUserNameAndAvatar")
    CommonResp<List<UserInfoResponse>> getUserNameAndAvatar(@RequestParam List<Long> uid);

    /**
     * 批量根据匿名id查询匿名名称与头像
     */
    @PostMapping("/user/rpc/biz/getAnonymousNameAndAvatar")
    CommonResp<List<IdentityInfoResponse>> getAnonymousNameAndAvatar(@RequestParam List<Long> identityId);

    @PostMapping("/user/rpc/fill_openid_by_unionid")
    void fillOpenIdByUnionId(@RequestBody FillOpenIdRequest fillOpenIdRequest);

    @PostMapping("/user/rpc/get_user_info")
    CommonResp<GetUserInfoResponse> getUserInfo(@RequestBody GetUserInfoRequest getUserInfoRequest);

    /**
     * 根据用户id获取用户服务号openid
     */
    @PostMapping("/user/rpc/getUserServiceAccountOpenId")
    String getUserServiceAccountOpenId(@RequestParam Long uid);

    /**
     * 获取用户的粉丝列表
     *
     * @param uid 用户id
     * @return 关注的人
     */
    @PostMapping("/user/rpc/biz/getUserFollowers")
    CommonResp<UserFollowerResponse> getUserFollowers(@RequestParam Long uid);

    /**
     * 根据用户id获取创建者  以后用来当机器人的check
     */
    @PostMapping("/user/rpc/getUserCreateBy")
    String getUserCreateBy(@RequestParam Long uid);

    /**
     * 获取设置（key）处于激活状态的用户列表
     */
    @PostMapping("/user/rpc/getActivateConfKeyUserIds")
    List<Long> getActivateConfKeyUserIds(@RequestParam String key);

    /**
     * 积分
     */
    @PostMapping("/user/rpc/calculateEnergy")
    CommonResp<Boolean> calculateEnergyRpc(@RequestBody EnergyRequest energyRequest);
}
