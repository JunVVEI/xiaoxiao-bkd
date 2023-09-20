package com.xiaoxiao.bbs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.bbs.config.ThreadPoolConfig;
import com.xiaoxiao.bbs.mapper.ActivityMapper;
import com.xiaoxiao.bbs.mapper.PostMapper;
import com.xiaoxiao.bbs.model.dto.SearchQuery;
import com.xiaoxiao.bbs.model.entity.Post;
import com.xiaoxiao.bbs.model.vo.ActivityVO;
import com.xiaoxiao.bbs.model.vo.PostVO;
import com.xiaoxiao.bbs.service.PostService;
import com.xiaoxiao.bbs.service.SearchService;
import com.xiaoxiao.bbs.util.PageUtil;
import com.xiaoxiao.common.api.CommonPage;
import com.xiaoxiao.common.api.CommonResp;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.user.rpc.model.request.UserSearchRequest;
import com.xiaoxiao.user.rpc.model.response.UserSearchResponse;
import com.xiaoxiao.user.rpc.service.RpcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author zjw
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Resource
    private PostMapper postMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private RpcUserService rpcUserService;

    @Resource
    private PostService postService;

    @Resource(name = ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static final int TYPE_POST = 1;
    private static final int TYPE_ACTIVITY = 2;
    private static final int TYPE_USER = 3;

    @Override
    public CommonResp searchByKeyword(SearchQuery searchQuery) {
        // 获取用户id
        if (Objects.isNull(searchQuery.getUserId())) {
            CommonUser currentUser = UserContext.getCurrentUser();
            searchQuery.setUserId(currentUser.getUid());
        }
        // 仅搜索帖子
        if (searchQuery.getType().equals(TYPE_POST)) {
            return CommonResp.success(searchPostByKeyword(searchQuery));
        }
        // 仅搜索活动
        if (searchQuery.getType().equals(TYPE_ACTIVITY)) {
            return CommonResp.success(searchActivityByKeyword(searchQuery));
        }
        // 仅搜索用户
        if (searchQuery.getType().equals(TYPE_USER)) {
            return CommonResp.success(searchUserByKeyword(searchQuery));
        }
        // 异步搜索帖子
        CompletableFuture<CommonPage> postcf = CompletableFuture.supplyAsync(
                () -> searchPostByKeyword(searchQuery),
                threadPoolTaskExecutor
        ).whenComplete(
                (result, ex) -> {
                    if (Objects.nonNull(ex)) {
                        log.error("搜索帖子异常: {}", ex);
                    }
                }
        );
        // 异步搜索活动
        CompletableFuture<CommonPage> activitycf = CompletableFuture.supplyAsync(
                () -> searchActivityByKeyword(searchQuery),
                threadPoolTaskExecutor
        ).whenComplete(
                (result, ex) -> {
                    if (Objects.nonNull(ex)) {
                        log.error("搜索活动异常: {}", ex);
                    }
                }
        );
        CommonPage userPage = searchUserByKeyword(searchQuery);
        CommonPage postPage = null;
        CommonPage activityPage = null;
        try {
            postPage = postcf.get();
            activityPage = activitycf.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> data = new HashMap<>(3);
        data.put("post", postPage);
        data.put("activity", activityPage);
        data.put("user", userPage);
        return CommonResp.success(data);
    }

    /**
     * 根据关键词搜索用户
     *
     * @param searchQuery 搜索请求封装
     * @return 搜索分页结果
     */
    private CommonPage searchUserByKeyword(SearchQuery searchQuery) {
        UserSearchRequest userSearchRequest = BeanUtil.copyProperties(searchQuery, UserSearchRequest.class);
        CommonResp<UserSearchResponse> userSearchResponseCommonResp = rpcUserService.searchUserByKeyword(userSearchRequest);
        UserSearchResponse userSearchResponse = userSearchResponseCommonResp.getData();
        CommonPage userPage = CommonPage.newCommonPage(userSearchResponse.getRpcFollowUserVOPage());
        return userPage;
    }

    /**
     * 根据关键词搜索帖子
     *
     * @param searchQuery 搜索请求封装
     * @return 搜索分页结果
     */
    private CommonPage searchPostByKeyword(SearchQuery searchQuery) {
        Page<PostVO> postVOPage = postMapper.queryPostListByKeyword(
                new Page<>(searchQuery.getCurrentPage(), searchQuery.getPageSize()),
                searchQuery.getKeyword(),
                searchQuery.getUserId()
        );
        if (CollectionUtil.isEmpty(postVOPage.getRecords())) {
            return CommonPage.newCommonPage(new Page<PostVO>());
        }
        Page<Post> postPage = postMapper.selectPage(
                new Page<>(searchQuery.getCurrentPage(), searchQuery.getPageSize()),
                new LambdaQueryWrapper<Post>()
                        .in(
                                Post::getId,
                                postVOPage.getRecords().stream().map(PostVO::getId).collect(Collectors.toList())
                        )
                        .orderByDesc(Post::getCreateTime)
        );
        return CommonPage.newCommonPage(
                PageUtil.convertPage(postPage, postService.postList2PostVOList(postPage.getRecords()))
        );
    }

    /**
     * 根据关键词搜索活动
     *
     * @param searchQuery 搜索请求封装
     * @return 搜索分页结果
     */
    private CommonPage searchActivityByKeyword(SearchQuery searchQuery) {
        Page<ActivityVO> activityVOPage = activityMapper.queryActivityListByKeyword(
                new Page<>(searchQuery.getCurrentPage(), searchQuery.getPageSize()),
                searchQuery.getKeyword(),
                searchQuery.getUserId()
        );
        return CommonPage.newCommonPage(activityVOPage);
    }
}
