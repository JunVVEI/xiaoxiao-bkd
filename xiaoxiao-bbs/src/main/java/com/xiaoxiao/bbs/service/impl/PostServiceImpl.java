package com.xiaoxiao.bbs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xiaoxiao.bbs.config.ThreadPoolConfig;
import com.xiaoxiao.bbs.config.WeChatConfig;
import com.xiaoxiao.bbs.constants.enums.EnergyEnum;
import com.xiaoxiao.bbs.constants.enums.LikeFlagEnum;
import com.xiaoxiao.bbs.constants.enums.LikeTypeEnum;
import com.xiaoxiao.bbs.constants.enums.TagEnum;
import com.xiaoxiao.bbs.mapper.BrowsingHistoryMapper;
import com.xiaoxiao.bbs.mapper.LikeMapper;
import com.xiaoxiao.bbs.mapper.PostMapper;
import com.xiaoxiao.bbs.model.bo.LikeBO;
import com.xiaoxiao.bbs.model.dto.*;
import com.xiaoxiao.bbs.model.entity.BrowsingHistory;
import com.xiaoxiao.bbs.model.entity.Like;
import com.xiaoxiao.bbs.model.entity.Post;
import com.xiaoxiao.bbs.model.vo.PostVO;
import com.xiaoxiao.bbs.model.vo.TagVO;
import com.xiaoxiao.bbs.service.*;
import com.xiaoxiao.bbs.util.PageUtil;
import com.xiaoxiao.common.api.BasePageCondition;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.user.rpc.model.request.EnergyRequest;
import com.xiaoxiao.user.rpc.model.response.IdentityInfoResponse;
import com.xiaoxiao.user.rpc.model.response.UserInfoResponse;
import com.xiaoxiao.user.rpc.model.vo.RpcUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
 * @since 2022-10-27 11:23:00
 */
@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;

    @Resource
    private RpcService rpcService;

    @Resource
    private LikeService likeService;
    @Resource
    private BrowsingHistoryMapper browsingHistoryMapper;

    @Resource
    private ContentSecurityService contentSecurityService;

    @Resource
    @Lazy
    private InformService informService;

    @Resource
    private WeChatConfig weChatConfig;

    @Resource(name = ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static final String SCHEMA_LINK_API = "https://api.weixin.qq.com/wxa/generatescheme";

    private static final String WX_BOT_SEND_MSG_API = "http://localhost:8590/wechat_bot/send_message";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean createUserPost(PostDTO postDTO) {
        PostDTO.checkIsValid(postDTO);
        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);

        // 用户发帖需要进行安全检查
        createUserPostSafetyCheck(commonUser, postDTO);

        return createPost(commonUser, postDTO);
    }

    @Override
    public boolean createPost(CommonUser commonUser, PostDTO postDTO) {
        PostDTO.checkIsValid(postDTO);
        CommonUser.assertIsLogInUser(commonUser);

        Post post = new Post();

        post.setUserId(commonUser.getUid());
        // 当为匿名发布时，identityId 不为空
        post.setIdentityId(postDTO.getIdentityId());

        post.setContent(postDTO.getContent());
        post.setMediaPath(postDTO.getMediaPath());
        post.setTagId(postDTO.getTagId());

        post.setStatus(0);
        post.setIsDelete(XiaoXiaoConstEnum.UN_DELETE.getVal());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setShareCount(0);
        post.setViewCount(0);

        post.setCreateTime(new Timestamp(System.currentTimeMillis()));
        post.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        int insert = postMapper.insert(post);

        if (postDTO.getIdentityId() == null && insert != 0) {
            informService.asyncInformFollower(commonUser, post.getId());
        }

        CompletableFuture.runAsync(
                () -> {
                    if (insert != 0 && !commonUser.getUsername().contains("推广")) {
                        syncToWxGroups(post);
                    }
                },
                threadPoolTaskExecutor
        );


        return insert != 0;
    }

    @Override
    public void syncToWxGroups(Post post) {
        String link = getSchemaLink(post.getId().toString());
        StringBuilder pre = new StringBuilder();
        for (int i = 0; i < 20 && i < post.getContent().length(); i++) {
            pre.append(post.getContent().charAt(i));
        }
        pre.append("...\n");
        String text = pre + link;
        String[] groupList = weChatConfig.getGroups().split(",");
        if (post.getTagId() != null && post.getTagId() == 2) {
            groupList = new String[]{"农职院求职交流群"};
        }
        sendToWxGroup(groupList, text);
    }

    private void sendToWxGroup(String[] groupNames, String text) {
        String url = WX_BOT_SEND_MSG_API;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode groupNameArray = objectMapper.createArrayNode();
        for (String groupName : groupNames) {
            groupNameArray.add(groupName);
        }
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("object_type", 1);
        requestBody.set("group_name", groupNameArray);
        requestBody.put("text", text);
        ;


        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
    }

    private String getSchemaLink(String id) {
        String url = SCHEMA_LINK_API + "?access_token=" + rpcService.getMiniAppAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode requestBody = objectMapper.createObjectNode().set("jump_wxa", objectMapper.createObjectNode()
                .put("path", "/pages/home/detailPage/detailPage")
                .put("query", "id=" + id));

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        JSON body = JSONUtil.parse(responseBody);
        String link = (String) body.getByPath("openlink");
        log.info("get schema link succeed {}", link);
        return link;
    }


    private void createUserPostSafetyCheck(CommonUser commonUser, PostDTO postDTO) {
        try {
//            //检查是否封禁
//            CompletableFuture<Boolean> isUserRecentlyBannedFuture = CompletableFuture.supplyAsync(
//                    () -> reportService.checkIsUserRecentlyBanned(commonUser.getUid()),
//                    threadPoolTaskExecutor
//            );

            // 内容安全审查
            CompletableFuture<Boolean> isSafeContentFuture = CompletableFuture.supplyAsync(
                    () -> contentSecurityService.msgSecCheck(postDTO.getContent(), commonUser.getOpenid()),
                    threadPoolTaskExecutor
            );

//            //检查是否封禁
//            if (isUserRecentlyBannedFuture.get()) {
//                throw new ApiException(StatusCode.FAILED, "您处于封禁状态中, 无法发帖");
//            }

            if (!isSafeContentFuture.get()) {
                throw new ApiException(StatusCode.FAILED, "内容安全审查不通过");
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("", e);
            throw new ApiException("服务器异常,发帖失败");
        }

    }

    @Override
    public IPage<PostVO> queryPostList(PostListQuery postListQuery) {
        PostListQuery.checkIsValid(postListQuery);

        // 查询数据库中的数据
        Page<Post> postPage = postMapper.selectPage(
                new Page<>(postListQuery.getCurrentPage(), postListQuery.getPageSize()),
                new LambdaQueryWrapper<Post>()
                        .eq(Post::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
                        .and(
                                postListQuery.getUserId() != null && postListQuery.getIdentityId() == null,
                                qw -> qw.eq(Post::getUserId, postListQuery.getUserId()).isNull(Post::getIdentityId)
                        )
                        .and(
                                postListQuery.getUserId() == null && postListQuery.getIdentityId() != null,
                                qw -> qw.eq(Post::getIdentityId, postListQuery.getIdentityId())
                        )
                        .eq(postListQuery.getTagId() != null, Post::getTagId, postListQuery.getTagId())
                        .orderBy(postListQuery.getOrderFlag() == 1, false, Post::getLikeCount)
                        .orderBy(postListQuery.getOrderFlag() == 2, false, Post::getCreateTime)
        );

        return PageUtil.convertPage(postPage, postList2PostVOList(postPage.getRecords()));
    }

    @Override
    public IPage<PostVO> listHomePosts(ListHomePostsDTO listHomePostsDTOy) {
        // TODO
        return null;
    }

    @Override
    public IPage<PostVO> listMyPosts(ListMyPostsDTO listMyPostsDTO) {
        // TODO: 参数校验
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();
        IPage<Post> postIPage = listUserPosts(uid, XiaoXiaoConstEnum.USER.getVal(), listMyPostsDTO);
        return PageUtil.convertPage(postIPage, postList2PostVOList(postIPage.getRecords()));
    }

    @Override
    public IPage<PostVO> listUserPosts(ListUserPostsDTO listUserPostsDTO) {
        // TODO: 参数校验
        if (Objects.equals(listUserPostsDTO.getType(), XiaoXiaoConstEnum.USER.getVal())) {
            IPage<Post> postIPage = listUserPosts(
                    listUserPostsDTO.getUid(),
                    listUserPostsDTO.getType(),
                    listUserPostsDTO
            );
            return PageUtil.convertPage(postIPage, postList2PostVOList(postIPage.getRecords()));
        }

        if (Objects.equals(listUserPostsDTO.getType(), XiaoXiaoConstEnum.ANONYMOUS_IDENTITY.getVal())) {
            IPage<Post> postIPage = listUserPosts(
                    listUserPostsDTO.getIdentityId(),
                    listUserPostsDTO.getType(),
                    listUserPostsDTO
            );
            return PageUtil.convertPage(postIPage, postList2PostVOList(postIPage.getRecords()));
        }
        return new Page<>();
    }

    private IPage<Post> listUserPosts(long targetId, int type, BasePageCondition basePageCondition) {
        if (type == XiaoXiaoConstEnum.USER.getVal()) {
            return postMapper.selectPage(
                    new Page<>(basePageCondition.getCurrentPage(), basePageCondition.getPageSize()),
                    new LambdaQueryWrapper<Post>()
                            .eq(Post::getUserId, targetId)
                            .isNull(Post::getIdentityId)
                            .eq(Post::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
                            .orderByDesc(Post::getCreateTime)
            );
        }

        if (type == XiaoXiaoConstEnum.ANONYMOUS_IDENTITY.getVal()) {
            return postMapper.selectPage(
                    new Page<>(basePageCondition.getCurrentPage(), basePageCondition.getPageSize()),
                    new LambdaQueryWrapper<Post>()
                            .eq(Post::getIdentityId, targetId)
                            .eq(Post::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
                            .orderByDesc(Post::getCreateTime)
            );
        }

        return new Page<>(basePageCondition.getCurrentPage(), basePageCondition.getPageSize());
    }

    @Override
    public List<PostVO> postList2PostVOList(List<Post> postList) {
        List<UserInfoResponse> userNameAndAvatar = rpcService.getUserNameAndAvatar(
                postList.stream()
                        .filter(item -> !isPostAnonymously(item))
                        .map(Post::getUserId)
                        .distinct()
                        .collect(Collectors.toList())
        );
        Map<Long, UserInfoResponse> userNameAndAvatarMap = userNameAndAvatar.stream()
                .collect(Collectors.toMap(UserInfoResponse::getUid, Function.identity(), (a, b) -> a));


        List<IdentityInfoResponse> anonymousNameAndAvatar = rpcService.getAnonymousNameAndAvatar(
                postList.stream()
                        .filter(this::isPostAnonymously)
                        .map(Post::getIdentityId)
                        .distinct()
                        .collect(Collectors.toList())
        );
        Map<Long, IdentityInfoResponse> anonymousNameAndAvatarMap = anonymousNameAndAvatar.stream()
                .collect(Collectors.toMap(IdentityInfoResponse::getIdentityId, Function.identity(), (a, b) -> a));

        // 点赞字段标识
        List<Long> userLikedPostIds;
        CommonUser currentUser = UserContext.getCurrentUser();
        if (CommonUser.isLogInUser(currentUser)) {
            userLikedPostIds = likeService.filterValidIds(
                    currentUser.getUid(),
                    postList.stream().map(Post::getId).collect(Collectors.toList()),
                    LikeTypeEnum.POST,
                    LikeFlagEnum.LIKE
            );
        } else {
            userLikedPostIds = new ArrayList<>();
        }

        return postList.stream().map(item -> {
                    PostVO postVO = new PostVO();
                    BeanUtils.copyProperties(item, postVO);
                    postVO.setPostId(item.getId());
                    postVO.setContent(item.getContent());
                    postVO.setCreateTime(item.getCreateTime());
                    postVO.setShareCount(item.getShareCount());
                    postVO.setLikeCount(item.getLikeCount());
                    postVO.setViewCount(item.getViewCount());
                    postVO.setCommentCount(item.getCommentCount());

                    if (isPostAnonymously(item)) {
                        IdentityInfoResponse identityInfoResponse = anonymousNameAndAvatarMap.getOrDefault(
                                item.getIdentityId(),
                                new IdentityInfoResponse()
                        );
                        postVO.setCreatorName(identityInfoResponse.getName());
                        postVO.setAvatar(identityInfoResponse.getAvatar());
                        postVO.setUserId(null);
                    } else {
                        UserInfoResponse userInfoResponse = userNameAndAvatarMap.getOrDefault(
                                item.getUserId(),
                                new UserInfoResponse()
                        );
                        postVO.setCreatorName(userInfoResponse.getName());
                        postVO.setAvatar(userInfoResponse.getAvatar());
                    }

                    postVO.setIsLike(userLikedPostIds.contains(item.getId()));
                    if (CommonUser.isLogInUser(currentUser)) {
                        postVO.setIsCreator(Objects.equals(item.getUserId(), currentUser.getUid()));
                    }
                    return postVO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 是否是匿名发帖
     */
    @Override
    public boolean isPostAnonymously(Post post) {
        return Objects.nonNull(post.getUserId()) && Objects.nonNull(post.getIdentityId());
    }

    @Override
    public PostVO getPostDetail(PostQuery postQuery) {
        PostQuery.checkIsValid(postQuery);
        Post post = postMapper.selectById(postQuery.getPostId());
        PostVO postVO = new PostVO();
        // 得改改
        BeanUtils.copyProperties(post, postVO);
        if (isPostAnonymously(post)) {
            List<IdentityInfoResponse> anonymousNameAndAvatar = rpcService.getAnonymousNameAndAvatar(
                    Collections.singletonList(postVO.getIdentityId())
            );
            IdentityInfoResponse first = CollectionUtil.getFirst(anonymousNameAndAvatar);
            if (first != null) {
                postVO.setCreatorName(first.getName());
                postVO.setAvatar(first.getAvatar());
                // 匿名发布 隐藏用户id
                postVO.setUserId(null);
            } else {
                return new PostVO();
            }
        } else {
            List<UserInfoResponse> userNameAndAvatar = rpcService.getUserNameAndAvatar(
                    Collections.singletonList(postVO.getUserId())
            );
            UserInfoResponse first = CollectionUtil.getFirst(userNameAndAvatar);
            if (first != null) {
                postVO.setCreatorName(first.getName());
                postVO.setAvatar(first.getAvatar());
            } else {
                return new PostVO();
            }
        }
        CommonUser currentUser = UserContext.getCurrentUser();
        if (CommonUser.isLogInUser(currentUser)) {
            postVO.setIsLike(
                    likeService.isUserLiked(
                            currentUser.getUid(),
                            postVO.getId(),
                            LikeTypeEnum.POST
                    )
            );
            postVO.setIsCreator(Objects.equals(post.getUserId(), currentUser.getUid()));
        }
        // TODO: 改异步
        BrowsingHistory browsingHistory = new BrowsingHistory();
        browsingHistory.setUserId(currentUser.getUid());
        browsingHistory.setBrowsingHistoryId(postQuery.getPostId());
        browsingHistory.setBrowsingTime(System.currentTimeMillis());
        browsingHistoryMapper.insertBrowsingHistory(browsingHistory);
        postMapper.updateViewCount(postVO.getId());
        return postVO;
    }

    @Override
    public int plusShareCount(Long id) {
        return postMapper.plusShareCount(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return postMapper.deleteById(id) != 0;
    }

    @Override
    public IPage<PostVO> getFollowingsPost(PostListQuery postListQuery) {
        CommonUser currentUser = UserContext.getCurrentUser();
        if (!CommonUser.isLogInUser(currentUser)) {
            return new Page<>(postListQuery.getCurrentPage(), postListQuery.getPageSize());
        }

        PostListQuery.checkIsValid(postListQuery);
        List<RpcUserVO> userVOList = rpcService.getUserFollowing(currentUser.getUid()).getRpcUserVOList();
        // 若用户没有关注任何人，则返回一个空页
        if (userVOList.size() == 0) {
            return new Page<>(postListQuery.getCurrentPage(), postListQuery.getPageSize());
        }

        List<Long> followingUidList = userVOList.stream().map(RpcUserVO::getUserId).filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> followingIdentityIdList = userVOList.stream().map(RpcUserVO::getIdentityId).filter(Objects::nonNull).collect(Collectors.toList());

        // 查询数据库中的数据
        Page<Post> postPage = postMapper.selectPage(
                new Page<>(postListQuery.getCurrentPage(), postListQuery.getPageSize()),
                new LambdaQueryWrapper<Post>()
                        .and(
                                qw -> qw
                                        .and(
                                                CollectionUtil.isNotEmpty(followingUidList),
                                                q -> q.in(Post::getUserId, followingUidList).isNull(Post::getIdentityId)
                                        )
                                        .or(
                                                CollectionUtil.isNotEmpty(followingIdentityIdList),
                                                q -> q.in(Post::getIdentityId, followingIdentityIdList)
                                        )
                        )
                        .eq(Post::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
                        .orderByDesc(Post::getCreateTime)
        );

        return PageUtil.convertPage(postPage, postList2PostVOList(postPage.getRecords()));
    }

    @Override
    public IPage<PostVO> getMyPosts(PostListQuery postListQuery) {
        PostListQuery.checkIsValid(postListQuery);
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Page<Post> postPage = postMapper.selectPage(
                new Page<>(postListQuery.getCurrentPage(), postListQuery.getPageSize()),
                new LambdaQueryWrapper<Post>()
                        .eq(Post::getUserId, currentUser.getUid())
                        .eq(Post::getIsDelete, XiaoXiaoConstEnum.UN_DELETE)
                        .isNull(postListQuery.getType() == 2, Post::getIdentityId)
                        .isNotNull(postListQuery.getType() == 3, Post::getIdentityId)
                        .orderBy(true, false, Post::getCreateTime)
        );

        return PageUtil.convertPage(postPage, postList2PostVOList(postPage.getRecords()));
    }

    @Override
    public boolean likePost(Long postId) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        LikeBO likeBO = new LikeBO();
        likeBO.setUid(uid);
        likeBO.setContentId(postId);
        likeBO.setLikeTypeEnum(LikeTypeEnum.POST);
        likeBO.setLikeFlagEnum(LikeFlagEnum.LIKE);
        if (likeService.saveOrUpdate(likeBO)) {
            UpdateWrapper<Post> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", postId);
            wrapper.setSql("like_count = like_count + 1");
            postMapper.update(null, wrapper);

            //只有点赞才需要计算 其他情况不需要
            if(likeBO.getLikeTypeEnum().equals(LikeTypeEnum.POST) && likeBO.getLikeFlagEnum().equals(LikeFlagEnum.LIKE)) {
                Post post = this.getPostById(postId);
                EnergyRequest energyRequest = new EnergyRequest();
                energyRequest.setType(EnergyEnum.LIKE.getType());
                energyRequest.setUserid(post.getUserId());
                energyRequest.setRelateId(postId);
                if(!rpcService.calculateEnergy(energyRequest)){
                    log.warn(postId+"add like failed");
                }
            }


            try {
                informService.asyncSendPostLikeInform(postId);
            } catch (Exception e) {
                log.error("发送点赞通知异常", e);
            }
        }
        return true;
    }

    @Override
    public boolean unlikePost(Long postId) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        LikeBO likeBO = new LikeBO();
        likeBO.setUid(uid);
        likeBO.setContentId(postId);
        likeBO.setLikeTypeEnum(LikeTypeEnum.POST);
        likeBO.setLikeFlagEnum(LikeFlagEnum.DELETED);
        if (likeService.saveOrUpdate(likeBO)) {
            UpdateWrapper<Post> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", postId);
            wrapper.setSql("like_count = like_count - 1");
            postMapper.update(null, wrapper);
        }
        return true;
    }

    @Override
    public boolean updatePostCommentCount(long postId, int addCount) {
        if (addCount == 0) {
            return true;
        }
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return false;
        }
        post.setCommentCount(post.getCommentCount() + addCount);
        int i = postMapper.updateById(post);
        return i != 0;
    }

    @Override
    public int getUserPostCount(Long uid) {
        if (uid == null) {
            return 0;
        }
        Long count = postMapper.selectCount(
                new LambdaQueryWrapper<Post>()
                        .eq(Post::getUserId, uid)
                        .isNull(Post::getIdentityId)
                        .eq(Post::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );
        return Math.toIntExact(count);
    }

    @Override
    public int getUserPostLikedSum(Long uid) {
        if (uid == 0) {
            return 0;
        }
        Integer count = postMapper.sumUserPostLikeCount(uid);
        if (count == null) {
            return 0;
        }
        return count;
    }

    @Override
    public int getIdentityPostCount(Long identityId) {
        if (identityId == null) {
            return 0;
        }
        Long count = postMapper.selectCount(
                new LambdaQueryWrapper<Post>()
                        .eq(Post::getIdentityId, identityId)
                        .eq(Post::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );
        return Math.toIntExact(count);
    }

    @Override
    public List<Post> listPostsByIds(List<Long> postIds) {
        if (CollectionUtil.isEmpty(postIds)) {
            return new ArrayList<>();
        }
        List<Post> posts = postMapper.selectList(
                new LambdaQueryWrapper<Post>()
                        .in(Post::getId, postIds)
                        .eq(Post::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );
        return posts == null ? new ArrayList<>() : posts;
    }

    @Override
    public Post getPostById(Long postId) {
        return postMapper.selectById(postId);
    }

    @Override
    public List<TagVO> listTags() {
        return Arrays.stream(TagEnum.values())
                .map(item -> {
                    TagVO tagVO = new TagVO();
                    tagVO.setTagId(item.getTagId());
                    tagVO.setName(item.getName());
                    return tagVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PostVO> listPin() {
        List<Post> postList = postMapper.selectList(
                new LambdaQueryWrapper<Post>()
                        .eq(Post::getStatus, 1)
                        .eq(Post::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
                        .orderByDesc(Post::getCreateTime)
        );
        Page<Post> postPage = new Page<>();
        postPage.setRecords(postList);
        return postList2PostVOList(postPage.getRecords());
    }


    @Resource
    private LikeMapper likeMapper;

    @Override
    public IPage<PostVO> likeHistory(Long currentPage, Long pageSize) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);

        Page<Like> likePage = likeMapper.selectPage(
                new Page<>(currentPage, pageSize),
                new LambdaQueryWrapper<Like>()
                        .eq(Like::getUid, currentUser.getUid())
                        .eq(Like::getType, LikeTypeEnum.POST.getType())
                        .eq(Like::getFlag, LikeFlagEnum.LIKE.getFlag())
                        .orderByDesc(Like::getUpdateTime)
        );

        List<Post> posts = listPostsByIds(
                likePage.getRecords()
                        .stream()
                        .map(Like::getContentId)
                        .collect(Collectors.toList())
        );

        List<PostVO> postVOS = postList2PostVOList(posts);

        return PageUtil.convertPage(likePage, postVOS);
    }
}
