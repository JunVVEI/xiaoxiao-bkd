package com.xiaoxiao.bbs.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.dto.PostListQuery;
import com.xiaoxiao.bbs.model.vo.PostVO;
import com.xiaoxiao.bbs.service.HotPostService;
import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 林嫄袁
 * @since 2023/3/8 20:00
 */
@RestController
@RequestMapping("/bbs/post/hotpost")
public class HotPostController {
    @Resource
    HotPostService hotPostService;

    /**
     * 获取热度前100的帖子
     *
     * @return 热度前一百的帖子的数据
     */
    @PostMapping("/queryList")
    @XiaoXiaoResponseBody
    public IPage<PostVO> getHotPostList(@RequestBody PostListQuery postListQuery) {
        return hotPostService.getHotPostList(postListQuery);
    }

    /**
     * 计算热度前100的帖子并存入redis
     */
    @GetMapping("/hot")
    @XiaoXiaoResponseBody
    public void hot() {
        hotPostService.queryHotPost();
    }
}
