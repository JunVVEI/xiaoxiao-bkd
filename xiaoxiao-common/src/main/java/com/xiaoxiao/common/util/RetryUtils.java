package com.xiaoxiao.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryUtils {

    public static <T> T retryOnException(int retries, Supplier<T> supplier) {
        Exception lastException = null;

        for (int i = 0; i < retries; i++) {
            try {
                return supplier.get();
            } catch (Exception e) {
                log.error("重试失败",e);
                lastException = e;
                // optional: log the exception
            }
        }

        // if we've exhausted all retries, rethrow the last exception
        if (lastException != null) {
            log.error("最终重试失败",lastException);
            throw new RuntimeException( lastException);
        }

        return null; // or throw an exception
    }
}
