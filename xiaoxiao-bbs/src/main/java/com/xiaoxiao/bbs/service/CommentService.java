package com.xiaoxiao.bbs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.dto.PostCommentDTO;
import com.xiaoxiao.bbs.model.dto.PostCommentQuery;
import com.xiaoxiao.bbs.model.dto.PostSubCommentQuery;
import com.xiaoxiao.bbs.model.entity.Comment;
import com.xiaoxiao.bbs.model.vo.PostCommentVO;
import com.xiaoxiao.bbs.model.vo.PostSubCommentVO;
import com.xiaoxiao.common.user.CommonUser;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Junwei
 * @since 2022-10-29 04:18:48
 */
public interface CommentService {

    /**
     * 查询一级根评论
     */
    IPage<PostCommentVO> queryCommentPage(PostCommentQuery postCommentQuery);

    /**
     * 查询子评论
     */
    IPage<PostSubCommentVO> queryPostSubCommentPage(PostSubCommentQuery postSubCommentQuery);

    /**
     * 用户添加帖子一级评论
     */
    PostCommentVO addUserPostComment(PostCommentDTO postCommentDTO);

    /**
     * 添加帖子一级评论
     */
    PostCommentVO addPostComment(CommonUser commonUser, PostCommentDTO postCommentDTO);

    /**
     * 用户添加帖子二级评论
     */
    PostSubCommentVO addUserPostSubComment(PostCommentDTO postCommentDTO);

    /**
     * 添加帖子二级评论
     */
    PostSubCommentVO addPostSubComment(CommonUser commonUser, PostCommentDTO postCommentDTO);

    boolean deleteById(Long commentId);

    /**
     * 点赞评论
     */
    boolean likePost(Long commentId);

    /**
     * 取消点赞评论
     */
    boolean unlikePost(Long commentId);

    /**
     * 根据id查询评论列表
     */
    List<Comment> listCommentByIds(List<Long> commentIds);

    /**
     * 根据id查询评论
     */
    Comment getCommentById(Long id);

    /**
     * 根据帖子id查询m评论的用户id
     */
    List<Long> getCommentMarkerIds(Long postId);
}
