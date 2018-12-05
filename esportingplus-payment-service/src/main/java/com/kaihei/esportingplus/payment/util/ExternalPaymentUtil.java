package com.kaihei.esportingplus.payment.util;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.HttpUtils;
import com.kaihei.esportingplus.payment.api.enums.*;
import com.kaihei.esportingplus.payment.data.jpa.repository.AlipaySettingRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.ExternalPaymentOrderRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.PayChannelRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.TenpaySettingReppository;
import com.kaihei.esportingplus.payment.domain.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ExternalPaymentUtil
 * @Description TODO
 * @Author xusisi
 * @Date 2018/11/21 下午4:28
 */
@Component
public class ExternalPaymentUtil {

    private static Logger logger = LoggerFactory.getLogger(ExternalPaymentUtil.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private ExternalPaymentOrderRepository paymentOrderRepository;

    @Autowired
    private AlipaySettingRepository alipaySettingRepository;

    @Autowired
    private TenpaySettingReppository tenpaySettingReppository;

    @Autowired
    private PayChannelRepository payChannelRepository;

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplate;

    /**
     * 根据APPID，channelTag获取对应的支付配置信息
     *
     * @param : [appId, channelTag]
     * @Author : xusisi
     **/
    public AbstractEntity getPaySetting(String appId, String channelTag, String channelType) {
        PayChannel payChannel = payChannelRepository.findOneByTagAndAppSettingsAppId(channelTag, appId);
        if (payChannel == null) {
            throw new BusinessException(BizExceptionEnum.UNSUPPORT_PAY_CHANNEL);
        }
        if (!payChannel.getAppSettings().iterator().hasNext()) {
            throw new BusinessException(BizExceptionEnum.UNAUTH_PAY_CHANNEL);
        }
        if (AppSettingStatus.CLOSE == AppSettingStatus.valueOf(payChannel.getAppSettings().iterator().next().getState())) {
            throw new BusinessException(BizExceptionEnum.APP_PAY_CHANNEL_IS_CLOSED);
        }
        if (PayChannelStatus.DISABLE == PayChannelStatus.valueOf(payChannel.getState())) {
            throw new BusinessException(BizExceptionEnum.PAY_CHANNEL_IS_DISABLE);
        }

        PayChannelEnum channel = PayChannelEnum.lookup(channelTag);

        AbstractEntity setting = null;
        String payKey = String.format(RedisKey.PAY_SETTING_KEY_PREFIX, channel.getValue().toLowerCase());
        if (ExternalOrderPayChannelType.TENPAY.getValue().equals(channelType)) {
            setting = cacheManager.get(payKey, TenpaySetting.class);
            if (setting == null) {
                setting = tenpaySettingReppository.findOneByChannelId(payChannel.getId());
                if (setting == null) {
                    throw new BusinessException(BizExceptionEnum.PAY_CHANNEL_SETTING_NOT_CONFIG);
                }
                cacheManager.set(payKey, setting, -1);
            }
        } else if (ExternalOrderPayChannelType.ALIPAY.getValue().equals(channelType)) {
            setting = cacheManager.get(payKey, AlipaySetting.class);
            if (setting == null) {
                setting = alipaySettingRepository.findOneByChannelId(payChannel.getId());
                if (setting == null) {
                    throw new BusinessException(BizExceptionEnum.PAY_CHANNEL_SETTING_NOT_CONFIG);
                }
                cacheManager.set(payKey, setting, -1);
            }
        }

        logger.info("{}-支付渠道配置信息已缓存", channel.getName());

        if (setting == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_SETTING_CONFIG_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_SETTING_CONFIG_NOT_EXIST);
        }
        return setting;
    }

    /**
     * @Description:
     * @Param: [orderType, outTradeNo]
     * @Return com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder
     * @Author: xusisi
     */

    /**
     * 根据业务订单号，业务订单类型获取支付订单信息
     * 先从Redis中读取，如果不存在，去数据库读取
     *
     * @param : [orderType, outTradeNo]
     * @Author : xusisi
     **/
    public ExternalPaymentOrder getPaymentOrder(String orderType, String outTradeNo) throws BusinessException {
        logger.debug("入参 >> orderType : {} ,outTradeNo : {}", orderType, outTradeNo);

        String paymentKey = String.format(RedisKey.EXTERNAL_PAYMENT_ORDER_KEY, orderType, outTradeNo);
        ExternalPaymentOrder paymentOrder = cacheManager.get(paymentKey, ExternalPaymentOrder.class);
        if (paymentOrder == null) {
            paymentOrder = paymentOrderRepository.findOneByOrderTypeAndOutTradeNo(orderType, outTradeNo);
        }
        logger.debug("出参 >> paymentOrder : {}", paymentOrder);
        return paymentOrder;
    }

    /**
     * 在创建支付订单时，检查订单状态
     *
     * @param : [externalPaymentOrder]
     * @Author : xusisi
     **/
    public void checkPaymentStateWhenCreate(ExternalPaymentOrder paymentOrder) throws BusinessException {
        logger.debug("入参 >> ExternalPaymentOrder : {} ", paymentOrder);
        String payState = paymentOrder.getState();
        logger.debug("payState : {} ", payState);
        //判断定是否支付，是否关闭，是否撤销，是否执行完
        //是否已经付款
        if (ExternalPayStateEnum.SUCCESS.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_SUCCESS.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_SUCCESS);
        }

        //是否正在发起订单关闭
        if (ExternalPayStateEnum.CLOSING.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_CLOSING.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_CLOSING);
        }

        //是否已经关闭
        if (ExternalPayStateEnum.CLOSED.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_CLOSED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_CLOSED);
        }

        //是否发起订单撤销
        if (ExternalPayStateEnum.CANCELING.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_CANCELING.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_CANCELING);
        }

        //是否已经撤销
        if (ExternalPayStateEnum.CANCEL.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_CANCEL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_CANCEL);
        }

        //是否已经关闭无法退款
        if (ExternalPayStateEnum.CLOSED_NO_REFUND.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_CLOSED_NO_REFUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_CLOSED_NO_REFUND);
        }

    }

    /**
     * 在发起订单关闭时，校验订单状态
     *
     * @param : [paymentOrder]
     * @Author : xusisi
     **/
    public void checkPaymentStateWhenClose(ExternalPaymentOrder paymentOrder) throws BusinessException {
        logger.debug("入参 >> ExternalPaymentOrder : {}", paymentOrder);

        String payState = paymentOrder.getState();
        logger.debug("payState : {} ", payState);
        //判断定是否支付，是否关闭，是否撤销，是否执行完
        //是否已经付款
        if (ExternalPayStateEnum.SUCCESS.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_PAIED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_PAIED);
        }

        //是否正在发起订单关闭
        if (ExternalPayStateEnum.CLOSING.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_CLOSING.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_CLOSING);
        }

        //是否已经关闭
        if (ExternalPayStateEnum.CLOSED.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_CLOSED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_CLOSED);
        }

        //是否发起订单撤销
        if (ExternalPayStateEnum.CANCELING.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_CANCELING.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_CANCELING);
        }

        //是否已经撤销
        if (ExternalPayStateEnum.CANCEL.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_CANCEL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_CANCEL);
        }

        //是否已经关闭无法退款
        if (ExternalPayStateEnum.CLOSED_NO_REFUND.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_FINISHED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CLOSE_PAYORDER_FINISHED);
        }

    }

    /**
     * 在创建退款订单时，校验支付订单状态
     *
     * @param : [paymentOrder]
     * @Author : xusisi
     **/
    public void checkPaymentStateWhenRefund(ExternalPaymentOrder paymentOrder) throws BusinessException {

        logger.debug("入参 >> ExternalPaymentOrder : {} ", paymentOrder);

        String payState = paymentOrder.getState();
        logger.debug("payState : {}", payState);

        //判断定是否支付，是否关闭，是否发起关闭，是否撤销，是否发起撤销，是否执行完
        //是否待支付
        if (ExternalPayStateEnum.UNPAIED.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_REFUND_PAYORDER_UNPAIED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_REFUND_PAYORDER_UNPAIED);
        }

        //是否正在发起订单关闭
        if (ExternalPayStateEnum.CLOSING.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_PAY_ORDER_CLOSING.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_PAY_ORDER_CLOSING);
        }

        //是否已经关闭
        if (ExternalPayStateEnum.CLOSED.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_REFUND_PAYORDER_CLOSED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_REFUND_PAYORDER_CLOSED);
        }

        //是否发起订单撤销
        if (ExternalPayStateEnum.CANCELING.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_PAY_ORDER_CANCELING.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_PAY_ORDER_CANCELING);
        }

        //是否已经撤销
        if (ExternalPayStateEnum.CANCEL.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_REFUND_PAYORDER_CANCEL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_REFUND_PAYORDER_CANCEL);
        }

        //是否交易结束，不可退款
        if (ExternalPayStateEnum.CLOSED_NO_REFUND.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_REFUND_PAYORDER_FINISHED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_REFUND_PAYORDER_FINISHED);
        }

    }

    /**
     * 发起撤销时，校验订单信息 只有发生支付系统超时或者支付结果未知时可调用撤销
     * 先判断订单是否存在，是否正在关闭中，已经关闭，撤销中，已经撤销。
     *
     * @param : [paymentOrder]
     * @Author : xusisi
     **/
    public void checkPaymentStateWhenCancel(ExternalPaymentOrder paymentOrder) throws BusinessException {

        logger.debug("入参 >> ExternalPaymentOrder : {}", paymentOrder);
        String payState = paymentOrder.getState();
        logger.debug("payState : {} ", payState);

        //是否已经支付成功
        if (ExternalPayStateEnum.SUCCESS.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_SUCCESS.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_SUCCESS);
        }

        //是否正在发起订单关闭
        if (ExternalPayStateEnum.CLOSING.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_CLOSING.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_CLOSING);
        }

        //是否已经关闭
        if (ExternalPayStateEnum.CLOSED.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_CLOSED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_CLOSED);
        }

        //是否发起订单撤销
        if (ExternalPayStateEnum.CANCELING.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_CANCELING.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_CANCELING);
        }

        //是否已经撤销
        if (ExternalPayStateEnum.CANCEL.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_CANCEL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_CANCEL);
        }

        //是否交易结束，不可退款
        if (ExternalPayStateEnum.CLOSED_NO_REFUND.getCode().equals(payState)) {
            logger.error("exception : {} ", BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_FINISHED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ALIPAY_CANCEL_PAYORDER_FINISHED);
        }

    }

    /**
     * 更新MySQL中订单信息、更新Redis中订单信息
     *
     * @param : [paymentOrder]
     * @Author : xusisi
     **/
    public void refreshPaymentInfo(ExternalPaymentOrder paymentOrder) {
        String outTradeNo = paymentOrder.getOutTradeNo();
        String orderType = paymentOrder.getOrderType();

        String paymentKey = String.format(RedisKey.EXTERNAL_PAYMENT_ORDER_KEY, orderType, outTradeNo);
        cacheManager.del(paymentKey);
        paymentOrderRepository.save(paymentOrder);
        cacheManager.set(paymentKey, paymentOrder, RedisKey.SAVE_DATA_TIME);
    }

    /**
     * @Description: 将数据回调通知给业务方
     * @Param: [notifyUrl, packet]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    public Boolean callBack(String notifyUrl, ResponsePacket packet) {
        logger.debug("回调URL >> {} ", notifyUrl);
        logger.debug("提交给业务方的参数 : {} ", packet);
        Boolean flag = true;
        try {
            ResponsePacket response = restTemplate.postForObject(notifyUrl, HttpUtils.buildParam(packet), ResponsePacket.class);
            logger.debug("业务方返回 >> {} ", response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception : 回调通知业务方时发生异常 >> {} ", e);
            flag = false;
        }
        return flag;
    }

    /***
     * 根据业务订单号，业务订单类型获取对应的appId,channelTag
     * @param outTradeNo
     * @param orderType
     * @return
     */
    public Map<String, String> getPaySetting(String outTradeNo, String orderType) {

        ExternalPaymentOrder paymentOrder = this.getPaymentOrder(orderType, outTradeNo);
        if (paymentOrder == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND);
        }
        String appId = paymentOrder.getSourceAppId();
        String channelTag = paymentOrder.getChannelName();
        Map<String, String> payMap = new HashMap<>(2);
        payMap.put("appId", appId);
        payMap.put("channelTag", channelTag);
        return payMap;
    }

}
