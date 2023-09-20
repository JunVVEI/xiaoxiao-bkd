package com.xiaoxiao.bbs.service;

import com.xiaoxiao.common.user.CommonUser;

public interface InformService {

    /**
     * 发帖后像追随者发送通知
     */
    void asyncInformFollower(CommonUser commonUser, Long postId);

    /**
     * 发送帖子点赞通知
     */
    void asyncSendPostLikeInform(Long postId);

    void asyncSendPostCommentInform(Long postId);

    void asyncSendSubCommentInform(Long commentId);

    /**
     * 想m评论的人发送通知
     */
    void asyncSendInformToMarker(Long postId);

}
