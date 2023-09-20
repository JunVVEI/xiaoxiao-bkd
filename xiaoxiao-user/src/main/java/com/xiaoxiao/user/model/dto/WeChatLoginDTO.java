package com.xiaoxiao.user.model.dto;

import lombok.Data;

@Data
public class WeChatLoginDTO {

    private String code;

    private String userName;

    private String avatar;
}
