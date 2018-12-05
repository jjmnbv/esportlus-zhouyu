package com.kaihei.esportingplus.common.tools;

import java.util.concurrent.ForkJoinPool;

public class ForkJionUtils {

    /**
     *@Description: 定制并发线程数
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/16 16:41
    */
    static {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", Runtime.getRuntime().availableProcessors() * 2 + "");
    }

    public static ForkJoinPool getCommonPool(){
        return ForkJoinPool.commonPool();
    }
}