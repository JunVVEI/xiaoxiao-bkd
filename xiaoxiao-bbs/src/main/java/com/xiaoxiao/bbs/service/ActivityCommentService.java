package com.xiaoxiao.bbs.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.bbs.model.dto.ActCommentDTO;
import com.xiaoxiao.bbs.model.dto.ActCommentQuery;
import com.xiaoxiao.bbs.model.dto.ActLikeDTO;
import com.xiaoxiao.bbs.model.entity.ActivityComment;
import com.xiaoxiao.bbs.model.vo.ActCommentVO;




/**
 * @author chenhaowen
 */
public interface ActivityCommentService extends IService<ActivityComment> {

    /**
     * 查询
     *
     * @param actCommentQuery 查询参数
     * @return 查询结果
     */
      IPage<ActCommentVO> actCommentQuery(ActCommentQuery actCommentQuery);

    /**
     * 发布评论
     *
     * @param actCommentDTO 活动评论id
     * @return 操作结果
     */
    boolean saveActComment(ActCommentDTO actCommentDTO);

    /**
     * 点赞活动评论
     *
     * @param actLikeDTO 传入参数
     * @return 操作结果
     */
    boolean like(ActLikeDTO actLikeDTO);

    /**
     * 取消点赞活动评论
     *
     * @param actLikeDTO  传入参数
     * @return 操作结果
     */
    boolean unlike(ActLikeDTO actLikeDTO);

    /**
     * 删除评论
     *
     * @param id 活动评论id
     * @return 操作结果
     */
    boolean deleteById(Long id);

}
