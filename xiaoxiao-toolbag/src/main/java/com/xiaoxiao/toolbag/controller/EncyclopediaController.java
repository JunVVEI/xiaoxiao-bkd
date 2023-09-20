package com.xiaoxiao.toolbag.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.toolbag.model.dto.encyclopedia.EncyclopediaDTO;
import com.xiaoxiao.toolbag.model.dto.encyclopedia.EncyclopediaQuery;
import com.xiaoxiao.toolbag.model.vo.EncyclopediaVO;
import com.xiaoxiao.toolbag.service.EncyclopediaService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenjunwei
 * @since 2023-01-26 12:20:39
 */
@RestController
@RequestMapping("/tb/doc")
public class EncyclopediaController {
    @Resource
    private EncyclopediaService encyclopediaService;

    @PostMapping("queryList")
    @XiaoXiaoResponseBody
    public IPage<EncyclopediaVO> query(@RequestBody EncyclopediaQuery encyclopediaQuery) {
        return encyclopediaService.listQuery(encyclopediaQuery);
    }

    @GetMapping("queryById")
    @XiaoXiaoResponseBody
    public EncyclopediaVO queryById(String id) {
        return encyclopediaService.queryById(id);
    }

    @GetMapping("queryByDocKey")
    @XiaoXiaoResponseBody
    public EncyclopediaVO queryByDocKey(String docKey) {
        return encyclopediaService.queryByDocKey(docKey);
    }


    @PostMapping("add")
    @XiaoXiaoResponseBody
    public boolean add(@RequestBody EncyclopediaDTO encyclopediaDTO) {
        return encyclopediaService.saveEncyclopedia(encyclopediaDTO);
    }

}

