package com.xiaoxiao.taskCourier.service;

import com.xiaoxiao.baseservice.rpc.model.response.SendMailResponse;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendMailRequest;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendWeChatInformRequest;
import com.xiaoxiao.user.rpc.model.response.IdentityInfoResponse;
import com.xiaoxiao.user.rpc.model.response.UserFollowingResponse;
import com.xiaoxiao.user.rpc.model.response.UserInfoResponse;
import com.xiaoxiao.user.rpc.model.vo.RpcUserVO;

import java.util.List;
import java.util.Map;

/**
 * 将使用的rpc接口都在这里在做一层处理，如远程调用出现异常的兜底处理可以在这里做
 * 以应对以一些异常情况，项目较小，可以都写到这里
 *
 * @author chenjunwei
 */
public interface RpcService {

    /**
     * 获取用户关注的人列表
     *
     * @param uid 用户id
     * @return 关注的人
     */
    UserFollowingResponse getUserFollowing(Long uid);

    /**
     * 批量根据id查询用户名称与头像
     */
    List<UserInfoResponse> getUserNameAndAvatar(List<Long> uid);

    /**
     * 批量根据匿名id查询匿名名称与头像
     */
    List<IdentityInfoResponse> getAnonymousNameAndAvatar(List<Long> identityId);

    /**
     * 发送邮件
     */
    SendMailResponse sendMail(SendMailRequest sendMailRequest);

    /**
     * 发送微信通知
     */
    boolean sendWeChatInform(SendWeChatInformRequest sendWeChatInformRequest);

    /**
     * 批量根据id查询用户名称与头像映射
     */
    Map<Long, UserInfoResponse> getUserNameAndAvatarMap(List<Long> uid);

    /**
     * 批量根据匿名id查询匿名名称与头像映射
     */
    Map<Long, IdentityInfoResponse> getAnonymousNameAndAvatarMap(List<Long> identityId);

    /**
     * 获取用户追随者列表
     *
     * @param uid 用户id
     * @return 关注的人
     */
    List<RpcUserVO> getUserFollowers(Long uid);

    /**
     * 获取创建者
     */
    String getUserCreateBy(Long uid);

    /**
     * 获取小程序accessToken
     */
    String getMiniAppAccessToken();
}
