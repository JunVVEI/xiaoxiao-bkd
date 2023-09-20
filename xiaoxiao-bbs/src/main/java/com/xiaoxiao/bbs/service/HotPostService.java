package com.xiaoxiao.bbs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.bbs.model.dto.PostListQuery;
import com.xiaoxiao.bbs.model.entity.Post;
import com.xiaoxiao.bbs.model.vo.PostVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @author myf lyy
 */
public interface HotPostService extends IService<Post> {

    /**
     * 获取热榜前100的帖子数据
     *
     * @return 热度前一百的帖子的数据
     */
    IPage<PostVO> getHotPostList(PostListQuery postListQuery);

    /**
     * 每15min从mysql中取出热榜数据数据存入redis
     */
    void queryHotPost();
}

