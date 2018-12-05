package com.kaihei.esportingplus;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.GamingTeamServiceApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import redis.clients.jedis.Pipeline;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PrepareForTest({UserSessionContext.class,CacheManagerFactory.class,EventBus.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*","com.dangdang.*"})
@SpringBootTest(classes = GamingTeamServiceApplication.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class AbstractGamingTeamMockTest {
    @Autowired
    private WebApplicationContext context;

    protected MockMvc mockMvc;
    protected CacheManager cacheManager;
    protected Pipeline pipelined;
    protected RedissonClient redissonClient;
    protected RLock rLock;
    {
        //  MockitoAnnotations.initMocks(this);
        cacheManager = Mockito.mock(CacheManager.class);
        PowerMockito.mockStatic(CacheManagerFactory.class);
        PowerMockito.when(CacheManagerFactory.create()).thenReturn(cacheManager);
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
        pipelined = Mockito.mock(Pipeline.class);
        PowerMockito.when(cacheManager.pipelined()).thenReturn(pipelined);
        redissonClient = Mockito.mock(RedissonClient.class);
        Mockito.when(cacheManager.redissonClient()).thenReturn(redissonClient);
        rLock = Mockito.mock(RLock.class);
        Mockito.when(redissonClient.getLock(Mockito.anyString())).thenReturn(rLock);
    }
    @Before
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
