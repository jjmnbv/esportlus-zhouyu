package com.kaihei.esportingplus.payment.repository;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinRechargeRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: esportingplus
 * @description:
 * @author: xusisi
 * @create: 2018-11-16 16:32
 **/
public class GcoinRechargeTest extends BaseTest {

    @Autowired
    private GCoinRechargeRepository gCoinRechargeRepository;

    @Test
    public void TestSave() {

        //GCoinRechargeOrder order = new GCoinRechargeOrder();
        //order.setOutTradeNo("10086");
        //order.setOrderType("17");
        //order.setRechargeType("0000");
        //order.setRemarks("remarks");
        //order.setSubject("subject");
        //order.setBody("body");
        //order.setOrderId("1000000010");
        //order.setId(100100l);
        //gCoinRechargeRepository.save(order);
    }
}
