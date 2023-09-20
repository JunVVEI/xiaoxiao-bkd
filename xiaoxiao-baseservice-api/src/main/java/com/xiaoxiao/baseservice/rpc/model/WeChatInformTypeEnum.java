package com.xiaoxiao.baseservice.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum WeChatInformTypeEnum {

    LIKE(1, "您有新的点赞", "点赞"),

    COMMENT(2, "您有新的评论", "评论"),

    SUB_COMMENT(3, "您的评论有新的回复", "回复评论"),

    FOLLOW(4, "您有新的关注", "关注"),

    FOLLOWING_NEW_POST(5, "您关注的人有新的动态", "关注的人动态"),

    POST_MARK(6, "您关注的帖子有新的动态", "关注的帖子动态"),

    POST_COURSE(7, "您明天的课表已到达", "您明天的课表已到达"),

    LOTTERY1(8, "<高中趣事>一等奖获奖通知", "获奖通知"),

    LOTTERY2(9, "<高中趣事>二等奖获奖通知", "获奖通知"),

    LOTTERY3(10, "<高中趣事>三等奖获奖通知", "获奖通知"),
    ;

    private final int type;

    private final String showContent;

    private final String desc;

    public static WeChatInformTypeEnum getWeChatInformTypeEnumByType(int type) {
        return Arrays.stream(WeChatInformTypeEnum.values())
                .filter(weChatInformTypeEnum -> weChatInformTypeEnum.getType() == type)
                .findFirst()
                .orElse(null);
    }

}
