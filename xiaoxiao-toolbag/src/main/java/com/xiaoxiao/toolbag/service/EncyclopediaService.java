package com.xiaoxiao.toolbag.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.toolbag.model.dto.encyclopedia.EncyclopediaDTO;
import com.xiaoxiao.toolbag.model.dto.encyclopedia.EncyclopediaQuery;
import com.xiaoxiao.toolbag.model.entity.Encyclopedia;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.toolbag.model.vo.EncyclopediaVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenjunwei
 * @since 2023-01-26 12:20:39
 */
public interface EncyclopediaService extends IService<Encyclopedia> {

    /**
     * 列表查询
     *
     * @param encyclopediaQuery 查询条件
     * @return 列表
     */
    IPage<EncyclopediaVO> listQuery(EncyclopediaQuery encyclopediaQuery);

    /**
     * 根据id查询
     *
     * @param id id
     * @return 数据
     */
    EncyclopediaVO queryById(String id);

    /**
     * 根据docKey查询
     *
     * @param docKey docKey
     * @return 数据
     */
    EncyclopediaVO queryByDocKey(String docKey);

    /**
     * 新增校园百科
     *
     * @param encyclopediaDTO 百科DTO
     * @return 是否成功
     */
    boolean saveEncyclopedia(EncyclopediaDTO encyclopediaDTO);

}
