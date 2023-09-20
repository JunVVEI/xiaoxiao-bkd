package com.xiaoxiao.user.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.user.constants.enums.EnergyCount;
import com.xiaoxiao.user.model.dto.EnergyDTO;
import com.xiaoxiao.user.model.entity.UserEnergy;
import com.xiaoxiao.user.mapper.UserEnergyMapper;
import com.xiaoxiao.user.model.vo.UserEnergyPageVO;
import com.xiaoxiao.user.model.vo.UserEnergyVO;
import com.xiaoxiao.user.service.UserEnergyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yaoyao
 * @since 2023-08-30 01:38:28
 */
@Service
public class UserEnergyServiceImpl extends ServiceImpl<UserEnergyMapper, UserEnergy> implements UserEnergyService {

    @Transactional
    @Override
    public Boolean calculateEnergy(EnergyDTO energyDTO) {
        if(!EnergyDTO.checkIsVaild(energyDTO)){
            return Boolean.FALSE;
        }
        QueryWrapper<UserEnergy> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEnergy::getUserid, energyDTO.getUserid()).orderByDesc(UserEnergy::getCreateTime);
        UserEnergy latestRecord = this.getOne(queryWrapper.last("LIMIT 1 FOR UPDATE"));

        UserEnergy userEnergy = new UserEnergy();
        userEnergy.setUserid(energyDTO.getUserid());
        userEnergy.setType(energyDTO.getType());
        EnergyCount energyCount = calculateCount(energyDTO.getType());
        userEnergy.setEnergyCount(energyCount.getCount());
        userEnergy.setDescription(energyCount.getDesc());
        userEnergy.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userEnergy.setRelateId(energyDTO.getRelateId());
        if (latestRecord != null) {
            userEnergy.setEnergySum(latestRecord.getEnergySum() + userEnergy.getEnergyCount());
        } else {
            userEnergy.setEnergySum(userEnergy.getEnergyCount());
        }
        this.save(userEnergy);
        return Boolean.TRUE;
    }

    @Override
    public UserEnergyPageVO queryUserEnergyPage(Long currentPage, Long pageSize) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long userid = currentUser.getUid();
        Page<UserEnergy> page = new Page<>(currentPage, pageSize);
        QueryWrapper<UserEnergy> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEnergy::getUserid, userid).orderByDesc(UserEnergy::getCreateTime);
        IPage<UserEnergy> energyPage = this.page(page, queryWrapper);
        IPage<UserEnergyVO> voPage = new Page<>();
        BeanUtils.copyProperties(energyPage, voPage);
        voPage.setRecords(energyPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList()));

        UserEnergyPageVO userEnergyPageVO = new UserEnergyPageVO();
        userEnergyPageVO.setPage(voPage);
        userEnergyPageVO.setSum(energyPage.getRecords().get(0).getEnergySum());
        return userEnergyPageVO;
    }

    @Transactional
    @Override
    public Boolean deleteOne(Long id) {
        UserEnergy oldRecord = this.getById(id);
        if (oldRecord == null) {
            throw new ApiException("log not found");
        }

        UserEnergy latestRecord = this.getOne(new QueryWrapper<UserEnergy>().lambda()
                .eq(UserEnergy::getUserid, oldRecord.getUserid())
                .orderByDesc(UserEnergy::getCreateTime)
                .last("LIMIT 1 FOR UPDATE"));

        UserEnergy userEnergy = new UserEnergy();
        BeanUtils.copyProperties(oldRecord, userEnergy);
        userEnergy.setId(null);
        userEnergy.setEnergyCount(-oldRecord.getEnergyCount());
        userEnergy.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userEnergy.setDescription("回退能量");
        if (latestRecord != null) {
            userEnergy.setEnergySum(latestRecord.getEnergySum() + userEnergy.getEnergyCount());
        } else {
            userEnergy.setEnergySum(userEnergy.getEnergyCount());
        }

        this.save(userEnergy);

        return Boolean.TRUE;
    }

    private UserEnergyVO convertToVO(UserEnergy userEnergy) {
        UserEnergyVO vo = new UserEnergyVO();
        BeanUtils.copyProperties(userEnergy, vo);
        return vo;
    }

    private EnergyCount calculateCount(Integer type) {
        for (EnergyCount energyCount : EnergyCount.values()) {
            if (energyCount.getType().equals(type)) {
                return energyCount;
            }
        }
        throw new ApiException("Invalid type: " + type);
    }
}