package com.xiaoxiao.bbs.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxiao.bbs.mapper.PostMapper;
import com.xiaoxiao.bbs.model.dto.PostListQuery;
import com.xiaoxiao.bbs.model.entity.Post;
import com.xiaoxiao.bbs.model.vo.PostVO;
import com.xiaoxiao.bbs.service.HotPostService;
import com.xiaoxiao.bbs.service.PostService;
import com.xiaoxiao.bbs.util.PageUtil;
import com.xiaoxiao.common.cache.RedisService;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j

public class HotPostServiceImpl extends ServiceImpl<PostMapper, Post> implements HotPostService {
    @Resource
    private RedisService redisService;

    @Resource
    private PostMapper postMapper;

    @Resource
    private PostService postService;

    private static final int TopN = 500;

    private final String TOP_POSTS_KEY = "top_posts";

    /**
     * 获取热度前100的帖子
     *
     * @return 热度前100的帖子数据
     */
    @Override
    public IPage<PostVO> getHotPostList(PostListQuery postListQuery) {
        try {
            String topPostsKey = getTopPostsKeyFromTagId(postListQuery.getTagId());
            // 将 Redis 中查出热榜数据
            Set<String> hotPostSet = redisService.reverseRange(
                    topPostsKey,
                    (postListQuery.getCurrentPage() - 1) * postListQuery.getPageSize(),
                    postListQuery.getCurrentPage() * postListQuery.getPageSize() - 1
            );

            // 将 Set 集合中 string 类型的帖子 id 转为 long 型
            List<Long> idList = hotPostSet.stream().map(Long::parseLong).collect(Collectors.toList());

            // 批量查询
            List<Post> postList = postService.listPostsByIds(idList);
            // in查询出来的数据不一定和in中的数据排序一致，需手动重新排一次
            Map<Long, Post> postMap = postList.stream().collect(Collectors.toMap(Post::getId, Function.identity()));
            List<Post> alignPostList = new ArrayList<>();
            for (Long postId : idList) {
                Post post = postMap.get(postId);
                if (post != null) {
                    alignPostList.add(post);
                }
            }
            if (alignPostList.size() == 0) {
                return postService.queryPostList(postListQuery);
            }
            Page<Post> postPage = new Page<>();
            postPage.setRecords(alignPostList);
            postPage.setTotal(TopN);
            postPage.setSize(postListQuery.getPageSize());
            postPage.setCurrent(postListQuery.getCurrentPage());
            return PageUtil.convertPage(postPage, postService.postList2PostVOList(postPage.getRecords()));
        } catch (Exception e) {
            log.error("获取帖子发生异常{}，执行兜底逻辑,page{}", e, postListQuery);
            postListQuery.setOrderFlag(2);
            return postService.queryPostList(postListQuery);
        }
    }

    private String getTopPostsKeyFromTagId(Long tagId) {
        if (tagId == null) {
            return TOP_POSTS_KEY;
        }
        return TOP_POSTS_KEY + ":" + tagId;
    }

    @Override
    @Scheduled(fixedRate = 60 * 5 * 1000)
    public void queryHotPost() {
        int pageSize = 100;
        int currentPage = 1;
        boolean hasNextPage = true;

        Map<Long, PriorityQueue<Post>> topPostsMap = new HashMap<>();
        PriorityQueue<Post> topPostsGeneral = new PriorityQueue<>(TopN, Comparator.comparingDouble(Post::getHotScore));

        while (hasNextPage) {
            Page<Post> page = new Page<>(currentPage, pageSize);
            page = postMapper.selectPage(page, null);
            List<Post> posts = page.getRecords();
            for (Post post : posts) {
                if (XiaoXiaoConstEnum.DELETED.getVal().equals(post.getIsDelete())) {
                    continue;
                }
                Long tagId = post.getTagId();
                PriorityQueue<Post> topPosts = topPostsMap.getOrDefault(tagId, new PriorityQueue<>(TopN, Comparator.comparingDouble(Post::getHotScore)));
                double score = post.getHotScore();
                if (topPosts.size() < TopN) {
                    topPosts.offer(post);
                } else if (score > topPosts.peek().getHotScore()) {
                    topPosts.poll();
                    topPosts.offer(post);
                }
                topPostsMap.put(tagId, topPosts);

                // maintain general top posts list
                if (topPostsGeneral.size() < TopN) {
                    topPostsGeneral.offer(post);
                } else if (score > topPostsGeneral.peek().getHotScore()) {
                    topPostsGeneral.poll();
                    topPostsGeneral.offer(post);
                }
            }
            hasNextPage = page.hasNext();
            currentPage++;
        }

        for (Map.Entry<Long, PriorityQueue<Post>> entry : topPostsMap.entrySet()) {
            redisService.swapHotPost(TOP_POSTS_KEY + ":" + entry.getKey(), formatTopPostsForLua(entry.getValue()));
        }

        redisService.swapHotPost(TOP_POSTS_KEY, formatTopPostsForLua(topPostsGeneral));

        log.info("15min数据更新行为完成");
    }

    private String[] formatTopPostsForLua(PriorityQueue<Post> topPosts) {
        String[] formattedPosts = new String[topPosts.size() * 2];
        int i = 0;
        for (Post post : topPosts) {
            formattedPosts[i++] = String.valueOf(post.getHotScore());
            formattedPosts[i++] = String.valueOf(post.getId());
        }
        return formattedPosts;
    }
}
