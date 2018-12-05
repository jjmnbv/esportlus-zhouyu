package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.payment.BaseMvcTest;
import com.kaihei.esportingplus.payment.api.vo.WalletBillsVO;
import com.kaihei.esportingplus.payment.api.vo.WalletsVO;
import com.kaihei.esportingplus.payment.config.EncryptConfig;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.GCoinRewardService;
import com.kaihei.esportingplus.payment.service.WalletService;
import com.kaihei.esportingplus.payment.util.RSAUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletControllerTest extends BaseMvcTest {

    private static final Logger logger = LoggerFactory.getLogger(WalletControllerTest.class);

    @Autowired
    private EncryptConfig encryptConfig;
    @MockBean
    BillFlowService mockedBillFlowService;
    @MockBean
    GCoinRewardService gCoinService;
    @MockBean
    WalletService walletService;
    @Autowired
    BillFlowService billFlowService;

    @Test
    public void getBills_successful() throws Exception {
        List<WalletBillsVO> data = new ArrayList<>();
        data.add(new WalletBillsVO());
        ResponsePacket<List<WalletBillsVO>> result = ResponsePacket.onSuccess(data);
        // 模拟接口返回
        BDDMockito.when(mockedBillFlowService.getGCoinBill(Matchers.any()))
                .thenReturn(data);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("moneyType", "001");
        params.add("page", "0");
        params.add("size", "10");
//        params.add("paymentChannel", "");
//        params.add("orderDimension", "");
//        params.add("orderType", "");

        this.mvc.perform(get("/wallets/1/bills").params(params))
                .andExpect(status().isOk())
                .andExpect(content().json(JacksonUtils.toJson(result)))
                .andDo(print());
    }

    @Test
    public void getWallets_successful() throws Exception {
        WalletsVO data = new WalletsVO();
        data.setGcoinAmount(BigDecimal.valueOf(20.12));
        data.setStarlightAmount(BigDecimal.valueOf(20.12));

        ResponsePacket<WalletsVO> result = ResponsePacket.onSuccess(data);
        // 模拟接口返回
        BDDMockito.when(walletService.getBalance(Matchers.anyString()))
                .thenReturn(data);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("moneyType", "001");
//        params.add("page", "0");
//        params.add("size", "10");

        this.mvc.perform(get("/wallets/1").params(params))
                .andExpect(status().isOk())
                .andExpect(content().json(JacksonUtils.toJson(result)))
                .andDo(print());
    }

    @Test
    public void getAllStarlight_successful() throws Exception {

        BDDMockito.when(mockedBillFlowService
                .getAllStarlight(Matchers.anyString(), Matchers.anyString()))
                .thenReturn(3003);

        Map<String, Integer> data = new HashMap<>(1);
        data.put("starlightAmount", 3003);
        ResponsePacket<Map<String, Integer>> result = ResponsePacket.onSuccess(data);

        this.mvc.perform(get("/wallets/1/starlight").param("tradeType", "T001"))
                .andExpect(status().isOk())
                .andExpect(content().json(JacksonUtils.toJson(result)))
                .andDo(print());
    }

    @Test
    public void getAllStarlight() throws Exception {

        Map<String, Integer> data = new HashMap<>(1);
        data.put("allAmount", 40);
        data.put("dayAmount", 10);
        data.put("monthAmount", 6);
        ResponsePacket<Map<String, Integer>> result = ResponsePacket.onSuccess(data);

        this.mvc.perform(get("/wallets/1/starlight").param("tradeType", "T001"))
                .andExpect(status().isOk())
                .andExpect(content().json(JacksonUtils.toJson(result)))
                .andDo(print());
    }

    @Ignore
    @Test
    public void testEncryptData() throws Exception {
        String privateKey = encryptConfig.getPrivateKey();
        String orderId = "test data order id";
        byte[] data = orderId.getBytes();
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
        String encodedStr = Base64Utils.encodeToString(encodedData);
        logger.info("测试原加密后的密文：{}", encodedStr);
        Map<String, String> respMap = new HashMap<String, String>();
        respMap.put("userId", "1");
        respMap.put("order_id", encodedStr);
        ResponsePacket<Map<String, String>> result = ResponsePacket.onSuccess(respMap);
        this.mvc.perform(post("/wallets/1/test_encrypt_data")).andExpect(status().isOk())
                .andExpect(content().json(JacksonUtils.toJson(result), true));
    }

    @Test
    public void urlTest() throws Exception {
        this.mvc.perform(post("/wallets/1/reward/1")).andExpect(status().isOk());
        this.mvc.perform(post("/wallets/1/reward")).andExpect(status().isOk());
        this.mvc.perform(get("/wallets/1")).andExpect(status().isOk());
        this.mvc.perform(get("/wallets/1/bills")).andExpect(status().isOk());
        this.mvc.perform(get("/wallets/1/starlight")).andExpect(status().isOk());
    }

}
