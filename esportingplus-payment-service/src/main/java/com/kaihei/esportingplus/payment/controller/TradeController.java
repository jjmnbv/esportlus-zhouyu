package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.payment.api.enums.PayChannelEnum;
import com.kaihei.esportingplus.payment.api.feign.TradeServiceClient;
import com.kaihei.esportingplus.payment.api.params.CloseOrCancelPayOrderParams;
import com.kaihei.esportingplus.payment.api.params.PayOrderParams;
import com.kaihei.esportingplus.payment.api.params.RefundOrderParams;
import com.kaihei.esportingplus.payment.api.vo.CreateRefundOrderReturnVo;
import com.kaihei.esportingplus.payment.api.vo.TestPayVo;
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;
import com.kaihei.esportingplus.payment.service.PayService;
import com.kaihei.esportingplus.payment.util.*;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 暴鸡支付服务
 *
 * @author haycco
 */
@RestController
@Api(tags = {"暴鸡支付服务相关API"})
@RequestMapping("/trade")
public class TradeController implements TradeServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    @Qualifier("tenpayService")
    PayService tenpayService;

    @Autowired
    @Qualifier("alipayService")
    PayService alipayService;

    @Autowired
    @Qualifier("walletPayService")
    PayService walletPayService;

    @Autowired
    private ExternalPaymentUtil externalPaymentUtil;

    @Autowired
    private ExternalRefundUtil externalRefundUtil;

    @Override
    public ResponsePacket<Map<String, String>> create(@RequestBody PayOrderParams payOrderParams,
                                                      @RequestParam(value = "appId", required = true) String appId,
                                                      @RequestParam(value = "channelTag", required = true) String channelTag) {

        logger.info("入参 >> ");
        logger.info("PayOrderParams :{} ", payOrderParams);
        logger.info("appId :{} , channelTag : {}", appId, channelTag);

        //校验入参
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, payOrderParams);

        //根据channelTag提交给不同的处理方
        if (PayChannelEnum.WALLET_PAY.getValue().equals(channelTag)) {
            logger.info("暴鸡币支付");
            Map<String, String> map = walletPayService.createPaymentOrder(payOrderParams, appId, channelTag, null, null, null);
            logger.debug("出参 >> map :{} ", map);
            return ResponsePacket.onSuccess(map);

        } else if (PayChannelEnum.ALI_APP_PAY.getValue().equals(channelTag) || PayChannelEnum.ALI_H5_PAY.getValue().equals(channelTag)) {
            logger.debug("支付宝支付");
            //支付宝业务
            String clientIP = payOrderParams.getIp();

            Map<String, String> map = alipayService.createPaymentOrder(payOrderParams, appId, channelTag, null, clientIP, null);

            logger.debug("出参 >> map : {} ", map);

            return ResponsePacket.onSuccess(map);
        } else {
            logger.info("微信/QQ支付");
            //腾讯支付业务
            Map<String, String> map = tenpayService.createPaymentOrder(payOrderParams, appId, channelTag, null, "", null);
            logger.debug("出参 >> map : {} ", map);

            return ResponsePacket.onSuccess(map);
        }
    }

    @Override
    public ResponsePacket cancel(@RequestBody CloseOrCancelPayOrderParams closeOrCancelPayOrderParams) {

        logger.info("入参 >>>> ");
        logger.info("payOrderParams : {} ", closeOrCancelPayOrderParams);
        //校验入参
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, closeOrCancelPayOrderParams);

        String outTradeNo = closeOrCancelPayOrderParams.getOutTradeNo();
        String orderType = closeOrCancelPayOrderParams.getOrderType();

        Map<String, String> payMap = externalPaymentUtil.getPaySetting(outTradeNo, orderType);
        String appId = payMap.get("appId");
        String channelTag = payMap.get("channelTag");
        logger.info("appId : {} , channelTag : {} ", appId, channelTag);

        //根据channelTag提交给不同的处理方
        if (PayChannelEnum.ALI_APP_PAY.getValue().equals(channelTag) || PayChannelEnum.ALI_H5_PAY.getValue().equals(channelTag)) {

            //支付宝业务
            ExternalPaymentOrder externalPaymentOrder = alipayService.cancelPaymentOrder(closeOrCancelPayOrderParams, appId, channelTag, null);
            logger.debug("paymentOrderReturnVo >> {}", externalPaymentOrder);
            return ResponsePacket.onSuccess(externalPaymentOrder);

        } else {
            logger.debug("该渠道暂未开通支付订单取消功能");
            return null;
        }

    }

    /**
     * 关闭订单
     */
    @Override
    public ResponsePacket<ExternalPaymentOrder> close(@RequestBody CloseOrCancelPayOrderParams closeOrCancelPayOrderParams) {
        logger.info("入参 >>>> ");
        logger.info("payOrderParams : {} ", closeOrCancelPayOrderParams);

        //校验入参
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, closeOrCancelPayOrderParams);
        String outTradeNo = closeOrCancelPayOrderParams.getOutTradeNo();
        String orderType = closeOrCancelPayOrderParams.getOrderType();
        Map<String, String> payMap = externalPaymentUtil.getPaySetting(outTradeNo, orderType);
        String appId = payMap.get("appId");
        String channelTag = payMap.get("channelTag");
        logger.info("appId : {} , channelTag : {} ", appId, channelTag);

        //判断微信/qq/支付宝
        if (PayChannelEnum.ALI_APP_PAY.getValue().equals(channelTag) || PayChannelEnum.ALI_H5_PAY.getValue().equals(channelTag)) {

            logger.debug("支付宝-支付订单关闭功能");
            ExternalPaymentOrder externalPaymentOrder = alipayService.closePaymentOrder(closeOrCancelPayOrderParams, appId, channelTag, null);
            logger.debug("出参 >> externalPaymentOrder : {} ", externalPaymentOrder);
            return ResponsePacket.onSuccess(externalPaymentOrder);

        } else if (PayChannelEnum.WALLET_PAY.getValue().equals(channelTag)) {
            logger.error("该渠道暂未开通订单关闭功能");
            return null;
        } else {
            logger.debug("微信/QQ支付-支付订单关闭功能");
            //腾讯支付业务
            ExternalPaymentOrder externalPaymentOrder = tenpayService.closePaymentOrder(closeOrCancelPayOrderParams, appId, channelTag, null);
            logger.debug("出参 >> externalPaymentOrder : {} ", externalPaymentOrder);
            return ResponsePacket.onSuccess(externalPaymentOrder);
        }
    }

    @Override
    public ResponsePacket query(@PathVariable("outTradeNo") String outTradeNo,
                                @RequestParam(value = "orderType", required = true) String orderType) {

        logger.info("入参 >>>> ");
        logger.info("outTradeNo : {} , orderType : {} ", outTradeNo, orderType);

        Map<String, String> payMap = externalPaymentUtil.getPaySetting(outTradeNo, orderType);
        String appId = payMap.get("appId");
        String channelTag = payMap.get("channelTag");
        logger.info("appId : {} , channelTag : {} ", appId, channelTag);

        if (PayChannelEnum.ALI_APP_PAY.getValue().equals(channelTag) || PayChannelEnum.ALI_H5_PAY.getValue().equals(channelTag)) {
            logger.debug("支付宝-查询支付订单详情");
            ExternalPaymentOrder paymentOrder = alipayService.searchPaymentOrderInfo(outTradeNo, appId, channelTag, null, orderType);
            return ResponsePacket.onSuccess(paymentOrder);

        } else if (PayChannelEnum.WALLET_PAY.getValue().equals(channelTag)) {

            return null;
        } else {
            //腾讯支付业务
            logger.debug("微信/QQ支付-查询支付订单详情");
            ExternalPaymentOrder paymentOrder = tenpayService.searchPaymentOrderInfo(outTradeNo, appId, channelTag, null, orderType);
            return ResponsePacket.onSuccess(paymentOrder);
        }
    }

    @Override
    public ResponsePacket refund(@RequestBody RefundOrderParams refundOrderParams) {

        logger.info("入参 >>>> ");
        logger.info("refundOrderParams : {}", refundOrderParams);

        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, refundOrderParams);
        String outTradeNo = refundOrderParams.getOutTradeNo();
        String orderType = refundOrderParams.getOrderType();
        Map<String, String> payMap = externalPaymentUtil.getPaySetting(outTradeNo, orderType);
        String appId = payMap.get("appId");
        String channelTag = payMap.get("channelTag");
        logger.info("appId : {} , channelTag : {} ", appId, channelTag);

        if (PayChannelEnum.ALI_APP_PAY.getValue().equals(channelTag) || PayChannelEnum.ALI_H5_PAY.getValue().equals(channelTag)) {
            logger.debug("支付宝-支付订单退款");
            CreateRefundOrderReturnVo refundOrder = alipayService.createRefundOrder(refundOrderParams, appId, channelTag, null);
            return ResponsePacket.onSuccess(refundOrder);

        } else if (PayChannelEnum.WALLET_PAY.getValue().equals(channelTag)) {
            return null;
        } else {
            logger.debug("微信/QQ支付-支付订单退款");
            CreateRefundOrderReturnVo refundOrder = tenpayService.createRefundOrder(refundOrderParams, appId, channelTag, null);
            return ResponsePacket.onSuccess(refundOrder);
        }

    }

    @Override
    public ResponsePacket refundQuery(@PathVariable("outRefundNo") String outRefundNo) {
        logger.debug("入参 >>>> ");
        logger.info("outRefundNo : {} ", outRefundNo);

        Map<String, String> payMap = externalRefundUtil.getPaySetting(outRefundNo);
        String appId = payMap.get("appId");
        String channelTag = payMap.get("channelTag");
        logger.info("appId : {} , channelTag : {} ", appId, channelTag);

        //接口参数判断
        if (PayChannelEnum.ALI_APP_PAY.getValue().equals(channelTag) || PayChannelEnum.ALI_H5_PAY.getValue().equals(channelTag)) {
            logger.debug("支付宝支付-查询退款订单详情");
            ExternalRefundOrder externalRefundOrder = alipayService.searchRefundOrderInfo(outRefundNo, appId, channelTag, null);
            logger.debug("出参 >> externalRefundOrder :{} ", externalRefundOrder);
            return ResponsePacket.onSuccess(externalRefundOrder);
        } else if (PayChannelEnum.WALLET_PAY.getValue().equals(channelTag)) {
            logger.debug("暴鸡币支付-查询退款订单详情");
            return null;
        } else {
            logger.debug("微信/QQ支付-查询退款订单详情");
            ExternalRefundOrder externalRefundOrder = tenpayService.searchRefundOrderInfo(outRefundNo, appId, channelTag, null);
            logger.debug("出参 >> externalRefundOrder :{} ", externalRefundOrder);
            return ResponsePacket.onSuccess(externalRefundOrder);
        }

    }

    @Override
    public Object createTest(@RequestBody PayOrderParams payOrderParams,
                             @RequestParam(value = "appId", required = true) String appId,
                             @RequestParam(value = "channelTag", required = true) String channelTag) {

        logger.info("入参 >>>> ");
        logger.info("PayOrderParams :{} ", payOrderParams);
        logger.info("appId :{} , channelTag : {}", appId, channelTag);

        payOrderParams.setCurrencyType("CNY");
        payOrderParams.setArea("china");
        payOrderParams.setIp("192.168.0.1");
        payOrderParams.setNotifyUrl("https://dev.kaiheikeji.com/v3/gcoin/recharge_notify/");
        payOrderParams.setSubject("subject");
        payOrderParams.setTotalAmount(101);
        String clientIP = payOrderParams.getIp();
        String area = payOrderParams.getArea();
        //校验入参
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, payOrderParams);

        //根据channelTag提交给不同的处理方
        if (PayChannelEnum.WALLET_PAY.getValue().equals(channelTag)) {
            logger.info("暴鸡币支付");
            Map<String, String> map = walletPayService.createPaymentOrder(payOrderParams, appId, channelTag, null, null, null);
            logger.debug("出参 >> map :{} ", map);
            TestPayVo vo = new TestPayVo();
            vo.setOrderString(map.get("sign"));
            return vo;

        } else if (PayChannelEnum.ALI_APP_PAY.getValue().equals(channelTag) || PayChannelEnum.ALI_H5_PAY.getValue().equals(channelTag)) {
            logger.debug("支付宝支付");
            //支付宝业务

            Map<String, String> map = alipayService.createPaymentOrder(payOrderParams, appId, channelTag, null, clientIP, area);

            logger.debug("出参 >> map :{} ", map);
            TestPayVo vo = new TestPayVo();
            vo.setOrderString(map.get("sign"));
            return vo;
        } else {
            logger.info("微信/QQ支付");

            //腾讯支付业务
            Map<String, String> map = tenpayService.createPaymentOrder(payOrderParams, appId, channelTag, null, clientIP, area);
            logger.debug("出参 >> map : {} ", map);
            Map<String, String> result = new HashMap<>();
            result.put("appid", map.get("appid"));
            result.put("partnerid", map.get("partnerid"));
            result.put("prepayid", map.get("prepayid"));
            result.put("package", map.get("packageX"));
            result.put("timestamp", map.get("timestamp"));
            result.put("noncestr", map.get("noncestr"));
            result.put("sign", map.get("sign"));

            logger.debug("出参 >> {}", FastJsonUtils.toJson(result));
            return FastJsonUtils.toJson(result);
        }

    }

    @PostMapping(value = "/notify/{appId}/{channelTag}/{type}")
    public Object paymentNotify(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable(value = "appId", required = true) String appId,
                                @PathVariable(value = "channelTag", required = true) String channelTag,
                                @PathVariable(value = "type", required = true) String type) throws Exception {
        logger.info("入参 >>>>");
        logger.info("appId {} ,channelTag :{} ", appId, channelTag);

        if (PayChannelEnum.WALLET_PAY.getValue().equals(channelTag)) {
            logger.debug("暴鸡币支付-处理暴鸡币支付的回调信息");
            //暴鸡币支付成功以后发起的回调信息
            Map<String, String> maps = AlipayUtil.handleAlipayRequestParams(request);
            logger.debug("maps >> ", maps);
            walletPayService.walletNotify(maps);
            return ResponsePacket.onSuccess();

        } else if (PayChannelEnum.ALI_APP_PAY.getValue().equals(channelTag) || PayChannelEnum.ALI_H5_PAY.getValue().equals(channelTag)) {

            logger.debug("支付宝支付-处理支付宝支付的回调信息");
            Map<String, String> receiveMap = AlipayUtil.handleAlipayRequestParams(request);
            logger.info("支付宝支付回调参数:" + receiveMap);
            boolean signVerified = false;
            try {
                signVerified = alipayService.handleNotify(receiveMap, appId, channelTag, null, null);
            } catch (BusinessException e) {
                logger.error("exception : {} ", e.getErrMsg());
                return "fail";
            }

            if (signVerified) {
                return "success";
            }
            return "fail";
        } else {
            logger.debug("type : {} ", type);
            //支付回调
            try {
                String xml = TenpayUtil.handleTenpayRequest(request);
                logger.debug("xml : {} ", xml);
                Map<String, String> resultMap = new HashMap<>(1);
                resultMap.put("xmlStr", xml);
                tenpayService.handleNotify(resultMap, appId, channelTag, null, type);

            } catch (Exception e) {
                return PayXmlutils.mapToXml(TenpayConstants.errorMap);
            }

            return PayXmlutils.mapToXml(TenpayConstants.successMap);

        }

    }

    @PostMapping("/notifyTest/{appId}/{channelTag}/{type}")
    public Object paymentNotifyTest(HttpServletRequest request, HttpServletResponse response,
                                    @PathVariable(value = "appId", required = true) String appId,
                                    @PathVariable(value = "channelTag", required = true) String channelTag,
                                    @PathVariable(value = "type", required = true) String type) throws Exception {
        logger.debug("入参 >>>>");
        logger.info("appId {} ,channelTag :{} ", appId, channelTag);
        //判断微信/qq/支付宝
        if (PayChannelEnum.ALI_APP_PAY.getValue().equals(channelTag) || PayChannelEnum.ALI_H5_PAY.getValue().equals(channelTag)) {

            Map<String, String> receiveMap = AlipayUtil.handleAlipayRequestParams(request);
            logger.info("支付宝支付回调参数:" + receiveMap);
            boolean signVerified = false;
            try {
                signVerified = alipayService.handleNotify(receiveMap, appId, channelTag, null, null);
            } catch (BusinessException e) {
                logger.error("exception : {} ", e.getErrMsg());
                return "fail";
            }

            if (signVerified) {
                return "success";
            }
            return "fail";

        } else {

            logger.debug("type : {} ", type);

            try {
                String xml = TenpayUtil.handleTenpayRequest(request);
                logger.debug("xml : {} ", xml);
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("xmlStr", xml);
                tenpayService.handleNotify(resultMap, appId, channelTag, null, type);

            } catch (Exception e) {
                return PayXmlutils.mapToXml(TenpayConstants.errorMap);
            }

            return PayXmlutils.mapToXml(TenpayConstants.successMap);

        }

    }

}