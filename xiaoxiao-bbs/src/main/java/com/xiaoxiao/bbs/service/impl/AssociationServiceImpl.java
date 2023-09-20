package com.xiaoxiao.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoxiao.bbs.mapper.AssociationMapper;
import com.xiaoxiao.bbs.model.dto.AssociationQueryDTO;
import com.xiaoxiao.bbs.model.entity.Association;
import com.xiaoxiao.bbs.model.enums.AssociationLevelOptionEnum;
import com.xiaoxiao.bbs.model.enums.AssociationTypeOptionEnum;
import com.xiaoxiao.bbs.model.vo.AssociationVO;
import com.xiaoxiao.bbs.service.AssociationService;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenjunwei
 * @since 2023-07-16 06:57:53
 */
@Service
@Slf4j
public class AssociationServiceImpl implements AssociationService {

    @Resource
    private AssociationMapper associationMapper;

    @Override
    public IPage<AssociationVO> query(AssociationQueryDTO associationQueryDTO) {
        AssociationQueryDTO.checkIsValid(associationQueryDTO);

        LambdaQueryWrapper<Association> queryWrapper = new LambdaQueryWrapper<Association>()
                .eq(Association::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal());

        if (Objects.nonNull(associationQueryDTO.getKeyWords())) {
            for (String keyWord : associationQueryDTO.getKeyWords()) {
                queryWrapper = queryWrapper.like(Association::getJsonData, keyWord);
            }
        }

        Page<Association> associationPage = associationMapper.selectPage(
                new Page<>(
                        associationQueryDTO.getCurrentPage(),
                        associationQueryDTO.getPageSize()
                ),
                queryWrapper
        );
        return associationPage.convert(this::transform2AssociationVO);
    }


    @Override
    public AssociationVO queryById(Integer id) {
        Association association = associationMapper.selectById(id);
        if (Objects.equals(association.getIsDelete(), XiaoXiaoConstEnum.DELETED.getVal())) {
            throw new ApiException("数据不存在啦");
        }
        return transform2AssociationVO(associationMapper.selectById(id));
    }

    @Override
    public AssociationVO add(Association association) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);

        association.setUid(currentUser.getUid());
        association.setIsDelete(XiaoXiaoConstEnum.UN_DELETE.getVal());
        association.setCreateTime(new Timestamp(System.currentTimeMillis()));
        association.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        int insert = associationMapper.insert(association);
        if (insert != 1) {
            throw new ApiException("新增失败");
        }
        return transform2AssociationVO(association);
    }

    @Override
    public AssociationVO edit(Association association) {
        association.setCreateTime(null);
        association.setUpdateTime(null);
        association.setIsDelete(null);

        int i = associationMapper.updateById(association);
        if (i != 1) {
            throw new ApiException("更新失败");
        }
        return transform2AssociationVO(associationMapper.selectById(association.getId()));
    }

    @Override
    public boolean deleteById(@RequestParam Integer id) {
        Association association = associationMapper.selectById(id);
        if (Objects.equals(association.getIsDelete(), XiaoXiaoConstEnum.DELETED.getVal())) {
            return true;
        }

        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        if (!Objects.equals(association.getUid(), currentUser.getUid())) {
            throw new ApiException("删除失败，您不是该信息的创建人");
        }

        association.setIsDelete(XiaoXiaoConstEnum.DELETED.getVal());
        association.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        int i = associationMapper.updateById(association);
        return i != 0;
    }

    private AssociationVO transform2AssociationVO(Association association) {
        String jsonData = association.getJsonData();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map;
        try {
            map = mapper.readValue(jsonData, Map.class);
        } catch (Exception e) {
            log.error("社团数据解析异常", e);
            throw new ApiException(StatusCode.BIZ_ERROR);
        }

        AssociationVO associationVO = new AssociationVO();
        associationVO.putAll(map);
        associationVO.put("id", association.getId());
        associationVO.put("createTime", association.getCreateTime());
        associationVO.put("updateTime", association.getUpdateTime());

        CommonUser currentUser = UserContext.getCurrentUser();
        if (CommonUser.isLogInUser(currentUser)) {
            boolean isCreator = Objects.equals(currentUser.getUid(), association.getUid());
            associationVO.put("isCreator", isCreator);
        }
        return associationVO;
    }

    @Override
    public List<String> getAssociationTypeOptions() {
        return AssociationTypeOptionEnum.getEnableBalueList();
    }

    @Override
    public List<String> getAssociationLevelOptions() {
        return AssociationLevelOptionEnum.getEnableValueList();
    }
}
