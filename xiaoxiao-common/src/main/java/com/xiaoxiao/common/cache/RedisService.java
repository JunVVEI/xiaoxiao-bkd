package com.xiaoxiao.common.cache;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.RedisScript;

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

public interface RedisService {

    /**
     * 存储数据
     */
    void set(String key, String value);

    /**
     * 带过期时间的set
     */
    @Deprecated
    void set(String key, String value, long expire);

    /**
     * 带过期时间的set
     */
    void set(String key, String value, long expire, TimeUnit timeUnit);

    /**
     * set保留最大值
     */
    void setMax(String key, String value, long expire);

    /**
     * 获取数据
     */
    String get(String key);

    /**
     * 设置超期时间
     */
    boolean expire(String key, long expire);

    /**
     * 删除数据
     */
    void remove(String key);

    /**
     * 自增操作
     *
     * @param delta 自增步长
     */
    Long increment(String key, long delta);

    /**
     * 索引倒序排列指定区间元素
     *
     * @param key   键名
     * @param start 起始位置
     * @param end   末尾位置
     * @return 加入后结果
     */
    Set<String> reverseRange(String key, long start, long end);

    /**
     * 索引倒序排列区间值
     *
     * @param key   键名
     * @param start 起始位置
     * @param end   末尾位置
     * @return 排序后结果
     */
    Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScore(String key, long start, long end);

    /**
     * 索引正序排列区间值
     *
     * @param key   键名
     * @param start 起始位置
     * @param end   末尾位置
     * @return 排序后结果
     */
    Set<ZSetOperations.TypedTuple<String>> rangeWithScore(String key, long start, long end);

    /**
     * 获取索引区间内的元素
     *
     * @param key   键名
     * @param start 起始位置
     * @param end   末尾位置
     * @return 计算后结果
     */
    Set<String> range(String key, long start, long end);

    /**
     * 计算Set中元素的个数
     *
     * @param key 键名
     * @return 计算后结果
     */
    Long zCard(String key);


    void swapHotPost(String key, String[] args);
}
