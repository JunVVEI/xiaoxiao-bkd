package com.xiaoxiao.bbs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.bbs.mapper.BrowsingHistoryMapper;
import com.xiaoxiao.bbs.model.entity.BrowsingHistory;
import com.xiaoxiao.bbs.model.entity.Post;
import com.xiaoxiao.bbs.model.vo.PostVO;
import com.xiaoxiao.bbs.service.BrowsingHistoryService;
import com.xiaoxiao.bbs.service.PostService;
import com.xiaoxiao.bbs.util.PageUtil;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 浏览历史 服务实现类
 * </p>
 *
 * @author yaoyao
 * @since 2023-05-28 10:24:34
 */
@Service
public class BrowsingHistoryServiceImpl implements BrowsingHistoryService {

    @Resource
    BrowsingHistoryMapper browsingHistoryMapper;
    @Resource
    PostService postService;

    public IPage<PostVO> searchBrowsingHistory(Long currentPage, Long pageSize) {
        currentPage = currentPage == null ? 1 : currentPage;
        pageSize = pageSize == null ? 20 : pageSize;
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        Page<BrowsingHistory> browsingHistoryPage = browsingHistoryMapper.selectPage(
                new Page<>(currentPage, pageSize),
                new LambdaQueryWrapper<BrowsingHistory>()
                        .eq(BrowsingHistory::getUserId, uid)
                        .orderByDesc(BrowsingHistory::getBrowsingTime)
        );
        if (CollectionUtil.isEmpty(browsingHistoryPage.getRecords())) {
            return new Page<>();
        }
        List<Long> postIds = browsingHistoryPage.getRecords().stream()
                .map(BrowsingHistory::getBrowsingHistoryId)
                .collect(Collectors.toList());
        List<Post> postList = postService.listPostsByIds(postIds);
        Map<Long, Post> postMap = postList.stream().collect(Collectors.toMap(Post::getId, Function.identity()));
        List<Post> alignPostList = new ArrayList<>();
        for (Long postId : postIds) {
            Post post = postMap.get(postId);
            if (post != null) {
                alignPostList.add(post);
            }
        }
        return PageUtil.convertPage(browsingHistoryPage, postService.postList2PostVOList(alignPostList));
    }
}
