package com.xiaoxiao.bbs.model.dto;

import com.xiaoxiao.common.api.BasePageCondition;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ListUserPostsDTO extends BasePageCondition {
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 匿名id
     */
    private Long identityId;

    /**
     * 类型
     * {@link com.xiaoxiao.common.constant.XiaoXiaoConstEnum#USER}
     * {@link com.xiaoxiao.common.constant.XiaoXiaoConstEnum#ANONYMOUS_IDENTITY}
     */
    private Integer type;

}
