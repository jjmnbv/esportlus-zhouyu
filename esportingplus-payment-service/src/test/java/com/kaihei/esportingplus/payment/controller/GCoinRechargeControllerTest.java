package com.kaihei.esportingplus.payment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kaihei.esportingplus.payment.BaseMvcTest;
import org.junit.Test;

public class GCoinRechargeControllerTest extends BaseMvcTest {

    @Test
    public void urlTest() throws Exception {
        this.mvc.perform(post("/gcoin/recharge")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(put("/gcoin/recharge/1")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(get("/gcoin/recharge/1")).andExpect(status().isOk()).andDo(print());
    }
}
