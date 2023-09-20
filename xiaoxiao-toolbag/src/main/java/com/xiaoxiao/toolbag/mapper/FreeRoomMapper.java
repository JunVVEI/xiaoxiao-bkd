package com.xiaoxiao.toolbag.mapper;

import com.xiaoxiao.toolbag.model.entity.FreeRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yaoyao
 * @since 2023-08-04 12:12:53
 */
@Mapper
public interface FreeRoomMapper extends BaseMapper<FreeRoom> {
    void insertMany(List<FreeRoom> list);
}
