package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.data.jpa.repository.StarlightBillRepository;
import com.kaihei.esportingplus.payment.domain.entity.StarlightBill;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class BillFlowServiceTest extends BaseTest {

    @Autowired
    BillFlowService billFlowService;

    @MockBean
    StarlightBillRepository starlightBillRepository;

    @Test
    public void getUsableStarlight_successful() {

        StarlightBill s1 = new StarlightBill();
        s1.setAmount(new BigDecimal(10.41).setScale(2, BigDecimal.ROUND_DOWN));

        StarlightBill s2 = new StarlightBill();
        s2.setAmount(new BigDecimal(10.21).setScale(2, BigDecimal.ROUND_DOWN));

        StarlightBill s3 = new StarlightBill();
        s3.setAmount(new BigDecimal(10.31).setScale(2, BigDecimal.ROUND_DOWN));

        List<StarlightBill> starlightBills = Lists.newArrayList(s1, s2, s3);

        BDDMockito.when(starlightBillRepository
                .findAllByUserIdAndTradeType(Matchers.anyString(), Matchers.anyString()))
                .thenReturn(starlightBills);

        Integer allStarlight = billFlowService.getAllStarlight("1", "T001");

        assertTrue(3093 == allStarlight);
    }

    @Test
    public void getUsableStarlight() {
        Integer allStarlight = billFlowService.getAllStarlight("1", "T001");
    }
}
