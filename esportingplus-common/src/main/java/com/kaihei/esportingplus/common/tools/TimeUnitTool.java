package com.kaihei.esportingplus.common.tools;

import java.util.concurrent.TimeUnit;

/**
 * @author 谢思勇
 *
 * 报错无视的Sleep
 */
public class TimeUnitTool {

    public static void sleep(Integer seconds) {
        sleep(seconds, TimeUnit.SECONDS);
    }

    public static void sleep(Integer time, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }


}
