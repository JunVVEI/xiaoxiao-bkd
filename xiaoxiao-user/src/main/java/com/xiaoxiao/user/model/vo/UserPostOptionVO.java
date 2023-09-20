package com.xiaoxiao.user.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserPostOptionVO {

    private List<PostOption> postOptionList;

    @Data
    public static class PostOption {
        /**
         * 类型 1实名， 2匿名
         */
        private int type;

        /**
         * 前端显示给用户的值
         */
        private String value;

        private String avatar;

        private Long uid;

        private Long identityId;
    }
}
