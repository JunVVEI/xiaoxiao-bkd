package com.xiaoxiao.bbs.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.dto.PostCommentDTO;
import com.xiaoxiao.bbs.model.dto.PostCommentQuery;
import com.xiaoxiao.bbs.model.dto.PostSubCommentQuery;
import com.xiaoxiao.bbs.model.vo.PostCommentVO;
import com.xiaoxiao.bbs.model.vo.PostSubCommentVO;
import com.xiaoxiao.bbs.service.CommentService;
import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Junwei
 * @since 2022-10-29 04:18:48
 */
@RestController
@RequestMapping("/bbs/postComment")
public class PostCommentController {
    @Resource
    private CommentService commentService;

    /**
     * 查询某帖子的一级根评论
     */
    @PostMapping("queryPostCommentPage")
    @XiaoXiaoResponseBody
    public IPage<PostCommentVO> queryPostCommentPage(@RequestBody PostCommentQuery postCommentQuery) {
        return commentService.queryCommentPage(postCommentQuery);
    }

    /**
     * 查询某帖子的子评论
     */
    @PostMapping("queryPostSubCommentPage")
    @XiaoXiaoResponseBody
    public IPage<PostSubCommentVO> queryPostSubCommentPage(@RequestBody PostSubCommentQuery postSubCommentQuery) {
        return commentService.queryPostSubCommentPage(postSubCommentQuery);
    }

    /**
     * 添加根评论
     */
    @PostMapping("addPostComment")
    @XiaoXiaoResponseBody
    public PostCommentVO addPostComment(@RequestBody PostCommentDTO postCommentDTO) {
        return commentService.addUserPostComment(postCommentDTO);
    }

    /**
     * 添加子评论
     */
    @PostMapping("addPostSubComment")
    @XiaoXiaoResponseBody
    public PostSubCommentVO addPostSubComment(@RequestBody PostCommentDTO postCommentDTO) {
        return commentService.addUserPostSubComment(postCommentDTO);
    }

    /**
     * 删除评论
     */
    @PostMapping("del")
    @XiaoXiaoResponseBody
    public Boolean deleteById(Long commentId) {
        return commentService.deleteById(commentId);
    }

    /**
     * 点赞帖子
     */
    @PostMapping("/like")
    @XiaoXiaoResponseBody
    public boolean likePost(Long commentId) {
        return commentService.likePost(commentId);
    }

    /**
     * 取消点赞帖子
     */
    @PostMapping("/unlike")
    @XiaoXiaoResponseBody
    public boolean unlikePost(Long commentId) {
        return commentService.unlikePost(commentId);
    }
}

