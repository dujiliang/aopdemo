package com.yunbao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: djl
 * @Date: 2019/4/18 14:59
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CountLock {

    /** 锁名称*/
    String lockName() default "";

    /** 重试次数*/
    int retryTimes() default 0;

    /** 重试的间隔时间ms*/
    long retryWait() default 0;
}
