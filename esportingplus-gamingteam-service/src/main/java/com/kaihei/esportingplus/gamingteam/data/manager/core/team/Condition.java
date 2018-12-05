package com.kaihei.esportingplus.gamingteam.data.manager.core.team;

import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;

/**
 * @author 谢思勇
 *
 * 条件抽象
 *
 * 可在条件上添加 "{@link com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene} 或 @{@link com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition} 注解
 *
 * 无注解为默认的条件判断器
 *
 */
public abstract class Condition<T extends TeamGame> extends TeamPerformer<T> {

    protected ThreadLocal<Boolean> result = new InheritableThreadLocal<>();


    public boolean condition() {
        perform();
        Boolean result = this.result.get();
        return result != null && result;
    }

    /**
     * 由子类实现
     */
    @Override
    protected void doPerform() {
        boolean onCondition = this.onCondition();
        result.set(onCondition);
    }

    /**
     * 由子类实现
     *
     * 判断是否满足条件
     */
    protected abstract boolean onCondition();

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        result.remove();
    }
}
