package com.xiaoxiao.user.rpc.model.response;

import lombok.Data;

@Data
public class UserInfoResponse {

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 名称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;
}
