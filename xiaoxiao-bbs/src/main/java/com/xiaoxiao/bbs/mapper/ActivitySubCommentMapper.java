package com.xiaoxiao.bbs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.dto.ActSubCommentQuery;
import com.xiaoxiao.bbs.model.entity.ActivitySubComment;
import com.xiaoxiao.bbs.model.vo.ActCommentVO;
import com.xiaoxiao.bbs.model.vo.ActSubCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author chenhaowen
 */
@Mapper
public interface ActivitySubCommentMapper extends BaseMapper<ActivitySubComment> {

    /**
     * 点赞量+1
     */
    int plusLike(Long commentId);

    /**
     * 点赞量-1
     */
    int minusLike(Long commentId);

    /**
     * 父评论回复数-1
     */
    int minusComment(Long id);

    /**
     * 父评论回复数+1
     */
    int plusComment(Long id);

    /**
     * 传入子评论id，返回父评论id
     */
    long findId(long id);

    /**
     * 查询是否点赞
     */
    boolean findLike(Long userId, Long commentId);

    /**
     * 添加点赞记录
     */
    int addLike(Long userId, Long commentId, int status);

    /**
     * 删除点赞记录
     */
    int deleteLike(Long userId, Long commentId, int status);

    /**
     * 判断评论是否存在
     */
    boolean checkCommentExist(Long id);

    /**
     * 返回评论列表
     */
    IPage<ActSubCommentVO> selectCommentsLikedByUser(IPage<ActSubCommentVO> page, @Param("actSubCommentQuery") ActSubCommentQuery actSubCommentQuery);

}
