package com.xiaoxiao.user.rpc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.common.api.CommonResp;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.user.mapper.IdentityMapper;
import com.xiaoxiao.user.mapper.UserMapper;
import com.xiaoxiao.user.model.dto.EnergyDTO;
import com.xiaoxiao.user.model.dto.UserDTO;
import com.xiaoxiao.user.model.entity.Identity;
import com.xiaoxiao.user.model.entity.User;
import com.xiaoxiao.user.model.enums.UserRelationEnum;
import com.xiaoxiao.user.model.vo.FollowUserVO;
import com.xiaoxiao.user.rpc.model.request.EnergyRequest;
import com.xiaoxiao.user.rpc.model.request.FillOpenIdRequest;
import com.xiaoxiao.user.rpc.model.request.GetUserInfoRequest;
import com.xiaoxiao.user.rpc.model.request.UserSearchRequest;
import com.xiaoxiao.user.rpc.model.response.*;
import com.xiaoxiao.user.rpc.model.vo.RpcFollowUserVO;
import com.xiaoxiao.user.rpc.model.vo.RpcUserVO;
import com.xiaoxiao.user.rpc.service.RpcUserService;
import com.xiaoxiao.user.service.FollowService;
import com.xiaoxiao.user.service.UserConfService;
import com.xiaoxiao.user.service.UserEnergyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * RpcServiceImpl
 * </p>
 *
 * @author Junwei
 * @since 2023/2/24
 */
@RestController
@Slf4j
public class RpcUserServiceImpl implements RpcUserService {

    @Resource
    private FollowService followService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserEnergyService userEnergyService;

    @Resource
    private IdentityMapper identityMapper;

    @Resource
    private UserConfService userConfService;

    @PostMapping("/user/rpc/biz/getUserFollowing")
    public CommonResp<UserFollowingResponse> getUserFollowing(@RequestParam Long uid) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUid(uid);
        userDTO.setType(XiaoXiaoConstEnum.USER.getVal());
        List<FollowUserVO> userFollowing = followService.getUserFollowing(userDTO);
        List<RpcUserVO> rpcUserVOList = userFollowing.stream().map(item -> {
            RpcUserVO rpcUserVO = new RpcUserVO();
            BeanUtil.copyProperties(item, rpcUserVO);
            return rpcUserVO;
        }).collect(Collectors.toList());
        UserFollowingResponse userFollowingResponse = new UserFollowingResponse();
        userFollowingResponse.setRpcUserVOList(rpcUserVOList);
        return CommonResp.success(userFollowingResponse);
    }

    @GetMapping("/user/rpc/biz/search")
    public CommonResp<UserSearchResponse> searchUserByKeyword(UserSearchRequest userSearchRequest) {
        // 搜索用户
        Page<RpcFollowUserVO> userVOPage = userMapper.searchUserByKeyword(
                new Page<>(userSearchRequest.getCurrentPage(), userSearchRequest.getPageSize()),
                userSearchRequest.getKeyword(),
                userSearchRequest.getUserId()
        );
        // 搜索匿名身份
        Page<RpcFollowUserVO> identityVOPage = identityMapper.searchIdentityByKeyword(
                new Page<>(userSearchRequest.getCurrentPage(), userSearchRequest.getPageSize()),
                userSearchRequest.getKeyword(),
                userSearchRequest.getUserId()
        );
        // 查询当前用户关注的人
        UserDTO userDTO = new UserDTO();
        userDTO.setUid(userSearchRequest.getUserId());
        List<FollowUserVO> userFollowers = followService.getUserFollowing(userDTO);
        // 给搜索出的用户设置与当前用户的关注关系
        List<RpcFollowUserVO> rpcUserVOList = userVOPage.getRecords();
        for (RpcFollowUserVO rpcUserVO : rpcUserVOList) {
            rpcUserVO.setType(0);
            for (FollowUserVO userFollower : userFollowers) {
                if (rpcUserVO.getUserId().equals(userFollower.getUserId())) {
                    rpcUserVO.setRelType(userFollower.getRelType());
                    rpcUserVO.setRelName(userFollower.getRelName());
                } else {
                    rpcUserVO.setRelType(UserRelationEnum.NONE.getType());
                    rpcUserVO.setRelName(UserRelationEnum.NONE.getName());
                }
            }
        }
        // 给搜索出的匿名身份设置与当前用户的关注关系
        List<RpcFollowUserVO> rpcIdentityVOList = identityVOPage.getRecords();
        for (RpcFollowUserVO rpcIdentityVO : rpcIdentityVOList) {
            rpcIdentityVO.setType(1);
            for (FollowUserVO userFollower : userFollowers) {
                if (rpcIdentityVO.getIdentityId().equals(userFollower.getIdentityId())) {
                    rpcIdentityVO.setRelType(userFollower.getRelType());
                    rpcIdentityVO.setRelName(userFollower.getRelName());
                } else {
                    rpcIdentityVO.setRelType(UserRelationEnum.NONE.getType());
                    rpcIdentityVO.setRelName(UserRelationEnum.NONE.getName());
                }
            }
        }
        // 将用户和匿名身份合并
        rpcUserVOList.addAll(rpcIdentityVOList);
        // 转换为分页结果
        long rpcUserVOPageTotal = userVOPage.getTotal() + identityVOPage.getTotal();
        userVOPage.setRecords(rpcUserVOList);
        userVOPage.setTotal(rpcUserVOPageTotal);
        //返回
        UserSearchResponse userSearchResponse = new UserSearchResponse();
        userSearchResponse.setRpcFollowUserVOPage(userVOPage);
        return CommonResp.success(userSearchResponse);
    }

    @PostMapping("/user/rpc/getUserNameAndAvatar")
    public CommonResp<List<UserInfoResponse>> getUserNameAndAvatar(List<Long> uid) {
        if (CollectionUtil.isEmpty(uid)) {
            return CommonResp.success(new ArrayList<>());
        }
        List<User> userList = userMapper.selectBatchIds(uid.stream().distinct().collect(Collectors.toList()));
        List<UserInfoResponse> UserInfoResponseList = userList.stream().map(item -> {
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            userInfoResponse.setUid(item.getId());
            userInfoResponse.setName(item.getUserName());
            userInfoResponse.setAvatar(item.getAvatar());
            return userInfoResponse;
        }).collect(Collectors.toList());
        return CommonResp.success(UserInfoResponseList);
    }

    @PostMapping("/user/rpc/biz/getAnonymousNameAndAvatar")
    public CommonResp<List<IdentityInfoResponse>> getAnonymousNameAndAvatar(List<Long> identityId) {
        if (CollectionUtil.isEmpty(identityId)) {
            return CommonResp.success(new ArrayList<>());
        }
        List<Identity> identityList = identityMapper.selectBatchIds(identityId.stream().distinct().collect(Collectors.toList()));
        List<IdentityInfoResponse> identityInfoResponseList = identityList.stream().map(item -> {
            IdentityInfoResponse identityInfoResponse = new IdentityInfoResponse();
            identityInfoResponse.setIdentityId(item.getId());
            identityInfoResponse.setName(item.getName());
            identityInfoResponse.setAvatar(item.getAvatar());
            return identityInfoResponse;
        }).collect(Collectors.toList());
        return CommonResp.success(identityInfoResponseList);
    }

    @PostMapping("/user/rpc/fill_openid_by_unionid")
    public void fillOpenIdByUnionId(@RequestBody FillOpenIdRequest fillOpenIdRequest) {
        log.info("fillOpenIdByUnionId req:{}", fillOpenIdRequest.toString());
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUnionid, fillOpenIdRequest.getUnionId())
                .set(User::getServiceAccountOpenId, fillOpenIdRequest.getOpenId());
        userMapper.update(null, updateWrapper);
    }

    @PostMapping("/user/rpc/get_user_info")
    public CommonResp<GetUserInfoResponse> getUserInfo(@RequestBody GetUserInfoRequest getUserInfoRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("service_account_openid").eq("openid", getUserInfoRequest.getOpenId());
        User user = userMapper.selectOne(queryWrapper);
        return CommonResp.success(new GetUserInfoResponse(user.getServiceAccountOpenId()));
    }

    @PostMapping("/user/rpc/getUserServiceAccountOpenId")
    public String getUserServiceAccountOpenId(@RequestParam Long uid) {
        User user = userMapper.selectById(uid);
        return user.getServiceAccountOpenId();
    }

    @Override
    @PostMapping("/user/rpc/biz/getUserFollowers")
    public CommonResp<UserFollowerResponse> getUserFollowers(Long uid) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUid(uid);
        userDTO.setType(XiaoXiaoConstEnum.USER.getVal());
        List<FollowUserVO> userFollowing = followService.getUserFollowers(userDTO);
        List<RpcUserVO> rpcUserVOList = userFollowing.stream().map(item -> {
            RpcUserVO rpcUserVO = new RpcUserVO();
            BeanUtil.copyProperties(item, rpcUserVO);
            return rpcUserVO;
        }).collect(Collectors.toList());
        UserFollowerResponse userFollowerResponse = new UserFollowerResponse();
        userFollowerResponse.setRpcUserVOList(rpcUserVOList);
        return CommonResp.success(userFollowerResponse);
    }

    @PostMapping("/user/rpc/getUserCreateBy")
    public String getUserCreateBy(@RequestParam Long uid) {
        User user = userMapper.selectById(uid);
        return user.getCreateBy();
    }

    @PostMapping("/user/rpc/getActivateConfKeyUserIds")
    public List<Long> getActivateConfKeyUserIds(@RequestParam String key) {
        return userConfService.getActivateConfKeyUserIds(key);
    }

    @PostMapping("/user/rpc/calculateEnergy")
    public CommonResp<Boolean> calculateEnergyRpc(@RequestBody EnergyRequest energyRequest) {
        EnergyDTO energyDTO = new EnergyDTO();
        BeanUtil.copyProperties(energyRequest, energyDTO);
        return CommonResp.success(userEnergyService.calculateEnergy(energyDTO));
    }
}
