package com.kaihei.esportingplus.user.thirdparty.service;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.user.UserServiceApplication;
import com.kaihei.esportingplus.user.external.rongyun.RongYunService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yangshidong
 * @Title: RongYunServiceImplTest
 * @date 2018/9/13 10:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class RongYunServiceImplTest {

    @Autowired
    private RongYunService rongYunService;

    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }

    @Test
    public void testGetToken() {
        String token = null;
        token = rongYunService
                .getToken("userxxd2", "username", "http://www.rongcloud.cn/images/logo.png");
        System.out.println(token);
        Assert.assertNotNull(token);
    }

    @Test
    public void testUpdateUser(){
        boolean result = false;
        result = rongYunService.updateUser("userxxd2", "username1", "http://www.rongcloud.cn/images/logo.png");
        System.out.println(result);
        Assert.assertTrue(result);
    }

    @Test
    public void sendSystemMessage(){
        String[] userids = new String[150];
        for(int i=0;i<150;i++){
            userids[i] = "10000"+i;
        }
//        rongYunService.sendSystemMessage(userids,"欢迎来到暴鸡电竞");
    }
}
