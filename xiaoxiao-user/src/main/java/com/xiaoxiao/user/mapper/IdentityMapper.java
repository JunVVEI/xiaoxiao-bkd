package com.xiaoxiao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.user.model.entity.Identity;
import com.xiaoxiao.user.rpc.model.vo.RpcFollowUserVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户身份表 Mapper 接口
 * </p>
 *
 * @author junwei
 * @since 2022-11-20 03:36:59
 */
@Mapper
public interface IdentityMapper extends BaseMapper<Identity> {

    /**
     * 根据关键词搜索匿名身份
     *
     * @param page
     * @param keyword
     * @param userId
     * @return
     */
    Page<RpcFollowUserVO> searchIdentityByKeyword(Page<RpcFollowUserVO> page, String keyword, Long userId);
}
