package com.kaihei.esportingplus.payment.repository;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.data.jpa.repository.StarlightBillRepository;
import com.kaihei.esportingplus.payment.domain.entity.StarlightBill;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * @author: tangtao
 **/
public class StarlightBalanceRepositoryTest extends BaseTest {

    @Autowired
    StarlightBillRepository starlightBillRepository;

    @Test
    public void saveTest() {
        StarlightBill starlightBill = new StarlightBill();
        starlightBill.setOrderId("000");
        starlightBill.setUserId("000");
        starlightBill.setSubject("000");
        starlightBill.setAmount(new BigDecimal(10));
        starlightBill.setOrderType("000");
        starlightBill.setTradeType("000");

        StarlightBill saved = starlightBillRepository.save(starlightBill);

        assertEquals(starlightBill.getSubject(), saved.getSubject());

        starlightBillRepository.delete(saved.getId());

    }
}
