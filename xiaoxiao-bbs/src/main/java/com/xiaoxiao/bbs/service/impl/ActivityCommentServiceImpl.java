package com.xiaoxiao.bbs.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxiao.bbs.mapper.ActivityCommentMapper;
import com.xiaoxiao.bbs.model.dto.ActCommentDTO;
import com.xiaoxiao.bbs.model.dto.ActCommentQuery;
import com.xiaoxiao.bbs.model.dto.ActLikeDTO;
import com.xiaoxiao.bbs.model.entity.ActivityComment;
import com.xiaoxiao.bbs.model.vo.ActCommentVO;
import com.xiaoxiao.bbs.service.ActivityCommentService;
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
public class ActivityCommentServiceImpl extends ServiceImpl<ActivityCommentMapper, ActivityComment> implements ActivityCommentService {

    @Resource
    ActivityCommentMapper activityCommentMapper;

    @Override
    public IPage<ActCommentVO> actCommentQuery(ActCommentQuery actCommentQuery) {
        ActCommentQuery.checkIsValid(actCommentQuery);

        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);

        actCommentQuery.setUserId(commonUser.getUid());
        Page<ActCommentVO> activityCommentPage = new Page<>(actCommentQuery.getCurrentPage(), actCommentQuery.getPageSize());

        return activityCommentMapper.selectCommentsLikedByUser(activityCommentPage, actCommentQuery);

    }

    @Override
    public boolean saveActComment(ActCommentDTO actCommentDTO) {
        ActCommentDTO.checkIsValid(actCommentDTO);

        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);

        ActivityComment activityComment = ActCommentDTO.prepareActComment(actCommentDTO);
        activityComment.setUserId(commonUser.getUid());
        activityComment.setCreatorName(commonUser.getUsername());
        log.info("发布活动评论 {}", activityComment);
        return this.save(activityComment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean like(ActLikeDTO actLikeDTO) {
        ActLikeDTO.checkIsValid(actLikeDTO);
        AssertUtil.isTrue(activityCommentMapper.checkCommentExist(actLikeDTO.getCommentId()), "该评论不存在");

        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);
        //查询是否已点过赞
        if (activityCommentMapper.findLike(commonUser.getUid(), actLikeDTO.getCommentId())) {
            throw new ApiException(StatusCode.FAILED, "请勿重复点赞");
        }
        activityCommentMapper.plusLike(actLikeDTO.getCommentId());
        return (activityCommentMapper.addLike(commonUser.getUid(), actLikeDTO.getCommentId(), 1)) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean unlike(ActLikeDTO actLikeDTO) {
        ActLikeDTO.checkIsValid(actLikeDTO);
        AssertUtil.isTrue(activityCommentMapper.checkCommentExist(actLikeDTO.getCommentId()), "该评论不存在");

        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);
        //查询是否已点过赞,若未点过,则直接返回成功
        if (!activityCommentMapper.findLike(commonUser.getUid(), actLikeDTO.getCommentId())) {
            return true;
        }
        activityCommentMapper.minusLike(actLikeDTO.getCommentId());
        return (activityCommentMapper.deleteLike(commonUser.getUid(), actLikeDTO.getCommentId(), 1)) > 0;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteById(Long id) {
        AssertUtil.isTrue(activityCommentMapper.checkCommentExist(id), "该评论不存在");
        this.activityCommentMapper.deleteSubComment(id);
        return this.removeById(id);
    }

}
