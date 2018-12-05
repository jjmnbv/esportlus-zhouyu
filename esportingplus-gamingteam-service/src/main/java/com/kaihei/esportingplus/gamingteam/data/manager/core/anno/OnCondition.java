package com.kaihei.esportingplus.gamingteam.data.manager.core.anno;


import com.kaihei.esportingplus.gamingteam.data.manager.core.team.Condition;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Operation#operate()} {@link TeamOperationValidator#validate()} {@link
 * EventPublisher#publish()}
 *
 * 这三个类在执行相应方法前会根据判断是处于当前场景是否处于注解的 {@link OnCondition#value()}的场景下
 *
 * 如果不是则跳过
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnCondition {

    Class<? extends Condition>[] value() default {};

    Class<? extends Condition>[] excludes() default {};

    OnConditionLogic logic() default OnConditionLogic.OR;

    enum OnConditionLogic {
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
