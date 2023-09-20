package com.xiaoxiao.bbs.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.vo.PostVO;
import com.xiaoxiao.bbs.service.BrowsingHistoryService;
import com.xiaoxiao.bbs.model.entity.BrowsingHistory;
import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 浏览历史 前端控制器
 * </p>
 *
 * @author yaoyao
 * @since 2023-05-28 10:24:34
 */
@RestController
@RequestMapping("/bbs/browsingHistory")
public class BrowsingHistoryController {
    @Resource
    private BrowsingHistoryService browsingHistoryService;

    @GetMapping
    @XiaoXiaoResponseBody
    public IPage<PostVO> searchBrowsingHistory(Long currentPage, Long pageSize) {
        return browsingHistoryService.searchBrowsingHistory(currentPage, pageSize);
    }
}

