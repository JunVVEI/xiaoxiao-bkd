package com.xiaoxiao.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目通用约定的一些常量枚举值，只写整个项目都约定的值，具体到每一个模块的枚举值写到，对应模块中
 *
 * @author chenjunwei
 */
@Getter
@AllArgsConstructor
public enum XiaoXiaoConstEnum {

    /**
     * 失败
     */
    FAILED(0, "失败"),

    /**
     * 成功
     */
    SUCCESS(1, "成功"),

    /**
     * 未删除
     */
    UN_DELETE(0, "未删除"),

    /**
     * 已删除
     */
    DELETED(1, "已删除"),

    /**
     * 未启动
     */
    DISABLE(0, "未启动"),

    /**
     * 启用
     */
    ENABLE(1, "启用"),

    /**
     * 展示
     */
    SHOW(1, "展示"),

    /**
     * 不展示
     */
    UN_SHOW(0, "不展示"),

    /**
     * 实名、非匿名、真实身份
     */
    USER(1, "实名、非匿名、真实身份"),

    /**
     * 匿名
     */
    ANONYMOUS_IDENTITY(2, "匿名"),
    /**
     * 发送消息
     */
    SEND_MSG(1, "发送消息"),

    /**
     * 不发送消息
     */
    NO_SEND_MSG(0, "不发送消息"),
    ;


    private final Integer val;
    private final String desc;
}
