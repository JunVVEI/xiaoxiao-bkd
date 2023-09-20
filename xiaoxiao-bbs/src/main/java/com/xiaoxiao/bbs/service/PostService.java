package com.xiaoxiao.bbs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.dto.*;
import com.xiaoxiao.bbs.model.entity.Post;
import com.xiaoxiao.bbs.model.vo.PostVO;
import com.xiaoxiao.bbs.model.vo.TagVO;
import com.xiaoxiao.common.user.CommonUser;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Junwei
 * @since 2022-10-27 11:23:00
 */
public interface PostService {

    /**
     * 用户发帖
     */
    boolean createUserPost(PostDTO postDTO);

    /**
     * 发帖
     */
    boolean createPost(CommonUser commonUser, PostDTO postDTO);

    /**
     * 分页查询帖子列表
     */
    @Deprecated
    IPage<PostVO> queryPostList(PostListQuery postListQuery);

    /**
     * 首页帖子列表
     */
    IPage<PostVO> listHomePosts(ListHomePostsDTO listHomePostsDTO);

    /**
     * 我的帖子列表
     */
    IPage<PostVO> listMyPosts(ListMyPostsDTO listMyPostsDTO);

    /**
     * 查询用户帖子列表
     */
    IPage<PostVO> listUserPosts(ListUserPostsDTO listUserPostsDTO);

    /**
     * 获取帖子详情
     */
    PostVO getPostDetail(PostQuery postQuery);

    /**
     * 帖子浏览量自增
     *
     * @param id 帖子id
     * @return 影响行数
     */
    int plusShareCount(Long id);

    /**
     * 删除帖子
     *
     * @param id 帖子id
     * @return 删除结果
     */
    boolean deleteById(Long id);

    /**
     * 获取关注的人的帖子列表
     *
     * @param postListQuery 帖子列表请求参数
     * @return 分页结果
     */
    IPage<PostVO> getFollowingsPost(PostListQuery postListQuery);

    /**
     * 查看自己的发帖列表
     *
     * @param postListQuery 帖子请求参数
     * @return 分页结果
     */
    @Deprecated
    IPage<PostVO> getMyPosts(PostListQuery postListQuery);

    /**
     * 点赞帖子
     */
    boolean likePost(Long postId);

    /**
     * 取消点赞帖子
     */
    boolean unlikePost(Long postId);

    /**
     * 更新帖子的评论数量，add 表示需要增加的数量，如果为负数则表示减少add，返回执行是否成功
     */
    boolean updatePostCommentCount(long postId, int addCount);

    /**
     * 获取用户发帖量
     */
    int getUserPostCount(Long uid);


    /**
     * 获取帖子用户获赞量
     */
    int getUserPostLikedSum(Long uid);

    /**
     * 获取匿名用户发帖量
     */
    int getIdentityPostCount(Long identityId);

    List<PostVO> postList2PostVOList(List<Post> postList);

    /**
     * 根据id列表查询post
     */
    List<Post> listPostsByIds(List<Long> postIds);

    /**
     * 根据帖子id获取帖子实体
     */
    Post getPostById(Long postId);

    /**
     * 列表查询标签
     */
    List<TagVO> listTags();

    /**
     * 是否匿名发帖
     */
    boolean isPostAnonymously(Post post);

    /**
     * 列表所有置顶帖子
     */
    List<PostVO> listPin();


    void syncToWxGroups(Post post);

    /**
     * 获取点赞历史
     */
    IPage<PostVO> likeHistory(Long currentPage, Long pageSize);
}
