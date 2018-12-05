package com.kaihei.esportingplus.payment.service;

import static org.junit.Assert.assertEquals;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.api.params.ExternalTradeBillQueryParams;
import com.kaihei.esportingplus.payment.api.vo.ExternalTradeBillVo;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: tangtao
 **/
public class ExternalTradeBillServiceTest extends BaseTest {

    @Autowired
    ExternalTradeBillService externalTradeBillService;

//    @Test
//    public void saveTest() {
//
//        ExternalTradeBill tradeBill = new ExternalTradeBill();
//
//        tradeBill.setChannel("WechatPay");
//        tradeBill.setOrderId("000");
//        tradeBill.setOrderType("000");
//        tradeBill.setOutTradeNo("000000");
//        tradeBill.setTransactionId("000000");
//        tradeBill.setFlowNo("000000");
//        tradeBill.setTotalFee(0);
//        tradeBill.setTradeType("000");
//
//        tradeBill = externalTradeBillService.save(tradeBill);
//
//        System.out.println(tradeBill);
//
//        assertTrue(tradeBill.getId() != null);
//
//    }

    @Test
    public void queryTest() {

        ExternalTradeBillQueryParams params = new ExternalTradeBillQueryParams();
        params.setCreateDateBegin("2017-01-01");
        params.setCreateDateEnd("2020-12-30");

        List<ExternalTradeBillVo> billList = externalTradeBillService.query(params);

        assertEquals(6, billList.size());

    }
}
