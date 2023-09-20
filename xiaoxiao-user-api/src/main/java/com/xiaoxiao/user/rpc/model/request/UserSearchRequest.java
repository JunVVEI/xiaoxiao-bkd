package com.xiaoxiao.user.rpc.model.request;

import com.xiaoxiao.common.api.BasePageCondition;
import lombok.Data;

/**
 * @author zjw
 */
@Data
public class UserSearchRequest extends BasePageCondition {
    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 用户真实id
     */
    private Long userId;

}
