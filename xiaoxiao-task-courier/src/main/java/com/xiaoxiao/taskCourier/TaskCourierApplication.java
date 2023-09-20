package com.xiaoxiao.taskCourier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * TaskCourierApplication
 * </p>
 *
 * @author Junwei
 * @since 2023/08/7
 */
@EnableFeignClients(basePackages = "com.xiaoxiao")
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.xiaoxiao.common", "com.xiaoxiao.taskCourier"})
@EnableScheduling
@EnableAsync
public class TaskCourierApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskCourierApplication.class, args);
    }
}
