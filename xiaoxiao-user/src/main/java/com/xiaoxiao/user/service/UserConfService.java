package com.xiaoxiao.user.service;

import com.xiaoxiao.user.model.dto.UserConfDTO;
import com.xiaoxiao.user.model.vo.UserConfVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenjunwei
 * @since 2023-08-17 10:57:05
 */
public interface UserConfService {
    boolean setConf(UserConfDTO userConfDTO);

    boolean toggleConf(String key);

    List<UserConfVO> getUserConf(Long uid);

    List<Long> getActivateConfKeyUserIds(String key);
}
