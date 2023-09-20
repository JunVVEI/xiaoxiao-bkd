package com.xiaoxiao.user.service;

import com.xiaoxiao.user.model.dto.SchoolDTO;
import com.xiaoxiao.user.model.dto.UserDTO;
import com.xiaoxiao.user.model.dto.UserInfoDTO;
import com.xiaoxiao.user.model.vo.CurrentUserInfoVO;
import com.xiaoxiao.user.model.vo.SchoolVO;
import com.xiaoxiao.user.model.vo.UserPostOptionVO;
import com.xiaoxiao.user.model.vo.UserPublicInfoVO;

/**
 * <p>
 * UserBizService
 * </p>
 *
 * @author Junwei
 * @since 2023/2/14
 */
public interface UserBizService {

    /**
     * 获取用户基础公开信息
     */
    UserPublicInfoVO getUserPublicInfo(UserDTO userDTO);

    /**
     * 获取用户个人信息
     */
    CurrentUserInfoVO getCurrentUserInfo();

    /**
     * 用户发布时的可使用身份选项
     */
    UserPostOptionVO getUserPostOptions();
}
