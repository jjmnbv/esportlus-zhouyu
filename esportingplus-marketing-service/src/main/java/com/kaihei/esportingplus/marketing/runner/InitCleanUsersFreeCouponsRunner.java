package com.kaihei.esportingplus.marketing.runner;

import com.kaihei.esportingplus.common.task.JobScheduleService;
import com.kaihei.esportingplus.marketing.job.CleanUserFreeCouponsJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author zl.zhao
 * @description:定时清理用户过期免费券任务类
 * @date: 2018/11/21 16:03
 */
@Component
public class InitCleanUsersFreeCouponsRunner implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(InitCleanUsersFreeCouponsRunner.class);


    @Autowired
    private CleanUserFreeCouponsJob cleanUserFreeCouponsJob;

    @Autowired
    private JobScheduleService jobScheduleService;

    //每天0点1分执行
    private final String CRON = "0 1 0 * * ? *";

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.info("开始初始化免费券清理定时任务...");

        //添加定时任务
        jobScheduleService.addJob(cleanUserFreeCouponsJob, "cleanUserFreeCouponsJob" , CRON);

        logger.info("初始化免费券清理定时任务完成...");
    }
}
