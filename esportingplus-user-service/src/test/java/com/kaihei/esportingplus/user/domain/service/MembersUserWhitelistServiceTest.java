package com.kaihei.esportingplus.user.domain.service;

/**
 * @author xiekeqing
 * @Title: MembersUserWhitelistServiceTest
 * @Description: TODO
 * @date 2018/9/2017:57
 */

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.user.UserServiceApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xiekeqing
 * @Title: MembersUserWhitelistServiceTest
 * @Description: TODO
 * @date 2018/9/1215:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class MembersUserWhitelistServiceTest {

    @Autowired
    private MembersUserWhitelistService membersUserWhitelistService;

    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }

    @Test
    public void testGetByIdentifierOrUnionId(){
        boolean ifExists = false;
        ifExists = membersUserWhitelistService.exists(254032L);
        System.out.println(ifExists);
        Assert.assertTrue(ifExists);

    }

//    @Test
    public void testInitToCache(){
        membersUserWhitelistService.initToCache();
    }

}
