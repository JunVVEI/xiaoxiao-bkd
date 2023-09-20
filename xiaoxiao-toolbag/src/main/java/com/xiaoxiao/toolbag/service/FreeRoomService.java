package com.xiaoxiao.toolbag.service;

import com.xiaoxiao.toolbag.model.dto.freeroom.FreeRoomQuery;
import com.xiaoxiao.toolbag.model.entity.FreeRoom;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.toolbag.model.vo.FreeRoomData;
import com.xiaoxiao.toolbag.model.vo.FreeRoomVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaoyao
 * @since 2023-08-04 12:12:53
 */
public interface FreeRoomService extends IService<FreeRoom> {
    FreeRoomData getBuildingList();

    List<FreeRoomVO> getFreeRoomList(FreeRoomQuery freeRoomQuery);
}
