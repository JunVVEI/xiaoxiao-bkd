package com.xiaoxiao.bbs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxiao.bbs.constants.enums.LikeFlagEnum;
import com.xiaoxiao.bbs.constants.enums.LikeTypeEnum;
import com.xiaoxiao.bbs.mapper.ActivityMapper;
import com.xiaoxiao.bbs.model.bo.LikeBO;
import com.xiaoxiao.bbs.model.vo.ActivityVO;
import com.xiaoxiao.bbs.model.dto.ActivityDTO;
import com.xiaoxiao.bbs.model.dto.ActivityQuery;
import com.xiaoxiao.bbs.model.entity.Activity;
import com.xiaoxiao.bbs.service.ActivityService;
import com.xiaoxiao.bbs.service.LikeService;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mayunfei
 * @since 2023-02-07 15:44:51
 */
@Service
@Slf4j
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Resource
    ActivityMapper activityMapper;

    @Resource
    LikeService likeService;

    @Override
    public boolean saveActivity(ActivityDTO activityDTO) {
        ActivityDTO.checkIsValid(activityDTO);
        log.info("新增活动 {}", activityDTO);
        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);

        Activity activity = ActivityDTO.prepareActivity(activityDTO);
        activity.setUserId(commonUser.getUid());
        activity.setCreatorName(commonUser.getUsername());

        return this.save(activity);
    }

    @Override
    public Page<ActivityVO> query(ActivityQuery activityQuery) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        if (activityQuery.getOrder() == 0) {
            queryWrapper.orderByDesc("activity_time");
        }
        if (activityQuery.getOrder() == 1) {
            queryWrapper.orderByDesc("cheer");
        }

        Page<Activity> page = new Page<>(activityQuery.getCurrentPage(), activityQuery.getPageSize());

        Page<Activity> activityPage = activityMapper.selectPage(page, queryWrapper);
        List<ActivityVO> activityVOList = activityPage.getRecords().stream()
                .map(activity -> {
                    ActivityVO activityVO = new ActivityVO();
                    if (!activity.isDelete()) {
                        BeanUtil.copyProperties(activity, activityVO);
                    }
                    return activityVO;
                })
                .collect(Collectors.toList());

        Page<ActivityVO> pageResult = new Page<>();
        BeanUtil.copyProperties(activityPage, pageResult);
        pageResult.setRecords(activityVOList);

        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean like(Long activityId) {
        LikeBO likeBO = prepareLikeBO(activityId, LikeFlagEnum.LIKE);

        CommonUser commonUser = UserContext.getCurrentUser();
        List<LikeBO> likeBOList = likeService.listByUidAndContentIds(commonUser.getUid(), Collections.singletonList(activityId), LikeTypeEnum.ACTIVITY);
        for (LikeBO check : likeBOList) {
            if (check.getContentId() != null && check.getContentId().equals(activityId) && check.getLikeFlagEnum() != LikeFlagEnum.DISLIKE) {
                log.info("不可重复打气！");
                return false;
            }
        }
        boolean res = likeService.saveOrUpdate(likeBO);
        activityMapper.addCheer(activityId);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLike(Long activityId) {
        LikeBO likeBO = prepareLikeBO(activityId, LikeFlagEnum.DISLIKE);

        CommonUser commonUser = UserContext.getCurrentUser();
        List<LikeBO> likeBOList = likeService.listByUidAndContentIds(commonUser.getUid(), Collections.singletonList(activityId), LikeTypeEnum.ACTIVITY);
        for (LikeBO check : likeBOList) {
            if (check.getContentId() != null && check.getContentId().equals(activityId) && check.getLikeFlagEnum() != LikeFlagEnum.LIKE) {
                log.info("不可重复取消打气！");
                return false;
            }
        }
        boolean res = likeService.saveOrUpdate(likeBO);
        activityMapper.deleteCheer(activityId);
        return res;
    }

    private LikeBO prepareLikeBO(Long postId, LikeFlagEnum likeFlagEnum) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        LikeBO likeBO = new LikeBO();
        likeBO.setUid(uid);
        likeBO.setContentId(postId);
        likeBO.setLikeTypeEnum(LikeTypeEnum.ACTIVITY);
        likeBO.setLikeFlagEnum(likeFlagEnum);
        return likeBO;
    }

}
