package com.kaihei.esportingplus.user.thirdparty.service;

import com.kaihei.esportingplus.api.feign.ChickenpointUseConfigClient;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointUseConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.DictionaryVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.payment.api.feign.WithdrawServiceClient;
import com.kaihei.esportingplus.user.UserServiceApplication;
import com.kaihei.esportingplus.user.external.message.SendMessageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringCloudInterfaceTest {

    @Autowired
    private SendMessageService messageService;

    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }


    @Autowired
    private ChickenpointUseConfigClient chickenpointUseConfigClient;

    @Autowired
    private WithdrawServiceClient withdrawServiceClient;


    @Test
    public void callInterfaceTest() {

        ResponsePacket<DictionaryVO<ChickenpointUseConfigVO>> config = chickenpointUseConfigClient.getChickenpointUseConfigVo();
        System.out.println(FastJsonUtils.toJson(config));
        Assert.assertNotNull(config);

        ResponsePacket packet = withdrawServiceClient
                .convertScoreToStarlight("essef", 10,String.valueOf("111"));
        System.out.println(FastJsonUtils.toJson(packet));
        Assert.assertNotNull(packet);
    }
}
