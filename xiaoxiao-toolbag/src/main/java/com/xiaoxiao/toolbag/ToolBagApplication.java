package com.xiaoxiao.toolbag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * ToolBagApplication
 * </p>
 *
 * @author Junwei
 * @since 2022/11/19
 */
@EnableFeignClients(basePackages = "com.xiaoxiao")
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.xiaoxiao.common", "com.xiaoxiao.toolbag"})
public class ToolBagApplication {
    public static void main(String[] args) {
        SpringApplication.run(ToolBagApplication.class, args);
    }
}
