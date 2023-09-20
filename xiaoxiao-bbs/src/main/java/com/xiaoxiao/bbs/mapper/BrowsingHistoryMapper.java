package com.xiaoxiao.bbs.mapper;

import com.xiaoxiao.bbs.model.entity.BrowsingHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 浏览历史 Mapper 接口
 * </p>
 *
 * @author yaoyao
 * @since 2023-05-28 10:24:34
 */
@Mapper
public interface BrowsingHistoryMapper extends BaseMapper<BrowsingHistory> {
    Long insertBrowsingHistory(BrowsingHistory browsingHistory);
}
