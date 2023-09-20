package com.xiaoxiao.user.service;

import com.xiaoxiao.user.model.dto.UserDTO;
import com.xiaoxiao.user.model.enums.UserRelationEnum;
import com.xiaoxiao.user.model.vo.FollowUserVO;

import java.util.List;

public interface FollowService {

    /**
     * 查询用户关注的人列表
     *
     * @return
     */
    List<FollowUserVO> getUserFollowing(UserDTO userDTO);

    /**
     * 查询主身份的被关注列表
     *
     * @param userDTO
     * @return
     */
    List<FollowUserVO> getUserFollowers(UserDTO userDTO);

    /**
     * 新增关注
     *
     * @param userDTO
     * @return
     */
    Boolean follow(UserDTO userDTO);

    /**
     * 取消关注
     *
     * @param userDTO
     * @return
     */
    Boolean unfollow(UserDTO userDTO);

    /**
     * 获取用户关注的人的数量
     *
     * @param userDTO
     * @return
     */
    Long getUserFollowingCount(UserDTO userDTO);

    /**
     * 获取用户被关注的数量
     *
     * @param userDTO
     * @return
     */
    Long getFollowerCount(UserDTO userDTO);

    /**
     * 获取userDTO与当前用户间的关系
     */
    UserRelationEnum getUserRelationship(UserDTO userDTO);

}
