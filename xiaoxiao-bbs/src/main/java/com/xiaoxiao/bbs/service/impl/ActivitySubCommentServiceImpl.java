package com.xiaoxiao.bbs.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxiao.bbs.mapper.ActivitySubCommentMapper;
import com.xiaoxiao.bbs.model.dto.ActLikeDTO;
import com.xiaoxiao.bbs.model.dto.ActSubCommentDTO;
import com.xiaoxiao.bbs.model.dto.ActSubCommentQuery;
import com.xiaoxiao.bbs.model.entity.ActivitySubComment;
import com.xiaoxiao.bbs.model.vo.ActSubCommentVO;
import com.xiaoxiao.bbs.service.ActivitySubCommentService;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author chenhaowen
 */
@Service
@Slf4j
public class ActivitySubCommentServiceImpl extends ServiceImpl<ActivitySubCommentMapper, ActivitySubComment> implements ActivitySubCommentService {

    @Resource
    ActivitySubCommentMapper activitySubCommentMapper;

    @Override
    public IPage<ActSubCommentVO> actSubCommentQuery(ActSubCommentQuery actSubCommentQuery) {
        ActSubCommentQuery.checkIsValid(actSubCommentQuery);

        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);

        actSubCommentQuery.setUserId(commonUser.getUid());
        Page<ActSubCommentVO> activitySubCommentPage = new Page<>(actSubCommentQuery.getCurrentPage(), actSubCommentQuery.getPageSize());

        return activitySubCommentMapper.selectCommentsLikedByUser(activitySubCommentPage, actSubCommentQuery);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean like(ActLikeDTO actLikeDTO) {
        ActLikeDTO.checkIsValid(actLikeDTO);

        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);

        AssertUtil.isTrue(activitySubCommentMapper.checkCommentExist(actLikeDTO.getCommentId()), "该评论不存在");
        //查询是否已点过赞
        if (activitySubCommentMapper.findLike(commonUser.getUid(), actLikeDTO.getCommentId())) {
            throw new ApiException(StatusCode.FAILED, "请勿重复点赞");
        }
        activitySubCommentMapper.plusLike(actLikeDTO.getCommentId());
        return (activitySubCommentMapper.addLike(commonUser.getUid(), actLikeDTO.getCommentId(), 2))>0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean unlike(ActLikeDTO actLikeDTO) {
        ActLikeDTO.checkIsValid(actLikeDTO);
        AssertUtil.isTrue(activitySubCommentMapper.checkCommentExist(actLikeDTO.getCommentId()), "该评论不存在");

        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);

        //查询是否已点过赞,若未点过,则直接返回成功
        if (!activitySubCommentMapper.findLike(commonUser.getUid(), actLikeDTO.getCommentId())) {
            return true;
        }
        activitySubCommentMapper.minusLike(actLikeDTO.getCommentId());
        return (activitySubCommentMapper.deleteLike(commonUser.getUid(), actLikeDTO.getCommentId(), 2))>0;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(long commentId) {
        AssertUtil.isTrue(activitySubCommentMapper.checkCommentExist(commentId), "该评论不存在");
        activitySubCommentMapper.minusComment(activitySubCommentMapper.findId(commentId));
        return this.removeById(commentId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(ActSubCommentDTO actSubCommentDTO) {
        ActSubCommentDTO.checkIsValid(actSubCommentDTO);

        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);

        activitySubCommentMapper.plusComment(actSubCommentDTO.getCommentId());
        ActivitySubComment activitySubComment = ActSubCommentDTO.prepareActSubComment(actSubCommentDTO);
        activitySubComment.setUserId(commonUser.getUid());
        activitySubComment.setCreatorName(commonUser.getUsername());
        log.info("发布活动子评论 {}", activitySubComment);
        return this.save(activitySubComment);
    }
}
