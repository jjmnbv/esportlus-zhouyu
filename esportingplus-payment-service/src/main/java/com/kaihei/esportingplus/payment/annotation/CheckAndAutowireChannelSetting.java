package com.kaihei.esportingplus.payment.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检查及注入支付渠道配置信息
 *
 * @author haycco
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckAndAutowireChannelSetting {

    /**
     * 应用ID
     */
    String[] appId() default {};
    /**
     * 渠道标签
     */
    String[] channelTag() default {};
}
