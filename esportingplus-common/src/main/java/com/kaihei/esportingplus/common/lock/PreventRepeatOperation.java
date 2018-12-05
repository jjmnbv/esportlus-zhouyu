package com.kaihei.esportingplus.common.lock;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 结合 redis 实现防止用户重复操作
 *
 * @author liangyi
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventRepeatOperation {

    /**
     * key 失效时间, 秒(s)
     */
    int expireTime() default 2;

}
