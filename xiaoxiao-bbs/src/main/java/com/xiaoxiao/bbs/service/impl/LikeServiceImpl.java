package com.xiaoxiao.bbs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxiao.bbs.constants.enums.LikeFlagEnum;
import com.xiaoxiao.bbs.constants.enums.LikeTypeEnum;
import com.xiaoxiao.bbs.mapper.LikeMapper;
import com.xiaoxiao.bbs.model.bo.LikeBO;
import com.xiaoxiao.bbs.model.entity.Like;
import com.xiaoxiao.bbs.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenjunwei
 * @since 2023-02-19 10:42:46
 */
@Service
@Slf4j
public class LikeServiceImpl implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean saveOrUpdate(LikeBO likeBO) {
        if (!LikeBO.checkIsValid(likeBO)) {
            log.error("LikeService#saveOrUpdate 参数校验失败: {}", JSONUtil.toJsonStr(likeBO));
            return false;
        }
        RLock lock = redissonClient.getLock(
                likeBO.getUid() + ":" + likeBO.getContentId() + ":" + likeBO.getLikeTypeEnum().getType()
        );
        try {
            lock.lock();
            Like oneLike = likeMapper.selectOne(
                    new LambdaQueryWrapper<Like>()
                            .eq(Like::getUid, likeBO.getUid())
                            .eq(Like::getContentId, likeBO.getContentId())
                            .eq(Like::getType, likeBO.getLikeTypeEnum().getType())
            );

            switch (likeBO.getLikeFlagEnum()) {
                case LIKE:
                case DISLIKE:
                    if (Objects.isNull(oneLike)) {
                        // 如果不存在原始数据直接新增数据
                        Like like = LikeBO.prepareLike(likeBO);
                        return likeMapper.insert(like) != 0;
                    } else {
                        // 更新原有数据
                        // 如果数据库中的flag标志一致则无需更新
                        if (Objects.equals(oneLike.getFlag(), likeBO.getLikeFlagEnum().getFlag())) {
                            return false;
                        }
                        oneLike.setFlag(likeBO.getLikeFlagEnum().getFlag());
                        oneLike.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                        return likeMapper.updateById(oneLike) != 0;
                    }
                case DELETED:
                    if (Objects.isNull(oneLike)) {
                        return false;
                    } else {
                        // 更新原有数据
                        // 如果数据库中的flag标志一致则无需更新
                        if (Objects.equals(oneLike.getFlag(), likeBO.getLikeFlagEnum().getFlag())) {
                            return false;
                        }
                        oneLike.setFlag(likeBO.getLikeFlagEnum().getFlag());
                        oneLike.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                        return likeMapper.updateById(oneLike) != 0;
                    }
                default:
                    return false;
            }
        } catch (Exception e) {
            log.error("LikeServiceImpl.saveOrUpdate 异常", e);
        } finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public List<LikeBO> listByContentId(Long contentId, LikeTypeEnum likeTypeEnum) {
        // TODO
        return null;
    }

    @Override
    public List<LikeBO> listByUid(Long uid, @Nullable LikeTypeEnum likeTypeEnum) {
        // TODO
        return null;
    }

    @Override
    public List<LikeBO> listByUidAndContentIds(Long uid, List<Long> contentIs, LikeTypeEnum likeTypeEnum) {
        List<Like> likes = likeMapper.selectList(
                new LambdaQueryWrapper<Like>()
                        .eq(Like::getUid, uid)
                        .in(Like::getContentId, contentIs)
                        .eq(Like::getType, likeTypeEnum.getType())
                        .ne(Like::getFlag, LikeFlagEnum.DELETED.getFlag())
        );
        return likes.stream().map(LikeBO::parseLike).collect(Collectors.toList());
    }

    @Override
    public List<Long> filterValidIds(
            Long uid,
            List<Long> contentIs,
            LikeTypeEnum likeTypeEnum,
            LikeFlagEnum likeFlagEnum
    ) {
        if (CollectionUtil.isEmpty(contentIs)) {
            return new ArrayList<>();
        }
        if (likeTypeEnum == null) {
            log.error("参数likeTypeEnum不能为空");
            return new ArrayList<>();
        }
        List<Like> likes = likeMapper.selectList(
                new LambdaQueryWrapper<Like>()
                        .select(Like::getContentId)
                        .eq(Like::getUid, uid)
                        .eq(Like::getType, likeTypeEnum.getType())
                        .eq(Like::getFlag, likeFlagEnum.getFlag())
                        .in(Like::getContentId, contentIs)
        );
        return likes.stream().map(Like::getContentId).collect(Collectors.toList());
    }

    @Override
    public boolean isUserLiked(Long uid, Long contentId, LikeTypeEnum likeTypeEnum) {
        if (uid == null || contentId == null || likeTypeEnum == null) {
            return false;
        }
        return likeMapper.exists(
                new LambdaQueryWrapper<Like>()
                        .eq(Like::getUid, uid)
                        .eq(Like::getContentId, contentId)
                        .eq(Like::getType, likeTypeEnum.getType())
                        .eq(Like::getFlag, LikeFlagEnum.LIKE.getFlag())
        );
    }
}
