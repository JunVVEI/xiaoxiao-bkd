package com.xiaoxiao.bbs.model.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.bbs.model.entity.Activity;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * @author mayunfei
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityDTO extends Activity {


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
     * 媒体路径
     */
    private String mediaPath;

    public static void checkIsValid(ActivityDTO activityDTO) {
        AssertUtil.isTrue(StrUtil.isNotBlank(activityDTO.getActivityContent()), "参数校验失败");
        AssertUtil.isTrue(StrUtil.isNotBlank(activityDTO.getActivityLocal()), "参数校验失败");
        AssertUtil.isTrue(StrUtil.isNotBlank(activityDTO.getActivityNotice()), "参数校验失败");
        AssertUtil.isTrue(StrUtil.isNotBlank(activityDTO.getActivityTopic()), "参数校验失败");
        AssertUtil.isTrue(activityDTO.getActivityForm() != null, "参数校验失败");
        AssertUtil.isTrue(activityDTO.getActivityNumberOfPeople() != null, "参数校验失败");
        AssertUtil.isTrue(activityDTO.getActivityTime() != null, "参数校验失败");

    }

    public static Activity prepareActivity(ActivityDTO activityDTO) {
        Activity activity = new Activity();
        BeanUtil.copyProperties(activityDTO, activity);
        return activity;
    }
}
