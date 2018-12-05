package com.kaihei.esportingplus.gamingteam.data.manager.core.team;

import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;

/**
 * @author 谢思勇
 *
 * 根据@{@link com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene} 和@{@link com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition}
 *
 * 在某种场景下、或达到某些条件的时候，进行验证
 *
 *
 */
public abstract class Validator<T extends TeamGame> extends TeamPerformer<T> {

    public void validate() {
        perform();
    }

    /**
     * 由子类实现
     */
    @Override
    protected void doPerform() {
        doValidate();
    }

    /**
     * 由子类实现
     *
     * 不满足条件抛 {@link com.kaihei.esportingplus.common.exception.BusinessException}
     */
    protected abstract void doValidate();
}
