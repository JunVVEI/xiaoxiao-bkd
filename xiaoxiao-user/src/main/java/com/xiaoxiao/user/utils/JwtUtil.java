package com.xiaoxiao.user.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT工具类
 *
 * @author zhengjianwei
 */
public class JwtUtil {

    /**
     * 默认有效期为60 * 60 * 1000  一个小时
     */
    public static final Long DEFAULT_JWT_TTL = 60 * 60 * 1000L;

    /**
     * 设置秘钥明文
     */
    public static final String JWT_KEY = "12345667890qwer";

    /**
     * 签发者
     */
    public static final String ISSUER = "xiaoxiao";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成jwt
     *
     * @param subject   token中要存放的数据（json格式）
     * @param claimsMap token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     */
    public static String createJWT(String subject, Map<String, Object> claimsMap, Long ttlMillis) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, claimsMap, ttlMillis, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Map<String, Object> claims, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.DEFAULT_JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                // 唯一的ID
                .setId(uuid)
                .setClaims(claims)
                // 主题  可以是JSON数据
                .setSubject(subject)
                // 签发者
                .setIssuer(JwtUtil.ISSUER)
                // 签发时间
                .setIssuedAt(now)
                // 使用HS256对称加密算法签名, 第二个参数为秘钥
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate);
    }

    /**
     * 生成加密后的秘钥 secretKey
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析
     */
    public static Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
    }
}
