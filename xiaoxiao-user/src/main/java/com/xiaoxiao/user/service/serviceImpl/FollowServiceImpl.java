package com.xiaoxiao.user.service.serviceImpl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.user.mapper.FollowMapper;
import com.xiaoxiao.user.mapper.IdentityMapper;
import com.xiaoxiao.user.model.dto.UserDTO;
import com.xiaoxiao.user.model.entity.Follow;
import com.xiaoxiao.user.model.entity.Identity;
import com.xiaoxiao.user.model.enums.UserRelationEnum;
import com.xiaoxiao.user.model.vo.FollowUserVO;
import com.xiaoxiao.user.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * FollowServiceImpl
 * </p>
 *
 * @author Junwei
 * @since 2023/2/14
 */
@Service
@Slf4j
public class FollowServiceImpl implements FollowService {

    @Resource
    private FollowMapper followMapper;

    @Resource
    private IdentityMapper identityMapper;

    @Override
    public List<FollowUserVO> getUserFollowing(UserDTO userDTO) {
        Long uid;
        if (Objects.nonNull(userDTO.getUid())) {
            uid = userDTO.getUid();
        } else {
            CommonUser currentUser = UserContext.getCurrentUser();
            CommonUser.assertIsLogInUser(currentUser);
            uid = currentUser.getUid();
        }

        // 获得关注列表的follow_id及type
        List<FollowUserVO> followList = followMapper.listUserFollowing(uid);

        Long identityId;
        Long currentUserId;
        CommonUser currentUser = UserContext.getCurrentUser();
        if (CommonUser.isLogInUser(currentUser)) {
            currentUserId = currentUser.getUid();

            identityId = CollectionUtil.getFirst(
                    identityMapper.selectList(
                            new LambdaQueryWrapper<Identity>()
                                    .eq(Identity::getUid, currentUserId)
                                    .eq(Identity::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
                    )
            ).getId();
        } else {
            identityId = null;
            currentUserId = null;
        }

        return followList.stream()
                .filter(item -> Objects.nonNull(item.getUserId()) || Objects.nonNull(item.getIdentityId()))
                .peek(item -> {
                    if (item.getUserId() != null && item.getIdentityId() == null) {
                        UserDTO userDTO1 = new UserDTO();
                        userDTO1.setUid(item.getUserId());
                        userDTO1.setType(XiaoXiaoConstEnum.USER.getVal());
                        UserRelationEnum userRelationship = getUserRelationship(userDTO1);
                        item.setRelType(userRelationship.getType());
                        item.setRelName(userRelationship.getName());
                        item.setIsFollowing(userRelationship.getIsFollowing());

                        if (CommonUser.isLogInUser(currentUser)) {
                            item.setIsCurrentUserSelf(Objects.equals(currentUserId, item.getUserId()));
                        }
                    } else {
                        item.setRelType(UserRelationEnum.FOLLOWING.getType());
                        item.setRelName(UserRelationEnum.FOLLOWING.getName());

                        if (CommonUser.isLogInUser(currentUser)) {
                            item.setIsCurrentUserSelf(Objects.equals(identityId, item.getIdentityId()));
                        }
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FollowUserVO> getUserFollowers(UserDTO userDTO) {
        List<FollowUserVO> followUserVOS;
        Long currentUserId;
        CommonUser currentUser = UserContext.getCurrentUser();
        if (CommonUser.isLogInUser(currentUser)) {
            currentUserId = currentUser.getUid();
        } else {
            currentUserId = null;
        }
        if (Objects.nonNull(userDTO) && Objects.nonNull(userDTO.getIdentityId())) {
            followUserVOS = followMapper.listIdentityFollower(userDTO.getIdentityId());

            return followUserVOS.stream()
                    .filter(item -> Objects.nonNull(item.getUserId()) || Objects.nonNull(item.getIdentityId()))
                    .peek(item -> {
                        item.setRelType(UserRelationEnum.FOLLOWED.getType());
                        item.setRelName(UserRelationEnum.FOLLOWED.getName());
                        item.setIsFollowing(false);

                        if (CommonUser.isLogInUser(currentUser)) {
                            item.setIsCurrentUserSelf(Objects.equals(item.getUserId(), currentUserId));
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            Long uid;
            if (Objects.nonNull(userDTO) && Objects.nonNull(userDTO.getUid())) {
                uid = userDTO.getUid();
            } else {
                CommonUser.assertIsLogInUser(currentUser);
                uid = currentUser.getUid();
            }
            followUserVOS = followMapper.listUserFollower(uid);

            return followUserVOS.stream()
                    .filter(item -> Objects.nonNull(item.getUserId()) || Objects.nonNull(item.getIdentityId()))
                    .peek(item -> {
                        UserDTO userDTO1 = new UserDTO();
                        userDTO1.setUid(item.getUserId());
                        userDTO1.setType(XiaoXiaoConstEnum.USER.getVal());
                        UserRelationEnum userRelationship = getUserRelationship(userDTO1);
                        item.setRelType(userRelationship.getType());
                        item.setRelName(userRelationship.getName());
                        item.setIsFollowing(userRelationship.getIsFollowing());

                        if (CommonUser.isLogInUser(currentUser)) {
                            item.setIsCurrentUserSelf(Objects.equals(item.getUserId(), currentUserId));
                        }
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Boolean follow(UserDTO userDTO) {
        UserDTO.checkIsValid(userDTO);

        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        Follow follow = new Follow();
        follow.setSelfId(uid);
        follow.setStatus(XiaoXiaoConstEnum.UN_DELETE.getVal());
        if (Objects.nonNull(userDTO.getIdentityId())) {
            follow.setFollowId(userDTO.getIdentityId());
            follow.setType(XiaoXiaoConstEnum.ANONYMOUS_IDENTITY.getVal());
        } else if (Objects.nonNull(userDTO.getUid())) {
            follow.setFollowId(userDTO.getUid());
            follow.setType(XiaoXiaoConstEnum.USER.getVal());
        } else {
            return false;
        }

        // 查询是否已关注如果如果没关注则新增关注
        Follow one = followMapper.selectOne(new QueryWrapper<>(follow));
        if (Objects.isNull(one)) {
            followMapper.insert(follow);
        }
        return true;
    }

    @Override
    public Boolean unfollow(UserDTO userDTO) {
        UserDTO.checkIsValid(userDTO);

        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        Long followId;
        int type;
        if (Objects.nonNull(userDTO.getIdentityId())) {
            followId = userDTO.getIdentityId();
            type = XiaoXiaoConstEnum.ANONYMOUS_IDENTITY.getVal();
        } else if (Objects.nonNull(userDTO.getUid())) {
            followId = userDTO.getUid();
            type = XiaoXiaoConstEnum.USER.getVal();
        } else {
            return false;
        }

        Wrapper<Follow> updateWrapper = new UpdateWrapper<Follow>()
                .lambda()
                .eq(Follow::getSelfId, uid)
                .eq(Follow::getFollowId, followId)
                .eq(Follow::getType, type)
                .eq(Follow::getStatus, XiaoXiaoConstEnum.UN_DELETE.getVal())
                .set(Follow::getStatus, XiaoXiaoConstEnum.DELETED.getVal());
        followMapper.update(null, updateWrapper);
        return true;
    }

    @Override
    public Long getUserFollowingCount(UserDTO userDTO) {
        Long count = 0L;
        Long selfId;
        if (Objects.nonNull(userDTO.getUid())) {
            selfId = userDTO.getUid();
        } else {
            CommonUser currentUser = UserContext.getCurrentUser();
            selfId = currentUser.getUid();
            if (Objects.isNull(selfId)) {
                return count;
            }
        }

        count = followMapper.selectCount(
                new LambdaQueryWrapper<Follow>()
                        .eq(Follow::getSelfId, selfId)
                        .eq(Follow::getStatus, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );
        return count;
    }

    @Override
    public Long getFollowerCount(UserDTO userDTO) {
        Long count = 0L;
        Long followId;
        int type;

        if (Objects.nonNull(userDTO.getIdentityId())) {
            followId = userDTO.getIdentityId();
            type = XiaoXiaoConstEnum.ANONYMOUS_IDENTITY.getVal();
        } else if (Objects.nonNull(userDTO.getUid())) {
            followId = userDTO.getUid();
            type = XiaoXiaoConstEnum.USER.getVal();
        } else {
            CommonUser currentUser = UserContext.getCurrentUser();
            followId = currentUser.getUid();
            type = XiaoXiaoConstEnum.USER.getVal();
            if (Objects.isNull(followId)) {
                return count;
            }
        }

        count = followMapper.selectCount(
                new LambdaQueryWrapper<Follow>()
                        .eq(Follow::getFollowId, followId)
                        .eq(Follow::getType, type)
                        .eq(Follow::getStatus, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );
        return count;
    }

    @Override
    public UserRelationEnum getUserRelationship(UserDTO userDTO) {
        UserDTO.checkIsValid(userDTO);

        CommonUser currentUser = UserContext.getCurrentUser();
        if (!CommonUser.isLogInUser(currentUser)) {
            return UserRelationEnum.NONE;
        }
        Long uid = currentUser.getUid();


        if (Objects.nonNull(userDTO.getIdentityId())) {
            Long identityId = userDTO.getIdentityId();

            boolean beFollowing = followMapper.exists(
                    new LambdaQueryWrapper<Follow>()
                            .eq(Follow::getSelfId, uid)
                            .eq(Follow::getFollowId, identityId)
                            .eq(Follow::getType, XiaoXiaoConstEnum.ANONYMOUS_IDENTITY.getVal())
                            .eq(Follow::getStatus, XiaoXiaoConstEnum.UN_DELETE.getVal())
            );

            if (beFollowing) {
                return UserRelationEnum.FOLLOWING;
            } else {
                return UserRelationEnum.NONE;
            }

        } else if (Objects.nonNull(userDTO.getUid())) {
            Long otherUid = userDTO.getUid();

            boolean beFollowing = followMapper.exists(
                    new LambdaQueryWrapper<Follow>()
                            .eq(Follow::getSelfId, uid)
                            .eq(Follow::getFollowId, otherUid)
                            .eq(Follow::getType, XiaoXiaoConstEnum.USER.getVal())
                            .eq(Follow::getStatus, XiaoXiaoConstEnum.UN_DELETE.getVal())
            );

            boolean beFollowed = followMapper.exists(
                    new LambdaQueryWrapper<Follow>()
                            .eq(Follow::getSelfId, otherUid)
                            .eq(Follow::getFollowId, uid)
                            .eq(Follow::getType, XiaoXiaoConstEnum.USER.getVal())
                            .eq(Follow::getStatus, XiaoXiaoConstEnum.UN_DELETE.getVal())
            );

            if (beFollowing && beFollowed) {
                return UserRelationEnum.MUTUAL_FOLLOWING;
            } else if (beFollowing && !beFollowed) {
                return UserRelationEnum.FOLLOWING;
            } else if (!beFollowing && beFollowed) {
                return UserRelationEnum.FOLLOWED;
            } else {
                return UserRelationEnum.NONE;
            }
        }

        throw new ApiException(StatusCode.VALIDATE_FAILED);
    }


}
