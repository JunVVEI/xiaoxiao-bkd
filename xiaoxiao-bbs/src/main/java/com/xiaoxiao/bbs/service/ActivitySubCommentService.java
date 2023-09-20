package com.xiaoxiao.bbs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.bbs.model.dto.ActCommentQuery;
import com.xiaoxiao.bbs.model.dto.ActLikeDTO;
import com.xiaoxiao.bbs.model.dto.ActSubCommentDTO;
import com.xiaoxiao.bbs.model.dto.ActSubCommentQuery;
import com.xiaoxiao.bbs.model.entity.ActivityComment;
import com.xiaoxiao.bbs.model.entity.ActivitySubComment;
import com.xiaoxiao.bbs.model.vo.ActCommentVO;
import com.xiaoxiao.bbs.model.vo.ActSubCommentVO;

/**
 * @author chenhaowen
 */
public interface ActivitySubCommentService extends IService<ActivitySubComment> {

    /**
     * 查询
     *
     * @param actSubCommentQuery 查询参数
     * @return 查询结果
     */
    IPage<ActSubCommentVO> actSubCommentQuery(ActSubCommentQuery actSubCommentQuery);

    /**
     * 点赞活动子评论
     *
     * @param actLikeDTO 传入参数
     * @return 操作结果
     */
    boolean like(ActLikeDTO actLikeDTO);

    /**
     * 取消点赞活动子评论
     *
     * @param actLikeDTO  传入参数
     * @return 操作结果
     */
    boolean unlike(ActLikeDTO actLikeDTO);

    /**
     * 删除活动子评论
     *
     * @param commentId 子评论id
     * @return 操作结果
     */
    boolean delete(long commentId);

    /**
     * 发布活动子评论
     *
     * @param actSubCommentDTO 子评论DTO
     * @return 操作结果
     */
    boolean add(ActSubCommentDTO actSubCommentDTO);

}
