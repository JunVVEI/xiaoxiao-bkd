package com.xiaoxiao.common.api;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author JunWEI
 * @since 2022/10/21
 */
@Data
public class BasePageCondition {

    // 页码
    private Long currentPage = 1L;

    // 页大小
    private Long pageSize = 10L;

}
