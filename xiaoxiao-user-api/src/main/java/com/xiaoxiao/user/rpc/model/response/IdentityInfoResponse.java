package com.xiaoxiao.user.rpc.model.response;

import lombok.Data;

@Data
public class IdentityInfoResponse {

    /**
     * 用户id
     */
    private Long identityId;

    /**
     * 名称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;
}
