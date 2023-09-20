package com.xiaoxiao.bbs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.dto.ActCommentQuery;
import com.xiaoxiao.bbs.model.entity.ActivityComment;
import com.xiaoxiao.bbs.model.vo.ActCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;



/**
 * @author chenhaowen
 */
@Mapper
public interface ActivityCommentMapper extends BaseMapper<ActivityComment> {

  /**
   * 删除子评论
   */
  void deleteSubComment(Long id);

  /**
   * 点赞量+1
   */
  int plusLike(Long commentId);

  /**
   * 点赞量-1
   */
  int minusLike(Long commentId);

  /**
   * 添加点赞记录
   */
  int addLike(Long userId,Long commentId,int status);

  /**
   * 删除点赞记录
   */
  int deleteLike(Long userId,Long commentId,int status);

  /**
   * 查询是否点过赞
   */
  boolean findLike(Long userId,Long commentId);

  /**
   * 判断评论是否存在
   */
  boolean checkCommentExist(Long id);

  /**
   * 返回评论列表
   */
  IPage<ActCommentVO> selectCommentsLikedByUser(IPage<ActCommentVO> page, @Param("actCommentQuery") ActCommentQuery actCommentQuery);

}
