package com.kaihei.esportingplus.core;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * TODO 功能描述
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/9 15:55
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }

}
