package com.xiaoxiao.toolbag.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxiao.toolbag.mapper.FreeRoomMapper;
import com.xiaoxiao.toolbag.model.bo.classroom.Building;
import com.xiaoxiao.toolbag.model.dto.freeroom.FreeRoomQuery;
import com.xiaoxiao.toolbag.model.entity.FreeRoom;
import com.xiaoxiao.toolbag.model.vo.FreeRoomData;
import com.xiaoxiao.toolbag.model.vo.FreeRoomVO;
import com.xiaoxiao.toolbag.service.FreeRoomService;
import com.xiaoxiao.toolbag.util.CacheUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yaoyao
 * @since 2023-08-04 12:12:53
 */
@Service
public class FreeRoomServiceImpl extends ServiceImpl<FreeRoomMapper, FreeRoom> implements FreeRoomService {

    private CacheUtil<String, List<FreeRoom>> cache = new CacheUtil<>(1000, 10);

    @Override
    public FreeRoomData getBuildingList() {
        FreeRoomData freeRoomData = new FreeRoomData();

        List<String> buildingIdList = Arrays.asList("8", "3", "67", "22", "W133f0ea0000WH", "W133f0ea0000WI", "W133f0ea0000WJ", "69");
        List<String> buildingNameList = Arrays.asList("教一", "教三", "教四", "教五A栋", "教五B栋", "教五C栋", "教五D栋", "教六");
        List<Building> buildings = IntStream.range(0, buildingIdList.size())
                .mapToObj(i -> {
                    Building building = new Building();
                    building.setBuildingId(buildingIdList.get(i));
                    building.setBuildingName(buildingNameList.get(i));
                    return building;
                })
                .collect(Collectors.toList());
        freeRoomData.setBuildingList(buildings);
        return freeRoomData;
    }

    @Override
    public List<FreeRoomVO> getFreeRoomList(FreeRoomQuery freeRoomQuery) {
        //todo 增加缓存
        List<FreeRoom> freeRoomList = this.list(
                new QueryWrapper<FreeRoom>().lambda()
                        .eq(FreeRoom::getWeek, freeRoomQuery.getWeek())
                        .eq(FreeRoom::getDay, freeRoomQuery.getDay())
                        .eq(FreeRoom::getBuildingId, freeRoomQuery.getBuildingId())
                        .orderByAsc(FreeRoom::getClassroom)
                        .orderByAsc(FreeRoom::getCourseTime)
        );
        if (Objects.isNull(freeRoomList)) {
            return new ArrayList<>();
        }
        Map<String, List<FreeRoom>> groupedByClassroom = freeRoomList.stream()
                .collect(Collectors.toMap(FreeRoom::getClassroom,
                        room -> new ArrayList<>(Collections.singletonList(room)),
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        },
                        LinkedHashMap::new));
        List<FreeRoomVO> freeRoomVOList = new ArrayList<>();
        groupedByClassroom.forEach((key, freeRooms) -> {
            char[] freeList = "111111111111111".toCharArray();
            Integer seat = freeRooms.get(0).getSeatNumber();
            Integer seatExam = freeRooms.get(0).getExamSeatNumber();
            freeRooms.stream().map(FreeRoom::getCourseTime).forEach(courseTime -> {
                        int index = Integer.parseInt(courseTime);
                        freeList[index - 1] = '0';
                    }
            );
            FreeRoomVO freeRoomVO = new FreeRoomVO();
            freeRoomVO.setFreeList(new String(freeList));
            freeRoomVO.setClassroom(key);
            freeRoomVO.setSeatNumber(seat);
            freeRoomVO.setSeatNumber(seatExam);
            freeRoomVOList.add(freeRoomVO);
        });
        return freeRoomVOList;
    }
}
