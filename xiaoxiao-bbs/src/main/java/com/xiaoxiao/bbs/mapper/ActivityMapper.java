package com.xiaoxiao.bbs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.bbs.model.entity.Activity;
import com.xiaoxiao.bbs.model.vo.ActivityVO;
import com.xiaoxiao.bbs.model.vo.PostVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mayunfei
 * @since 2023-02-07 11:36:01
 */
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    int addCheer(Long activityId);

    int deleteCheer(Long activityId);

    Page<ActivityVO> queryActivityListByKeyword(Page<ActivityVO> page, String keyword, Long userId);
}
