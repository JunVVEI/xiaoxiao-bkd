package com.xiaoxiao.common.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class RedisLuaConfig {

    @Bean
    public DefaultRedisScript<Long> setMaxLuaScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/set_max.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean
    public DefaultRedisScript<Long> swapHotPostScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/swap_hotpost.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

}
