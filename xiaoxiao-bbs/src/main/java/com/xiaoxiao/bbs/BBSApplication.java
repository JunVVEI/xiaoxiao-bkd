package com.xiaoxiao.bbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * BBSApplication
 * </p>
 *
 * @author Junwei
 * @since 2022/10/27
 */
@EnableFeignClients(basePackages = "com.xiaoxiao")
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.xiaoxiao.common", "com.xiaoxiao.bbs"})
@EnableScheduling
@EnableAsync
public class BBSApplication {
    public static void main(String[] args) {
        SpringApplication.run(BBSApplication.class, args);
    }
}
