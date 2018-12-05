package com.kaihei.esportingplus.payment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kaihei.esportingplus.payment.BaseMvcTest;
import org.junit.Test;


public class AttachControllerTest extends BaseMvcTest {

    @Test
    public void urlTest() throws Exception {
        this.mvc.perform(get("/attach/account/1")).andExpect(status().isOk());
    }
}
