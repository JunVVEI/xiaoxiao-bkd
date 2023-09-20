package com.xiaoxiao.toolbag.model.vo;

import com.xiaoxiao.toolbag.model.bo.classroom.Building;
import com.xiaoxiao.toolbag.model.bo.classroom.CourseTime;
import lombok.Data;

import java.util.List;

/**
 * @author yaoyao
 * @Description
 * @create 2023-08-05 1:21
 */
@Data
public class FreeRoomData {
    List<Building> buildingList;
}
