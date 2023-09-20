package com.xiaoxiao.gateway.filter;

import cn.hutool.extra.spring.SpringUtil;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ApiFilter implements GlobalFilter, Ordered {

    private static final String CUSTOM_KPI_NAME_COUNTER = "custom.kpi.counter"; //api调用次数。

    private static MeterRegistry registry;

    void getRegistry() {
        if (registry == null) {
            //这里使用的时SpringUtil获取Bean，没有用@Autowired注解，Autowired会因为加载时机问题导致拿不到；SpringUtil.getBean网上实现有很多，可以自行搜索；
            registry = SpringUtil.getBean(MeterRegistry.class);
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        getRegistry();
        ServerHttpRequest request = exchange.getRequest();
        //统计调用次数
        registry.counter(
                CUSTOM_KPI_NAME_COUNTER,
                "uri", request.getURI().getPath(),
                "method", request.getMethod().name()
        ).increment();

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("Request {} took {} ms", exchange.getRequest().getURI().getPath(), duration);
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
