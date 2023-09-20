package com.xiaoxiao.baseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * <p>
 * BaseServiceApplication
 * </p>
 *
 * @author Junwei
 * @since 2022/11/6
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = "com.xiaoxiao")
@ComponentScan({"com.xiaoxiao.common", "com.xiaoxiao.baseservice"})
public class BaseServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseServiceApplication.class, args);
    }
}
