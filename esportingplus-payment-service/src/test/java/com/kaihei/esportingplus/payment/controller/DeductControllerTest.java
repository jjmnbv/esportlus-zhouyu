package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.BaseMvcTest;
import com.kaihei.esportingplus.payment.api.vo.DeductOrderVo;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class DeductControllerTest extends BaseMvcTest {

    @MockBean
    BillFlowService mockedBillFlowService;

    @Test
    public void createGCoinDeduct_successful() throws Exception {
        DeductOrderVo data = new DeductOrderVo();
        ResponsePacket<DeductOrderVo> result = ResponsePacket.onSuccess(data);

//        List<WalletBillsVO> data = new ArrayList<>();
//        ResponsePacket<List<WalletBillsVO>> result = ResponsePacket.onSuccess(data);
//        // 模拟接口返回
//        BDDMockito.when(mockedBillFlowService.getGCoinBill(Matchers.any()))
//                .thenReturn(data);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("moneyType", "001");
//        params.add("page", "0");
//        params.add("size", "10");
////        params.add("paymentChannel", "");
////        params.add("orderDimension", "");
////        params.add("orderType", "");
//
//        this.mvc.perform(get("/wallets/1/bills").params(params)).andExpect(status().isOk())
//                .andExpect(content().json(JacksonUtils.toJson(result)));
    }

}
