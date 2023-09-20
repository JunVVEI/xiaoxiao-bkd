package com.xiaoxiao.common.config.web;

import com.xiaoxiao.common.user.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * <p>
 * CustomWebMvcConfigurer
 * </p>
 *
 * @author Junwei
 * @since 2022/11/21
 */
@Slf4j
@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {

    @Resource
    UserInterceptor userInterceptor;

    /**
     * 添加自定义的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor);
    }
}

