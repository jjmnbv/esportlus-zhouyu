package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.BaseMvcTest;
import com.kaihei.esportingplus.payment.api.params.CloudAccountOrderParams;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountRespVo;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CloudAccountControllerTest extends BaseMvcTest {

    private Logger logger = LoggerFactory.getLogger(CloudAccountControllerTest.class);

    @Autowired
    private CloudAccountController cloudAccountController;

    @Test
    public void urlTest() throws Exception {
//        this.mvc.perform(post("/cloud/pay/ANDROID_BJDJ/C007")).andExpect(status().isOk()).andDo(print());
//        this.mvc.perform(get("/cloud/account/ANDROID_BJDJ/C007")).andExpect(status().isOk()).andDo(print());
//        this.mvc.perform(get("/cloud/order/ANDROID_BJDJ/C007")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testCreateWithdrawOrder() throws Exception {

        CloudAccountOrderParams orderParams = new CloudAccountOrderParams();
        // 提现到微信
        orderParams.setChannel("zfb");
        orderParams.setOutTradeNo("180309155950h52bxrKS7pgcfseOGi0q");
        orderParams.setRealName("谌珍君");
        orderParams.setCardNo("15813829560");
        orderParams.setIdCard("43072519910722831X");
        orderParams.setTotalFee("11");

        String appName = "ANDROID_BJDJ";
        String channelName = "C007";

        ResponsePacket<CloudAccountRespVo> responsePacket = cloudAccountController.create(orderParams, appName, channelName, null);
        Assert.assertNotNull(responsePacket);
        logger.info("response : {} ", responsePacket.getData().toString());

    }

}
