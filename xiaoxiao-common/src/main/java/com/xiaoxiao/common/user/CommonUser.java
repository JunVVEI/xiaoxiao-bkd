package com.xiaoxiao.common.user;

import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * User
 * </p>
 *
 * @author Junwei
 * @since 2022/12/5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonUser {

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 微信unionid
     */
    private String unionid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 账号版本
     */
    private String version;

    /**
     * 权限列表
     */
    private List<String> perms;

    public static void assertIsLogInUser(CommonUser commonUser) {
        AssertUtil.isTrue(isLogInUser(commonUser), StatusCode.UNAUTHORIZED);
    }

    public static boolean isLogInUser(CommonUser commonUser) {
        return Objects.nonNull(commonUser) && Objects.nonNull(commonUser.getUid()) && 0 < commonUser.getUid();
    }

}
