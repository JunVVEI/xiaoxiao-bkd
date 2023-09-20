package com.xiaoxiao.bbs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.bbs.config.ThreadPoolConfig;
import com.xiaoxiao.bbs.constants.enums.LikeFlagEnum;
import com.xiaoxiao.bbs.constants.enums.LikeTypeEnum;
import com.xiaoxiao.bbs.mapper.CommentMapper;
import com.xiaoxiao.bbs.model.bo.LikeBO;
import com.xiaoxiao.bbs.model.dto.PostCommentDTO;
import com.xiaoxiao.bbs.model.dto.PostCommentQuery;
import com.xiaoxiao.bbs.model.dto.PostSubCommentQuery;
import com.xiaoxiao.bbs.model.entity.Comment;
import com.xiaoxiao.bbs.model.enums.PostCommentQueryOrderEnum;
import com.xiaoxiao.bbs.model.vo.PostCommentVO;
import com.xiaoxiao.bbs.model.vo.PostSubCommentVO;
import com.xiaoxiao.bbs.service.*;
import com.xiaoxiao.bbs.util.PageUtil;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.common.util.AssertUtil;
import com.xiaoxiao.user.rpc.model.response.IdentityInfoResponse;
import com.xiaoxiao.user.rpc.model.response.UserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Junwei
 * @since 2022-10-29 04:18:48
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private PostService postService;

    @Resource
    private RpcService rpcService;

    @Resource
    private LikeService likeService;

    @Resource
    private ContentSecurityService contentSecurityService;

    @Resource
    @Lazy
    private InformService informService;

//    @Resource
//    @Lazy
//    private ReportService reportService;

    @Resource(name = ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public IPage<PostCommentVO> queryCommentPage(PostCommentQuery postCommentQuery) {
        PostCommentQuery.checkIsValid(postCommentQuery);

        Page<Comment> commentPage = commentMapper.selectPage(
                new Page<>(postCommentQuery.getCurrentPage(), postCommentQuery.getPageSize()),
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getPostId, postCommentQuery.getPostId())
                        .eq(Comment::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
                        .isNull(Comment::getRootCommentId)
                        .isNull(Comment::getParentId)
                        .orderByDesc(
                                postCommentQuery.getOrder() == PostCommentQueryOrderEnum.ORDER_BY_LIKE.getOrder(),
                                Comment::getLikeCount
                        )
                        .orderByDesc(
                                true,
                                Comment::getCreateTime
                        )
        );

        return PageUtil.convertPage(commentPage, comment2commentVO(commentPage.getRecords()));
    }

    private List<PostCommentVO> comment2commentVO(List<Comment> commentList) {
        Map<Long, UserInfoResponse> userNameAndAvatarMap = rpcService.getUserNameAndAvatarMap(
                commentList.stream()
                        .filter(item -> !isAnonymously(item))
                        .map(Comment::getUserId)
                        .distinct()
                        .collect(Collectors.toList())
        );

        Map<Long, IdentityInfoResponse> anonymousNameAndAvatarMap = rpcService.getAnonymousNameAndAvatarMap(
                commentList.stream()
                        .filter(this::isAnonymously)
                        .map(Comment::getIdentityId)
                        .distinct()
                        .collect(Collectors.toList())
        );

        // 点赞字段标识
        List<Long> userLikedCommentIds;
        CommonUser currentUser = UserContext.getCurrentUser();
        if (CommonUser.isLogInUser(currentUser)) {
            userLikedCommentIds = likeService.filterValidIds(
                    currentUser.getUid(),
                    commentList.stream().map(Comment::getId).collect(Collectors.toList()),
                    LikeTypeEnum.POST_COMMENT,
                    LikeFlagEnum.LIKE
            );
        } else {
            userLikedCommentIds = new ArrayList<>();
        }

        return commentList.stream().map(item -> {
                    PostCommentVO postCommentVO = new PostCommentVO();
                    postCommentVO.setCommentId(item.getId());
                    postCommentVO.setPostId(item.getPostId());
                    postCommentVO.setContent(item.getContent());
                    postCommentVO.setCreateTime(item.getCreateTime());
                    postCommentVO.setLikeCount(item.getLikeCount());
                    postCommentVO.setSubCommentCount(item.getSubCommentCount());

                    if (!isAnonymously(item)) {
                        UserInfoResponse userInfoResponse = userNameAndAvatarMap.getOrDefault(
                                item.getUserId(),
                                new UserInfoResponse()
                        );
                        postCommentVO.setCreatorName(userInfoResponse.getName());
                        postCommentVO.setAvatar(userInfoResponse.getAvatar());
                    } else {
                        IdentityInfoResponse identityInfoResponse = anonymousNameAndAvatarMap.getOrDefault(
                                item.getIdentityId(),
                                new IdentityInfoResponse()
                        );
                        postCommentVO.setCreatorName(identityInfoResponse.getName());
                        postCommentVO.setAvatar(identityInfoResponse.getAvatar());
                    }

                    postCommentVO.setIsLiked(userLikedCommentIds.contains(postCommentVO.getCommentId()));
                    if (CommonUser.isLogInUser(currentUser)) {
                        postCommentVO.setIsCreator(Objects.equals(currentUser.getUid(), item.getUserId()));
                    }
                    if (isAnonymously(item)) {
                        postCommentVO.setIdentityId(item.getIdentityId());
                    } else {
                        postCommentVO.setUid(item.getUserId());
                    }

                    postCommentVO.setMediaPath(item.getMediaPath());
                    postCommentVO.setLikeCount(
                            item.getLikeCount() == null ? 0 : item.getLikeCount()
                    );

                    // 子评论处理
                    if (item.getSubCommentCount() != null && item.getSubCommentCount() != 0) {
                        PostSubCommentQuery postSubCommentQuery = new PostSubCommentQuery();
                        postSubCommentQuery.setRootCommentId(item.getId());
                        postSubCommentQuery.setCurrentPage(1L);
                        postSubCommentQuery.setPageSize(1L);
                        IPage<PostSubCommentVO> postSubCommentVOIPage = queryPostSubCommentPage(postSubCommentQuery);
                        postCommentVO.setSubCommentList(postSubCommentVOIPage.getRecords());
                    }
                    return postCommentVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public IPage<PostSubCommentVO> queryPostSubCommentPage(PostSubCommentQuery postSubCommentQuery) {
        Page<Comment> commentPage = commentMapper.selectPage(
                new Page<>(postSubCommentQuery.getCurrentPage(), postSubCommentQuery.getPageSize()),
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getRootCommentId, postSubCommentQuery.getRootCommentId())
                        .eq(Comment::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
                        .orderByAsc(Comment::getCreateTime)
        );

        return PageUtil.convertPage(commentPage, subComment2subCommentVO(commentPage.getRecords()));
    }

    private List<PostSubCommentVO> subComment2subCommentVO(List<Comment> commentList) {
        Map<Long, UserInfoResponse> userNameAndAvatarMap = rpcService.getUserNameAndAvatarMap(
                commentList.stream()
                        .filter(item -> !isAnonymously(item))
                        .map(Comment::getUserId)
                        .distinct()
                        .collect(Collectors.toList())
        );

        Map<Long, IdentityInfoResponse> anonymousNameAndAvatarMap = rpcService.getAnonymousNameAndAvatarMap(
                commentList.stream()
                        .filter(this::isAnonymously)
                        .map(Comment::getIdentityId)
                        .distinct()
                        .collect(Collectors.toList())
        );

        // 点赞字段标识
        List<Long> userLikedCommentIds;
        CommonUser currentUser = UserContext.getCurrentUser();
        if (CommonUser.isLogInUser(currentUser)) {
            userLikedCommentIds = likeService.filterValidIds(
                    currentUser.getUid(),
                    commentList.stream().map(Comment::getId).collect(Collectors.toList()),
                    LikeTypeEnum.POST_COMMENT,
                    LikeFlagEnum.LIKE
            );
        } else {
            userLikedCommentIds = new ArrayList<>();
        }


        List<Comment> parentComments = listCommentByIds(
                commentList.stream()
                        .map(Comment::getParentId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );

        Map<Long, Comment> parentCommentsMap = parentComments.stream().collect(Collectors.toMap(Comment::getId, Function.identity()));
        Map<Long, UserInfoResponse> parentUserNameAndAvatarMap = rpcService.getUserNameAndAvatarMap(
                parentComments.stream()
                        .filter(item -> !isAnonymously(item))
                        .map(Comment::getUserId)
                        .distinct()
                        .collect(Collectors.toList())
        );
        Map<Long, IdentityInfoResponse> parentAnonymousNameAndAvatarMap = rpcService.getAnonymousNameAndAvatarMap(
                parentComments.stream()
                        .filter(this::isAnonymously)
                        .map(Comment::getIdentityId)
                        .distinct()
                        .collect(Collectors.toList())
        );

        return commentList.stream().map(item -> {
                    PostSubCommentVO postSubCommentVO = new PostSubCommentVO();
                    postSubCommentVO.setPostId(item.getPostId());
                    postSubCommentVO.setCommentId(item.getId());
                    postSubCommentVO.setParentId(item.getParentId());

                    if (!isAnonymously(parentCommentsMap.get(item.getParentId()))) {
                        UserInfoResponse userInfoResponse = parentUserNameAndAvatarMap.getOrDefault(
                                parentCommentsMap.get(item.getParentId()).getUserId(),
                                new UserInfoResponse()
                        );
                        postSubCommentVO.setParentCreatorName(userInfoResponse.getName());
                    } else {
                        IdentityInfoResponse identityInfoResponse = parentAnonymousNameAndAvatarMap.getOrDefault(
                                parentCommentsMap.get(item.getParentId()).getIdentityId(),
                                new IdentityInfoResponse()
                        );
                        postSubCommentVO.setParentCreatorName(identityInfoResponse.getName());
                    }

                    postSubCommentVO.setContent(item.getContent());
                    postSubCommentVO.setMediaPath(item.getMediaPath());

                    if (!isAnonymously(item)) {
                        UserInfoResponse userInfoResponse = userNameAndAvatarMap.getOrDefault(
                                item.getUserId(),
                                new UserInfoResponse()
                        );
                        postSubCommentVO.setCreatorName(userInfoResponse.getName());
                        postSubCommentVO.setAvatar(userInfoResponse.getAvatar());
                    } else {
                        IdentityInfoResponse identityInfoResponse = anonymousNameAndAvatarMap.getOrDefault(
                                item.getIdentityId(),
                                new IdentityInfoResponse()
                        );
                        postSubCommentVO.setCreatorName(identityInfoResponse.getName());
                        postSubCommentVO.setAvatar(identityInfoResponse.getAvatar());
                    }

                    postSubCommentVO.setCreateTime(item.getCreateTime());
                    postSubCommentVO.setLikeCount(
                            item.getLikeCount() == null ? 0 : item.getLikeCount()
                    );
                    postSubCommentVO.setIsLiked(userLikedCommentIds.contains(item.getId()));
                    if (CommonUser.isLogInUser(currentUser)) {
                        postSubCommentVO.setIsCreator(Objects.equals(currentUser.getUid(), item.getUserId()));
                    }
                    if (isAnonymously(item)) {
                        postSubCommentVO.setIdentityId(item.getIdentityId());
                    } else {
                        postSubCommentVO.setUid(item.getUserId());
                    }
                    if (CommonUser.isLogInUser(currentUser)) {
                        postSubCommentVO.setIsCreator(Objects.equals(currentUser.getUid(), item.getUserId()));
                    }

                    return postSubCommentVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PostCommentVO addUserPostComment(PostCommentDTO postCommentDTO) {
        PostCommentDTO.checkIsValid(postCommentDTO);
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        saveUserCommentCheck(currentUser, postCommentDTO);
        return addPostComment(currentUser, postCommentDTO);
    }

    @Override
    public PostCommentVO addPostComment(CommonUser commonUser, PostCommentDTO postCommentDTO) {
        PostCommentDTO.checkIsValid(postCommentDTO);

        Comment comment = prepareNewComment(commonUser, postCommentDTO);
        int insert = commentMapper.insert(comment);

        if (insert != 0) {
            afterAddCommentAsync(comment);
        }

        List<PostCommentVO> postCommentVOS = comment2commentVO(Collections.singletonList(comment));
        return postCommentVOS.get(0);
    }

    private void afterAddCommentAsync(Comment comment) {
        CompletableFuture.runAsync(
                () -> {
                    // 更新帖子的回复数量
                    postService.updatePostCommentCount(comment.getPostId(), 1);

                    try {
                        informService.asyncSendPostCommentInform(comment.getPostId());
                    } catch (Exception e) {
                        log.error("帖子评论通知异常", e);
                    }

                    if (!Objects.equals(comment.getContent(), "m")) {
                        informService.asyncSendInformToMarker(comment.getPostId());
                    }
                },
                threadPoolTaskExecutor
        );
    }

    @Override
    public PostSubCommentVO addUserPostSubComment(PostCommentDTO postCommentDTO) {
        PostCommentDTO.checkIsValid(postCommentDTO);
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        saveUserCommentCheck(currentUser, postCommentDTO);
        return addPostSubComment(currentUser, postCommentDTO);
    }

    @Override
    public PostSubCommentVO addPostSubComment(CommonUser commonUser, PostCommentDTO postCommentDTO) {
        PostCommentDTO.checkIsValid(postCommentDTO);

        Comment comment = prepareNewComment(commonUser, postCommentDTO);
        int insert = commentMapper.insert(comment);

        if (insert != 0) {
            afterAddSubCommentAsync(comment);
        }

        List<PostSubCommentVO> postSubCommentVOS = subComment2subCommentVO(Collections.singletonList(comment));
        return postSubCommentVOS.get(0);
    }

    private void afterAddSubCommentAsync(Comment comment) {
        CompletableFuture.runAsync(
                () -> {
                    // 更新帖子的回复数量
                    postService.updatePostCommentCount(comment.getPostId(), 1);

                    // 发送回复通知
                    if (comment.getParentId() != null) {
                        Comment parentComment = commentMapper.selectById(comment.getParentId());
                        if (parentComment != null) {
                            try {
                                informService.asyncSendSubCommentInform(parentComment.getId());
                            } catch (Exception e) {
                                log.error("回复评论通知异常", e);
                            }
                        }
                    }

                    // 更新根评论数量
                    if (comment.getRootCommentId() != null) {
                        updateSubCommentCount(comment.getRootCommentId(), 1);
                    }

                    if (!Objects.equals(comment.getContent(), "m")) {
                        informService.asyncSendInformToMarker(comment.getPostId());
                    }
                },
                threadPoolTaskExecutor
        );
    }

    private void saveUserCommentCheck(CommonUser commonUser, PostCommentDTO postCommentDTO) {
        try {
            // 内容安全审查
            CompletableFuture<Boolean> isSafeContentFuture = CompletableFuture.supplyAsync(
                    () -> contentSecurityService.msgSecCheck(postCommentDTO.getContent(), commonUser.getOpenid()),
                    threadPoolTaskExecutor
            );

            //检查是否封禁
//            CompletableFuture<Boolean> isUserRecentlyBannedFuture = CompletableFuture.supplyAsync(
//                    () -> reportService.checkIsUserRecentlyBanned(commonUser.getUid()),
//                    threadPoolTaskExecutor
//            );

            if (!isSafeContentFuture.get()) {
                throw new ApiException(StatusCode.FAILED, "内容安全审查不通过");
            }

//            if (isUserRecentlyBannedFuture.get()) {
//                throw new ApiException(StatusCode.FAILED, "系统忙碌中");
//            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("", e);
            throw new ApiException("服务器异常,发帖失败");
        }
    }

    private Comment prepareNewComment(CommonUser commonUser, PostCommentDTO postCommentDTO) {
        Comment comment = new Comment();

        comment.setPostId(postCommentDTO.getPostId());
        comment.setParentId(postCommentDTO.getParentId());

        // 如果父id不为空则表明是subComment
        if (postCommentDTO.getParentId() != null) {
            Comment parentComment = commentMapper.selectById(postCommentDTO.getParentId());
            if (parentComment != null) {
                Long rootCommentId;
                if (parentComment.getRootCommentId() != null) {
                    rootCommentId = parentComment.getRootCommentId();
                } else {
                    rootCommentId = parentComment.getId();
                }
                comment.setRootCommentId(rootCommentId);
            }
        }

        comment.setContent(postCommentDTO.getContent());
        comment.setMediaPath(postCommentDTO.getMediaPath());

        CommonUser.assertIsLogInUser(commonUser);
        comment.setUserId(commonUser.getUid());
        comment.setIdentityId(postCommentDTO.getIdentityId());
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        comment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        comment.setIsDelete(XiaoXiaoConstEnum.UN_DELETE.getVal());

        comment.setSubCommentCount(0);

        return comment;
    }

    // 删除评论
    @Override
    public boolean deleteById(Long commentId) {
        AssertUtil.isTrue(commentId != null && 0 < commentId, StatusCode.VALIDATE_FAILED);
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            return true;
        }

        int delRes = commentMapper.deleteById(commentId);
        if (delRes != 0) {
            afterDelCommentAsync(comment);
            delChildComment(comment);
        }

        return true;
    }

    private void delChildComment(Comment comment) {
        if (comment == null) {
            return;
        }
        List<Comment> childComments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getParentId, comment.getId())
                        .eq(Comment::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );
        if (CollectionUtil.isEmpty(childComments)) {
            return;
        }

        for (Comment childComment : childComments) {
            commentMapper.deleteById(childComment.getId());
            afterDelCommentAsync(childComment);
            delChildComment(childComment);
        }
    }

    private void afterDelCommentAsync(Comment comment) {
        CompletableFuture.runAsync(
                () -> {
                    // 更新根评论的子评论数量
                    if (comment.getRootCommentId() != null) {
                        updateSubCommentCount(comment.getRootCommentId(), -1);
                    }

//                    // 更新父评论的子评论数量
//                    if (comment.getParentId() != null) {
//                        updateSubCommentCount(comment.getParentId(), -1);
//                    }

                    // 更新帖子的回复数量
                    postService.updatePostCommentCount(comment.getPostId(), -1);
                },
                threadPoolTaskExecutor
        );

    }

    private boolean updateSubCommentCount(long commentId, int addCount) {
        if (addCount == 0) {
            return true;
        }
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            return false;
        }
        Integer subCommentCount = comment.getSubCommentCount();
        if (subCommentCount == null) {
            subCommentCount = 0;
        }
        comment.setSubCommentCount(subCommentCount + addCount);
        int i = commentMapper.updateById(comment);
        return i != 0;
    }

    @Override
    public boolean likePost(Long commentId) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        LikeBO likeBO = new LikeBO();
        likeBO.setUid(uid);
        likeBO.setContentId(commentId);
        likeBO.setLikeTypeEnum(LikeTypeEnum.POST_COMMENT);
        likeBO.setLikeFlagEnum(LikeFlagEnum.LIKE);
        if (likeService.saveOrUpdate(likeBO)) {
            UpdateWrapper<Comment> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", commentId);
            wrapper.setSql("like_count = like_count + 1");
            commentMapper.update(null, wrapper);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean unlikePost(Long commentId) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        LikeBO likeBO = new LikeBO();
        likeBO.setUid(uid);
        likeBO.setContentId(commentId);
        likeBO.setLikeTypeEnum(LikeTypeEnum.POST_COMMENT);
        likeBO.setLikeFlagEnum(LikeFlagEnum.DELETED);
        if (likeService.saveOrUpdate(likeBO)) {
            UpdateWrapper<Comment> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", commentId);
            wrapper.setSql("like_count = like_count - 1");
            commentMapper.update(null, wrapper);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Comment> listCommentByIds(List<Long> commentIds) {
        if (CollectionUtil.isEmpty(commentIds)) {
            return new ArrayList<>();
        }
        return commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .in(Comment::getId, commentIds)
                        .eq(Comment::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentMapper.selectOne(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getId, id)
                        .eq(Comment::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );
    }

    @Override
    public List<Long> getCommentMarkerIds(Long postId) {
        if (postId == null) {
            return new ArrayList<>();
        }
        List<Comment> comments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getPostId, postId)
                        .eq(Comment::getContent, "m")
                        .eq(Comment::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
                        .select(Comment::getUserId)
        );

        return comments.stream().map(Comment::getUserId).distinct().collect(Collectors.toList());
    }

    /**
     * 是否是匿名发帖
     */
    private boolean isAnonymously(Comment comment) {
        return Objects.nonNull(comment.getUserId()) && Objects.nonNull(comment.getIdentityId());
    }

}
