package com.xiaoxiao.bbs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.entity.BrowsingHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.bbs.model.vo.PostVO;

/**
 * <p>
 * 浏览历史 服务类
 * </p>
 *
 * @author yaoyao
 * @since 2023-05-28 10:24:34
 */
public interface BrowsingHistoryService {

    /**
     * 分页查询浏览历史
     */
    IPage<PostVO> searchBrowsingHistory(Long currentPage, Long pageSize);
}
