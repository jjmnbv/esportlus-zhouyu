package com.kaihei.esportingplus.user.runner;

import com.kaihei.esportingplus.user.domain.service.MembersUserWhitelistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @description:初始化用户白名单列表到缓存
 * @date: 2018/11/22 16:37
 */
@Component
public class InitUserWhiteListRunner implements ApplicationRunner {

    private static Logger logger = LoggerFactory.getLogger(InitUserWhiteListRunner.class);


    @Autowired
    private MembersUserWhitelistService membersUserWhitelistService;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.info("cmd=run >> 开始初始化用户白名单列表到缓存.");

        membersUserWhitelistService.initToCache();

        logger.info("cmd=run >> 初始化用户白名单列表到缓存完成.");
    }
}
