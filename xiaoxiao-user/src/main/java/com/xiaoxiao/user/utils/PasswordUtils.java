package com.xiaoxiao.user.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author zhengjianwei
 */
public class PasswordUtils {

    /**
     * 密码加密器
     */
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 执行密码加密
     *
     * @param rawPassword 密码的原文，即原始密码
     * @return 密码的密码，即基于原文加密得到的结果
     */
    public static String encode(String rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 密码匹配，校验密码是否正确
     *
     * @param rawPassword    密码
     * @param encodePassword 加密后的密码
     * @return 匹配结果
     */
    public static boolean matches(String rawPassword, String encodePassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodePassword);
    }


}
