package com.kaihei.esportingplus.common.thread.pool;

import static com.kaihei.esportingplus.common.thread.ThreadNames.*;

import com.kaihei.esportingplus.common.spi.Spi;
import com.kaihei.esportingplus.common.thread.NamedPoolThreadFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 此线程池可伸缩，线程空闲一定时间后回收，新请求重新创建线程
 * ThreadPoolManager -> CommonExecutorFactory -> DefaultExecutor -> NamedPoolThreadFactory(ThreadFactory)
 * 
 * @author LiuQing.Qin
 * @date 2017年12月7日 下午5:11:35
 */
@Spi(order = 1)
public final class CommonExecutorFactory implements ExecutorFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommonExecutorFactory.class);
  
  private static int corePoolSize = 10;
  private static int maxPoolSize = 300;
  private static int keepAliveSeconds = 60;
  /**允许缓冲在队列中的任务数 (0:不缓冲、负数：无限大、正数：缓冲的任务数)*/
  private static int workQueueSize = 10000;
  
  private Executor get(ThreadPoolConfig config) {
    String name = config.getName();
    int corePoolSize = config.getCorePoolSize();
    int maxPoolSize = config.getMaxPoolSize();
    int keepAliveSeconds = config.getKeepAliveSeconds();
    BlockingQueue<Runnable> queue = config.getQueue();

    return new DefaultExecutor(corePoolSize
            , maxPoolSize
            , keepAliveSeconds
            , TimeUnit.SECONDS
            , queue
            , new NamedPoolThreadFactory(name)
            , new DumpThreadRejectedHandler(config));
  }

  @Override
  public Executor get(String name) {
    final ThreadPoolConfig config;
    switch (name) {
      case ESPORTINGPLUS_COMMON:
        config = ThreadPoolConfig.build(THREAD_INTERACT_SYS)
        .setCorePoolSize(corePoolSize)
        .setMaxPoolSize(maxPoolSize)
        .setKeepAliveSeconds(TimeUnit.SECONDS.toSeconds(keepAliveSeconds))
        .setQueueCapacity(workQueueSize)
        .setRejectedPolicy(ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS);
        break;
      case ESPORTINGPLUS_EVENT_BUS:
        config = ThreadPoolConfig
                .build(THREAD_EVENT_BUS)
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setKeepAliveSeconds(TimeUnit.SECONDS.toSeconds(10))
                .setQueueCapacity(workQueueSize)
                .setRejectedPolicy(ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS);
        break;
      case ESPORTINGPLUS_TOKEN_BUS:
        config = ThreadPoolConfig
                .build(ESPORTINGPLUS_TOKEN_BUS)
                .setCorePoolSize(corePoolSize)
                .setMaxPoolSize(maxPoolSize)
                .setKeepAliveSeconds(TimeUnit.SECONDS.toSeconds(10))
                .setQueueCapacity(workQueueSize)
                .setRejectedPolicy(ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS);
        break;
      case ESPORTINGPLUS_TASK_TIMER: {
        ScheduledThreadPoolExecutor executor =
            new ScheduledThreadPoolExecutor(corePoolSize, new NamedPoolThreadFactory(THREAD_TASK_TIMER),
                (r, e) -> LOGGER.error("one task timer was rejected, context=" + r));
        executor.setRemoveOnCancelPolicy(true);
        return executor;
      }
      default:
        throw new IllegalArgumentException("no executor for " + name);
    }

    return get(config);
  }

  public static Properties loadProperties(String fileName) {
    InputStream is = CommonExecutorFactory.class.getResourceAsStream(fileName);
    if (is == null) {
      LOGGER.warn("load properties(file:" + fileName + ") error, the file is not exist");
      return new Properties();
    }
    
    Properties prop = new Properties();
    try {
      prop.load(is);
    } catch (IOException e) {
      LOGGER.error("load properties(file:" + fileName + ") error", e);
    }
    return prop;
  }
}
