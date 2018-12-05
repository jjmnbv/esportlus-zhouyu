package com.kaihei.esportingplus.common;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 
 * @author LiuQing.Qin
 * @date 2017年12月7日 下午6:08:10
 */
public class ThreadPoolManagerTest {

  public static void main(String[] args) throws InterruptedException {
    ThreadPoolManager.INSTANCE.getDefaultExecutor().execute(() -> {
      System.out.println(Thread.currentThread().getName() + " -> hello world!!!");
    });

    ThreadPoolManager.INSTANCE.newThread("xx", () -> {
        System.out.println(Thread.currentThread().getName() + " -> test");
    }).start();


    ScheduledExecutorService findOrdersExecutor =  ThreadPoolManager.INSTANCE
            .getTaskTimer();

    System.out.println("111111");
    findOrdersExecutor.schedule(new Callable<Integer>(){
      @Override
      public Integer call() throws Exception {
        System.out.println(2);
        return 11;
      }
    },3, TimeUnit.SECONDS);

    LockSupport.parkNanos(300000);
    findOrdersExecutor.shutdownNow();
  }
}
