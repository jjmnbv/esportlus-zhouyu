package com.kaihei.esportingplus.marketing.api.event;

/**
 * 用户任务服务handler接口，所有用户相关任务抽象为同意接口
 *
 * @author xiekeqing
 * @date 2018/10/07 14:35:24
 */
public interface UserEventHandler {

    /**
     * 用户参与任务逻辑执行，根据不同的类型执行对应的任务逻辑。
     *
     * @param userEvent 用户事件
     * @return 是否处理成功
     */
    public boolean handle(UserEvent userEvent);

}
