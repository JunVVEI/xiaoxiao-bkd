package com.xiaoxiao.taskCourier.model.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RewardTaskVO {

    private Long id;

    private Long uid;

    private String userName;

    private String userAvatar;

    private Boolean isCreator;

    /**
     * 内容
     */
    private String content;

    /**
     * 媒体资源路径逗号分隔
     */
    private String mediaPath;

    /**
     * 赏金
     */
    private Double bounty;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 状态
     */
    private Integer status;


    /**
     * 状态
     */
    private String statusName;

    private Long commentCount;

    private Timestamp createTime;

    private Integer type;
}
