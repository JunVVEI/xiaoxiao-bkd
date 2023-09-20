package com.xiaoxiao.common.user;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

/**
 * <p>
 * 用户基础信息拦截器，将请求头中的用户基础信息提取出来，放到ThreadLocal中便于后续使用
 * </p>
 *
 * @author Junwei
 * @since 2022/11/21
 */
@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler
    ) {
        try {
            String header = request.getHeader("Common-User");
            if (StrUtil.isNotBlank(header)) {
                header = new String(Base64.getDecoder().decode(header));
                if (!StrUtil.isBlank(header)) {
                    CommonUser commonUser = JSONUtil.toBean(header, CommonUser.class);
                    UserContext.set(commonUser);
                }
            }
        } catch (Exception e) {
            log.error("解析通用用户上下文异常 {}", request, e);
        }

        return true;
    }


    @Override
    public void afterCompletion(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler,
            Exception ex
    ) {
        UserContext.remove();
    }

}
