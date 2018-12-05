package com.kaihei.esportingplus.gamingteam.data.manager.core.anno;

import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.Scene;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnScene {

    Class<? extends Scene>[] excludes() default {};

    OnSceneLogic logic() default OnSceneLogic.OR;

    Class<? extends Scene>[] includes() default {};

    enum OnSceneLogic {
        /**
         * on 或 excludes
         */
        OR,
        /**
         * on 同时 excludes
         */
        AND
    }
}
