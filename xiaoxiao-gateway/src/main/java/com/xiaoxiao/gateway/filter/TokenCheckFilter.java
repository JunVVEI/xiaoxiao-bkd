package com.xiaoxiao.gateway.filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xiaoxiao.common.api.CommonResp;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.cache.RedisService;
import com.xiaoxiao.common.constant.XiaoXiaoConst;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.util.AssertUtil;
import com.xiaoxiao.gateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

/**
 * 检测token的过滤器
 *
 * @author zjw
 */
@Component
@Slf4j
public class TokenCheckFilter implements GlobalFilter, Ordered {

    @Resource
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");

            // 游客处理
            if (StrUtil.isBlank(token)) {
                return handleVisitor(exchange, chain);
            }

            // 登陆用户处理
            return handleLoginUser(exchange, chain);
        } catch (ApiException apiException) {
            return writeResp(exchange.getResponse(), apiException.getStatusCode());
        } catch (Exception exception) {
            log.error("网关异常 {}", JSONUtil.toJsonStr(exchange.getRequest()), exception);
            return writeResp(exchange.getResponse(), StatusCode.BIZ_ERROR);
        }
    }

    private Mono<Void> handleVisitor(ServerWebExchange exchange, GatewayFilterChain chain) {
        CommonUser commonUser = generateVisitor();
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
        requestBuilder.headers(
                k -> k.set(
                        "Common-User",
                        Base64.getEncoder().encodeToString(JSONUtil.toJsonStr(commonUser).getBytes())
                )
        );
        exchange.mutate().request(exchange.getRequest()).build();
        return chain.filter(exchange);
    }

    private Mono<Void> handleLoginUser(ServerWebExchange exchange, GatewayFilterChain chain) {
        CommonUser commonUser;
        try {
            // 获取token
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            AssertUtil.isTrue(StringUtils.isNotBlank(token), StatusCode.UNAUTHORIZED);

            // 解析token获取通用用户，异常抛出则返回UNAUTHORIZED错误
            String jwt = token.replace(XiaoXiaoConst.BEARER, "");
            Claims claims = JwtUtil.parseJWT(jwt);
            checkIsJWTValid(claims);
            commonUser = BeanUtil.fillBeanWithMap(claims, new CommonUser(), false);

            ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
            requestBuilder.headers(k -> k.remove("Authorization"));
            requestBuilder.headers(
                    k -> k.set(
                            "Common-User",
                            Base64.getEncoder().encodeToString(JSONUtil.toJsonStr(commonUser).getBytes())
                    )
            );

            exchange.mutate().request(exchange.getRequest()).build();
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("jwt解析异常", e);
            return handleVisitor(exchange, chain);
        }
    }


    private Mono<Void> writeResp(ServerHttpResponse serverHttpResponse, StatusCode statusCode) {
        serverHttpResponse.setStatusCode(HttpStatus.OK);
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JSONUtil.toJsonStr(CommonResp.failed(statusCode)).getBytes(StandardCharsets.UTF_8));
        return serverHttpResponse.writeWith(Flux.just(dataBuffer));
    }

    private CommonUser generateVisitor() {
        return CommonUser.builder()
                .uid(-1L)
                .build();
    }

    private static final String JWT_EXPIRED = "jwt:expired:";


    private void checkIsJWTValid(Claims claims) {
        Long uid = claims.get("uid", Long.class);
        String effectiveTime = redisService.get(JWT_EXPIRED + uid);
        if (StringUtils.isBlank(effectiveTime)) {
            return;
        }

        Timestamp effectiveTimestamp = new Timestamp(Long.parseLong(effectiveTime));
        Date iat = claims.get("iat", Date.class);
        if (iat.after(effectiveTimestamp)) {
            return;
        }
        throw new ApiException(StatusCode.UNAUTHORIZED);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
