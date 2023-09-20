package com.xiaoxiao.bbs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.bbs.model.vo.ActivityVO;
import com.xiaoxiao.bbs.model.dto.ActivityDTO;
import com.xiaoxiao.bbs.model.dto.ActivityQuery;
import com.xiaoxiao.bbs.model.entity.Activity;

/**
 * @author mayunfei
 * @since 2023-02-07 15:42:40
 */
public interface ActivityService extends IService<Activity> {
    /**
     * 发布活动
     *
     * @param activityDTO 活动发布用户输入的内容
     * @return 返回操作响应
     */
    boolean saveActivity(ActivityDTO activityDTO);


    /**
     * 查询活动列表
     *
     * @param activityQuery 查询条件
     * @return 返回操作响应
     */
    Page<ActivityVO> query(ActivityQuery activityQuery);

    /**
     * 活动打气
     *
     * @param activityId 活动id
     * @return 返回操作响应。当返回操作成功，并且data是ture时，打气成功。当返回操作成功，data是false时，则为不能重复打气的响应
     */
    boolean like(Long activityId);

    /**
     * 取消活动打气
     *
     * @param activityId 活动id
     * @return 返回操作响应。当返回操作成功，并且data是ture时，取消打气成功。当返回操作成功，data是false时，则为不能重复取消打气的响应
     */
    boolean removeLike(Long activityId);

}
