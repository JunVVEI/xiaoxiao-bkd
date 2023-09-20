package com.xiaoxiao.toolbag.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author yaoyao
 * @Description
 * @create 2023-05-21 23:34
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenAiLimit {
    /**
     * 时间类型
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 访问次数
     */
    int count() default 100;
}
