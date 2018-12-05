package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.api.params.PayOrderParams;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zhouyu
 * @Date: 2018/11/6 12:04
 * @Description:微信支付測試類
 */
public class TenpayServiceImplTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(TenpayServiceImplTest.class);

    @Autowired
    PayService tenpayService;

    @Test
    public void  testCreateOrder(){
        PayOrderParams payOrderParams = new PayOrderParams();
        payOrderParams.setBody("开黑测试");
//        tradeController.create(payOrderParams,"baojidianjing","baojidianjing_weixAPP");

    }

    @Test
    public void testCreatePaymentOrder() throws Exception {
        PayOrderParams payOrderParams = new PayOrderParams();
    }

}
