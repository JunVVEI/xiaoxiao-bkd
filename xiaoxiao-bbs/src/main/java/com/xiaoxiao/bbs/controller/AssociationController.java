package com.xiaoxiao.bbs.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.dto.AssociationQueryDTO;
import com.xiaoxiao.bbs.model.entity.Association;
import com.xiaoxiao.bbs.model.vo.AssociationVO;
import com.xiaoxiao.bbs.service.AssociationService;
import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenjunwei
 * @since 2023-07-16 06:57:53
 */
@RestController
@RequestMapping("/bbs/association")
public class AssociationController {
    @Resource
    private AssociationService associationService;

    @PostMapping("queries")
    @XiaoXiaoResponseBody
    public IPage<AssociationVO> query(@RequestBody AssociationQueryDTO associationQueryDTO) {
        return associationService.query(associationQueryDTO);
    }

    @PostMapping("association")
    @XiaoXiaoResponseBody
    public AssociationVO queryById(@RequestParam Integer id) {
        return associationService.queryById(id);
    }

    @PostMapping("add")
    @XiaoXiaoResponseBody
    @Deprecated
    public AssociationVO add(@RequestBody HashMap<String, Object> m) {
        Association association = new Association();
        association.setId((Long) m.get("id"));
        association.setJsonData(JSONUtil.toJsonStr(m));
        return associationService.add(association);
    }

    @PostMapping("addOrEdit")
    @XiaoXiaoResponseBody
    public AssociationVO addOrEdit(@RequestBody HashMap<String, Object> m) {
        Association association = new Association();
        association.setJsonData(JSONUtil.toJsonStr(m));
        if (Objects.isNull(m.get("id"))) {
            return associationService.add(association);
        }
        association.setId(Long.valueOf(m.get("id").toString()));
        return associationService.edit(association);
    }

    @PostMapping("del")
    @XiaoXiaoResponseBody
    public boolean deleteById(@RequestParam Integer id) {
        return associationService.deleteById(id);
    }

    @GetMapping("getAssociationTypeOptions")
    @XiaoXiaoResponseBody
    public List<String> getAssociationTypeOptions() {
        return associationService.getAssociationTypeOptions();
    }

    @GetMapping("getAssociationLevelOptions")
    @XiaoXiaoResponseBody
    public List<String> getAssociationLevelOptions() {
        return associationService.getAssociationLevelOptions();
    }

}

