package com.xiaoxiao.bbs.service.impl;

import com.xiaoxiao.baseservice.rpc.model.WeChatInformTypeEnum;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendWeChatInformRequest;
import com.xiaoxiao.bbs.config.ThreadPoolConfig;
import com.xiaoxiao.bbs.model.entity.Comment;
import com.xiaoxiao.bbs.model.entity.Post;
import com.xiaoxiao.bbs.service.CommentService;
import com.xiaoxiao.bbs.service.InformService;
import com.xiaoxiao.bbs.service.PostService;
import com.xiaoxiao.bbs.service.RpcService;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.user.rpc.model.vo.RpcUserVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class InformServiceImpl implements InformService {

    @Resource
    private RpcService rpcService;

    @Resource
    private PostService postService;

    @Resource
    private CommentService commentService;

    @Resource(name = ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    @Async(ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    public void asyncInformFollower(CommonUser commonUser, Long postId) {
        if (postId == null) {
            return;
        }

        List<RpcUserVO> userFollowers = rpcService.getUserFollowers(commonUser.getUid());

        for (RpcUserVO userFollower : userFollowers) {
            CompletableFuture.runAsync(
                    () -> {
                        SendWeChatInformRequest sendWeChatInformRequest = new SendWeChatInformRequest();
                        sendWeChatInformRequest.setUid(userFollower.getUserId());
                        sendWeChatInformRequest.setWeChatInformType(
                                WeChatInformTypeEnum.FOLLOWING_NEW_POST.getType()
                        );
                        sendWeChatInformRequest.setContent("您关注的对象发布了新的帖子!");
                        sendWeChatInformRequest.setPagePath(parsePostDetailPath(postId));

                        rpcService.sendWeChatInform(sendWeChatInformRequest);
                    },
                    threadPoolTaskExecutor
            );
        }
    }

    @Override
    @Async(ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    public void asyncSendPostLikeInform(Long postId) {
        Post post = postService.getPostById(postId);

        SendWeChatInformRequest sendWeChatInformRequest = new SendWeChatInformRequest();
        sendWeChatInformRequest.setUid(post.getUserId());
        sendWeChatInformRequest.setWeChatInformType(WeChatInformTypeEnum.LIKE.getType());
        String informContent = "您的帖子《" + contentSummery(post.getContent(), 7) + "..." + "》收获了一个赞";
        sendWeChatInformRequest.setContent(informContent);
        sendWeChatInformRequest.setPagePath(parsePostDetailPath(post.getId()));

        rpcService.sendWeChatInform(sendWeChatInformRequest);
    }

    @Override
    @Async(ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    public void asyncSendPostCommentInform(Long postId) {
        Post post = postService.getPostById(postId);

        SendWeChatInformRequest sendWeChatInformRequest = new SendWeChatInformRequest();
        sendWeChatInformRequest.setUid(post.getUserId());
        sendWeChatInformRequest.setWeChatInformType(WeChatInformTypeEnum.COMMENT.getType());
        String informContent = "有人评论了您的帖子《" + contentSummery(post.getContent(), 7) + "..." + "》";
        sendWeChatInformRequest.setContent(informContent);
        sendWeChatInformRequest.setPagePath(parsePostDetailPath(postId));

        rpcService.sendWeChatInform(sendWeChatInformRequest);
    }

    @Override
    @Async(ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    public void asyncSendSubCommentInform(Long commentId) {
        Comment comment = commentService.getCommentById(commentId);

        SendWeChatInformRequest sendWeChatInformRequest = new SendWeChatInformRequest();
        sendWeChatInformRequest.setUid(comment.getUserId());
        sendWeChatInformRequest.setWeChatInformType(WeChatInformTypeEnum.SUB_COMMENT.getType());
        String informContent = "有人回复了您的评论《" + contentSummery(comment.getContent(), 5) + "》";
        sendWeChatInformRequest.setContent(informContent);
        sendWeChatInformRequest.setPagePath(parsePostDetailPath(comment.getPostId()));

        rpcService.sendWeChatInform(sendWeChatInformRequest);
    }


    @Override
    @Async(ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    public void asyncSendInformToMarker(Long postId) {
        Post post = postService.getPostById(postId);
        List<Long> commentMarkerId = commentService.getCommentMarkerIds(postId);

        for (Long markerId : commentMarkerId) {
            SendWeChatInformRequest sendWeChatInformRequest = new SendWeChatInformRequest();
            sendWeChatInformRequest.setUid(markerId);
            sendWeChatInformRequest.setWeChatInformType(WeChatInformTypeEnum.POST_MARK.getType());
            String informContent = "您关注的帖子《" + contentSummery(post.getContent(), 7) + "..." + "》有新的评论!";
            sendWeChatInformRequest.setContent(informContent);
            sendWeChatInformRequest.setPagePath(parsePostDetailPath(postId));

            rpcService.sendWeChatInform(sendWeChatInformRequest);
        }
    }

    private String parsePostDetailPath(Long postId) {
        return "/pages/home/detailPage/detailPage?id=" + postId;
    }

    private String contentSummery(String content, int maxLength) {
        content = removeEmoji(content);
        if (content.length() < maxLength) {
            return content;
        }

        return (String) content.subSequence(0, maxLength);
    }

    private String removeEmoji(String str) {
        return str.replaceAll(
                        "[\\p{C}\\p{So}\uFE00-\uFE0F\\x{E0100}-\\x{E01EF}]+",
                        ""
                )
                .replaceAll(
                        " {2,}",
                        " "
                );
    }

}
