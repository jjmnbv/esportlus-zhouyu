package com.kaihei.esportingplus.gamingteam.data.manager.core.anno;//package com.kaihei.esportingplus.gamingteam.data.manager.pvp.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非前端请求 根据请求参数Mock一个 {@link com.kaihei.esportingplus.common.web.UserSessionContext}放到线程里
 *
 * @author 谢思勇
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MockUser {

    String avatar() default "avatar";

    String username() default "username";

    String uid() default "uid";

    String chickenId() default "chickenId";
}
