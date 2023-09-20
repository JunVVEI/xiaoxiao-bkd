package com.xiaoxiao.toolbag.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheUtil<K, V> {
    private Cache<K, V> cache;

    public CacheUtil(long maxSize, long expireAfterWrite) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireAfterWrite, TimeUnit.MINUTES)
                .build();
    }

    public V get(K key, Loader<K, V> loader) {
        try {
            return cache.get(key, () -> loader.load(key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface Loader<K, V> {
        V load(K key) throws Exception;
    }
}
