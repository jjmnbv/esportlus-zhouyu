package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.api.enums.BizOrderType;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinBillRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinPaymentOrder;
import org.junit.After;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class WalletHistoryServiceTest extends BaseTest {

    @Autowired
    GCoinBillRepository gCoinBillRepository;

    @Autowired
    WalletService walletHistoryService;

    @After
    public void tearDown() {
        gCoinBillRepository.deleteAllInBatch();
    }

    @Test
    public void saveGCoinPaymentOrderHistoryDetails_successful() {
        GCoinPaymentOrder order = new GCoinPaymentOrder();
        order.setOrderId("python_order_id_123");
        order.setUserId("uid_1");
        order.setSubject("subject1");
        order.setBody("body1");
        order.setOrderType(String.valueOf(BizOrderType.FLEET.getCode()));
        order.setSourceId("test");
        order.setAmount(new BigDecimal(10));

        boolean flag = walletHistoryService.saveHistoryDetails(order);
        assertThat(flag).isTrue();
    }

    @Test(expected = Exception.class)
    public void saveGCoinPaymentOrderHistoryDetails_failure() {
        // 模拟接口返回
        BDDMockito.when(mockedSnowFlake.nextId()).thenReturn(1237895468l);

        GCoinPaymentOrder order = new GCoinPaymentOrder();
        walletHistoryService.saveHistoryDetails(order);
    }

}
