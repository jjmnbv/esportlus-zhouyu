package com.kaihei.esportingplus.common.thread.pool;

import com.kaihei.esportingplus.common.spi.SpiLoader;
import java.util.concurrent.Executor;

/**
 * 线程池Executor工厂
 * ThreadPoolManager -> CommonExecutorFactory -> DefaultExecutor -> NamedPoolThreadFactory(ThreadFactory)
 * @author LiuQing.Qin
 * @date 2017年12月7日 下午5:16:05
 */
public interface ExecutorFactory {
    String ESPORTINGPLUS_COMMON = "esportingplus-common";
    String ESPORTINGPLUS_EVENT_BUS = "esportingplus-event-bus";
    String ESPORTINGPLUS_TOKEN_BUS = "esportingplus-token-bus";
    String ESPORTINGPLUS_TASK_TIMER = "esportingplus-task-timer";

    Executor get(String name);

    static ExecutorFactory create() {
        return SpiLoader.load(ExecutorFactory.class);
    }
}
