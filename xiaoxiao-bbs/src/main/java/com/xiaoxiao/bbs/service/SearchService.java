package com.xiaoxiao.bbs.service;

import com.xiaoxiao.bbs.model.dto.SearchQuery;
import com.xiaoxiao.common.api.CommonResp;

/**
 * @author zjw
 */
public interface SearchService {

    /**
     * 根据关键词搜索
     * @param searchQuery 请求参数
     * @return 分页结果
     */
    CommonResp searchByKeyword(SearchQuery searchQuery);

}
