package com.kaihei.esportingplus.gamingteam.data.manager.core.team;

import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;

/**
 * @author 谢思勇
 *
 * 根据@{@link com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene} 和@{@link com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition}
 *
 * 在某种场景下、或达成某些条件时、进行一些相应的附加操作
 */
public abstract class Operation<T extends TeamGame> extends TeamPerformer<T> {

    public void operate() {
        perform();
    }

    /**
     * 由子类实现
     */
    @Override
    protected void doPerform() {
        doOperate();
    }

    /**
     * 由子类实现
     *
     * 进行一些Service后续的操作
     */
    protected abstract void doOperate();
}
