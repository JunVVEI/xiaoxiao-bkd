package com.xiaoxiao.bbs.model.dto;

import com.xiaoxiao.common.api.BasePageCondition;
import lombok.Data;

/**
 * @author zjw
 */
@Data
public class SearchQuery extends BasePageCondition {

    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 搜索类型（0：综合，1:帖子，2：活动，3：用户）
     */
    private Integer type;

    /**
     * 用户真实id
     */
    private Long userId;

}
