package com.kaihei.esportingplus.common.event;

/**
 * 事件消费者（订阅者），继承该类消费者类不需要再显示注册自已
 * 前提是EventBus类先调用方法{@link EventBus#create}，初始化线程池
 * @author LiuQing.Qin
 * @date 2018/4/20 13:31
 */
public abstract class EventConsumer {

    public EventConsumer() {
        EventBus.register(this);
    }

}
