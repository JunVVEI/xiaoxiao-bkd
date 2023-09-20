package com.xiaoxiao.bbs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxiao.bbs.model.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Junwei
 * @since 2022-10-29 04:18:48
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
