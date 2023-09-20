package com.xiaoxiao.user.service;

import com.xiaoxiao.user.model.dto.IdentityDTO;
import com.xiaoxiao.user.model.entity.Identity;
import com.xiaoxiao.user.model.vo.IdentityVO;

import java.util.List;

/**
 * <p>
 * 用户身份表 服务类
 * </p>
 *
 * @author junwei
 * @since 2022-11-20 03:36:59
 */
public interface IdentityService {

    /**
     * 新增社区身份
     */
    Identity addIdentity(Long uid);

    /**
     * 修改匿名身份
     */
    boolean updateIdentity(IdentityDTO identityDTO);

    /**
     * 修改昵称
     */
    boolean updateName(IdentityDTO identityDTO);

    /**
     * 获取匿名身份信息
     */
    IdentityVO getIdentity(Long identityId);

    /**
     * 获取当前用户的匿名身份列表
     */
    List<IdentityVO> listCurrentUserIdentities();

    /**
     * 获取当前用户的匿名身份列表
     */
    IdentityVO getCurrentUserIdentityInfo();

    /**
     * 获取当前用户的匿名身份列表
     */
    List<String> getIdentityAvatarLibrary();
}
