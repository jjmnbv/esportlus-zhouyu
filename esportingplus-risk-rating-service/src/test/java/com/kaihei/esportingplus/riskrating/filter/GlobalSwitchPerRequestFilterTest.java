package com.kaihei.esportingplus.riskrating.filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.riskrating.BaseMvcTest;
import com.kaihei.esportingplus.riskrating.api.params.LoginParams;
import com.kaihei.esportingplus.riskrating.service.RiskDictService;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class GlobalSwitchPerRequestFilterTest extends BaseMvcTest {

    @MockBean
    RiskDictService mockedRiskDictService;

    @Test
    public void testRiskSwitchPreHandlerSuccessful() throws Exception {

        BDDMockito.when(mockedRiskDictService.checkRiskSwitchStatus()).thenReturn(true);

        LoginParams loginParams = new LoginParams();
        loginParams.setDeviceId("tewtId");
        loginParams.setPhone("15478965472");
        loginParams.setType(1);
        loginParams.setUserId("ddd784569");

        ResponsePacket result = ResponsePacket.onSuccess();
        result.setCode(BizExceptionEnum.GLOBAL_RISK_SWITCH_CLOSED.getErrCode());
        result.setMsg(BizExceptionEnum.GLOBAL_RISK_SWITCH_CLOSED.getErrMsg());
        result.setData(null);

        String body = JsonsUtils.toJson(loginParams);
        this.mvc.perform(post("/risk/api/v1/risk/login_register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(JacksonUtils.toJson(result)))
                .andDo(print());
    }

}
