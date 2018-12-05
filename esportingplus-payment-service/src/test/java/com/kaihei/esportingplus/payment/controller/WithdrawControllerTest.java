package com.kaihei.esportingplus.payment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.payment.BaseMvcTest;
import com.kaihei.esportingplus.payment.api.vo.PageInfo;
import com.kaihei.esportingplus.payment.api.vo.WithdrawAuditListVo;
import com.kaihei.esportingplus.payment.service.WithdrawService;
import java.util.ArrayList;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class WithdrawControllerTest extends BaseMvcTest {

    /*@MockBean
    private SensorsAutoConfiguration mockSensorsAutoConfiguration;*/

    @MockBean
    private WithdrawService mockWithdrawService;

    @Test
    public void urlTest() throws Exception {
        this.mvc.perform(post("/withdraw/1")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(put("/withdraw/1")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(get("/withdraw/balance")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(post("/withdraw/exchange")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(get("/withdraw/1")).andExpect(status().isOk()).andDo(print());
        this.mvc.perform(post("/withdraw/balance/score")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void getAuditListByAppTest() throws Exception {
        PageInfo<WithdrawAuditListVo> data = new PageInfo<>();
        data.setTotal(0L);
        data.setList(new ArrayList<>());
        ResponsePacket<PageInfo<WithdrawAuditListVo>> result = ResponsePacket.onSuccess(data);
        // 模拟接口返回
        BDDMockito.when(mockWithdrawService
                .getAuditListByApp(Matchers.anyString(),Matchers.anyString(),Matchers.anyString()))
                .thenReturn(data);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", "0d582dfca0e84c48b2ddb7cb869d608b");
        params.add("page", "0");
        params.add("size", "10");

        this.mvc.perform(get("/withdraw/app/audits")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().json(JacksonUtils.toJson(result)))
                .andDo(print());
    }
}
