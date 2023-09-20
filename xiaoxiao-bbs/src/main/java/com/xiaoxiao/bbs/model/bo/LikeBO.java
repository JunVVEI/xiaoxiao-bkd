package com.xiaoxiao.bbs.model.bo;

import com.xiaoxiao.bbs.constants.enums.LikeFlagEnum;
import com.xiaoxiao.bbs.constants.enums.LikeTypeEnum;
import com.xiaoxiao.bbs.model.entity.Like;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p>
 * 点赞、点踩相关操作BO类
 * </p>
 *
 * @author Junwei
 * @since 2023/2/19
 */
@Data
public class LikeBO {

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 内容来源id
     */
    private Long contentId;

    /**
     * 内容来源枚举
     */
    private LikeTypeEnum likeTypeEnum;

    /**
     * 标记枚举
     */
    private LikeFlagEnum likeFlagEnum;

    public static boolean checkIsValid(LikeBO likeBO) {
        return Objects.nonNull(likeBO.getUid())
                && Objects.nonNull(likeBO.getContentId())
                && Objects.nonNull(likeBO.getLikeTypeEnum())
                && Objects.nonNull(likeBO.getLikeFlagEnum());
    }

    public static Like prepareLike(LikeBO likeBO) {
        if (Objects.isNull(likeBO)) {
            return null;
        }
        Like like = new Like();
        like.setUid(likeBO.getUid());
        like.setContentId(likeBO.getContentId());
        like.setType(likeBO.getLikeTypeEnum().getType());
        like.setFlag(likeBO.getLikeFlagEnum().getFlag());
        like.setCreateTime(new Timestamp(System.currentTimeMillis()));
        like.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return like;
    }

    public static LikeBO parseLike(Like like) {
        LikeBO likeBO = new LikeBO();
        likeBO.setUid(like.getUid());
        likeBO.setContentId(like.getContentId());
        likeBO.setLikeTypeEnum(LikeTypeEnum.getLikeTypeEnumByType(like.getType()));
        likeBO.setLikeFlagEnum(LikeFlagEnum.getLikeFlagEnumByFlag(like.getFlag()));
        return likeBO;
    }
}
