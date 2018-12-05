package com.kaihei.esportingplus.gamingteam.data.manager.core;

import java.lang.reflect.ParameterizedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public abstract class AbstractPerformer<T> implements Performer, EnvironmentAware {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 确保该{@link #perform()}只执行一次
     */
    private ThreadLocal<Boolean> done = new InheritableThreadLocal<>();

    /**
     * 泛型
     */
    protected Class<T> type;
    /**
     * 暂时写死、全部开启
     */
    private boolean enabled = true;

    @SuppressWarnings("unchecked")
    public AbstractPerformer() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        type = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }
    /**
     * 由子类实现
     */
    protected abstract void doPerform();

    /**
     * 判断是否要执行
     *
     * 由子类实现
     */
    protected abstract boolean onStage();

    /**
     * 是否支持
     */
    protected abstract boolean supported();

    @Override
    public void perform() {
        //没有开启直接返回
        if (!enabled) {
            return;
        }

        //执行过直接返回
        if (hasDone()) {
            return;
        }

        //不支持
        if (!supported()) {
            return;
        }

        //不需要执行直接返回
        if (!onStage()) {
            return;
        }

        long start = System.currentTimeMillis();

        doPerform();
        this.done.set(true);

        long executionTime = System.currentTimeMillis() - start;
        //执行时间超过200打印日志
        if (executionTime > 200) {
            log.warn("{} 执行时间 -> {}", this.getClass().getSimpleName(),
                    executionTime);
        }

    }


    protected boolean hasDone() {
        Boolean done = this.done.get();
        return done != null && done;
    }

    @Override
    public void afterPerform() {
        done.remove();
    }

    /**
     * Set the {@code Environment} that this component runs in.
     */
    @Override
    public void setEnvironment(Environment environment) {
        Boolean enabled = environment
                .getProperty("performer.enabled." + this.getClass().getSimpleName(),
                        Boolean.class);
        if (enabled != null) {
            this.enabled = enabled;
        }
    }
}
