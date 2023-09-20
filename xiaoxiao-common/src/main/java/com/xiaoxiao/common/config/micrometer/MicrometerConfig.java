package com.xiaoxiao.common.config.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * <p>
 * MybatisPlusConfig
 * </p>
 *
 * @author chenjunwei
 * @since 2023/2/4
 */
@Component
public class MicrometerConfig {

	@Bean
	MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
		return (registry) -> registry.config().commonTags("application", applicationName);
	}
}
