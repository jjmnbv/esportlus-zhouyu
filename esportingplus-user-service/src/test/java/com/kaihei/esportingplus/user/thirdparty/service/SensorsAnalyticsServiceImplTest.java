package com.kaihei.esportingplus.user.thirdparty.service;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.sensors.service.SensorsAnalyticsService;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.user.UserServiceApplication;
import java.util.HashMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yangshidong
 * @Title: RongYunServiceImplTest
 * @date 2018/9/13 14:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class SensorsAnalyticsServiceImplTest {

    @Autowired
    private SensorsAnalyticsService sensorsAnalyticsService;

    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }

    @Test
    public void trackSignUp() {
        sensorsAnalyticsService.trackSignUp("11111", "22222");
    }

    @Test
    public void profileSet() {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("name", "zhangsan");
        properties.put("sex", "male");
        properties.put("age", 21);
        properties.put("zone", "beijing");
        sensorsAnalyticsService.profileSet("11111", properties);
    }

    @Test
    public void profileSetOnce() {
    }

    @Test
    public void track() {
    }
}