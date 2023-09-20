package com.xiaoxiao.bbs.controller;

import com.xiaoxiao.bbs.model.dto.SearchQuery;
import com.xiaoxiao.bbs.service.SearchService;
import com.xiaoxiao.common.api.CommonResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zjw
 */
@RestController()
@RequestMapping("/bbs")
public class SearchController {

    @Resource
    private SearchService searchService;

    @GetMapping("/search")
    public CommonResp searchByKeyword(SearchQuery searchQuery){
        return searchService.searchByKeyword(searchQuery);
    }

}
