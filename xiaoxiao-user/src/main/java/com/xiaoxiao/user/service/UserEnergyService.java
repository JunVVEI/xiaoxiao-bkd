package com.xiaoxiao.user.service;

import com.xiaoxiao.user.model.dto.EnergyDTO;
import com.xiaoxiao.user.model.entity.UserEnergy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.user.model.vo.UserEnergyPageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaoyao
 * @since 2023-09-03 01:58:36
 */
@Service
public interface UserEnergyService extends IService<UserEnergy> {

    @Transactional
    Boolean calculateEnergy(EnergyDTO energyDTO);

    UserEnergyPageVO queryUserEnergyPage(Long currentPage, Long pageSize);

    @Transactional
    Boolean deleteOne(Long id);
}
