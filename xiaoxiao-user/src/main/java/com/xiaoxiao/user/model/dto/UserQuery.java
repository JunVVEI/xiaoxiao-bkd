package com.xiaoxiao.user.model.dto;

import com.xiaoxiao.common.api.BasePageCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zjw
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserQuery extends BasePageCondition {

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String userName;

    /**
     *
     */
    private Long roleId;
}
