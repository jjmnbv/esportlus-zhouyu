package com.kaihei.esportingplus;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.gamingteam.GamingTeamServiceApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamingTeamServiceApplication.class)//这里的Application是springboot的启动类名
@WebAppConfiguration
public class AbstractGamingTeamTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(TeamServiceImplTest.class);
    @Autowired
    protected WebApplicationContext context;
    protected MockMvc mockMvc;

    static {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }

    @Before
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
