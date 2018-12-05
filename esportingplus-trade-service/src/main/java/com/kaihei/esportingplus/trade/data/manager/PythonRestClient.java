package com.kaihei.esportingplus.trade.data.manager;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.HttpUtils;
import com.kaihei.esportingplus.trade.api.params.CheckCouponParams;
import com.kaihei.esportingplus.trade.api.params.ConsumeCouponParams;
import com.kaihei.esportingplus.trade.api.params.pay.WeiXinPayConfirmPacket;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PythonRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PythonRestClient.class);

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Value("${python.refundUrl}")
    private String refundUrl;

    @Value("${python.profitUrl}")
    private String profitUrl;

    @Value("${python.checkCouponUrl}")
    private String checkCouponUrl;

    @Value("${python.consumeCouponUrl}")
    private String consumeCouponUrl;

    @Value("${python.wxpay.orderUrl}")
    private String wxpayOrderUrl;

    /**
     * 申请退款
     */
    public ResponsePacket refund(String json) {
        LOGGER.info("申请退款：url={},param={}", refundUrl, json);
        return restTemplateExtrnal.postForObject(refundUrl, HttpUtils.buildJsonParam(json), ResponsePacket.class);
    }

    /**
     * 校验优惠券是否可用
     */
    public ResponsePacket checkCoupon(CheckCouponParams checkCouponParams){
        LOGGER.debug("校验优惠券是否可用：url={},param={}", checkCouponUrl, checkCouponParams);
        return restTemplateExtrnal.postForObject(checkCouponUrl, HttpUtils.buildParamWithSnake(checkCouponParams), ResponsePacket.class);
    }

    /**
     * 消费优惠券
     */
    public ResponsePacket consumeCoupon(ConsumeCouponParams consumeCouponParams){
        LOGGER.debug("消费优惠券：url={},param={}", consumeCouponUrl, consumeCouponParams);
        return restTemplateExtrnal.postForObject(consumeCouponUrl, HttpUtils.buildParamWithSnake(consumeCouponParams), ResponsePacket.class);
    }

    /**
     * 提交暴鸡收益到工作室统计
     */
    public ResponsePacket sendProfit(String profitJson){
        LOGGER.info("提交暴鸡收益到工作室统计：url={},param={}", profitUrl, profitJson);
        return restTemplateExtrnal.postForObject(profitUrl, HttpUtils.buildJsonParam(profitJson), ResponsePacket.class);
    }

    /**
     * 查询微信支付订单
     */
    public ResponsePacket getWxPayOrder(String orderSequence){
        LOGGER.info("查询微信支付订单：url={},param={}", wxpayOrderUrl, orderSequence);
        return restTemplateExtrnal.postForObject(wxpayOrderUrl + orderSequence, null, ResponsePacket.class);
    }


}