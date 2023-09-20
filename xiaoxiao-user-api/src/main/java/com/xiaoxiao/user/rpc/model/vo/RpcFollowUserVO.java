package com.xiaoxiao.user.rpc.model.vo;

import lombok.Data;

/**
 * @author zjw
 */
@Data
public class RpcFollowUserVO extends RpcUserVO{
    /**
     * 关系类型
     */
    private Integer relType;

    /**
     * 关系名称
     */
    private String relName;
}
