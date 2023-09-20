package com.xiaoxiao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.user.model.entity.User;
import com.xiaoxiao.user.rpc.model.vo.RpcFollowUserVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zjw
 * @since 2022-11-19 11:11:38
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据关键词搜索用户
     *
     * @param page
     * @param keyword
     * @param userId
     * @return
     */
    Page<RpcFollowUserVO> searchUserByKeyword(Page<RpcFollowUserVO> page, String keyword, Long userId);
}
