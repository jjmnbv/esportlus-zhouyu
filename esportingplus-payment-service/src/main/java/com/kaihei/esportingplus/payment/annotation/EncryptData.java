package com.kaihei.esportingplus.payment.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密注解，加了此注解的接口将进行数据加密操作
 *
 * @author haycco
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptData {

    /**
     * 默认为空则加密整个返回数据对象，可配置 JSON 的 key ，支持多 key 选配
     * example：{"data"}
     * example：{"/data/userId", "/data/order_id"}
     */
    String[] value() default {};
}
