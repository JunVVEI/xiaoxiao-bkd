package com.xiaoxiao.user.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.user.mapper.IdentityMapper;
import com.xiaoxiao.user.mapper.UserMapper;
import com.xiaoxiao.user.model.dto.UserDTO;
import com.xiaoxiao.user.model.entity.Identity;
import com.xiaoxiao.user.model.entity.User;
import com.xiaoxiao.user.model.enums.UserRelationEnum;
import com.xiaoxiao.user.model.vo.*;
import com.xiaoxiao.user.service.FollowService;
import com.xiaoxiao.user.service.IdentityService;
import com.xiaoxiao.user.service.RpcService;
import com.xiaoxiao.user.service.UserBizService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * UserBizServiceImpl
 * </p>
 *
 * @author Junwei
 * @since 2023/2/14
 */
@Service
public class UserBizServiceImpl extends ServiceImpl<UserMapper, User> implements UserBizService {

    @Resource
    FollowService followService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private IdentityMapper identityMapper;

    @Resource
    private IdentityService identityService;

    @Resource
    private RpcService rpcService;

    @Override
    public UserPublicInfoVO getUserPublicInfo(UserDTO userDTO) {
        UserDTO.checkIsValid(userDTO);

        UserPublicInfoVO userPublicInfoVO = new UserPublicInfoVO();

        if (Objects.equals(userDTO.getType(), XiaoXiaoConstEnum.USER.getVal())) {
            User user = userMapper.selectById(userDTO.getUid());
            userPublicInfoVO.setName(user.getUserName());
            userPublicInfoVO.setAvatar(user.getAvatar());
        } else if (Objects.equals(userDTO.getType(), XiaoXiaoConstEnum.ANONYMOUS_IDENTITY.getVal())) {
            Identity identity = identityMapper.selectById(userDTO.getIdentityId());
            userPublicInfoVO.setName(identity.getName());
            userPublicInfoVO.setAvatar(identity.getAvatar());
        }

        UserRelationEnum userRelationship = followService.getUserRelationship(userDTO);
        userPublicInfoVO.setRelType(userRelationship.getType());
        userPublicInfoVO.setRelName(userRelationship.getName());
        userPublicInfoVO.setIsFollowing(userRelationship.getIsFollowing());

        userPublicInfoVO.setFollowerCount(followService.getFollowerCount(userDTO));
        if (Objects.equals(userDTO.getType(), XiaoXiaoConstEnum.USER.getVal())) {
            userPublicInfoVO.setFollowingCount(followService.getUserFollowingCount(userDTO));
        }
        return userPublicInfoVO;
    }

    @Override
    public CurrentUserInfoVO getCurrentUserInfo() {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        CurrentUserInfoVO currentUserInfoVO = new CurrentUserInfoVO();
        User user = userMapper.selectById(currentUser.getUid());
        BeanUtils.copyProperties(user, currentUserInfoVO);

        UserDTO userDTO = new UserDTO();
        userDTO.setType(XiaoXiaoConstEnum.USER.getVal());
        userDTO.setUid(user.getId());
        currentUserInfoVO.setFollowerCount(Math.toIntExact(followService.getFollowerCount(userDTO)));
        currentUserInfoVO.setFollowingCount(Math.toIntExact(followService.getUserFollowingCount(userDTO)));
        currentUserInfoVO.setPostCount(rpcService.getUserPostCount(currentUser.getUid()));
        currentUserInfoVO.setLikeCount(rpcService.getUserLikeCount(currentUser.getUid()));

        return currentUserInfoVO;
    }

    @Override
    public UserPostOptionVO getUserPostOptions() {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);

        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getId, currentUser.getUid())
                        .eq(User::getIsDelete, XiaoXiaoConstEnum.UN_DELETE)
        );

        List<UserPostOptionVO.PostOption> postOptions = new ArrayList<>();
        UserPostOptionVO.PostOption postOption = new UserPostOptionVO.PostOption();
        postOption.setType(XiaoXiaoConstEnum.USER.getVal());
        postOption.setValue(user.getUserName());
        postOption.setAvatar(user.getAvatar());
        postOption.setUid(user.getId());
        postOptions.add(postOption);

        List<IdentityVO> identityVOS = identityService.listCurrentUserIdentities();
        identityVOS.forEach(item -> {
            UserPostOptionVO.PostOption postIdentityOption = new UserPostOptionVO.PostOption();
            postIdentityOption.setType(XiaoXiaoConstEnum.ANONYMOUS_IDENTITY.getVal());
            postIdentityOption.setValue(item.getName() + "(匿名)");
            postIdentityOption.setAvatar(item.getAvatar());
            postIdentityOption.setIdentityId(item.getIdentityId());
            postOptions.add(postIdentityOption);
        });

        UserPostOptionVO userPostOptionVO = new UserPostOptionVO();
        userPostOptionVO.setPostOptionList(postOptions);
        return userPostOptionVO;
    }
}
