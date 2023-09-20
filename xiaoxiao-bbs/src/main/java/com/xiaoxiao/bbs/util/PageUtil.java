package com.xiaoxiao.bbs.util;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public class PageUtil {

    public static <T, E> IPage<E> convertPage(IPage<T> iPage, List<E> list) {
        return ((IPage<E>) iPage).setRecords(list);
    }
}
