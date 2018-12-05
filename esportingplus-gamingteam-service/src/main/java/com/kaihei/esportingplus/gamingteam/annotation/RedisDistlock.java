package com.kaihei.esportingplus.gamingteam.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义分布式锁注解
 * 配合 aop + Redisson 实现分布式锁
 * @author liangyi
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisDistlock {

    /**
     * 锁失效时间, 毫秒(ms)
     * @return
     */
    long expireTime() default 2000;

}
