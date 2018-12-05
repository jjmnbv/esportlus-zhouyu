package com.kaihei.esportingplus.payment.repository;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.data.jpa.repository.OrderIncomeRepository;
import com.kaihei.esportingplus.payment.domain.entity.OrderIncome;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotEquals;

/**
 * @author: tangtao
 **/
public class OrderIncomeRepositoryTest extends BaseTest {

    @Autowired
    private OrderIncomeRepository orderIncomeRepository;

    @Test
    public void saveTest() {

        OrderIncome orderIncome = new OrderIncome();
        orderIncome.setTotalAmount(10);
        orderIncome.setUserId("0000");
        orderIncome.setPlatformIncome(1);
        orderIncome.setBaojiIncome(1);
        orderIncome.setAttach("0000");
        orderIncome.setFlowNo("0000");
        orderIncome.setRatio(new BigDecimal(1));
        orderIncome.setOrderType(2);

        orderIncome = orderIncomeRepository.save(orderIncome);

        OrderIncome forUpdate = new OrderIncome();
        forUpdate.setId(orderIncome.getId());
        forUpdate.setTotalAmount(20);
        forUpdate.setUserId("111");
        forUpdate.setPlatformIncome(11);
        forUpdate.setBaojiIncome(11);
        forUpdate.setAttach("111");
        forUpdate.setFlowNo("111");
        forUpdate.setRatio(new BigDecimal(2));

        forUpdate = orderIncomeRepository.saveAndFlush(forUpdate);

        assertNotEquals(orderIncome.getOrderType(), forUpdate.getOrderType());

        orderIncomeRepository.delete(forUpdate.getId());

    }

}
