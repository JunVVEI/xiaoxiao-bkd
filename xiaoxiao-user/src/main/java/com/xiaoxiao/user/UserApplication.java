package com.xiaoxiao.user;

import com.xiaoxiao.baseservice.rpc.service.RpcBaseService;
import com.xiaoxiao.bbs.rpc.service.RpcBbsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <p>
 * UserApplication
 * </p>
 *
 * @author Junwei
 * @since 2022/11/6
 */
@EnableDiscoveryClient
@EnableFeignClients(clients = {RpcBaseService.class, RpcBbsService.class})
@SpringBootApplication
@ComponentScan({"com.xiaoxiao.common", "com.xiaoxiao.user"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
