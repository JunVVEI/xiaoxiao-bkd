package com.xiaoxiao.bbs.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoxiao.bbs.model.entity.Activity;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author mayunfei
 */
@Data
public class ActivityVO {

    private String creatorName;

    /**
     * 打气计数
     */
    private Integer cheer;

    /**
     * 活动标题
     */
    private String activityTopic;

    /**
     * 活动形式
     */
    private Integer activityForm;

    /**
     * 活动举行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp activityTime;

    /**
     * 活动举办地点
     */
    private String activityLocal;



    /**
     * 活动人数
     */
    private Integer activityNumberOfPeople;

    /**
     * 活动须知
     */
    private String activityNotice;

    /**
     * 活动内容
     */
    private String activityContent;

    /**
     * 文件路径
     */
    private String mediaPath;

    public ActivityVO prepareActivityVO(Activity activity) {
        ActivityVO activityVO = new ActivityVO();
        BeanUtil.copyProperties(activity, activityVO);
        return activityVO;
    }
}
