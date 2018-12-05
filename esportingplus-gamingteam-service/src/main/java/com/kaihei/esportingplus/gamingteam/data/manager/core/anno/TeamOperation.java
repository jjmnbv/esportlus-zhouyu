package com.kaihei.esportingplus.gamingteam.data.manager.core.anno;

import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.NOOPScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.Scene;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link #sequencePath()}AOP通过该字段获取teamSequence、
 *
 * 从Redis加载 {@link com.kaihei.esportingplus.gamingteam.data.manager.pvp.context.PVPContext}
 * 相关数据到线程并在其他TeamService及其他AOP内进行相应的操作
 *
 * @author 谢思勇
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TeamOperation {

    String sequencePath() default "sequence";

    Class<? extends Scene> scene() default NOOPScene.class;

    boolean init() default false;

    boolean end() default false;
}
