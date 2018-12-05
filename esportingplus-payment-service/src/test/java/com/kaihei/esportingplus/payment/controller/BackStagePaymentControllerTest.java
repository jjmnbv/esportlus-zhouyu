package com.kaihei.esportingplus.payment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kaihei.esportingplus.payment.BaseMvcTest;
import org.junit.Test;

public class BackStagePaymentControllerTest extends BaseMvcTest {

    @Test
    public void urlTest() throws Exception {
        this.mvc.perform(post("/back_stage/gcoin_recharge")).andExpect(status().isOk())
                .andDo(print());
        ;
        this.mvc.perform(post("/back_stage/deduct")).andExpect(status().isOk());
    }
}
