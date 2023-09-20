package com.xiaoxiao.gateway;

import com.alibaba.cloud.sentinel.gateway.ConfigConstants;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowConfigStatement;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

/**
 * <p>
 * GatewayApplication
 * </p>
 *
 * @author Junwei
 * @since 2022/10/30
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan({"com.xiaoxiao.gateway", "com.xiaoxiao.common.cache"})
public class GatewayApplication {
	public static void main(String[] args) {
		/**
		 * 在Sentinel控制台中将此服务注册为网关类型
		 */
		System.setProperty(SentinelConfig.APP_TYPE_PROP_KEY, ConfigConstants.APP_TYPE_SCG_GATEWAY);
		SpringApplication.run(GatewayApplication.class, args);
	}

}
