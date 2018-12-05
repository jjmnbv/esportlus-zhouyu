package com.kaihei.esportingplus.payment;

import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.payment.data.jpa.repository.*;
import com.kaihei.esportingplus.payment.event.ApplePayRiskInfoEventConsumer;
import org.junit.After;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author: tangtao
 **/
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {
        PaymentServiceApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class BaseMvcTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @MockBean
    protected SnowFlake mockedSnowFlake;

    @MockBean
    protected ApplePayRiskInfoEventConsumer mockedApplePayRiskInfoEventConsumer;

    @Autowired
    protected MockMvc mvc;

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

    @After
    public void tearDown() {
        appSettingRepository.deleteAllInBatch();
        payChannelRepository.deleteAllInBatch();
        alipaySettingRepository.deleteAllInBatch();
        tenpaySettingReppository.deleteAllInBatch();
        capaySettingRepository.deleteAllInBatch();
    }
}
