package com.kaihei.esportingplus.gamingteam.data.manager.core.team;

import com.kaihei.esportingplus.gamingteam.data.manager.core.populator.DictionaryId2NameConvertor;
import com.kaihei.esportingplus.gamingteam.data.manager.core.team.after.AfterOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 谢思勇
 *
 * 一些业务无关参数在执行完 相关 {@link AfterOperation} 后执行，对相应的参数进行填充
 *
 * 参数填充器
 */
public abstract class Populator<T extends TeamGame> extends TeamPerformer<T> {

    @Autowired
    @Delegate
    protected DictionaryId2NameConvertor dictionaryId2NameConvertor;

    public void populate() {
        perform();
    }

    /**
     * 由子类实现
     */
    @Override
    protected void doPerform() {
        doPopulate();
    }

    /**
     * 由子类实现
     *
     * 满足条件就做参数的填充操作
     */
    protected abstract void doPopulate();
}
