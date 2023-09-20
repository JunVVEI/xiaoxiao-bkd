package com.xiaoxiao.baseservice.config;

import lombok.Data;

/**
 * <p>
 * ThreadPoolConfig
 * </p>
 *
 * @author Junwei
 * @since 2022/12/3
 */
@Data
public class ThreadPoolConfig {

    /**
     * ThreadPoolTaskExecutor Bean名称
     */
    private String beanName;

    /**
     * 核心线程数
     */
    private int corePoolSize;

    /**
     * 最大线程数
     */
    private int maxPoolSize;

    /**
     * 空闲线程存活时间, 单位: 秒
     */
    private int keepAliveSeconds;

    /**
     * 队列容量
     */
    private int queueCapacity;

    /**
     * 线程名称前缀
     */
    private String threadNamePrefix;


    /**
     * 参数校验
     *
     * @param threadPoolConfig
     */
    public static void isValid(ThreadPoolConfig threadPoolConfig) {
        // TODO: 参数校验
    }
}
