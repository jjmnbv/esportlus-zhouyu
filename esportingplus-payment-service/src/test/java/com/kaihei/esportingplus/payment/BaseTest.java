package com.kaihei.esportingplus.payment;

import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.payment.data.jpa.repository.*;
import com.kaihei.esportingplus.payment.event.ApplePayRiskInfoEventConsumer;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: tangtao
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseTest {

    @MockBean
    protected SnowFlake mockedSnowFlake;
    @Autowired
    protected ApplePayRiskInfoEventConsumer mockedApplePayRiskInfoEventConsumer;

    @Autowired
    protected AppSettingRepository appSettingRepository;

    @Autowired
    protected PayChannelRepository payChannelRepository;

    @Autowired
    protected AlipaySettingRepository alipaySettingRepository;

    @Autowired
    protected TenpaySettingReppository tenpaySettingReppository;

    @Autowired
    protected CapaySettingRepository capaySettingRepository;

    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }

    @After
    public void tearDown() {
//        appSettingRepository.deleteAllInBatch();
//        payChannelRepository.deleteAllInBatch();
//        alipaySettingRepository.deleteAllInBatch();
//        tenpaySettingReppository.deleteAllInBatch();
//        capaySettingRepository.deleteAllInBatch();
    }

}
