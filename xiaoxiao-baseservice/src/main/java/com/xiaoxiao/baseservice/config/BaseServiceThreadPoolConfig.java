package com.xiaoxiao.baseservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>
 * ThreadPoolConfig
 * </p>
 *
 * @author Junwei
 * @since 2022/12/3
 */
@Slf4j
@Configuration
public class BaseServiceThreadPoolConfig {
    /**
     * 业务线程池beanName
     */
    public static final String BIZ_THREAD_POOL_BEAN_NAME = "bizThreadPool";

    /**
     * 定时任务线程池beanName
     */
    public static final String TASK_THREAD_POOL_BEAN_NAME = "taskThreadPool";

    @Value("${bs.biz.thread.pool.config:0}")
    private String bizThreadPoolConfig;
    // TODO: 增加线程池配置

    /**
     * 业务线程池
     */
    @Bean(value = BIZ_THREAD_POOL_BEAN_NAME, initMethod = "initialize", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return generateThreadPoolExecutor(bizThreadPoolConfig, BIZ_THREAD_POOL_BEAN_NAME);
    }

    /**
     * 基于配置生成ThreadPoolTaskExecutor
     *
     * @param config
     * @return
     */
    public ThreadPoolTaskExecutor generateThreadPoolExecutor(String config, String targetBeanName) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // TODO: 根据配置初始化线程池
        return executor;
    }

}
