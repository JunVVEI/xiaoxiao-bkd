package com.xiaoxiao.bbs.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.bbs.model.dto.PostListQuery;
import com.xiaoxiao.bbs.model.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxiao.bbs.model.dto.PostListQuery;
import com.xiaoxiao.bbs.model.dto.PostQuery;
import com.xiaoxiao.bbs.model.entity.Post;
import com.xiaoxiao.bbs.model.vo.PostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Junwei
 * @since 2022-10-27 11:23:00
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    int updateViewCount(Long id);

    int plusShareCount(Long id);

    Page<PostVO> queryPostListByKeyword(Page<PostVO> page, String keyword, Long userId);

    @Select("SELECT SUM(like_count) FROM bbs_post WHERE user_id = #{uid}")
    Integer sumUserPostLikeCount(@Param("uid") Long uid);
}
