package com.xiaoxiao.toolbag.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.xiaoxiao.toolbag.model.entity.Encyclopedia;
import lombok.Data;

@Data
public class EncyclopediaVO {

    /**
     * id
     */
    private Long id;

    /**
     * docKey
     */
    private String docKey;

    /**
     * 标题
     */
    private String title;

    /**
     * 原始内容
     */
    private String rowContent;

    /**
     * MarkDown内容
     */
    private String mdContent;

    /**
     * 浏览量
     */
    private Integer viewCount;

    public static EncyclopediaVO prepareEncyclopediaVO(Encyclopedia encyclopedia) {
        EncyclopediaVO encyclopediaVO = new EncyclopediaVO();
        BeanUtil.copyProperties(encyclopedia, encyclopediaVO);
        return encyclopediaVO;
    }
}
