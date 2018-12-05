package com.kaihei.esportingplus.payment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kaihei.esportingplus.payment.BaseMvcTest;
import com.kaihei.esportingplus.payment.api.enums.DeductRatioEnum;
import com.kaihei.esportingplus.payment.service.DeductRatioService;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;

public class OrderIncomeControllerTest extends BaseMvcTest {

    @MockBean
    DeductRatioService mockedDeductRatioService;

    /*@MockBean
    SensorsAnalyticsService mockedSensorsAnalyticsService;*/
    /*@MockBean
    SensorsAutoConfiguration sensorsAutoConfiguration;*/

    @Test
    public void urlTest() throws Exception {

        this.mvc.perform(post("/order/income_calc")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(post("/order/order_ques")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(post("/order/income_room")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(get("/order/order_ratio")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetCalcAfterDeductRatioValueSuccessful() throws Exception {
        // 模拟接口返回
        BDDMockito.when(mockedDeductRatioService.getCalcAfterDeductRatioValue(2300, DeductRatioEnum.CALC_ORDER)).thenReturn(2200);
        BDDMockito.when(mockedSnowFlake.nextId()).thenReturn(22000000l);
        this.mvc.perform(get("/order/income_calc")).andExpect(status().isOk()).andDo(print());
    }
}
