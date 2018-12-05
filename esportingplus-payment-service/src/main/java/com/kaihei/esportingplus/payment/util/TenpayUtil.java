package com.kaihei.esportingplus.payment.util;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.payment.api.PayConstants;
import com.kaihei.esportingplus.payment.api.enums.ExternalPayStateEnum;
import com.kaihei.esportingplus.payment.api.enums.ExternalRefundStateEnum;
import com.kaihei.esportingplus.payment.api.enums.PayChannelEnum;
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;
import com.kaihei.esportingplus.payment.domain.entity.TenpaySetting;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName NewTenpayUtil
 * @Description TODO
 * @Author xusisi
 * @Date 2018/11/24 下午2:28
 */
public class TenpayUtil {
    private static Logger logger = LoggerFactory.getLogger(TenpayUtil.class);

    public static String handleTenpayRequest(HttpServletRequest request) {

        try {
            String xml = IOUtils.toString(request.getInputStream());
            if (StringUtils.isEmpty(xml)) {
                throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_NOTIFY_REQUEST_FAIL);
            }
            return xml;
        } catch (IOException e) {
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_NOTIFY_REQUEST_FAIL);
        }

    }

    /**
     * 根据channelTag获取对应的tradeType信息
     *
     * @param : [channelTag]
     * @Author : xusisi
     **/
    public static String getTradeTypeByChannelTag(String channelTag) {
        String tradeType = null;
        PayChannelEnum payChannelEnum = PayChannelEnum.lookup(channelTag);
        switch (payChannelEnum) {
            //APP支付
            case QQ_APP_PAY:
            case WECHAT_APP_PAY:
                tradeType = TenpayConstants.TENPAY_TRADE_TYPE_APP;
                break;
            //公众号或者小程序支付、微信内打开的H5页面
            case WECHAT_PA_PAY:
            case QQ_PA_PAY:
            case QQ_MP_PAY:
            case WECHAT_MP_PAY:
            case WECHAT_INNER_H5_PAY:
                tradeType = TenpayConstants.TENPAY_TRADE_TYPE_JSAPI;
                break;
            //微信外的浏览器打开的H5页面
            case WECHAT_OUTER_H5_PAY:
                tradeType = TenpayConstants.TENPAY_TRADE_TYPE_MWEB;
                break;
            default:
                break;
        }
        return tradeType;
    }

    /**
     * @Description:
     * @Param: [channelTag]
     * @Return boolean
     * @Author: zhouyu , xusisi
     */
    /**
     * 根据channelTag判断是微信还是QQ支付
     *
     * @param : [channelTag]
     * @Author : xusisi
     **/
    public static boolean getUrlQqOrWechat(String channelTag) {
        PayChannelEnum payChannelEnum = PayChannelEnum.lookup(channelTag);
        Optional.ofNullable(payChannelEnum).orElseThrow(() -> new BusinessException(BizExceptionEnum.TENPAY_PAY_SETTING_NOT_EXISTS));
        switch (payChannelEnum) {
            //QQ支付
            case QQ_PA_PAY:
            case QQ_APP_PAY:
            case QQ_MP_PAY:
                return false;
            default:
                return true;
        }
    }

    public static Map<String, String> constructCommonParam(TenpaySetting tenpaySetting) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put(TenpayConstants.APPID, tenpaySetting.getAppId());
        requestMap.put(TenpayConstants.MCH_ID, tenpaySetting.getMchId());
        requestMap.put(TenpayConstants.NONCE_STR, RandomStringUtils.random(32, true, true));
        return requestMap;
    }

    /**
     * 创建预支付订单信息
     *
     * @param : [paymentOrder, ip, channelTag, tenpaySetting]
     * @Author : xusisi
     **/
    public static Map<String, String> createPrePaymentInfo(ExternalPaymentOrder paymentOrder, String ip, String channelTag,
                                                           TenpaySetting tenpaySetting) throws Exception {

        //构建预支付参数
        Map<String, String> requestMap = constructCommonParam(tenpaySetting);
        requestMap.put(TenpayConstants.FEE_TYPE, TenpayConstants.CURRENCY_TYPE);
        String tradeType = getTradeTypeByChannelTag(channelTag);
        requestMap.put(TenpayConstants.TRADE_TYPE, tradeType);
        requestMap.put(TenpayConstants.SPBILL_CREATE_IP, ip);
        requestMap.put(TenpayConstants.SIGN_TYPE, tenpaySetting.getSignType());
        requestMap.put(TenpayConstants.OUT_TRADE_NO, paymentOrder.getOrderId());
        requestMap.put(TenpayConstants.TOTAL_FEE, String.valueOf(paymentOrder.getTotalFee()));
        //支付回调URL
        String payNotifyUrl = tenpaySetting.getNotifyUrl() + "/" + TenpayConstants.NOTIFY_TYPE_PAYMENT;
        requestMap.put(TenpayConstants.NOTIFY_URL, payNotifyUrl);
        requestMap.put(TenpayConstants.BODY, paymentOrder.getSubject());

        //是否是沙箱支付
        String sandboxEnable = tenpaySetting.getSandboxEnable();
        if (Boolean.TRUE.equals(Boolean.valueOf(sandboxEnable))) {
            //获取沙箱签名
            tenpaySetting.setApiSecret(PaySignUtils.getSandBoxSign(tenpaySetting));
        }
        requestMap.put(TenpayConstants.SIGN, PaySignUtils.generateSignature(requestMap, tenpaySetting.getApiSecret(), tenpaySetting.getSignType()));
        String reqBody = PayXmlutils.mapToXml(requestMap);

        logger.info("腾讯支付下单参数：{}", reqBody);
        String url = null;
        boolean weChat = getUrlQqOrWechat(channelTag);
        if (weChat) {
            //微信支付-是否是沙箱支付
            if (Boolean.TRUE.equals(Boolean.valueOf(sandboxEnable))) {
                url = PayConstants.SANDBOX_UNIFIED_ORDER_URL;
            } else {
                url = PayConstants.UNIFIED_ORDER_URL;
            }
        } else {
            //QQ支付
            url = PayConstants.QQ_UNIFIED_ORDER_SUFEIX;
        }
        //发起下单
        String response = new TencentPayRquest(tenpaySetting).requestWithoutCert(url, reqBody);
        logger.info("腾讯下单返回数据：{}", response);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("requestParams", reqBody);
        resultMap.put("responseParams", response);
        return resultMap;
    }

    /**
     * 校验预支付订单信息返回参数
     *
     * @param : [responseMap, orderId, channelTag, tenpaySetting]
     * @Author : zhouyu , xusisi
     **/
    public static ExternalPaymentOrder checkPrepayResponse(String respStr, TenpaySetting tenpaySetting,
                                                           ExternalPaymentOrder paymentOrder) throws BusinessException {
        //校验返回xml数据，校验签名，返回map
        Map<String, String> responseMap = PaySignUtils.processResponseXml(respStr, tenpaySetting);

        String returnCode = responseMap.get(TenpayConstants.RETURN_CODE);
        if (TenpayConstants.RETURN_CODE_FAIL.equals(returnCode)) {
            String returnMsg = responseMap.get(TenpayConstants.RETURN_MSG);
            logger.error("微信/QQ支付-调用预创建订单接口失败，code : {} ,msg :{} ", returnCode, returnMsg);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_CREATE_PAYMENT_CALL_FAIL);
        }
        String resultCode = responseMap.get(TenpayConstants.RESULT_CODE);
        if (TenpayConstants.RESULT_CODE_FAIL.equals(resultCode)) {
            String errCode = responseMap.get(TenpayConstants.ERR_CODE);
            String errCodeDes = responseMap.get(TenpayConstants.ERR_CODE_DES);
            logger.error("微信/QQ支付-创建订单业务失败,code :{} - msg :{} ", errCode, errCodeDes);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_CREATE_PAYMENT_SERVICE_FAIL);
        }

        String prepayId = responseMap.get(TenpayConstants.PREPAY_ID);
        paymentOrder.setState(ExternalPayStateEnum.UNPAIED.getCode());
        paymentOrder.setPrePayId(prepayId);

        return paymentOrder;

    }

    /**
     * 构建支付查询参数
     *
     * @param : [outTradeNo, tenCentpaySetting]
     * @Author : xusisi
     **/
    public static Map<String, String> queryPaymentInfo(String orderId, String channelTag, TenpaySetting tenpaySetting) {
        Map<String, String> requestMap = constructCommonParam(tenpaySetting);
        requestMap.put(TenpayConstants.OUT_TRADE_NO, orderId);

        String sandboxEnable = tenpaySetting.getSandboxEnable();
        if (Boolean.TRUE.equals(Boolean.valueOf(sandboxEnable))) {
            //获取沙箱签名(沙箱环境需要独立独立apiKey)
            tenpaySetting.setApiSecret(PaySignUtils.getSandBoxSign(tenpaySetting));
        }
        requestMap.put(TenpayConstants.SIGN, PaySignUtils.generateSignature(requestMap, tenpaySetting.getApiSecret(), tenpaySetting.getSignType()));
        String reqBody = PayXmlutils.mapToXml(requestMap);
        logger.info("微信/QQ-支付查询参数：{}", reqBody);

        String urlSufix = null;
        boolean wechat = getUrlQqOrWechat(channelTag);
        if (!wechat) {
            urlSufix = PayConstants.QQ_ORDER_QUERY_SUFEIX;
        } else {
            if (Boolean.TRUE.equals(Boolean.valueOf(sandboxEnable))) {
                urlSufix = PayConstants.SANDBOX_ORDER_QUERY_URL;
            } else {
                urlSufix = PayConstants.ORDER_QUERY_URL;
            }
        }
        //发起查询
        String queryResult = null;
        try {
            queryResult = new TencentPayRquest(tenpaySetting).requestWithoutCert(urlSufix, reqBody);
            logger.info("queryResult : {} ", queryResult);
        } catch (Exception e) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_TENPAY_QUERY_PAYMENT_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_QUERY_PAYMENT_FAIL);
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("requestParams", reqBody);
        resultMap.put("responseParams", queryResult);
        return resultMap;

    }

    /**
     * @Description: 解析查询返回
     * @Param: [responseMap]
     * @Return com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder
     * @Author: zhouyu
     */
    public static ExternalPaymentOrder checkQueryPaymentInfo(String respStr, TenpaySetting tenpaySetting, ExternalPaymentOrder paymentOrder) {
        //校验返回xml数据，校验签名，返回map
        Map<String, String> responseMap = PaySignUtils.processResponseXml(respStr, tenpaySetting);

        String returnCode = responseMap.get(TenpayConstants.RETURN_CODE);
        if (TenpayConstants.RETURN_CODE_FAIL.equals(returnCode)) {
            String returnMsg = responseMap.get(TenpayConstants.RETURN_MSG);
            logger.error("exception : {}，code : {} ,msg :{} ", BizExceptionEnum.EXTERNAL_TENPAY_QUERY_PAYMENT_CALL_FAIL.getErrMsg(), returnCode,
                    returnMsg);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_QUERY_PAYMENT_CALL_FAIL);
        }

        String resultCode = responseMap.get(TenpayConstants.RESULT_CODE);
        if (TenpayConstants.RESULT_CODE_FAIL.equals(resultCode)) {
            String errCode = responseMap.get(TenpayConstants.ERR_CODE);
            String errCodeDes = responseMap.get(TenpayConstants.ERR_CODE_DES);
            logger.error("exception :{} ,code :{} - msg :{} ", BizExceptionEnum.EXTERNAL_TENPAY_QUERY_PAYMENT_SERVICE_FAIL.getErrMsg(), errCode,
                    errCodeDes);

            //考虑订单不存在的情况下，直接将订单视为支付失败
            if (TenpayConstants.ERR_CODE_ORDERNOTEXIST.equals(errCode)) {
                paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
                return paymentOrder;
            }
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_QUERY_PAYMENT_SERVICE_FAIL);
        }

        String respTradeState = responseMap.get(TenpayConstants.TRADE_STATE);
        String respTransactionId = responseMap.get(TenpayConstants.TRANSACTION_ID);
        String respOutTradeNo = responseMap.get(TenpayConstants.OUT_TRADE_NO);
        String respTotalFeeStr = responseMap.get(TenpayConstants.TOTAL_FEE);
        Integer respTotalFee = CommonUtils.strToInteger(respTotalFeeStr);
        String orderId = paymentOrder.getOrderId();
        if (!respOutTradeNo.equals(orderId)) {
            logger.warn("orderId {} 与查询返回的不匹配 ", orderId);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_SEARCH_PAYMENT_ORDER_NOT_MATCH);
        }

        //交易订单金额不一致
        Integer respAmount = paymentOrder.getTotalFee();
        if (!respTotalFee.equals(respAmount)) {
            logger.error("orderId : {} 与查询返回的订单交易金额不一致", orderId);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_SEARCH_PAYMENT_ORDER_NOT_MATCH);
        }

        paymentOrder.setTransactionId(respTransactionId);

        if (TenpayConstants.TRADE_STATE_SUCCESS.equals(respTradeState)) {
            String timdEnd = responseMap.get(TenpayConstants.TIME_END);
            paymentOrder.setPaiedTime(DateUtil.str2dateWithYMDHMS(timdEnd));
            //支付成功
            paymentOrder.setState(ExternalPayStateEnum.SUCCESS.getCode());
        } else if (TenpayConstants.TRADE_STATE_CLOSED.equals(respTradeState)) {
            //已经关闭
            paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
        } else if (TenpayConstants.TRADE_STATE_USERPAYING.equals(respTradeState) || TenpayConstants.TRADE_STATE_NOTPAY.equals(respTradeState)
                || TenpayConstants.TRADE_STATE_PAYERROR.equals(respTradeState)) {
            //正在支付、未支付、支付失败
            paymentOrder.setState(ExternalPayStateEnum.UNPAIED.getCode());
        } else if (TenpayConstants.TRADE_STATE_REVOKED.equals(respTradeState)) {
            //已经撤销（刷卡支付）
            paymentOrder.setState(ExternalPayStateEnum.CANCEL.getCode());
        } else if (TenpayConstants.TRADE_STATE_REFUND.equals(respTradeState)) {
            //转入退款
            logger.info("订单转入退款，订单号：{}", respOutTradeNo);
            //paymentOrder.setState(ExternalRefundStateEnum.REFUNDING.getCode());
        }

        return paymentOrder;

    }

    /**
     * 创建退款订单申请
     *
     * @param : [refundOrder, tenpaySetting]
     * @Author : xusisi
     **/
    public static Map<String, String> createRefundInfo(ExternalRefundOrder refundOrder, ExternalPaymentOrder paymentOrder,
                                                       TenpaySetting tenpaySetting, String channelTag, String appId) {
        logger.info("入参 >> refundOrder : {} ", refundOrder);

        String payOrderId = refundOrder.getPayOrderId();
        String orderId = refundOrder.getOrderId();
        Integer refundFee = refundOrder.getTotalFee();
        Integer totalFee = paymentOrder.getTotalFee();

        Map<String, String> requestMap = constructCommonParam(tenpaySetting);
        requestMap.put(TenpayConstants.OUT_TRADE_NO, payOrderId);
        //退款订单号
        requestMap.put(TenpayConstants.OUT_REFUND_NO, orderId);
        //订单总金额
        requestMap.put(TenpayConstants.TOTAL_FEE, String.valueOf(totalFee));
        //退款金额
        requestMap.put(TenpayConstants.REFUND_FEE, String.valueOf(refundFee));

        boolean weChat = getUrlQqOrWechat(channelTag);
        if (!weChat) {
            //QQ退款需要商户号id和密码
            //操作员号
            requestMap.put(TenpayConstants.OP_USER_ID, tenpaySetting.getMchId());
            //操作员密码，就是商户号的登录密码MD5后
            try {
                requestMap.put(TenpayConstants.OP_USER_PASSWD, PaySignUtils.MD5(PayConstants.QQ_MCH_PASSWORD));
            } catch (Exception e) {
                logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_TENPAY_MD5_ERROR.getErrMsg());
                throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_MD5_ERROR);
            }
        } else {
            //QQ没有退款回调，微信有
            //退款回调URL
            String refundNotifyUrl = tenpaySetting.getNotifyUrl() + "/" + TenpayConstants.NOTIFY_TYPE_REFUND;
            requestMap.put(TenpayConstants.NOTIFY_URL, refundNotifyUrl);
        }
        //是否是沙箱支付
        String sandboxEnable = tenpaySetting.getSandboxEnable();
        if (Boolean.TRUE.equals(Boolean.valueOf(sandboxEnable))) {
            //获取沙箱签名(沙箱环境需要独立独立apiKey)
            tenpaySetting.setApiSecret(PaySignUtils.getSandBoxSign(tenpaySetting));
        }
        requestMap.put(TenpayConstants.SIGN, PaySignUtils.generateSignature(requestMap, tenpaySetting.getApiSecret(), tenpaySetting.getSignType()));
        String reqBody = PayXmlutils.mapToXml(requestMap);
        logger.info("腾讯支付退款参数：{}", reqBody);
        String urlSufix = null;
        if (!weChat) {
            urlSufix = PayConstants.QQ_ORDER_REFUND_SUFIX;
        } else {
            //是否是沙箱支付
            if (Boolean.TRUE.equals(Boolean.valueOf(sandboxEnable))) {
                urlSufix = PayConstants.SANDBOX_REFUND_URL;
            } else {
                urlSufix = PayConstants.REFUND_URL;
            }
        }
        //发起退款
        String response = null;
        try {
            response = new TencentPayRquest(tenpaySetting).requestWithCert(urlSufix, reqBody);
            logger.info("queryResult : {} ", response);
        } catch (Exception e) {
            logger.error("excepiton : {} ", BizExceptionEnum.EXTERNAL_TENPAY_REFUND_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_REFUND_FAIL);
        }

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("requestParams", reqBody);
        resultMap.put("responseParams", response);
        return resultMap;

    }

    public static ExternalRefundOrder checkRefundReponse(String queryResult, TenpaySetting tenpaySetting, ExternalRefundOrder refundOrder) {
        //解析返回数据
        Map<String, String> responseMap = PaySignUtils.processResponseXml(queryResult, tenpaySetting);

        String returnCode = responseMap.get(TenpayConstants.RETURN_CODE);
        if (TenpayConstants.RETURN_CODE_FAIL.equals(returnCode)) {
            String returnMsg = responseMap.get(TenpayConstants.RETURN_MSG);
            logger.error("exception : {}，code : {} ,msg :{} ", BizExceptionEnum.EXTERNAL_TENPAY_REFUND_CALL_FAIL.getErrMsg(), returnCode, returnMsg);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_REFUND_CALL_FAIL);
        }

        String resultCode = responseMap.get(TenpayConstants.RESULT_CODE);
        if (TenpayConstants.RESULT_CODE_FAIL.equals(resultCode)) {
            String errCode = responseMap.get(TenpayConstants.ERR_CODE);
            String errCodeDes = responseMap.get(TenpayConstants.ERR_CODE_DES);
            logger.error("exception :{} ,code :{} - msg :{} ", BizExceptionEnum.EXTERNAL_TENPAY_REFUND_SERVICE_FAIL.getErrMsg(), errCode, errCodeDes);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_REFUND_SERVICE_FAIL);
        }

        logger.debug("通信成功");

        String respRefundId = responseMap.get(TenpayConstants.REFUND_ID);
        refundOrder.setTransactionId(respRefundId);
        refundOrder.setState(ExternalRefundStateEnum.REFUNDING.getCode());

        return refundOrder;
    }

    public static Map<String, String> queryRefundInfo(ExternalRefundOrder refundOrder, TenpaySetting tenpaySetting, String channelTag) {

        String outRefundNo = refundOrder.getOrderId();
        String transactionId = refundOrder.getTransactionId();
        Map<String, String> requestMap = constructCommonParam(tenpaySetting);
        requestMap.put(TenpayConstants.REFUND_ID, transactionId);
        //requestMap.put(TenpayConstants.OUT_REFUND_NO, outRefundNo);
        requestMap.put(TenpayConstants.SIGN, PaySignUtils.generateSignature(requestMap, tenpaySetting.getApiSecret(), tenpaySetting.getSignType()));
        String reqBody = PayXmlutils.mapToXml(requestMap);
        logger.info("腾讯支付退款查询参数：{}", reqBody);

        String urlSufix = null;
        boolean weChat = getUrlQqOrWechat(channelTag);
        if (!weChat) {
            urlSufix = PayConstants.QQ_REFUND_QUERY_SUFIX;
        } else {
            //是否是沙箱支付
            String sandboxEnable = tenpaySetting.getSandboxEnable();
            if (Boolean.TRUE.equals(Boolean.valueOf(sandboxEnable))) {
                urlSufix = PayConstants.SANDBOX_REFUND_QUERY_URL;
            } else {
                urlSufix = PayConstants.REFUND_QUERY_URL;
            }
        }
        //发起退款查询
        String queryResult = null;
        try {
            queryResult = new TencentPayRquest(tenpaySetting).requestWithoutCert(urlSufix, reqBody);
            logger.info("queryResult : {} ", queryResult);

        } catch (Exception e) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_TENPAY_QUERY_REFUND_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_QUERY_REFUND_FAIL);
        }

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("requestParams", reqBody);
        resultMap.put("responseParams", queryResult);
        return resultMap;

    }

    public static ExternalRefundOrder checkQueryRefundInfo(String queryResult, TenpaySetting tenpaySetting, ExternalRefundOrder refundOrder) {
        //解析返回数据
        Map<String, String> responseMap = PaySignUtils.processResponseXml(queryResult, tenpaySetting);

        String returnCode = responseMap.get(TenpayConstants.RETURN_CODE);
        if (TenpayConstants.RETURN_CODE_FAIL.equals(returnCode)) {
            String returnMsg = responseMap.get(TenpayConstants.RETURN_MSG);
            logger.error("exception : {}，code : {} ,msg :{} ", BizExceptionEnum.EXTERNAL_TENPAY_QUERY_REFUND_CALL_FAIL.getErrMsg(), returnCode,
                    returnMsg);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_QUERY_REFUND_CALL_FAIL);
        }

        String resultCode = responseMap.get(TenpayConstants.RESULT_CODE);
        if (TenpayConstants.RESULT_CODE_FAIL.equals(resultCode)) {
            String errCode = responseMap.get(TenpayConstants.ERR_CODE);
            String errCodeDes = responseMap.get(TenpayConstants.ERR_CODE_DES);
            logger.error("exception :{} ,code :{} - msg :{} ", BizExceptionEnum.EXTERNAL_TENPAY_QUERY_REFUND_SERVICE_FAIL.getErrMsg(), errCode,
                    errCodeDes);
            //考虑退款订单不存在的情况，将退款订单信息认为是退款失败处理
            if (TenpayConstants.ERR_CODE_ORDERNOTEXIST.equals(errCode)) {
                refundOrder.setState(ExternalRefundStateEnum.FAILED.getCode());
                return refundOrder;
            }

            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_QUERY_REFUND_SERVICE_FAIL);
        }

        logger.debug("通信成功");
        String refundTime = responseMap.get(TenpayConstants.REFUND_SUCCESS_TIME_$N);
        refundOrder.setRefundTime(DateUtil.str2dateWithYMDHMS(refundTime));

        String refundStatus = responseMap.get(TenpayConstants.REFUND_STATUS);
        if (ExternalRefundStateEnum.REFUNDING.getCode().equals(refundStatus)) {
            refundOrder.setState(ExternalRefundStateEnum.REFUNDING.getCode());

        } else if (ExternalRefundStateEnum.SUCCESS.getCode().equals(refundStatus)) {
            refundOrder.setState(ExternalRefundStateEnum.SUCCESS.getCode());

        } else if ("CHANGE".equals(refundStatus)) {
            refundOrder.setState(ExternalRefundStateEnum.FAILED.getCode());

        } else if (ExternalRefundStateEnum.REFUNDING.getCode().equals(refundStatus)) {
            refundOrder.setState(ExternalRefundStateEnum.REFUNDING.getCode());
        }
        return refundOrder;

    }

    public static Map<String, String> closePayment(ExternalPaymentOrder paymentOrder, TenpaySetting tenpaySetting, String channelTag) {
        Map<String, String> requestMap = constructCommonParam(tenpaySetting);
        requestMap.put(TenpayConstants.OUT_TRADE_NO, paymentOrder.getOrderId());
        requestMap.put(TenpayConstants.SIGN, PaySignUtils.generateSignature(requestMap, tenpaySetting.getApiSecret(), tenpaySetting.getSignType()));
        String reqBody = PayXmlutils.mapToXml(requestMap);
        logger.info("腾讯关闭订单参数：{}", reqBody);

        String urlSufix = null;
        boolean weChat = getUrlQqOrWechat(channelTag);
        if (!weChat) {
            urlSufix = PayConstants.QQ_ORDER_CLOSE_SUFIX;
        } else {
            //是否是沙箱支付
            String sandboxEnable = tenpaySetting.getSandboxEnable();
            if (Boolean.TRUE.equals(Boolean.valueOf(sandboxEnable))) {
                urlSufix = PayConstants.SANDBOX_CLOSE_ORDER_URL;
            } else {
                urlSufix = PayConstants.CLOSE_ORDER_URL;
            }
        }
        //发起关闭订单
        String queryResult = null;
        try {
            queryResult = new TencentPayRquest(tenpaySetting).requestWithoutCert(urlSufix, reqBody);
            logger.info("queryResult : {} ", queryResult);
        } catch (Exception e) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_TENPAY_CLOSE_PAYMENT_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_CLOSE_PAYMENT_FAIL);
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("requestParams", reqBody);
        resultMap.put("responseParams", queryResult);
        return resultMap;

    }

    public static ExternalPaymentOrder checkClosePaymentResponse(String queryResult, TenpaySetting tenpaySetting, ExternalPaymentOrder paymentOrder) {
        //解析返回数据
        Map<String, String> responseMap = PaySignUtils.processResponseXml(queryResult, tenpaySetting);

        String returnCode = responseMap.get(TenpayConstants.RETURN_CODE);
        if (TenpayConstants.RETURN_CODE_FAIL.equals(returnCode)) {
            String returnMsg = responseMap.get(TenpayConstants.RETURN_MSG);
            logger.error("exception : {}，code : {} ,msg :{} ", BizExceptionEnum.EXTERNAL_TENPAY_QUERY_REFUND_CALL_FAIL.getErrMsg(), returnCode,
                    returnMsg);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_QUERY_REFUND_CALL_FAIL);
        }

        String resultCode = responseMap.get(TenpayConstants.RESULT_CODE);
        if (TenpayConstants.RESULT_CODE_FAIL.equals(resultCode)) {
            String errCode = responseMap.get(TenpayConstants.ERR_CODE);
            String errCodeDes = responseMap.get(TenpayConstants.ERR_CODE_DES);
            //如果已经关闭，还是返回成功
            if (TenpayConstants.ERR_CODE_ORDERCLOSED.equals(errCode)) {
                paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
                return paymentOrder;
            }

            logger.error("exception :{} ,code :{} - msg :{} ", BizExceptionEnum.EXTERNAL_TENPAY_QUERY_REFUND_SERVICE_FAIL.getErrMsg(), errCode,
                    errCodeDes);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_QUERY_REFUND_SERVICE_FAIL);
        }

        logger.debug("通信成功");
        paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
        return paymentOrder;

    }

}


