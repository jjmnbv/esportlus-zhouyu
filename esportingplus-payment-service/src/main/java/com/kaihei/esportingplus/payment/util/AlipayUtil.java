package com.kaihei.esportingplus.payment.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.payment.api.enums.AlipayPayStateEnum;
import com.kaihei.esportingplus.payment.api.enums.ExternalPayStateEnum;
import com.kaihei.esportingplus.payment.api.enums.ExternalRefundStateEnum;
import com.kaihei.esportingplus.payment.data.jpa.repository.PayChannelRepository;
import com.kaihei.esportingplus.payment.domain.entity.AlipaySetting;
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName AlipayUtil
 * @Description 支付宝工具类
 * @Author xusisi
 * @Date 2018/11/22 上午11:25
 */
public class AlipayUtil {

    private static Logger logger = LoggerFactory.getLogger(AlipayUtil.class);

    @Autowired
    private PayChannelRepository payChannelRepository;

    /**
     * 根据AlipaySetting获取Client
     *
     * @param : [alipaySetting]
     * @Author : xusisi
     **/
    public static AlipayClient loadAlipayClient(AlipaySetting alipaySetting) {

        //判断使用的是沙盒环境还是正式环境
        String sandboxEnable = alipaySetting.getSandboxEnable();
        String requestUrl = "";
        if (Boolean.TRUE.equals(Boolean.getBoolean(sandboxEnable))) {
            requestUrl = alipaySetting.getSandboxUrl();
        } else {
            requestUrl = alipaySetting.getRequestUrl();
        }
        AlipayClient alipayClient = new DefaultAlipayClient(requestUrl, alipaySetting.getAppId(),
                alipaySetting.getRsaPrivateKey(), alipaySetting.getFormat(), alipaySetting.getCharset(),
                alipaySetting.getAlipayPublicKey(),
                alipaySetting.getSignType());
        return alipayClient;
    }

    /**
     * 校验返回结果信息
     *
     * @param : [alipayResponse]
     * @Author : xusisi
     **/
    public static void checkResponse(AlipayResponse alipayResponse) {

        if (alipayResponse == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL);
        }

        if (!alipayResponse.isSuccess()) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_ALIPAY_CALL_FAIL);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_CALL_FAIL);
        }

    }

    /**
     * 构建支付订单加密信息
     *
     * @param : [paymentOrder, alipaySetting]
     * @Author : xusisi
     **/
    public static Map<String, Object> createPaymentInfo(ExternalPaymentOrder paymentOrder, AlipaySetting alipaySetting) {
        // 加载alipayClient信息
        AlipayClient alipayClient = loadAlipayClient(alipaySetting);

        //实例化具体的API对应的request类，类名称和接口名称对应，当前调用接口名称，alipay.trade.app.pay
        AlipayTradeAppPayRequest alipayTradeAppPayRequest = new AlipayTradeAppPayRequest();

        //sdk已经封装了公共参数，这里只需要传业务参数。以下方法为sdk的model入参方式（model和biz_content同时存在的时候取biz_content）
        AlipayTradeAppPayModel alipayTradeAppPayModel = new AlipayTradeAppPayModel();
        //商户网站唯一订单号
        //本地支付订单的orderId作为支付宝支付时的业务订单ID
        alipayTradeAppPayModel.setOutTradeNo(paymentOrder.getOrderId());
        //订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        String totalAmount = String.valueOf(new BigDecimal(paymentOrder.getTotalFee()).divide(new BigDecimal(100)));
        alipayTradeAppPayModel.setTotalAmount(totalAmount);
        alipayTradeAppPayModel.setProductCode(alipaySetting.getProductCode());
        alipayTradeAppPayModel.setTimeoutExpress(alipaySetting.getTimeoutExpress());
        alipayTradeAppPayModel.setSubject("支付宝支付-购买");

        logger.debug("model : {} ", FastJsonUtils.toJson(alipayTradeAppPayModel));

        alipayTradeAppPayRequest.setBizModel(alipayTradeAppPayModel);
        alipayTradeAppPayRequest.setNotifyUrl(alipaySetting.getNotifyUrl());

        AlipayTradeAppPayResponse alipayTradeAppPayResponse = null;
        try {

            alipayTradeAppPayResponse = alipayClient.sdkExecute(alipayTradeAppPayRequest);

            logger.debug("response : {} ", FastJsonUtils.toJson(alipayTradeAppPayResponse));
        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.error("exception :{} ", BizExceptionEnum.ALIPAY_PAY_ORDER_CREATE_EXCEPTION.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_PAY_ORDER_CREATE_EXCEPTION);
        }

        //将请求参数、返回结果封装成MAP返回
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("requestParams", alipayTradeAppPayRequest);
        resultMap.put("responseParams", alipayTradeAppPayResponse);
        return resultMap;
    }

    /**
     * 向支付宝发起查询支付订单信息请求
     *
     * @param : [paymentOrder, alipaySetting]
     * @Author : xusisi
     **/
    public static Map<String, Object> searchPaymentInfo(ExternalPaymentOrder paymentOrder, AlipaySetting alipaySetting) {

        //加载AlipayClient信息
        AlipayClient alipayClient = loadAlipayClient(alipaySetting);

        //本地支付订单的orderId作为支付宝支付时的业务订单ID
        AlipayTradeQueryModel alipayTradeQueryModel = new AlipayTradeQueryModel();
        String orderId = paymentOrder.getOrderId();
        alipayTradeQueryModel.setTradeNo(orderId);

        logger.debug("model :{}", FastJsonUtils.toJson(alipayTradeQueryModel));

        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
        alipayTradeQueryRequest.setBizModel(alipayTradeQueryModel);

        AlipayTradeQueryResponse alipayTradeQueryResponse = null;

        try {
            alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);

            logger.debug("response : {}", FastJsonUtils.toJson(alipayTradeQueryResponse));

        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.error("exception : {} ", e.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_SEARCH_PAYMENT_CALL_FAIL);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("requestParams", alipayTradeQueryRequest);
        resultMap.put("responseParams", alipayTradeQueryResponse);
        return resultMap;
    }

    /**
     * 解析查询订单返回的结果
     *
     * @param : [alipayResponse]
     * @Author : xusisi
     **/
    public static ExternalPaymentOrder checkSearchPaymentResponse(ExternalPaymentOrder paymentOrder,
                                                                  AlipayTradeQueryResponse alipayTradeQueryResponse) {

        if (alipayTradeQueryResponse == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL);
        }

        if (!alipayTradeQueryResponse.isSuccess()) {
            String subCode = alipayTradeQueryResponse.getSubCode();
            //订单不存在支付宝，则直接关闭订单信息
            if (AlipayConstants.ACQ_TRADE_NOT_EXIST.equals(subCode)) {
                paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
                return paymentOrder;
            }
            String subMsg = alipayTradeQueryResponse.getSubMsg();
            logger.error("exception : {} - {} ", subCode, subMsg);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_SEARCH_PAYMENT_CALL_FAIL);
        }
        String orderId = paymentOrder.getOrderId();
        Integer totalAmount = paymentOrder.getTotalFee();

        logger.debug("接口调用成功");
        String respTradeNo = alipayTradeQueryResponse.getTradeNo();
        String respOutTradeNo = alipayTradeQueryResponse.getOutTradeNo();
        String respTradeState = alipayTradeQueryResponse.getTradeStatus();
        //交易订单金额单位：元，两位小数
        String respAmountStr = alipayTradeQueryResponse.getTotalAmount();
        Integer respAmount = new BigDecimal(respAmountStr).multiply(new BigDecimal(100)).intValue();

        //交易订单不一致
        if (!respOutTradeNo.equals(orderId)) {
            logger.warn("orderId {} 与支付宝查询返回的不匹配 ", orderId);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_SEARCH_PAYMENT_ORDER_NOT_MATCH);
        }

        //交易订单金额不一致
        if (!totalAmount.equals(respAmount)) {
            logger.error("orderId : {} 与支付宝返回的订单交易金额不一致", orderId);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_SEARCH_PAYMENT_ORDER_NOT_MATCH);
        }

        paymentOrder.setTransactionId(respTradeNo);
        //更新支付订单状态
        if (AlipayConstants.WAIT_BUYER_PAY.equals(respTradeState)) {
            paymentOrder.setState(ExternalPayStateEnum.UNPAIED.getCode());

        } else if (AlipayConstants.TRADE_SUCCESS.equals(respTradeState)) {
            paymentOrder.setState(ExternalPayStateEnum.SUCCESS.getCode());

        } else if (AlipayConstants.TRADE_CLOSED.equals(respTradeState)) {
            paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());

        } else if (AlipayConstants.TRADE_FINISHED.equals(respTradeState)) {
            paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
        }

        return paymentOrder;

    }

    /**
     * 发起退款请求
     *
     * @param : [refundOrder, alipaySetting]
     * @Author : xusisi
     **/
    public static Map<String, Object> createRefund(ExternalRefundOrder refundOrder, AlipaySetting alipaySetting) {
        //加载AlipayClient
        AlipayClient alipayClient = loadAlipayClient(alipaySetting);

        AlipayTradeRefundRequest alipayTradeRefundRequest = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel alipayTradeRefundModel = new AlipayTradeRefundModel();
        Integer refundAmount = refundOrder.getTotalFee();

        //需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
        String totalAmount = String.valueOf(new BigDecimal(refundAmount).divide(new BigDecimal(100)));
        alipayTradeRefundModel.setRefundAmount(totalAmount);
        //订单支付时传入的商户订单号
        String payOrderId = refundOrder.getPayOrderId();
        alipayTradeRefundModel.setOutTradeNo(payOrderId);
        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
        String orderId = refundOrder.getOrderId();
        alipayTradeRefundModel.setOutRequestNo(orderId);
        logger.debug("model : {} ", FastJsonUtils.toJson(alipayTradeRefundModel));

        alipayTradeRefundRequest.setBizModel(alipayTradeRefundModel);
        AlipayTradeRefundResponse alipayTradeRefundResponse = null;

        try {
            alipayTradeRefundResponse = alipayClient.execute(alipayTradeRefundRequest);
            logger.debug("response : {} ", FastJsonUtils.toJson(alipayTradeRefundResponse));

        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.error("exception : {} ", e.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_REFUND_CALL_FAIL);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("requestParams", alipayTradeRefundRequest);
        resultMap.put("responseParams", alipayTradeRefundResponse);
        return resultMap;

    }

    public static Map<String, Object> checkRefundResponse(ExternalPaymentOrder paymentOrder, ExternalRefundOrder refundOrder,
                                                          AlipayTradeRefundResponse alipayTradeRefundResponse) {

        if (alipayTradeRefundResponse == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL);
        }
        Map<String, Object> map = new HashMap<>();

        if (!alipayTradeRefundResponse.isSuccess()) {
            logger.error("退款业务失败");
            String subCode = alipayTradeRefundResponse.getSubCode();
            String subMsg = alipayTradeRefundResponse.getSubMsg();
            logger.error("exception : {} - {} ", subCode, subMsg);

            //退款业务失败，考虑支付是否是支付订单引起的
            if (AlipayConstants.ACQ_TRADE_NOT_EXIST.equals(subCode)) {
                logger.info("退款失败，原因是支付订单不存在于支付宝系统，无法在进行退款");
                refundOrder.setState(ExternalRefundStateEnum.FAILED.getCode());
                //同时对应的支付订单应该处理为支付失败。
                paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
            } else if (AlipayConstants.ACQ_TRADE_HAS_FINISHED.equals(subCode)) {
                //支付订单已经完结，无法再退款，此时支付订单状态需要改变，退款订单状态为失败
                logger.info("退款失败，原因是，支付订单状态为已完结，无法在退款。");
                refundOrder.setState(ExternalRefundStateEnum.FAILED.getCode());
                paymentOrder.setState(ExternalPayStateEnum.CLOSED_NO_REFUND.getCode());

            } else {
                refundOrder.setState(ExternalRefundStateEnum.FAILED.getCode());
            }

            map.put("paymentOrder", paymentOrder);
            map.put("refundOrder", refundOrder);
            return map;

        }

        logger.debug("退款业务成功");

        //根据同步返回信息判断订单是否退款成功
        //其中"code":"10000","msg":"Success" 只是表示退款接口调用成功，
        // 实际是否退款成功需要依据同步返回参数中的fund_change（本次退款是否发生了资金变化）参数来判断
        // 如果fund_change返回的参数是Y那么就可以确定为退款成功
        String refundChange = alipayTradeRefundResponse.getFundChange();

        if (!"Y".equals(refundChange)) {
            // 退款金额无变化，判定为退款失败
            refundOrder.setState(ExternalRefundStateEnum.FAILED.getCode());
        } else {
            // 支付宝交易号
            String transactionId = alipayTradeRefundResponse.getTradeNo();
            // 退款支付时间
            Date refunPayTime = alipayTradeRefundResponse.getGmtRefundPay();
            // 退款成功
            refundOrder.setTransactionId(transactionId);
            refundOrder.setRefundTime(refunPayTime);
            refundOrder.setState(ExternalRefundStateEnum.SUCCESS.getCode());

        }

        map.put("paymentOrder", paymentOrder);
        map.put("refundOrder", refundOrder);
        return map;

    }

    /**
     * 发起查询退款请求
     *
     * @param : [refundOrder, alipaySetting]
     * @Author : xusisi
     **/
    public static Map<String, Object> searchRefundInfo(ExternalRefundOrder refundOrder, AlipaySetting alipaySetting) {

        AlipayClient alipayClient = loadAlipayClient(alipaySetting);

        AlipayTradeFastpayRefundQueryModel alipayTradeFastpayRefundQueryModel = new AlipayTradeFastpayRefundQueryModel();
        //请求退款接口时，传入的退款请求号
        String orderId = refundOrder.getOrderId();
        alipayTradeFastpayRefundQueryModel.setOutRequestNo(orderId);
        //订单支付时传入的商户订单号,和支付宝交易号不能同时为空。
        String payOrderId = refundOrder.getPayOrderId();
        alipayTradeFastpayRefundQueryModel.setOutTradeNo(payOrderId);

        AlipayTradeFastpayRefundQueryRequest alipayTradeFastpayRefundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();
        alipayTradeFastpayRefundQueryRequest.setBizModel(alipayTradeFastpayRefundQueryModel);

        logger.debug("model : {} ", FastJsonUtils.toJson(alipayTradeFastpayRefundQueryModel));

        AlipayTradeFastpayRefundQueryResponse alipayTradeFastpayRefundQueryResponse = null;
        try {

            alipayTradeFastpayRefundQueryResponse = alipayClient.execute(alipayTradeFastpayRefundQueryRequest);

            logger.debug("response : {} ", FastJsonUtils.toJson(alipayTradeFastpayRefundQueryResponse));
        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.error("exception : {} ", e.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_REFUND_SEARCH_EXCEPTION);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("requestParams", alipayTradeFastpayRefundQueryRequest);
        resultMap.put("responseParams", alipayTradeFastpayRefundQueryResponse);
        return resultMap;
    }

    public static Map<String, Object> checkSearchRefundResponse(ExternalPaymentOrder paymentOrder, ExternalRefundOrder refundOrder,
                                                                AlipayTradeFastpayRefundQueryResponse alipayTradeFastpayRefundQueryResponse) {

        if (alipayTradeFastpayRefundQueryResponse == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL);
        }

        if (!alipayTradeFastpayRefundQueryResponse.isSuccess()) {
            logger.info("查询业务失败");
            String subCode = alipayTradeFastpayRefundQueryResponse.getSubCode();
            String subMsg = alipayTradeFastpayRefundQueryResponse.getSubMsg();
            logger.error("exception : {} - {} ", subCode, subMsg);

            //调用失败，需要考虑订单不存在的情况，说明退款失败
            if ("TRADE_NOT_EXIST".equals(subCode)) {
                paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
            }
            refundOrder.setState(ExternalRefundStateEnum.FAILED.getCode());
            Map<String, Object> map = new HashMap<>();
            map.put("paymentOrder", paymentOrder);
            map.put("refundOrder", refundOrder);
            return map;
        }

        logger.info("业务调用成功");
        //如果该接口返回了查询数据，则代表退款成功，如果没有查询到则代表未退款成功，可以调用退款接口进行重试。
        //这里的返回查询数据指的就是在这个接口调用成功后是否有返回如示例中的out_request_no，out_trade_no，refund_amount，trade_no 等参数
        //如果有返回则可判定为退款成功

        //本笔退款对应的退款请求号
        String respOutRequestNo = alipayTradeFastpayRefundQueryResponse.getOutRequestNo();
        //创建交易传入的商户订单号
        String respOutTradeNo = alipayTradeFastpayRefundQueryResponse.getOutTradeNo();
        //本次退款金额
        String respRefundAmount = alipayTradeFastpayRefundQueryResponse.getRefundAmount();
        //支付宝交易号
        String respTradeNo = alipayTradeFastpayRefundQueryResponse.getTradeNo();
        //信息校验
        if (StringUtils.isEmpty(respOutRequestNo) || StringUtils.isEmpty(respOutTradeNo) || StringUtils
                .isEmpty(respRefundAmount) || StringUtils.isEmpty(respTradeNo)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_REFUND_SEARCH_RETURN_PARAMS_EXCEPTION.getErrMsg());
            //如果查不到对应的退款信息，表示退款失败，更新订单状态
            //更新退款订单状态
            refundOrder.setState(ExternalRefundStateEnum.FAILED.getCode());
        } else {
            if (respOutRequestNo.equals(refundOrder.getOrderId())) {
                logger.error("与商户提交的退款订单号不一致");
                logger.error("exception : {} ", BizExceptionEnum.ALIPAY_REFUND_SEARCH_REQUEST_NOT_EQUAL.getErrMsg());
                throw new BusinessException(BizExceptionEnum.ALIPAY_REFUND_SEARCH_REQUEST_NOT_EQUAL);
            }

            if (!respOutTradeNo.equals(refundOrder.getPayOrderId())) {
                logger.error("与商户提交的支付订单号不一致！");
                logger.error("exception : {} ", BizExceptionEnum.ALIPAY_REFUND_SEARCH_PAY_TRADE_NO_NOT_EQUAL.getErrMsg());
                throw new BusinessException(BizExceptionEnum.ALIPAY_REFUND_SEARCH_PAY_TRADE_NO_NOT_EQUAL);
            }

            if (!respTradeNo.equals(refundOrder.getTransactionId())) {
                logger.error("exception : {}", BizExceptionEnum.ALIPAY_REFUND_SEARCH_TRANSACTION_ID_NO_EQUAL.getErrMsg());
                throw new BusinessException(BizExceptionEnum.ALIPAY_REFUND_SEARCH_TRANSACTION_ID_NO_EQUAL);
            }

            BigDecimal respRefundAmount_Yuan = new BigDecimal(respRefundAmount);
            BigDecimal refundAmount_Yuan = new BigDecimal(refundOrder.getTotalFee()).divide(new BigDecimal(100));
            if (refundAmount_Yuan.compareTo(respRefundAmount_Yuan) != 0) {
                logger.error("与商户提交的退款金额不一致！");
                logger.error("exception : {} ", BizExceptionEnum.ALIPAY_REFUND_SEARCH_REFUND_MONEY_NOT_EQUAL.getErrMsg());
                throw new BusinessException(BizExceptionEnum.ALIPAY_REFUND_SEARCH_REFUND_MONEY_NOT_EQUAL);
            }

            //如果能查到对应的退款信息，表示退款成功，可以更新订单状态
            //更新退款订单状态
            refundOrder.setState(ExternalRefundStateEnum.SUCCESS.getCode());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("paymentOrder", paymentOrder);
        map.put("refundOrder", refundOrder);
        return map;
    }

    /**
     * 关闭支付订单
     *
     * @param : [args]
     * @Author : xusisi
     **/
    public static Map<String, Object> closePayment(ExternalPaymentOrder paymentOrder, AlipaySetting alipaySetting) {
        AlipayClient alipayClient = loadAlipayClient(alipaySetting);

        AlipayTradeCloseModel alipayTradeCloseModel = new AlipayTradeCloseModel();
        //trade_no该交易在支付宝系统中的交易流水号。
        //out_trade_no 订单支付时传入的商户订单号,和支付宝交易号不能同时为空。
        alipayTradeCloseModel.setOutTradeNo(paymentOrder.getOrderId());
        AlipayTradeCloseRequest alipayTradeCloseRequest = new AlipayTradeCloseRequest();
        alipayTradeCloseRequest.setBizModel(alipayTradeCloseModel);
        AlipayTradeCloseResponse alipayTradeCloseResponse = null;
        logger.debug("model : {} ", FastJsonUtils.toJson(alipayTradeCloseModel));

        try {
            alipayTradeCloseResponse = alipayClient.execute(alipayTradeCloseRequest);
            logger.debug("response : {} ", alipayTradeCloseResponse);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.error("exception : {} ", e.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CLOSE_ORDER_EXCEPTION);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("requestParams", alipayTradeCloseRequest);
        resultMap.put("responseParams", alipayTradeCloseResponse);
        return resultMap;
    }

    /**
     * 发起支付订单关闭
     *
     * @param : [paymentOrder, alipayTradeCloseResponse]
     * @Author : xusisi
     **/
    public static ExternalPaymentOrder checkClosePaymentResponse(ExternalPaymentOrder paymentOrder,
                                                                 AlipayTradeCloseResponse alipayTradeCloseResponse) {
        if (alipayTradeCloseResponse == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL);
        }

        if (!alipayTradeCloseResponse.isSuccess()) {
            logger.error("关闭支付订单业务失败");
            String subCode = alipayTradeCloseResponse.getSubCode();
            String subMsg = alipayTradeCloseResponse.getSubMsg();
            logger.error("exception : {} - {} ", subCode, subMsg);
            //如果订单不存在支付宝，则可以直接关闭本地的支付订单，不需要做订单信息校验

            if (AlipayConstants.ACQ_TRADE_NOT_EXIST.equals(subCode)) {
                logger.debug("订单不存在与支付宝系统中，故可以直接关闭");
                paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
                return paymentOrder;
            }

            throw new BusinessException(BizExceptionEnum.ALIPAY_CLOSE_ORDER_EXCEPTION);
        }

        logger.debug("关闭支付订单成功");
        //支付订单状态更新为关闭中
        //创建交易传入的商户订单号
        String respOutTradNo = alipayTradeCloseResponse.getOutTradeNo();
        //支付宝交易号
        String respTransactionId = alipayTradeCloseResponse.getTradeNo();
        String orderId = paymentOrder.getOrderId();
        if (!respOutTradNo.equals(orderId)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CLOSE_RETURN_OUT_TRADE_NO_NOT_EQUAL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CLOSE_RETURN_OUT_TRADE_NO_NOT_EQUAL);
        }

        if (respTransactionId.equals(paymentOrder.getTransactionId())) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CLOSE_RETURN_TRADE_NO_NOT_EQUAL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CLOSE_RETURN_TRADE_NO_NOT_EQUAL);
        }
        //关闭订单创建成功，具体结果以异步回调返回为准
        paymentOrder.setState(ExternalPayStateEnum.CLOSING.getCode());

        return paymentOrder;
    }

    public static Map<String, Object> cancelPayment(ExternalPaymentOrder paymentOrder, AlipaySetting alipaySetting) {
        AlipayClient alipayClient = loadAlipayClient(alipaySetting);
        String payOrderId = paymentOrder.getOrderId();
        //调用支付宝撤销接口
        AlipayTradeCancelModel alipayTradeCancelModel = new AlipayTradeCancelModel();
        alipayTradeCancelModel.setOutTradeNo(payOrderId);
        AlipayTradeCancelRequest alipayTradeCancelRequest = new AlipayTradeCancelRequest();
        alipayTradeCancelRequest.setBizModel(alipayTradeCancelModel);
        logger.debug("model : {} ", FastJsonUtils.toJson(alipayTradeCancelModel));
        AlipayTradeCancelResponse alipayTradeCancelResponse = null;
        try {
            alipayTradeCancelResponse = alipayClient.sdkExecute(alipayTradeCancelRequest);
            logger.debug("response : {} ", alipayTradeCancelResponse);

        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.error("exception : {} ", e.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CANCEL_CREATE_EXCEPTION);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("requestParams", alipayTradeCancelRequest);
        resultMap.put("responseParams", alipayTradeCancelResponse);

        return resultMap;

    }

    /**
     * 校验撤销操作返回的结果
     *
     * @param : [paymentOrder, alipayTradeCancelResponse]
     * @Author : xusisi
     **/
    public static ExternalPaymentOrder checkCancelPaymentResponse(ExternalPaymentOrder paymentOrder,
                                                                  AlipayTradeCancelResponse alipayTradeCancelResponse) {
        if (alipayTradeCancelResponse == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_RESPONSE_IS_NULL);
        }
        if (!alipayTradeCancelResponse.isSuccess()) {
            logger.error("订单撤销失败");
            String subCode = alipayTradeCancelResponse.getSubCode();
            String subMsg = alipayTradeCancelResponse.getSubMsg();
            logger.error("exception : {} - {} ", subCode, subMsg);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_CANCEL_CALL_FAIL);
        }
        logger.info("订单撤销操作调用成功");

        //支付宝交易号
        String respTradeNo = alipayTradeCancelResponse.getTradeNo();
        //商户订单号
        String respOutTradeNo = alipayTradeCancelResponse.getOutTradeNo();
        //是否需要重试
        String respRetryFlag = alipayTradeCancelResponse.getRetryFlag();

        if (StringUtils.isEmpty(respOutTradeNo) && StringUtils.isEmpty(respRetryFlag) && StringUtils.isEmpty(respTradeNo)) {
            logger.debug("订单信息不存在与支付宝订单系统中，可以直接关闭订单信息");
            paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
            return paymentOrder;
        }

        //本次撤销触发的交易动作 close：关闭交易，无退款;refund：产生了退款
        String respAction = alipayTradeCancelResponse.getAction();
        if (AlipayConstants.RESP_ACTION_CLOSE.equals(respAction)) {
            //更新支付订单状态为关闭
            paymentOrder.setState(ExternalPayStateEnum.CANCEL.getCode());
        } else if (AlipayConstants.RESP_ACTION_REFUND.equals(respAction)) {
            //更新订单状态为退款:因为用户支付金额没有进入我们系统，所以支付宝直接退款，我们本地系统不需要处理退款信息，对应支付订单状态更新为全额退款
            paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());
        }

        return paymentOrder;
    }

    public static ExternalPaymentOrder checkAlipayNotifyInfo(Map<String, String> params, AlipaySetting alipaySetting) throws AlipayApiException {
        Boolean signVerfied = false;
        logger.debug("param : {}", params);
        try {
            signVerfied = AlipaySignature.rsaCheckV1(params, alipaySetting.getAlipayPublicKey(), alipaySetting.getCharset(),
                    alipaySetting.getSignType());
        } catch (AlipayApiException e) {
            logger.error("exception :{} ", e.getErrMsg());
            e.printStackTrace();
            throw e;
        }

        if (!signVerfied) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_ALIPAY_NOTIFY_VALIDATE_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_ALIPAY_NOTIFY_VALIDATE_FAIL);
        }

        //验签成功返回success给支付宝，同时将数据保存至mq
        logger.debug("验签成功 。。。 ");

        //校验APPID
        String respAppId = params.get("app_id");
        if (!respAppId.equals(alipaySetting.getAppId())) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_PAY_ORDER_APPID_NOT_EQUAL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_PAY_ORDER_APPID_NOT_EQUAL);
        }

        //商户提交的订单号
        String respOrderId = params.get(AlipayConstants.RETURN_PARAM_OUT_TRADE_NO);
        //交易金额
        String respAmount = params.get(AlipayConstants.RETURN_PARAM_TOTAL_AMOUNT);
        //订单返回状态
        String respTradeStatus = params.get(AlipayConstants.RETURN_PARAM_TRADE_STATUS);

        ExternalPaymentOrder paymentOrder = new ExternalPaymentOrder();
        paymentOrder.setOrderId(respOrderId);
        BigDecimal tradeAmount = new BigDecimal(respAmount).multiply(new BigDecimal(100));
        paymentOrder.setTotalFee(tradeAmount.intValue());
        paymentOrder.setState(respTradeStatus);

        return paymentOrder;

    }

    /**
     * 处理支付宝回调信息
     *
     * @param : [args]
     * @Author : xusisi
     **/
    public static ExternalPaymentOrder handleAlipayCallBackInfo(Map<String, String> params, ExternalPaymentOrder paymentOrder) throws ParseException {

        //交易目前所处的状态
        String respTradeStatus = params.get(AlipayConstants.RETURN_PARAM_TRADE_STATUS);
        //支付宝交易凭证号
        String respTradeNo = params.get(AlipayConstants.RETURN_PARAM_TRADE_NO);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String state = paymentOrder.getState();
        //交易成功或退款
        if (AlipayPayStateEnum.TRADE_SUCCESS.getCode().equals(respTradeStatus) || AlipayPayStateEnum.TRADE_FINISHED.getCode().equals(respTradeStatus)) {
            //支付宝返回的交易订单号
            paymentOrder.setTransactionId(respTradeNo);
            //原订单待支付，通知的状态为支付成功，则表示该订单已经支付成功了~
            if (ExternalPayStateEnum.UNPAIED.getCode().equals(state) && AlipayPayStateEnum.TRADE_SUCCESS.getCode().equals(respTradeStatus)) {
                paymentOrder.setState(ExternalPayStateEnum.SUCCESS.getCode());
                //交易支付时间
                String gmtPayment = params.get(AlipayConstants.RETURN_PARAM_GMT_PAYMENT);
                Date paidTime = dateFormat.parse(gmtPayment);
                paymentOrder.setPaiedTime(paidTime);
            } else if (ExternalPayStateEnum.SUCCESS.getCode().equals(state) && AlipayPayStateEnum.TRADE_FINISHED.getCode().equals(respTradeStatus)) {
                //订单已经支付成功的情况下，收到支付宝通知订单状态为交易结束，不可退款，则订单状态更新为已经结束不可退款
                paymentOrder.setState(ExternalPayStateEnum.CLOSED_NO_REFUND.getCode());
            }
            //其他情况为支付成功后的部分退款，所以不更新订单状态信息

        } else if (AlipayPayStateEnum.TRADE_CLOSED.getCode().equals(respTradeStatus)) {

            if (ExternalPayStateEnum.UNPAIED.getCode().equals(state)) {
                //订单待支付，可以关闭订单
                paymentOrder.setState(ExternalPayStateEnum.CLOSED.getCode());

            } else if (ExternalPayStateEnum.SUCCESS.getCode().equals(state)) {
                //支付成功的订单，因为金额已经退完所以可以关闭订单
                paymentOrder.setState(ExternalPayStateEnum.CLOSED_NO_REFUND.getCode());
            }

        }
        return paymentOrder;

    }

    public static Map<String, String> handleAlipayRequestParams(HttpServletRequest request) throws Exception {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        Iterator<String> iterator = requestParams.keySet().iterator();

        while (iterator.hasNext()) {
            String valueStr = "";
            String name = iterator.next();
            String[] values = requestParams.get(name);
            logger.debug("value  = {} >> values = {}", name, values);

            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }

            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }
}
