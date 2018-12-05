package com.kaihei.esportingplus;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.params.TeamQueryParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PrepareForTest({UserSessionContext.class})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*","com.dangdang.*"})
@AutoConfigureMockMvc
public class TeamServiceImplTest extends AbstractGamingTeamTest {
    @Test
    public void test002FindTeamList(){
        try {
            UserSessionContext userSessionContext = initUserSessionContext();
            TeamQueryParams params = new TeamQueryParams();
            params.setOffset(1);
            params.setLimit(3);
            mockMvc
                    .perform(post("/gamingteam/88/list").characterEncoding("UTF-8").contentType(
                            MediaType.APPLICATION_JSON).header("Authorization", "123456")
                            .content(JacksonUtils.toJsonWithSnake(params)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(BizExceptionEnum.SUCCESS.getErrCode()))
                    .andExpect(jsonPath("$.msg").value(BizExceptionEnum.SUCCESS.getErrMsg()))
                    .andExpect(jsonPath("$.data").isNotEmpty());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }
    private UserSessionContext initUserSessionContext(){
        UserSessionContext userSessionContext = new UserSessionContext();
        userSessionContext.setUid("test001");
        userSessionContext.setUsername("测试用户1");
        PowerMockito.mockStatic(UserSessionContext.class);
        PowerMockito.when(UserSessionContext.getUser()).thenReturn(userSessionContext);
        return userSessionContext;
    }
}
