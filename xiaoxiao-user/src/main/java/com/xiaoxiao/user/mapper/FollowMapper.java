package com.xiaoxiao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxiao.user.model.entity.Follow;
import com.xiaoxiao.user.model.vo.FollowUserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 关注表接口
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

    /**
     * 用户关注的人列表
     */
    List<FollowUserVO> listUserFollowing(Long userId);

    /**
     * 关注真实身份的人列表
     */
    List<FollowUserVO> listUserFollower(Long userId);

    /**
     * 关注匿名身份的人列表
     */
    List<FollowUserVO> listIdentityFollower(Long identityId);

}
