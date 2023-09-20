package com.xiaoxiao.common.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author JunWEI
 * @since 2022/10/21
 */
@Getter
@ToString
public class CommonPage {

    private final List<?> data;

    private final Long currentPage;

    private final Long totalPage;

    private final Long pageSize;

    private final Long currentPageSize;

    private CommonPage(List<?> data, Long currentPage, Long pageSize, Long totalPage) {
        this.data = data;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.pageSize = pageSize;
        this.currentPageSize = (long) data.size();
    }

    public static CommonPage newCommonPage(List<?> data, Long currentPage, Long pageSize, Long totalPage) {
        return new CommonPage(data, currentPage, pageSize, totalPage);
    }

    public static CommonPage newCommonPage(List<?> data, Long currentPage, Long pageSize) {
        return newCommonPage(data, currentPage, pageSize, null);
    }

    public static CommonPage newCommonPage(List<?> data, Integer currentPage, Integer pageSize) {
        return newCommonPage(data, (long) currentPage, (long) pageSize, null);
    }

    public static CommonPage newCommonPage(List<?> data, Integer currentPage, Integer pageSize, Integer totalPage) {
        return newCommonPage(data, (long) currentPage, (long) pageSize, (long) totalPage);
    }

    public static CommonPage newCommonPage(IPage page) {
        return newCommonPage(page.getRecords(), page.getCurrent(), page.getSize(), (long) Math.ceil((double) page.getTotal() / (double) page.getSize()));
    }

}
