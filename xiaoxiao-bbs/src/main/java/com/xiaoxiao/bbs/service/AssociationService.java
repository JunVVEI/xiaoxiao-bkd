package com.xiaoxiao.bbs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.bbs.model.dto.AssociationQueryDTO;
import com.xiaoxiao.bbs.model.entity.Association;
import com.xiaoxiao.bbs.model.vo.AssociationVO;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenjunwei
 * @since 2023-07-16 06:57:53
 */
public interface AssociationService {

    IPage<AssociationVO> query(AssociationQueryDTO associationQueryDTO);

    AssociationVO queryById(Integer id);

    AssociationVO add(Association association);


    AssociationVO edit(Association association);

    boolean deleteById(Integer id);

    List<String> getAssociationTypeOptions();

    List<String> getAssociationLevelOptions();

}
