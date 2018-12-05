package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.payment.BaseMvcTest;
import com.kaihei.esportingplus.payment.api.params.PayOrderParams;
import com.kaihei.esportingplus.payment.api.params.RefundOrderParams;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TradeControllerTest extends BaseMvcTest {

    private Logger logger = LoggerFactory.getLogger(TradeControllerTest.class);
    @Autowired
    private TradeController tradeController;

    @Test
    public void urlTest() throws Exception {
        this.mvc.perform(post("/trade/pay/12/jg")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(put("/trade/cancel")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(put("/trade/close")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(get("/trade")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(post("/trade/refund")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(get("/trade/refund")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testAlipayPay() throws Exception {

        PayOrderParams payOrderParams = new PayOrderParams();
        payOrderParams.setOrderType("10");
        String outTradeNo = String.valueOf(System.currentTimeMillis());
        payOrderParams.setOutTradeNo(outTradeNo);
        payOrderParams.setTotalAmount(100);
        payOrderParams.setUserId("123456");
        String appName = "baoji";
        String channelName = "ALIPAY";
        // 模拟接口返回
        BDDMockito.when(mockedSnowFlake.nextId()).thenReturn(System.currentTimeMillis());

        Object responsePacket = tradeController.createTest(payOrderParams, appName, channelName);
        Assert.assertNotNull(responsePacket);
        logger.info("response : {} ", FastJsonUtils.toJson(responsePacket));

    }

    public void testAlipayRefund() throws Exception {

        RefundOrderParams refundOrderParams = new RefundOrderParams();
        refundOrderParams.setOrderType("10");
        String outTradeNo = String.valueOf(System.currentTimeMillis());
        refundOrderParams.setOutTradeNo(outTradeNo);
        refundOrderParams.setRefundAmount("10");
        refundOrderParams.setUserId("123456");
        String appName = "baoji";
        String channelName = "ALIPAY";
        // 模拟接口返回
        BDDMockito.when(mockedSnowFlake.nextId()).thenReturn(System.currentTimeMillis());
        tradeController.refund(refundOrderParams);

    }

}
