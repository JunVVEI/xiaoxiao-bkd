package com.xiaoxiao.toolbag.controller;

import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.toolbag.model.dto.freeroom.FreeRoomQuery;
import com.xiaoxiao.toolbag.model.vo.FreeRoomData;
import com.xiaoxiao.toolbag.model.vo.FreeRoomVO;
import com.xiaoxiao.toolbag.service.FreeRoomService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yaoyao
 * @since 2023-08-04 12:12:53
 */
@RestController
@RequestMapping("/tb/freeRoom")
    public class FreeRoomController {
    @Resource
    private FreeRoomService freeRoomService;

    @GetMapping("/getData")
    @XiaoXiaoResponseBody
    public FreeRoomData getFreeRoomData(){
        return freeRoomService.getBuildingList();
    }

    @PostMapping ("/getFreeRoom")
    @XiaoXiaoResponseBody
    public List<FreeRoomVO> getFreeRoom(@RequestBody FreeRoomQuery freeRoomQuery){
        return freeRoomService.getFreeRoomList(freeRoomQuery);
    }

}

