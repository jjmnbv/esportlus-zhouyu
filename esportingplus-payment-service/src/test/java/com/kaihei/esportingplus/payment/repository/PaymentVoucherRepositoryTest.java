package com.kaihei.esportingplus.payment.repository;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.data.mongodb.repository.PaymentVoucherRepository;
import com.kaihei.esportingplus.payment.domain.document.PaymentVoucher;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNotEquals;

/**
 * 支付凭证DAO单元测试用例
 *
 * @author haycco
 */
public class PaymentVoucherRepositoryTest extends BaseTest {

    @Autowired
    private PaymentVoucherRepository paymentVoucherRepository;

    @After
    public void tearDown() {
        paymentVoucherRepository.deleteAll();
    }

    @Test
    public void saveTest() {
        PaymentVoucher paymentVoucher = new PaymentVoucher();
        paymentVoucher.setAppId("BAOJI_APP_IOS");
        paymentVoucher.setClientIp("192.168.0.110");
        paymentVoucher.setCompletedTime(LocalDateTime.now());
        paymentVoucher.setMessage("test");
        paymentVoucher.setOrderId("2018000001");
        paymentVoucher.setRequestParams("query=abc&name=test");
        paymentVoucher.setRequestUrl("http://test");
        paymentVoucher.setRequestMethod("testMethod");
        paymentVoucher.setState("success");
        paymentVoucher.setTimestamp(LocalDateTime.now());
        paymentVoucher.setMetadata("test metadata");

        paymentVoucher = paymentVoucherRepository.save(paymentVoucher);
        assertNotEquals(paymentVoucher.getId(), "");
        assertNotEquals(paymentVoucher.getId(), null);
    }



}
