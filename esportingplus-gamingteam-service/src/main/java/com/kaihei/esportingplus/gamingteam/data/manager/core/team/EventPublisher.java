package com.kaihei.esportingplus.gamingteam.data.manager.core.team;

import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;

/**
 * @author 谢思勇
 *
 * 消息推送器
 *
 * 根据@{@link com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnScene} 和@{@link com.kaihei.esportingplus.gamingteam.data.manager.core.anno.OnCondition}
 *
 * 在相应场景 或 达成某些条件的时候、进行相关的消息推送操作
 */
public abstract class EventPublisher<T extends TeamGame> extends TeamPerformer<T> {

    public void publish() {
        perform();
    }

    /**
     * 由子类实现
     */
    @Override
    protected void doPerform() {
        doPublish();
    }

    /**
     * 由子类实现
     *
     * 不满足条件抛 {@link com.kaihei.esportingplus.common.exception.BusinessException}
     */
    protected abstract void doPublish();
}
