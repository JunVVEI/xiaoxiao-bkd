package com.xiaoxiao.bbs.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.dto.ListMyPostsDTO;
import com.xiaoxiao.bbs.model.dto.PostDTO;
import com.xiaoxiao.bbs.model.dto.PostListQuery;
import com.xiaoxiao.bbs.model.dto.PostQuery;
import com.xiaoxiao.bbs.model.vo.PostVO;
import com.xiaoxiao.bbs.model.vo.TagVO;
import com.xiaoxiao.bbs.service.PostService;
import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Junwei
 * @since 2022-10-27 11:23:00
 */
@RestController
@RequestMapping("/bbs/post")
public class PostController {

    @Resource
    private PostService postService;

    /**
     * 用户发帖
     */
    @PostMapping("/createPost")
    @XiaoXiaoResponseBody
    public boolean createPost(@RequestBody PostDTO postDTO) {
        return postService.createUserPost(postDTO);
    }

    /**
     * 列表查询帖子, 首页使用
     */
    @PostMapping("/query")
    @XiaoXiaoResponseBody
    @Deprecated
    public IPage<PostVO> queryPostList(@RequestBody PostListQuery postListQuery) {
        return postService.queryPostList(postListQuery);
    }

    /**
     * 我的帖子列表
     */
    @PostMapping("/listMyPosts")
    @XiaoXiaoResponseBody
    public IPage<PostVO> listMyPosts(@RequestBody ListMyPostsDTO listMyPostsDTO) {
        return postService.listMyPosts(listMyPostsDTO);
    }

    /**
     * 获取帖子详情
     */
    @PostMapping("/post")
    @XiaoXiaoResponseBody
    public PostVO getOnePost(@RequestBody PostQuery postQuery) {
        return postService.getPostDetail(postQuery);
    }

    /**
     * 分享量记录
     */
    @PostMapping("/shareCount")
    @XiaoXiaoResponseBody
    public boolean plusShareCount(@RequestParam Long id) {
        return postService.plusShareCount(id) > 0;
    }

    /**
     * 删除帖子
     */
    @PostMapping("/delPost")
    @XiaoXiaoResponseBody
    public boolean delPost(@RequestParam Integer id) {
        return postService.deleteById(id.longValue());
    }

    /**
     * 关注的人帖子列表
     */
    @PostMapping("/getPostsByFollow")
    @XiaoXiaoResponseBody
    public IPage<PostVO> getPostsByFollow(@RequestBody PostListQuery postListQuery) {
        return postService.getFollowingsPost(postListQuery);
    }

    /**
     * 我的帖子列表
     */
    @PostMapping("/getMyPosts")
    @XiaoXiaoResponseBody
    public IPage<PostVO> getMyPosts(@RequestBody PostListQuery postListQuery) {
        return postService.getMyPosts(postListQuery);
    }

    /**
     * 点赞帖子
     */
    @PostMapping("/like")
    @XiaoXiaoResponseBody
    public boolean likePost(Long postId) {
        return postService.likePost(postId);
    }

    /**
     * 取消点赞帖子
     */
    @PostMapping("/unlike")
    @XiaoXiaoResponseBody
    public boolean unlikePost(Long postId) {
        return postService.unlikePost(postId);
    }

    /**
     * 获取标签列表
     */
    @GetMapping("/listTags")
    @XiaoXiaoResponseBody
    public List<TagVO> listTags() {
        return postService.listTags();
    }


    /**
     * 列表所有置顶帖子
     */
    @GetMapping("listPin")
    @XiaoXiaoResponseBody
    public List<PostVO> listPin() {
        return postService.listPin();
    }

    @GetMapping("likeHistory")
    @XiaoXiaoResponseBody
    public IPage<PostVO> likeHistory(Long currentPage, Long pageSize) {
        return postService.likeHistory(currentPage, pageSize);
    }
}

