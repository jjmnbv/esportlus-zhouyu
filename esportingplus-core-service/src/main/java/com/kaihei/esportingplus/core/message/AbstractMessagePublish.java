package com.kaihei.esportingplus.core.message;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/29 15:18
 **/
public abstract class AbstractMessagePublish implements MessagePublish {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /** 超出频率限制后定时器发送*/
   private static Timer timer = new HashedWheelTimer(500L, TimeUnit.MILLISECONDS, 120);

   protected void addTask(TimerTask timerTask,long time, TimeUnit unit ){
        timer.newTimeout(timerTask, time, unit);
   }


    /***
     * 分批
     * @param source
     * @return
     */
    protected String[][] split(String[] source, int limit) {
        int length = source.length;
        if (length > limit) {
            int num = length % limit == 0 ? (length / limit) : (length / limit + 1);
            String[][] result = new String[num][];
            for (int i = 0; i < num; i++) {
                int start = i * limit;
                int end = Math.min ((i + 1) * limit , length );
                result[i] = new String[end - start];
                System.arraycopy(source, start, result[i], 0, end - start);
            }

            return result;
        } else {
            String[][] strings = new String[1][];
            strings[0] = source;
            return strings;
        }
    }

}
