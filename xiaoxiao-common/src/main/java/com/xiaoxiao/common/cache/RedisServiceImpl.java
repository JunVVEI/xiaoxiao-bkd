package com.xiaoxiao.common.cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
/**
 * <p>
 *
 * </p>
 *
 * @author JunWEI
 * @since 2022/10/21
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private DefaultRedisScript<Long> setMaxLuaScript;

    @Resource
    private DefaultRedisScript<Long> swapHotPostScript;

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }
    @Override
    @Deprecated
    public void set(String key, String value, long expire) {
        stringRedisTemplate.opsForValue().set(key, value, expire);
    }
    @Override
    public void set(String key, String value, long expire, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }
    @Override
    public void setMax(String key, String value, long expire) {
        List<String> keyList = Collections.singletonList(key);
        stringRedisTemplate.execute(setMaxLuaScript, keyList, value, String.valueOf(expire));
    }
    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
    @Override
    public boolean expire(String key, long expire) {
        return Boolean.TRUE.equals(stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS));
    }
    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }
    @Override
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }
    @Override
    public Set<String> reverseRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }
    @Override
    public Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScore(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }
    @Override
    public Set<ZSetOperations.TypedTuple<String>> rangeWithScore(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }
    @Override
    public Set<String> range(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }
    @Override
    public Long zCard(String key) {
        return stringRedisTemplate.opsForZSet().zCard(key);
    }


    @Override
    public void swapHotPost(String key, String[] args){
        stringRedisTemplate.execute(swapHotPostScript, Collections.singletonList(key),args);
    }
}
