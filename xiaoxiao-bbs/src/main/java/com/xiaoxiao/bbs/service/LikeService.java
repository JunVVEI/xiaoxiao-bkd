package com.xiaoxiao.bbs.service;

import com.xiaoxiao.bbs.constants.enums.LikeFlagEnum;
import com.xiaoxiao.bbs.constants.enums.LikeTypeEnum;
import com.xiaoxiao.bbs.model.bo.LikeBO;

import javax.annotation.Nullable;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenjunwei
 * @since 2023-02-19 10:42:46
 */
public interface LikeService {


    /**
     * 新增或更新点赞相关操作
     * return 数据库的数据是否存在更新
     */
    boolean saveOrUpdate(LikeBO likeBO);

    /**
     * 根据内容id与来源查询LikeBo列表
     */
    List<LikeBO> listByContentId(Long contentId, LikeTypeEnum likeTypeEnum);

    /**
     * 根据用户id与来源(可为空)查询LikeBo列表
     */
    List<LikeBO> listByUid(Long uid, @Nullable LikeTypeEnum likeTypeEnum);

    /**
     * 根据用户id与内容id列表查询LikeBo列表，即查询uid与contentId有记录的数据
     */
    List<LikeBO> listByUidAndContentIds(Long uid, List<Long> contentIs, LikeTypeEnum likeTypeEnum);

    /**
     * 返回列表中命中的id数据
     */
    List<Long> filterValidIds(Long uid, List<Long> contentIs, LikeTypeEnum likeTypeEnum, LikeFlagEnum likeFlagEnum);


    /**
     * 用户是否点赞了
     */
    boolean isUserLiked(Long uid, Long contentId, LikeTypeEnum likeTypeEnum);
}
