package com.kaihei.esportingplus.payment.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.payment.annotation.CheckAndAutowireChannelSetting;
import com.kaihei.esportingplus.payment.api.dto.CloudWithdrawOrderDto;
import com.kaihei.esportingplus.payment.api.enums.*;
import com.kaihei.esportingplus.payment.api.params.CloseOrCancelPayOrderParams;
import com.kaihei.esportingplus.payment.api.params.CloudAccountOrderParams;
import com.kaihei.esportingplus.payment.api.params.PayOrderParams;
import com.kaihei.esportingplus.payment.api.params.RefundOrderParams;
import com.kaihei.esportingplus.payment.api.vo.*;
import com.kaihei.esportingplus.payment.config.CloudAccountConfig;
import com.kaihei.esportingplus.payment.data.jpa.repository.CapaySettingRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.ExternalWithdrawOrderRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.PayChannelRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.WithdrawAuditRecordRepository;
import com.kaihei.esportingplus.payment.data.mongodb.repository.WithdrawVoucherRepository;
import com.kaihei.esportingplus.payment.domain.document.WithdrawVoucher;
import com.kaihei.esportingplus.payment.domain.entity.*;
import com.kaihei.esportingplus.payment.mq.producer.CloudAccountCreateProducer;
import com.kaihei.esportingplus.payment.mq.producer.CloudAccountNotifyProducer;
import com.kaihei.esportingplus.payment.service.CloudAccountService;
import com.kaihei.esportingplus.payment.service.ExternalTradeBillService;
import com.kaihei.esportingplus.payment.service.PayService;
import com.kaihei.esportingplus.payment.service.WithdrawService;
import com.maihaoche.starter.mq.base.MessageBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @program: esportingplus
 * @description: 云账户相关service
 * @author: xusisi, chenzhenjun
 * @create: 2018-10-24 18:04
 **/
@Service
public class CloudAccountServiceImpl implements CloudAccountService, PayService {

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    private static final Logger logger = LoggerFactory.getLogger(CloudAccountServiceImpl.class);

    private static final String HUNDRED = "100";

    /**
     * 查询商户余额Url
     */
    private static final String QUERY_ACCOUNT_URL = "https://api-jiesuan.yunzhanghu.com/api/payment/v1/query-accounts/";

    /**
     * 查询订单状态Url
     */
    private static final String QUERY_ORDER_URL = "https://api-jiesuan.yunzhanghu.com/api/payment/v1/query-realtime-order";

    @Autowired
    private ExternalWithdrawOrderRepository orderRepository;

    @Autowired
    private CapaySettingRepository capaySettingRepository;

    @Autowired
    private CloudAccountCreateProducer createProducer;

    @Autowired
    private CloudAccountNotifyProducer notifyProducer;

    @Autowired
    private PayChannelRepository payChannelRepository;

    @Autowired
    private WithdrawVoucherRepository withdrawVoucherRepository;

    @Autowired
    private ExternalTradeBillService tradeBillService;

    @Autowired
    private WithdrawService withdrawService;

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private WithdrawAuditRecordRepository auditRecordRepository;

    @Override
    public Map<String, String> createPaymentOrder(PayOrderParams payOrderParams, String appId, String channelTag, AbstractEntity paySetting,
                                                  String ip, String area) throws BusinessException {
        return null;
    }

    @Override
    public ExternalPaymentOrder searchPaymentOrderInfo(String outTradeNo, String appId, String channelTag, AbstractEntity paySetting,
                                                       String orderType) throws BusinessException {
        return null;
    }

    @Override
    public CreateRefundOrderReturnVo createRefundOrder(RefundOrderParams refundOrderParams, String appId, String channelTag,
                                                       AbstractEntity paySetting) throws BusinessException {
        return null;
    }

    @Override
    public ExternalRefundOrder searchRefundOrderInfo(String outRefundNo, String appId, String channelTag, AbstractEntity paySetting) throws BusinessException {
        return null;
    }

    @Override
    public ExternalPaymentOrder closePaymentOrder(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams, String appId, String channelTag,
                                                  AbstractEntity paySetting) throws BusinessException {
        return null;
    }

    @Override
    public ExternalPaymentOrder cancelPaymentOrder(CloseOrCancelPayOrderParams closeOrCancelPayOrderParams, String appId, String channelTag,
                                                   AbstractEntity paySetting) throws BusinessException {
        return null;
    }

    @Override
    public Boolean handleNotify(Map<String, String> params, String appId, String channelTag, AbstractEntity paySetting, String type) throws BusinessException {
        return null;
    }

    @CheckAndAutowireChannelSetting
    @Override
    public CloudAccountRespVo createWithdrawOrder(CloudAccountOrderParams orderParams, String appId, String channelTag, AbstractEntity paySetting,
                                                  String ip) throws BusinessException {

        CapaySetting capaySetting = (CapaySetting) paySetting;
        CloudAccountRespVo respVo = new CloudAccountRespVo();
        String outTradeNo = orderParams.getOutTradeNo();

        // 请求频率限制
        String frequencyLock = "cloud:withdraw:frequency:lock:" + outTradeNo;
        boolean hasLock = cacheManager.exists(frequencyLock);
        if (hasLock) {
            cacheManager.expire(frequencyLock, 1);
            logger.error("cloud: createWithdrawOrder >> Exception : "
                    + BizExceptionEnum.TEAM_OPERATE_TOO_FAST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.TEAM_OPERATE_TOO_FAST);
        }

        // 判断订单是否存在
        ExternalWithdrawOrder orderEntity = this.getOrderInfoByOutTradeNo(outTradeNo);
        if (null != orderEntity) {
            logger.error("CloudAccountServiceImpl >> create >> Exception : "
                    + BizExceptionEnum.CLOUDPAY_ORDER_EXISTS.getErrMsg() + ",业务订单号为：{}", outTradeNo);
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_EXISTS);
        }

        // 构建参数——根据渠道
        String channel = orderParams.getChannel();
        int totalFee = Integer.valueOf(orderParams.getTotalFee());

        // 微信最少提现1元
        if (CloudChannelEnum.WECHAT.getValue().equals(channel) && 100 > totalFee) {
            logger.error("CloudAccountServiceImpl >> create >> Exception : "
                    + BizExceptionEnum.CLOUDPAY_MONEY_LESSTHAN_1_YUAN.getErrMsg() + ",业务订单号为：{}", outTradeNo);
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_MONEY_LESSTHAN_1_YUAN);
        }

        String supportChannel = capaySetting.getSupportChannel();
        if (!supportChannel.contains(channel)) {
            logger.error("CloudAccountServiceImpl >> create >> Exception : "
                    + BizExceptionEnum.CLOUDPAY_CHANNEL_NOT_SUPPORT.getErrMsg() + ",渠道为：{}", channel);
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_CHANNEL_NOT_SUPPORT);
        }

        // 构建云账户提现Url
        String cloudUrl = "";
        CloudChannelEnum channelEnum = CloudChannelEnum.lookup(channel);
        switch (channelEnum) {
            case BANKCARD:
                cloudUrl = capaySetting.getBankCardUrl();
                break;
            case WECHAT:
                cloudUrl = capaySetting.getWechatPayUrl();
                break;
            case ALI:
                cloudUrl = capaySetting.getAlipayUrl();
                break;
            default:
                break;
        }

        // 校验通过——生成提现记录ID
        String orderId = String.valueOf(snowFlake.nextId());
        String key = String.format(RedisKey.EXTERNAL_CLOUD_PAY_KEY, outTradeNo);

        ExternalWithdrawOrder order = new ExternalWithdrawOrder();
        order.setOrderId(orderId);
        order.setOutTradeNo(outTradeNo);
        order.setTotalFee(Integer.valueOf(orderParams.getTotalFee()));
        order.setState(ExternalWithdrawStateEnum.PROCESSING.getValue());
        order.setCardNo(orderParams.getCardNo() == null ? "" : orderParams.getCardNo());
        order.setUserId(orderParams.getUserId());
        order.setIdcardNumber(orderParams.getIdCard() == null ? "" : orderParams.getIdCard());
        order.setRealName(orderParams.getRealName() == null ? "" : orderParams.getRealName());
        // 渠道存中文
        order.setChannel(CloudChannelEnum.lookup(channel).getName());
        order.setSourceAppId(appId);

        orderRepository.save(order);
        cacheManager.set(key, order, 24 * 60 * 60);

        CloudCreateMessageVo messageVo = new CloudCreateMessageVo();
        messageVo.setOutTradeNo(outTradeNo);
        messageVo.setDealerId(capaySetting.getDealerId());
        messageVo.setBrokerId(capaySetting.getBrokerId());
        messageVo.setCloudUrl(cloudUrl);
        messageVo.setNotifyUrl(capaySetting.getNotifyUrl());
        messageVo.setChannel(channel);
        messageVo.setAppKey(capaySetting.getAppKey());
        messageVo.setDescKey(capaySetting.getDescKey());
        messageVo.setAppId(appId);
        messageVo.setIp(ip);

        // 订单入库-消息处理
        try {
            Message message = MessageBuilder.of(messageVo).topic(RocketMQConstant.PAYMENT_TOPIC)
                    .tag(RocketMQConstant.EXTERNEL_CLOUD_CREATEORDER_TAG).build();
            message.setTransactionId("create_" + outTradeNo);
            logger.debug("cloud >> createOrder >> topic :{} , tag :{}  ",
                    RocketMQConstant.PAYMENT_TOPIC, RocketMQConstant.EXTERNEL_CLOUD_CREATEORDER_TAG);
            createProducer.sendMessageInTransaction(message, messageVo);

        } catch (Exception e) {
            logger.error("cloud >> createWithdrawOrder >> rocketMQ >> exception :{} ", e.getMessage());
            throw new BusinessException(BizExceptionEnum.ROCKETMQ_PRODUCER_ERROR);
        }
        respVo.setOrderId(orderId);
        respVo.setOutTradeNo(outTradeNo);
        respVo.setStatus("提现订单已受理");
        return respVo;
    }

    @Override
    public void walletNotify(Map<String, String> params) {

    }

    private ExternalWithdrawOrder getOrderInfoByOutTradeNo(String outTradeNo) {
        ExternalWithdrawOrder order = null;
        logger.debug("getOrderInfoByOutTradeNo 入参 >> outTradeNo={} >> ", outTradeNo);
        String key = String.format(RedisKey.EXTERNAL_CLOUD_PAY_KEY, outTradeNo);
        order = cacheManager.get(key, ExternalWithdrawOrder.class);

        if (null == order) {
            order = orderRepository.findByOutTradeNo(outTradeNo);
        }
        logger.debug("getOrderInfoByOutTradeNo 出参 >> " + order);
        return order;
    }

    @Override
    public List<CloudAccountDealerInfoVo> queryAccount(String appId, String channelTag) throws BusinessException {
        List<CloudAccountDealerInfoVo> list = new ArrayList<>();

        // 验证APP应用及渠道合法性
        PayChannel payChannel = payChannelRepository.findOneByTagAndAppSettingsAppId(channelTag, appId);
        if (null == payChannel) {
            logger.error("CloudAccountServiceImpl >> create >> Exception : "
                    + BizExceptionEnum.EXTERNAL_APP_CHANNEL_NOT_FOUND.getErrMsg() + ",appId为{}, tag为{}", appId, channelTag);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_APP_CHANNEL_NOT_FOUND);
        }
        CapaySetting capaySetting = capaySettingRepository.findOneByChannelId(payChannel.getId());
        String dealerId = capaySetting.getDealerId();

        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("dealer_id", dealerId);

        // 加密
        Map<String, String> encryptMap = null;
        try {
            String encryptJson = FastJsonUtils.toJson(paramMap);
            encryptMap = CloudAccountConfig.encryptData(encryptJson,
                    capaySetting.getDescKey(), capaySetting.getAppKey());
        } catch (Exception e) {
            logger.error("查询商户余额，商户号为{}， 构建加密参数异常:【{}】", dealerId, e.getMessage());
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_BALANCE_ENCRYPT_ERROR);
        }

        // 调用云账户接口
        String ret = null;
        try {
            ret = this.buildGetUrlAndReceiveData(encryptMap, QUERY_ACCOUNT_URL, dealerId);
        } catch (Exception e) {
            logger.error("查询商户余额，商户号为{}，调用云账户接口异常:【{}】", dealerId, e.getMessage());
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_BALANCE_NETWORK_ERROR);
        }
        if (StringUtils.isEmpty(ret)) {
            logger.error("云账户查询商户余额，商户号为{}，Exception：{}", dealerId, BizExceptionEnum.CLOUDPAY_BALANCE_RETURN_NULL);
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_BALANCE_RETURN_NULL);
        }

        JSONObject jsonObject = JSONObject.parseObject(ret);
        logger.debug(" >>云账户提现 >> thirdResponse  >> " + jsonObject.toString());

        String code = jsonObject.getString("code");
        if (!CloudReturnCodeEnum.SUCCESS.getValue().equals(code)) {
            String retMessage = jsonObject.getString("message");
            String name = CloudReturnCodeEnum.lookup(code).getName();
            logger.error("云账户提现订单异常，商户号为{}，错误申明为 :【{}】，云账户具体返回为：【{}】", dealerId, name, retMessage);
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_RETURN_ERROR);
        }
        JSONObject dataJson = jsonObject.getJSONObject("data");
        JSONArray array = dataJson.getJSONArray("dealer_infos");
        list = JSONObject.parseArray(array.toJSONString(), CloudAccountDealerInfoVo.class);

        return list;
    }

    /**
     * 处理通知
     *
     * @param requestMap(发送请求的那几个字段)
     * @return
     */
    @Override
    public String receiveCloudNotify(Map<String, String> requestMap) {
        /**
         * 1.验签
         * 2.解密
         * 3.验证订单号，商户号，金额
         */
        PayChannel payChannel = payChannelRepository.findOneByTag(PayChannelEnum.CLOUD_ACCOUNT_PAY.getValue());
        CapaySetting capaySetting = capaySettingRepository.findOneByChannelId(payChannel.getId());
        String appKey = capaySetting.getAppKey();
        String descKey = capaySetting.getDescKey();

        String mess = requestMap.get("mess");
        String data = requestMap.get("data");
        String timestamp = requestMap.get("timestamp");
        String cloudSign = requestMap.get("sign");

        // 验证签名
        String signPair = "data=%s&mess=%s&timestamp=%s&key=%s";
        String signOriginal = String.format(signPair, data, mess, timestamp, appKey);
        String sign = CloudAccountConfig.sha256_HMAC(signOriginal, appKey);
        if (!cloudSign.equals(sign)) {
            logger.error("处理云账户提现通知，验签失败，返回签名cloudSign={}, 系统生成sign={}", cloudSign, sign);
            return "fail";
        }

        // 解密
        String decryptData = "";
        try {
            decryptData = CloudAccountConfig.decryptData(data, descKey);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("处理云账户提现通知，解密失败，exception ={}", e.getMessage());
            return "fail";
        }

        // 验证返回数据是否与数据库记录一致(单号，金额) 及状态
        JSONObject jsonObject = JSONObject.parseObject(decryptData);
        JSONObject dataJson = jsonObject.getJSONObject("data");
        String outTradeNo = dataJson.getString("order_id");
        // 状态
        int status = dataJson.getIntValue("status");
        String pay = dataJson.getString("pay");
        // 完成时间
        String finishTime = dataJson.getString("finished_time");
        ExternalWithdrawOrder orderEntity = this.getOrderInfoByOutTradeNo(outTradeNo);
        if (null == orderEntity) {
            logger.error("CloudAccountServiceImpl >> notify >> Exception : "
                    + BizExceptionEnum.CLOUDPAY_ORDER_NOT_FUND.getErrMsg() + ",业务订单号为：{}", outTradeNo);
            return "fail";
        }
        BigDecimal totalFee = new BigDecimal(orderEntity.getTotalFee()).divide(new BigDecimal(HUNDRED));
        BigDecimal payAmount = new BigDecimal(pay);
        if (totalFee.compareTo(payAmount) != 0) {
            logger.error("CloudAccountServiceImpl >> notify >> Exception : "
                    + BizExceptionEnum.CLOUDPAY_ORDER_AMOUNT_NOTEQUAL.getErrMsg() + ",业务订单号为：{}", outTradeNo);
            return "fail";
        }
        String stateDB = orderEntity.getState(); // 数据库状态
        String stateHttp = CloudStatusStateMappingEnum.lookup(status).getName(); // 返回状态
        if (stateDB.equals(stateHttp) && CloudStatusStateMappingEnum.SUCCESS.getName().equals(stateDB)) {
            logger.info("该笔提现订单已经处理完成，订单号为{}", outTradeNo);
            return "success";
        }

        // 返回数据入MongoDB
        WithdrawVoucher withdrawVoucher = withdrawVoucherRepository.findOneByOrderId(orderEntity.getOrderId());
        withdrawVoucher.setMetadata(jsonObject);
        LocalDateTime completedTime = LocalDateTime.now();
        if (StringUtils.isEmpty(finishTime)) {
            withdrawVoucher.setCompletedTime(completedTime);
        } else {
            completedTime = DateUtil.str2LocalDateTime(finishTime, DateTimeFormatter.ofPattern(DateUtil.FORMATTER));
            withdrawVoucher.setCompletedTime(completedTime);
        }
        // 通过value获取name
        withdrawVoucher.setState(CloudStatusStateMappingEnum.lookup(status).getName());
        withdrawVoucherRepository.save(withdrawVoucher);

        // 状态入消息队列处理
        try {
            Message message = MessageBuilder.of(dataJson).topic(RocketMQConstant.PAYMENT_TOPIC)
                    .tag(RocketMQConstant.EXTERNEL_CLOUD_NOTIFYORDER_TAG).build();
            message.setTransactionId("notify_" + outTradeNo);
            logger.debug("cloud >> receiveCloudNotify >> topic :{} , tag :{}  ",
                    RocketMQConstant.PAYMENT_TOPIC, RocketMQConstant.EXTERNEL_CLOUD_NOTIFYORDER_TAG);
            notifyProducer.sendMessageInTransaction(message, dataJson);

        } catch (Exception e) {
            logger.error("cloud >> receiveCloudNotify >> rocketMq生产者出问题了 >> exception :{} ", e.getMessage());
            return "fail";
        }

        return "success";
    }

    /**
     * 主动查询
     *
     * @param outTradeNo
     * @param appId
     * @param tag
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CheckAndAutowireChannelSetting
    @Override
    public CloudWithdrawOrderDto searchWithdrawOrderInfo(String outTradeNo, String appId, String tag,
                                                         CapaySetting capaySetting) throws BusinessException {
        CloudWithdrawOrderDto orderDto = new CloudWithdrawOrderDto();

        // 判断订单是否存在
        ExternalWithdrawOrder orderEntity = this.getOrderInfoByOutTradeNo(outTradeNo);
        if (null == orderEntity) {
            logger.error("CloudAccountServiceImpl >> create >> Exception : "
                    + BizExceptionEnum.CLOUDPAY_ORDER_NOT_FUND.getErrMsg() + ",业务订单号为：{}", outTradeNo);
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_NOT_FUND);
        }

        String dealerId = capaySetting.getDealerId();

        String channel = orderEntity.getChannel();

        // 加密
        Map<String, String> dataMap = new LinkedHashMap<>();
        dataMap.put("order_id", outTradeNo);
        dataMap.put("channel", channel);

        // 加密
        Map<String, String> encryptMap = null;
        try {
            String encryptJson = FastJsonUtils.toJson(dataMap);
            encryptMap = CloudAccountConfig.encryptData(encryptJson,
                    capaySetting.getDescKey(), capaySetting.getAppKey());
        } catch (Exception e) {
            logger.error("云账户提现 主动查询订单，业务订单号为{}， 构建加密参数异常:【{}】", outTradeNo, e.getMessage());
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_ENCRYPT_ERROR);
        }

        // 调用云账户接口
        String ret = null;
        try {
            ret = this.buildGetUrlAndReceiveData(encryptMap, QUERY_ORDER_URL, dealerId);
        } catch (Exception e) {
            logger.error("查询云账户提现订单状态异常，订单号为{}，调用云账户接口异常:【{}】", outTradeNo, e.getMessage());
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_QUERY_NETWORK_ERROR);
        }
        if (StringUtils.isEmpty(ret)) {
            logger.error("云账户查询订单状态，订单号为{}，Exception：{}", outTradeNo, BizExceptionEnum.CLOUDPAY_QUERY_RETURN_NULL);
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_QUERY_RETURN_NULL);
        }

        JSONObject jsonObject = JSONObject.parseObject(ret);
        logger.debug(" >>云账户提现 >> 查询订单状态 >> thirdResponse  >> " + jsonObject.toString());

        String code = jsonObject.getString("code");
        if (!CloudReturnCodeEnum.SUCCESS.getValue().equals(code)) {
            String name = CloudReturnCodeEnum.lookup(code).getName();
            logger.error("云账户查询提现订单异常，订单号为{}，返回错误申明为 :【{}】", outTradeNo, name);
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_QUERY_RETURN_ERROR);
        }

        JSONObject dataJson = jsonObject.getJSONObject("data");
        int status = Integer.valueOf(dataJson.getString("status")); // 状态
        String pay = dataJson.getString("pay");
        BigDecimal totalFee = new BigDecimal(orderEntity.getTotalFee()).divide(new BigDecimal(HUNDRED));
        BigDecimal payAmount = new BigDecimal(pay);
        if (totalFee.compareTo(payAmount) != 0) {
            logger.error("CloudAccountServiceImpl >> queryOrder >> Exception : "
                    + BizExceptionEnum.CLOUDPAY_ORDER_AMOUNT_NOTEQUAL.getErrMsg() + ",业务订单号为：{}", outTradeNo);
            throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_AMOUNT_NOTEQUAL);
        }

        CloudStatusStateMappingEnum statusEnum = CloudStatusStateMappingEnum.lookup(status);
        String key = String.format(RedisKey.EXTERNAL_CLOUD_PAY_KEY, outTradeNo);
        // 根据状态处理业务逻辑
        this.updateCloudWithdrawOrderInfo(statusEnum, dataJson, orderEntity, key, "search");

        BeanUtils.copyProperties(orderEntity, orderDto);

        return orderDto;
    }

    /**
     * 根据返回标志进行逻辑处理
     * @param statusEnum 云账户提现返回枚举类
     * @param jsonObject 返回数据
     * @param order 云账户提现订单
     * @param key 订单redisKey
     * @param flag 更新标志
     */
    public void updateCloudWithdrawOrderInfo(CloudStatusStateMappingEnum statusEnum, JSONObject jsonObject,
                                             ExternalWithdrawOrder order, String key, String flag) {
        WithdrawAuditRecord record = auditRecordRepository.findByOrderId(order.getOutTradeNo());
        switch (statusEnum) {
            case PROCESSING:
            case SUSPEND:
            case WAITING:
            case WAIT_EXECUTE:
                String statusMessage = jsonObject.getString("status_message");
                logger.info("订单已受理，尚未真正处理成功，订单号为{},返回具体信息为{}", order.getOutTradeNo(), statusMessage);
                break;
            case SUCCESS:
                // 处理成功对应逻辑
                order = this.buildEntity(jsonObject, order);
                order.setState(ExternalWithdrawStateEnum.SUCCESS.getValue());
                orderRepository.save(order);
                cacheManager.del(key);
                cacheManager.set(key, order, 30 * 60);

                String finishTime = jsonObject.getString("finished_time"); // 完成时间
                WithdrawAuditListVo queryVo = new WithdrawAuditListVo();
                queryVo.setVerifyState(WithdrawVerifyEnum.SUCCESS.getValue());
                queryVo.setFinishDate(finishTime);
                queryVo.setId(record.getId());
                withdrawService.updateAuditState(queryVo);

                // 增加云账户提现流水-避免重复记录
                if ("notify".equals(flag)) {
                    tradeBillService.saveTradeBill(order);
                }
                break;
            case FAILED:
            case TIMEOUT:
                // 处理失败
                order = this.buildEntity(jsonObject, order);
                order.setState(ExternalWithdrawStateEnum.FAILED.getValue());
                orderRepository.save(order);
                cacheManager.del(key);
                cacheManager.set(key, order, 30 * 60);

                // 云账户回调-返回失败
                WithdrawAuditListVo vo = new WithdrawAuditListVo();
                vo.setVerifyState(WithdrawVerifyEnum.FAIL.getValue());
                vo.setId(record.getId());
                withdrawService.updateAuditState(vo);

                break;
            default:
                break;
        }
    }

    /**
     * 拼装参数
     *
     * @param jsonObject
     * @param order
     * @return
     */
    public ExternalWithdrawOrder buildEntity(JSONObject jsonObject, ExternalWithdrawOrder order) {
        String sysWalletRef = jsonObject.getString("sys_wallet_ref"); // 系统打款商户订单号
        String sysBankBill = jsonObject.getString("sys_bank_bill"); // 系统打款交易流⽔号
        String brokerWalletRef = jsonObject.getString("broker_wallet_ref"); // 代征主体打款商户订单号
        String brokerBankBill = jsonObject.getString("broker_bank_bill"); // 代征主体打款交易流⽔号
        String finishTime = jsonObject.getString("finished_time"); // 完成时间

        Date completedTime = DateUtil.str2Date(finishTime, DateUtil.FORMATTER);
        String statusCode = jsonObject.getString("status_detail");
        String statusMessage = jsonObject.getString("status_message");
        String detailMessage = jsonObject.getString("status_detail_message");
        String message = statusMessage + "," + detailMessage; // 状态信息合并

        order.setStatusCode(statusCode);
        order.setMessage(message);
        order.setBrokerBankBill(brokerBankBill);
        order.setBrokerWalletRef(brokerWalletRef);
        order.setPaiedTime(completedTime);
        order.setSysBankBill(sysBankBill);
        order.setSysWalletRef(sysWalletRef);

        return order;

    }

    /**
     * 构建云账户Get请求Url
     *
     * @param url
     * @return
     */
    private String buildGetUrlAndReceiveData(Map<String, String> encryptMap,
                                             String url, String dealerId) throws Exception {
        String ret = null;
//        String signPair = "mess=%s&sign_type=%s&timestamp=%s&data=%s&sign=%s";
//        String queryParams = String.format(signPair, mess, CloudAccountConfig.SIGN_TYPE, timestamp, data, sign);
        String mess = encryptMap.get("mess");
        // RestTemplate对url进行了处理，这里不需要进行编码了
        String data = encryptMap.get("data");
        String timestamp = encryptMap.get("timestamp");
        String sign = encryptMap.get("sign");
        String queryUrl = url + "?" + "mess=" + mess + "&sign_type=" + CloudAccountConfig.SIGN_TYPE + "&timestamp="
                + timestamp + "&data=" + data + "&sign=" + sign;
        logger.debug(">> 查询商户余额url为：【{}】", queryUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("dealer-id", dealerId);
        headers.set("request-id", dealerId + "-" + timestamp);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplateExtrnal.exchange(queryUrl, HttpMethod.GET, httpEntity, String.class);
        if (resp.getStatusCode().equals(HttpStatus.OK)) {
            ret = resp.getBody();
        }
        return ret;
    }

    /**
     * 返回格式-data外还有1层
     * {
     *     "notify_id":"14732279660721952",
     *     "notify_time":"2017-05-25 11:49:34",
     *     "data":{
     *         "pay":"100.00",
     *         "anchor_id":"test",
     *         "broker_amount":"-/-",
     *         "broker_id":"yunyi",
     *         "card_no":"6228021232235288058",
     *         "dealer_id":"sixcn",
     *         "id_card":"120101198812142146",
     *         "order_id":"201609010016562012999",
     *         "phone_no":"18601318616",
     *         "real_name":"测试",
     *         "ref":"79822056820047883",
     *         "notes":"56244623 20160901-1",
     *         "status":1,
     *         "status_detail":0,
     *         "status_message":"已打款",
     *         "status_detail_message":"已打款",
     *         "sys_amount":"100.00",
     *         "pay_remark":"打款备注",
     *         "tax":"-/-",
     *         "tax_level":"",
     *         "sys_wallet_ref":"",
     *         "sys_bank_bill":"",
     *         "broker_wallet_ref":"",
     *         "broker_bank_bill":""
     *     }
     * }
     */

}
