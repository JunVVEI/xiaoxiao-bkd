package com.xiaoxiao.bbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author zjw
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 业务线程池beanName
     */
    public static final String BIZ_THREAD_POOL_BEAN_NAME = "bizThreadPool";

    @Bean(value = BIZ_THREAD_POOL_BEAN_NAME, initMethod = "initialize", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(5000);
        return executor;
    }
}
