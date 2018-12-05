package com.kaihei.esportingplus.user.domain.service.impl;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.user.UserServiceApplication;
import com.kaihei.esportingplus.user.domain.service.MembersAuth3Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MembersAuth3ServiceImplTest {
    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }
    @Autowired
    private MembersAuth3Service membersAuth3Service;
}