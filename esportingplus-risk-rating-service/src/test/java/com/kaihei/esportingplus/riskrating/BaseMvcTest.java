package com.kaihei.esportingplus.riskrating;

/*import com.kaihei.esportingplus.common.sensors.config.SensorsAutoConfiguration;*/
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import com.kaihei.esportingplus.riskrating.event.AlermMsgSendEventConsumer;
import com.kaihei.esportingplus.riskrating.repository.RiskDictRepository;
import org.junit.After;
import org.junit.Before;
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
 * @author: haycco
 **/
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {RiskRatingServiceApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class BaseMvcTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MockMvc mvc;

    @MockBean
    AlermMsgSendEventConsumer alermMsgSendEventConsumer;

   /* @MockBean
    SensorsAutoConfiguration sensorsAutoConfiguration;*/

    @Autowired
    RiskDictRepository riskDictRepository;

    @Before
    public void tearUp(){
        RiskDict riskDict = new RiskDict();
        riskDict.setGroupCode("RISK_SWITCH");
        riskDict.setGroupName("风控总开关");
        riskDict.setItemCode("RISK_SWITCH");
        riskDict.setItemValue("0");
        riskDict.setItemName("风控开关，0关闭，1开启");
        riskDict.setDictStatus(1);
        riskDictRepository.save(riskDict);
        logger.info(riskDict.toString());
    }

    @After
    public void tearDown() {
//        riskDictRepository.deleteAllInBatch();
    }
}
