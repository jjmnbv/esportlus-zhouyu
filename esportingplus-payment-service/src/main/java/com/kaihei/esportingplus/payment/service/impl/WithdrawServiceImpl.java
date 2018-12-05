package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.feign.CompaintServiceClient;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.HttpConstant;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.payment.api.enums.*;
import com.kaihei.esportingplus.payment.api.params.*;
import com.kaihei.esportingplus.payment.api.vo.*;
import com.kaihei.esportingplus.payment.data.jpa.repository.*;
import com.kaihei.esportingplus.payment.domain.entity.*;
import com.kaihei.esportingplus.payment.mq.producer.ExchangeTransactionProducer;
import com.kaihei.esportingplus.payment.mq.producer.WithdrawTransactionProducer;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.DeductRatioService;
import com.kaihei.esportingplus.payment.service.PayService;
import com.kaihei.esportingplus.payment.service.WithdrawService;
import com.kaihei.esportingplus.payment.util.PageUtils;
import com.kaihei.esportingplus.user.api.feign.UserBalanceServiceClient;
import com.kaihei.esportingplus.user.api.vo.UserBalanceResutVo;
import com.maihaoche.starter.mq.base.MessageBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * 提现相关Service
 *
 * @author chenzhenjun
 * @create 2018-08-17 11:45
 **/
@Service
public class WithdrawServiceImpl implements WithdrawService {

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    private static final Logger logger = LoggerFactory.getLogger(WithdrawServiceImpl.class);

    private static final String HUNDRED = "100";

    private static final String CURRENCY_TYPE = "starlight";

    private static final String WITHDRAW_REJECT = "提现失败";

    private static final String SCORE_EXCHANGE_SUBJECT = "积分兑换暴击值";

    private static final String STARLIGHT_EXCHANGE_SUBJECT = "暴击值兑换暴鸡币";

    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private StarlightBalanceRepository starlightBillRepository;

    @Autowired
    private WithdrawTransactionProducer withdrawTransactionProducer;

    @Autowired
    private ExchangeTransactionProducer exchangeTransactionProducer;

    @Autowired
    private BillFlowService paymentBillService;

    @Autowired
    private UserBalanceServiceClient userBalanceServiceClient;

    @Autowired
    private CompaintServiceClient compaintServiceClient;

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Value("${python.order.complain.url}")
    private String orderComplainUrl;

    @Autowired
    private WithdrawConfigRepository withdrawConfigRepository;

    @Autowired
    private WithdrawAuditRecordRepository auditRecordRepository;

    @Autowired
    private PayService cloudAccountServiceImpl;

    @Autowired
    private DeductRatioService deductRatioService;

    @Autowired
    private WithdrawTaxRecordRepository taxRecordRepository;

    /**
     * @Description: 从redis中获取对应的体现订单信息
     * @Param: [orderId, userId]
     * @return: com.kaihei.esportingplus.payment.domain.entity.Withdraw
     * @Author: xusisi
     * @Date: 2018/8/19
     */
    @Override
    public WithdrawOrder getOrderInfoFromRedis(String orderId, String userId) {

        logger.debug("getOrderInfoFromRedis == withdraw 入参 >> orderId :{},userId:{}", orderId, userId);
        String key = RedisKeyType.WITHDRAW.getCode() + ":" + userId + ":" + orderId;
        WithdrawOrder withdrawOrder = cacheManager.get(key, WithdrawOrder.class);
        logger.debug("getOrderInfoFromRedis 出参 >> {} ", withdrawOrder);
        return withdrawOrder;
    }

    /*     新的提现及兑换实现代码      -------------start                    */

    @Override
    public Map<String, String> checkAndCreateWithdrawOrder(WithdrawCreateParams withdrawCreateParams) throws BusinessException {
        logger.debug("checkAndPushWithdrawOrder 入参 >> withdrawCreateParams = {} " + withdrawCreateParams.toString());
        Map<String, String> response = new HashMap<>();
        String userId = withdrawCreateParams.getUserId();
        String channel = withdrawCreateParams.getChannel();
        String notifyFlag = withdrawCreateParams.getNotifyFlag();
        // 普通暴鸡提现需要python通知，工作室暴鸡不需要
        if (WithdrawNotifyEnum.NEED.getCode().equals(notifyFlag) && PayChannelEnum.CLOUD_ACCOUNT_PAY.getValue().equals(channel)) {
            logger.error("checkAndCreateWithdrawOrder >> Exception : " + BizExceptionEnum.WITHDRAW_PARAMS_NOT_MATCH.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_PARAMS_NOT_MATCH);
        }
        if (WithdrawNotifyEnum.NOE_NEED.getCode().equals(notifyFlag) && (PayChannelEnum.WECHAT_APP_PAY.getValue().equals(channel)
                || PayChannelEnum.ALI_APP_PAY.getValue().equals(channel))) {
            logger.error("checkAndCreateWithdrawOrder >> Exception : "
                    + BizExceptionEnum.WITHDRAW_PARAMS_NOT_MATCH.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_PARAMS_NOT_MATCH);
        }

        StarlightBalance balanceEntity = starlightBillRepository.findOneByUserId(userId);

        if (balanceEntity == null) {
            logger.error("checkAndCreateWithdrawOrder >> Exception : " + BizExceptionEnum.STARACCOUNT_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STARACCOUNT_NOT_EXIST);
        }

        if (!AccountStateType.AVAILABLE.getCode().equals(balanceEntity.getState())) {
            logger.error("checkAndCreateWithdrawOrder >> Exception : " + BizExceptionEnum.STARTACCOUNT_UNAVALIABLE.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STARTACCOUNT_UNAVALIABLE);
        }

        String outTradeNo = withdrawCreateParams.getOutTradeNo();
        WithdrawOrder extistWithdraw = withdrawOrderRepository.findOneByOutTradeNo(outTradeNo);
        if (null != extistWithdraw) {
            logger.error("checkAndCreateWithdrawOrder >> Exception : " + BizExceptionEnum.WITHDRAW_REPEAT__FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_REPEAT__FAIL);
        }

        BigDecimal usableAmount = balanceEntity.getUsableAmount();
        //提现暴击值
        String amount = String.valueOf(withdrawCreateParams.getAmount());
        BigDecimal withdrawMoney = new BigDecimal(amount).divide(new BigDecimal(HUNDRED));
        if (usableAmount.compareTo(withdrawMoney) < 0) {
            logger.error("checkAndCreateWithdrawOrder >> Exception : "
                    + BizExceptionEnum.BALANCE_WITHDRAW__FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.BALANCE_WITHDRAW__FAIL);
        }

        // 校验通过——生成提现记录ID
        String orderId = String.valueOf(snowFlake.nextId());

        response.put("orderId", orderId);
        response.put("outTradeNo", outTradeNo);
        logger.debug("checkAndCreateWithdrawOrder 出参 >> reponse = {} " + response.toString());

        // 先入库，入Redis
        WithdrawOrder withdrawOrder = new WithdrawOrder();
        withdrawOrder.setOrderType(OrderTypeEnum.WITHDRAW_ORDER.getCode());
        withdrawOrder.setIsNotify(withdrawCreateParams.getNotifyFlag());
        withdrawOrder.setOrderId(orderId);
        withdrawOrder.setAmount(withdrawMoney);
        withdrawOrder.setStarAmount(withdrawMoney);
        withdrawOrder.setFee(new BigDecimal("0"));
        withdrawOrder.setUserId(userId);
        withdrawOrder.setChannel(channel);
        withdrawOrder.setState(WithdrawStatusType.CREATE.getCode());
        withdrawOrder.setSourceId(withdrawCreateParams.getSourceId());

        withdrawOrder.setBody(withdrawCreateParams.getBody());
        withdrawOrder.setDescription(withdrawCreateParams.getDescription());
        withdrawOrder.setSubject(withdrawCreateParams.getSubject());
        withdrawOrder.setOutTradeNo(withdrawCreateParams.getOutTradeNo());
        withdrawOrder.setMoneyType(withdrawCreateParams.getMoneyType() == null ? CURRENCY_TYPE : withdrawCreateParams.getMoneyType());
        //将订单信息保存至数据库
        withdrawOrderRepository.save(withdrawOrder);

        String key = RedisKeyType.WITHDRAW.getCode() + ":" + userId + ":" + orderId;
        //将订单信息存于redis中，过期时间为24小时
        cacheManager.set(key, withdrawOrder, 24 * 60 * 60);

        // 新增提现审核记录(工作室提现不需创建)
        if (!PayChannelEnum.CLOUD_ACCOUNT_PAY.getValue().equals(channel)) {
            WithdrawAuditRecord auditRecord = new WithdrawAuditRecord();
            auditRecord.setUid(userId);
            auditRecord.setChannel(WithdrawChannelEnum.lookupByValue(channel).getName());
            auditRecord.setOrderId(outTradeNo);
            auditRecord.setRemark("申请提现");
            auditRecord.setTotalFee(withdrawCreateParams.getAmount());
            auditRecord.setVerifyState(WithdrawVerifyEnum.WAIT.getValue());
            auditRecord.setSourceAppId(withdrawCreateParams.getSourceId());
            auditRecord.setBlockState(WithdrawBlockEnum.NOT_YET.getValue());
            auditRecord.setClientIp(withdrawCreateParams.getClientIp());
            auditRecordRepository.save(auditRecord);
        }

        // 由消费者 冻结暴击值
        WithdrawUpdateMessageVO messageVO = new WithdrawUpdateMessageVO();
        messageVO.setUserId(userId);
        messageVO.setOrderId(orderId);
        messageVO.setOutTradeNo(outTradeNo);
        messageVO.setNotifyUrl(withdrawCreateParams.getNotifyUrl());
        //发送至MQ
        try {
            // 创建提现订单的时候，先记录流水
            // 工作室提现流水在 consumer消费时才加，这里不需要记录
            //if (WithdrawNotifyEnum.NEED.getCode().equals(notifyFlag)) {
            //}

            Message message = MessageBuilder.of(messageVO).topic(RocketMQConstant.PAYMENT_TOPIC)
                    .tag(RocketMQConstant.STARLIGHT_WITHDRAW_TAG).build();
            message.setTransactionId(genTransactionId(orderId));
            logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.STARLIGHT_WITHDRAW_TAG,
                    message);

            withdrawTransactionProducer.sendMessageInTransaction(message, messageVO);

        } catch (Exception e) {
            logger.error("checkAndCreateWithdrawOrder >> exception :{} ", e.getMessage());
            throw new BusinessException(BizExceptionEnum.ROCKETMQ_PRODUCER_ERROR);
        }

        return response;
    }

    @Override
    public Map<String, String> updateWithdrawStatus(WithdrawUpdateParams withdrawUpdateParams)
            throws BusinessException {
        Map<String, String> response = new HashMap<>();
        String outTradeNo = withdrawUpdateParams.getOutTradeNo();
        String remitStatus = withdrawUpdateParams.getRemitStatus();
        String userId = withdrawUpdateParams.getUserId();
        String orderId = withdrawUpdateParams.getOrderId();
        WithdrawOrder withdrawOrder = null;
        if (StringUtils.isEmpty(orderId) && StringUtils.isEmpty(outTradeNo)) {
            logger.error("updateWithdrawStatus >> Exception : " + BizExceptionEnum.WITHDRAW_BOTH__NULL__FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_BOTH__NULL__FAIL);
        }

        withdrawOrder = this.getWithdrawStateInfoWithAllParams(outTradeNo, orderId, userId);
        if (withdrawOrder == null) {
            logger.error("updateWithdrawStatus >> Exception : " + BizExceptionEnum.ORDER_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ORDER_NOT_EXIST);
        }

        if (!WithdrawStatusType.CREATE.getCode().equals(withdrawOrder.getState())) {
            logger.error("updateWithdrawStatus >> Exception : " + BizExceptionEnum.WITHDRAW_FINISH.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_FINISH);
        }

        StarlightBalance balanceEntity = starlightBillRepository
                .findOneByUserId(withdrawOrder.getUserId());
        // 根据返回的结果进行相应的处理
        if (WithdrawResultType.SUCCESS.getCode().equals(remitStatus)) {
            withdrawOrder.setState(WithdrawStatusType.SUCCESS.getCode());
            withdrawOrderRepository.save(withdrawOrder);
            // 余额更新
            //提现金额
            BigDecimal withdrawAmount = withdrawOrder.getAmount();
            BigDecimal frozenAmount = balanceEntity.getFrozenAmount();
            // 冻结的额度恢复
            BigDecimal frozenAmountAfter = frozenAmount.subtract(withdrawAmount);
            balanceEntity.setFrozenAmount(frozenAmountAfter);
            starlightBillRepository.save(balanceEntity);

        } else if (WithdrawResultType.FAIL.getCode().equals(remitStatus)) {
            withdrawOrder.setState(WithdrawStatusType.FAIL.getCode());
            withdrawOrderRepository.save(withdrawOrder);
            //提现金额
            BigDecimal withdrawAmount = withdrawOrder.getAmount();
            BigDecimal frozenAmount = balanceEntity.getFrozenAmount();
            // 可用暴击值
            BigDecimal useableAmount = balanceEntity.getUsableAmount();
            // 冻结的额度恢复
            BigDecimal frozenAmountAfter = frozenAmount.subtract(withdrawAmount);
            // 可用暴击值恢复
            BigDecimal useableAmountAfter = useableAmount.add(withdrawAmount);
            balanceEntity.setBalance(useableAmountAfter);
            balanceEntity.setUsableAmount(useableAmountAfter);
            balanceEntity.setFrozenAmount(frozenAmountAfter);
            starlightBillRepository.save(balanceEntity);

            // 提现失败时，再新建一条流水，保证平衡
            // 退款订单
            withdrawOrder.setOrderType(OrderTypeEnum.REFUND_ORDER.getCode());
            withdrawOrder.setSubject(WITHDRAW_REJECT);
            withdrawOrder.setBody(WITHDRAW_REJECT);

            withdrawOrder.setBalance(balanceEntity.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
            paymentBillService.saveRecord(withdrawOrder);

        }

        // 更新redis, 变更 过期时间
        String key = RedisKeyType.WITHDRAW.getCode() + ":" + withdrawOrder.getUserId() + ":" + withdrawOrder.getOrderId();
        cacheManager.del(key);
        cacheManager.set(key, withdrawOrder, 30 * 60);

        response.put("outTradeNo", withdrawOrder.getOutTradeNo());
        response.put("state", withdrawOrder.getState());
        response.put("userId", withdrawOrder.getUserId());
        response.put("orderId", withdrawOrder.getOrderId());
        return response;

    }

    @Override
    public List<UserBalanceListVO> getStarLightValues(String userIds) {
        List<UserBalanceListVO> listVo = new ArrayList<>();

        List<StarlightBalance> balanceList = new ArrayList<StarlightBalance>();

        List<String> list = new ArrayList<String>();
        String str[] = userIds.split(",");
        list = Arrays.asList(str);

        balanceList = starlightBillRepository.findByUserIdIn(list);
        if (CollectionUtils.isNotEmpty(balanceList)) {
            for (StarlightBalance balance : balanceList) {
                UserBalanceListVO vo = new UserBalanceListVO();
                String userId = balance.getUserId();
                BigDecimal amount = balance.getUsableAmount();
                int value = amount.multiply(new BigDecimal(HUNDRED)).intValue();
                vo.setUserId(userId);
                vo.setValue(value);

                listVo.add(vo);
            }
        }

        return listVo;
    }

    @Override
    public WithdrawOrder getWithdrawStateInfo(String orderId, String userId) {
        logger.debug("getWithdrawStateInfo 入参 >> orderId={},userId={} >> ", orderId, userId);
        WithdrawOrder withdrawOrder = this.getOrderInfoFromRedis(orderId, userId);
        if (withdrawOrder == null) {
            withdrawOrder = withdrawOrderRepository.findOneByOrderIdAndUserId(orderId, userId);
        }
        logger.debug("getWithdrawStateInfo 出参 >> " + withdrawOrder);
        return withdrawOrder;
    }

    @Override
    public Map<String, String> convertStarlightToGCoin(StarlightExchangeParams starlightExchangeParams) throws BusinessException {
        logger.debug("convertStarlightToGCoin 入参 >> starlightExchangeParams = {} " + starlightExchangeParams.toString());
        Map<String, String> response = new HashMap<>();
        String userId = starlightExchangeParams.getUserId();

        ResponsePacket<UserBalanceResutVo> responsePacket = userBalanceServiceClient.getExchangeAuthority(userId);
        boolean exchangeFlag = responsePacket.getData().getExchangeMark();
        // 兑换开关配置
        if (!exchangeFlag) {
            logger.error("convertStarlightToGCoin >> 用户身份异常 >> Exception : " + BizExceptionEnum.EXCHANGE_SERVICE_REJECT.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXCHANGE_SERVICE_REJECT);
        }

        // 请求频率限制
        String frequencyLock = "exchange:frequency:lock:" + userId;
        boolean hasLock = cacheManager.exists(frequencyLock);
        if (hasLock) {
            cacheManager.expire(frequencyLock, 1);
            logger.error("convertStarlightToGCoin >> Exception : " + BizExceptionEnum.TEAM_OPERATE_TOO_FAST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.TEAM_OPERATE_TOO_FAST);
        }

        // 增加-投诉进行中(DNF车队)订单校验 add by chenzj 2018-11-01
        ResponsePacket<Integer> complainPacket = compaintServiceClient.checkUserBeComplained(userId);
        int complainCount = complainPacket.getData();
        if (0 < complainCount) {
            logger.error("convertStarlightToGCoin >> 免费车队订单 >> Exception : " + BizExceptionEnum.COMPLAIN_ORDER_EXIST_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.COMPLAIN_ORDER_EXIST_FAIL);
        }

        // 添加python那边的投诉订单校验
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(HttpConstant.CONTENT_TYPE_JSON);
        headers.setContentType(type);
        headers.set("Authorization", "Token  " + starlightExchangeParams.getPythonToken());

        HttpEntity httpEntity = new HttpEntity<>(headers);
        String checkOrderProcessingUrl = orderComplainUrl + "?uid=" + userId;
        logger.debug("请求Python有在投诉中的订单查询地址：{} ", checkOrderProcessingUrl);
        ResponseEntity<ResponsePacket> resp = restTemplateExtrnal.exchange(checkOrderProcessingUrl, HttpMethod.GET, httpEntity, ResponsePacket.class);

        ResponsePacket pythonResponse = resp.getBody();
        logger.info("resp.getBody() " + pythonResponse.toString());
        if (!pythonResponse.responseSuccess()) {
            logger.error(">> 调用Python接口获取兑换是否有投诉订单错误!参数: {}, 返回结果: {}", userId, pythonResponse);
            throw new BusinessException(pythonResponse.getCode(), pythonResponse.getMsg());
        }
        LinkedHashMap respMap = (LinkedHashMap) pythonResponse.getData();
        Boolean transferLimit = (Boolean) respMap.get("transfer_limit");
        if (transferLimit) {
            logger.error("convertStarlightToGCoin >> python有投诉订单 >> Exception : " + BizExceptionEnum.COMPLAIN_ORDER_EXIST_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.COMPLAIN_ORDER_EXIST_FAIL);
        }

        StarlightBalance balanceEntity = starlightBillRepository.findOneByUserId(userId);

        if (balanceEntity == null) {
            logger.error("convertStarlightToGCoin >> Exception : " + BizExceptionEnum.STARACCOUNT_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STARACCOUNT_NOT_EXIST);
        }

        if (!AccountStateType.AVAILABLE.getCode().equals(balanceEntity.getState())) {
            logger.error("convertStarlightToGCoin >> Exception : " + BizExceptionEnum.STARTACCOUNT_UNAVALIABLE.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STARTACCOUNT_UNAVALIABLE);
        }

        BigDecimal usableAmount = balanceEntity.getUsableAmount();
        //兑换暴击值
        String amount = String.valueOf(starlightExchangeParams.getAmount());
        BigDecimal withdrawMoney = new BigDecimal(amount).divide(new BigDecimal(HUNDRED));
        if (usableAmount.compareTo(withdrawMoney) < 0) {
            logger.error("convertStarlightToGCoin >> Exception : " + BizExceptionEnum.STAR_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STAR_ACCOUNT_BALANCE_NOT_ENOUGH);
        }

        // 生成兑换记录ID
        String orderId = String.valueOf(snowFlake.nextId());

        // 先入库，入Redis
        WithdrawOrder withdrawOrder = new WithdrawOrder();
        withdrawOrder.setOrderType(OrderTypeEnum.STAR_EXCHANGE_ORDER.getCode());
        withdrawOrder.setOrderId(orderId);
        withdrawOrder.setAmount(withdrawMoney);
        withdrawOrder.setStarAmount(withdrawMoney);
        withdrawOrder.setFee(new BigDecimal("0"));
        withdrawOrder.setUserId(userId);
        withdrawOrder.setChannel(PayChannelEnum.PLATFORM_SYSTEM.getValue());
        withdrawOrder.setState(WithdrawStatusType.CREATE.getCode());
        withdrawOrder.setSourceId(starlightExchangeParams.getSourceId());

        withdrawOrder.setBody(STARLIGHT_EXCHANGE_SUBJECT);
        withdrawOrder.setDescription(STARLIGHT_EXCHANGE_SUBJECT);
        withdrawOrder.setSubject(STARLIGHT_EXCHANGE_SUBJECT);
        withdrawOrder.setMoneyType(CURRENCY_TYPE);
        // app发起，不需要回调
        withdrawOrder.setIsNotify(WithdrawNotifyEnum.NOE_NEED.getCode());

        //将订单信息保存至数据库
        withdrawOrderRepository.save(withdrawOrder);

        String key = RedisKeyType.EXCHANGE.getCode() + ":" + userId + ":" + orderId;
        //将订单信息存于redis中，过期时间为24小时
        cacheManager.set(key, withdrawOrder, 24 * 60 * 60);

        // 冻结兑换暴击值
        BigDecimal frozenMoney = balanceEntity.getFrozenAmount().add(withdrawMoney);
        // 冻结 提现暴击值
        balanceEntity.setFrozenAmount(frozenMoney);
        BigDecimal leftAmount = balanceEntity.getBalance().subtract(withdrawMoney);
        balanceEntity.setUsableAmount(leftAmount);
        // 总的也对应减掉
        balanceEntity.setBalance(leftAmount);
        starlightBillRepository.save(balanceEntity);

        response.put("order_id", orderId);
        response.put("usable_amount", leftAmount.toString());
        logger.debug("convertStarlightToGCoin 出参 >> reponse = {} " + response.toString());

        ExchangeUpdateMessageVO messageVO = new ExchangeUpdateMessageVO();
        messageVO.setUserId(userId);
        messageVO.setOrderId(orderId);
        //发送至MQ
        try {
            Message message = MessageBuilder.of(messageVO).topic(RocketMQConstant.PAYMENT_TOPIC)
                    .tag(RocketMQConstant.STARLIGHT_EXCHANGE_TAG).build();
            message.setTransactionId(genTransactionId(orderId));
            logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} ",
                    RocketMQConstant.PAYMENT_TOPIC, RocketMQConstant.STARLIGHT_EXCHANGE_TAG,
                    message);

            exchangeTransactionProducer.sendMessageInTransaction(message, messageVO);

        } catch (Exception e) {
            logger.error("convertStarlightToGCoin >> exception :{} ", e.getMessage());
            throw new BusinessException(BizExceptionEnum.ROCKETMQ_PRODUCER_ERROR);
        }

        return response;
    }

    @Override
    public WithdrawOrder getWithdrawStateInfoWithAllParams(String outTradeNo, String orderId,
                                                           String userId) {
        logger.debug("getWithdrawStateInfoWithAllParams 入参 >> outTradeNo={}, orderId={},userId={} >> ", outTradeNo, orderId, userId);
        WithdrawOrder withdrawOrder = null;
        if (StringUtils.isNotEmpty(outTradeNo)) {
            withdrawOrder = withdrawOrderRepository.findOneByOutTradeNo(outTradeNo);
        } else {
            withdrawOrder = this.getWithdrawStateInfo(orderId, userId);
        }
        return withdrawOrder;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void convertScoreToStarlight(String userId, int amount, String outTradeNo) {
        logger.debug("convertScoreToStarlight 入参 >> userId = {},amount={},outTradeNo={}", userId, amount, outTradeNo);

        StarlightBalance balanceEntity = starlightBillRepository.findOneByUserId(userId);

        if (null == balanceEntity) {
            logger.error("convertScoreToStarlight >> Exception : " + BizExceptionEnum.STARACCOUNT_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STARACCOUNT_NOT_EXIST);
        }

        try {
            // 更新余额
            //提现暴击值
            BigDecimal withdrawMoney = new BigDecimal(amount).divide(new BigDecimal(HUNDRED));
            BigDecimal usableAmountAfter = balanceEntity.getUsableAmount().add(withdrawMoney);
            BigDecimal balanceAfter = balanceEntity.getBalance().add(withdrawMoney);
            balanceEntity.setUsableAmount(usableAmountAfter);
            balanceEntity.setBalance(balanceAfter);
            starlightBillRepository.save(balanceEntity);

            String orderId = String.valueOf(snowFlake.nextId());
            // 生成订单
            WithdrawOrder withdrawOrder = new WithdrawOrder();
            withdrawOrder.setAmount(withdrawMoney);
            withdrawOrder.setBody(SCORE_EXCHANGE_SUBJECT);
            withdrawOrder.setSubject(SCORE_EXCHANGE_SUBJECT);
            withdrawOrder.setChannel(PayChannelEnum.PLATFORM_SYSTEM.getValue());
            withdrawOrder.setFee(new BigDecimal("0"));
            withdrawOrder.setIsNotify(WithdrawNotifyEnum.NOE_NEED.getCode());
            withdrawOrder.setMoneyType(CURRENCY_TYPE);
            withdrawOrder.setOrderId(orderId);
            withdrawOrder.setOrderType(OrderTypeEnum.SCORE_EXCHANGE_ORDER.getCode());
            withdrawOrder.setSourceId(SourceType.PLATFORM.getCode());
            withdrawOrder.setStarAmount(withdrawMoney);
            withdrawOrder.setState(WithdrawStatusType.SUCCESS.getCode());
            withdrawOrder.setUserId(userId);
            // 珍林提供
            withdrawOrder.setOutTradeNo(outTradeNo);
            withdrawOrderRepository.save(withdrawOrder);

            // 生成流水
            withdrawOrder.setBalance(balanceEntity.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
            paymentBillService.saveRecord(withdrawOrder);
        } catch (BusinessException e) {
            logger.error("convertScoreToStarlight >> exception :{} ", e.getMessage());
            throw new BusinessException(e.getErrCode(), e.getErrMsg());
        }

    }

    @Override
    public WithdrawOrder getExchangeOrderInfo(String orderId, String userId) {
        logger.debug("getExchangeOrderInfo 入参 >> orderId={},userId={} >> ", orderId, userId);
        String key = RedisKeyType.EXCHANGE.getCode() + ":" + userId + ":" + orderId;
        WithdrawOrder withdrawOrder = cacheManager.get(key, WithdrawOrder.class);
        if (withdrawOrder == null) {
            withdrawOrder = withdrawOrderRepository.findOneByOrderIdAndUserId(orderId, userId);
        }
        logger.debug("getExchangeOrderInfo 出参 >> " + withdrawOrder);
        return withdrawOrder;
    }

    @Override
    public WithdrawConfigVo getWithdrawConfigVo() {
        WithdrawConfigVo vo = new WithdrawConfigVo();
        WithdrawConfig config = this.getWithdrawConfig();

        BeanUtils.copyProperties(config, vo);
        return vo;
    }

    private WithdrawConfig getWithdrawConfig() {
        WithdrawConfig config = cacheManager.get(RedisKey.PAYMENT_WITHDRAW_CONFIG, WithdrawConfig.class);
        if (null == config) {
            config = withdrawConfigRepository.findOne(1L);
            cacheManager.set(RedisKey.PAYMENT_WITHDRAW_CONFIG, config, 24 * 60 * 60);
        }
        return  config;
    }

    @Override
    public void updateWithdrawConfig(WithdrawConfigVo configVo) {
        logger.debug(">> 更新提现配置 >> 入参为 {}", configVo.toString());
        WithdrawConfig config = this.getWithdrawConfig();
        config.setState(configVo.getState() == null ? config.getState() : configVo.getState());
        config.setWithdrawLimit(configVo.getWithdrawLimit() == null ? config.getWithdrawLimit() : configVo.getWithdrawLimit());
        config.setWithdrawMin(config.getWithdrawMin() == null ? config.getWithdrawMin() : configVo.getWithdrawMin());
        config.setWithdrawMax(config.getWithdrawMax() == null ? config.getWithdrawMax() : configVo.getWithdrawMax());
        withdrawConfigRepository.save(config);

        cacheManager.set(RedisKey.PAYMENT_WITHDRAW_CONFIG, configVo, 24 * 60 * 60);
    }

    @Override
    public Map<String, String> createWithdrawAuditOrder(WithdrawAuditParams auditParams) {
        Map<String, String> auditMap = new HashMap<>();
        String uid = auditParams.getUid();
        Integer amount = auditParams.getAmount();
        Integer transferType = auditParams.getTransferType();
        String channel = WithdrawChannelEnum.lookup(transferType).getValue();
        String subject = WithdrawChannelEnum.lookup(transferType).getName();

        // TODO 用户实名认证
        // TODO 用户身份验证(黑名单|工作室暴鸡)
        // TODO 用户是否存在进行中订单
        // TODO 用户账号绑定列表

        // 提现配置相关参数是否满足
        WithdrawConfig withdrawConfig = this.getWithdrawConfig();
        String state = withdrawConfig.getState();
        int withdrawLimit = withdrawConfig.getWithdrawLimit();
        int withdrawMin = withdrawConfig.getWithdrawMin();
        int withdrawMax = withdrawConfig.getWithdrawMax();

        if (DeductRatioStatus.DISABLE.toString().equalsIgnoreCase(state)) {
            logger.error("createWithdrawAuditOrder >> Exception : " + BizExceptionEnum.WITHDRAW_FUNCTION_CLOSED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_FUNCTION_CLOSED);
        }

        List<String> verifyStateList = new ArrayList<>();
        verifyStateList.add(WithdrawVerifyEnum.WAIT.getValue());
        verifyStateList.add(WithdrawVerifyEnum.SUCCESS.getValue());
        verifyStateList.add(WithdrawVerifyEnum.VERIFY_SUCCESS.getValue());
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        Date queryStartDate = DateUtil.localDateTime2Date(startDate);
        Date queryEndDate = DateUtil.localDateTime2Date(LocalDateTime.now());
        Long usedTimes = auditRecordRepository.countByUidAndCreateDateBetweenAndVerifyStateIn(uid,
                queryStartDate, queryEndDate, verifyStateList);
        if (usedTimes.intValue() >= withdrawLimit) {
            logger.error(">> createWithdrawAuditOrder 每日提现次数超限，已提现次数:{}，限制次数:{}", usedTimes, withdrawLimit);
            throw new BusinessException(BizExceptionEnum.WITHDRAW_OVER_LIMIT, String.valueOf(withdrawLimit));
        }

        if (amount < withdrawMin) {
            logger.error(">> createWithdrawAuditOrder 提现金额{}不在范围【{}，{}】之内，", amount, withdrawMin, withdrawMax);
            throw new BusinessException(BizExceptionEnum.WITHDRAW_AMOUNT_MIN, String.valueOf(amount/100));
        }
        if (amount > withdrawMax) {
            logger.error(">> createWithdrawAuditOrder 提现金额{}不在范围【{}，{}】之内，", amount, withdrawMin, withdrawMax);
            throw new BusinessException(BizExceptionEnum.WITHDRAW_AMOUNT_MAX, String.valueOf(amount/100));
        }

        String outTradeNo = String.valueOf(snowFlake.nextId());

        // 接着判断账户是否存在|余额是否足够
        WithdrawCreateParams withdrawCreateParams = new WithdrawCreateParams();
        withdrawCreateParams.setUserId(uid);
        withdrawCreateParams.setAmount(amount);
        withdrawCreateParams.setChannel(channel);
        withdrawCreateParams.setSourceId(auditParams.getAppId());
        withdrawCreateParams.setOutTradeNo(outTradeNo);
        withdrawCreateParams.setSubject(subject);
        withdrawCreateParams.setBody(subject);
        withdrawCreateParams.setNotifyFlag(WithdrawNotifyEnum.NEED.getCode());
        withdrawCreateParams.setClientIp(auditParams.getClientIp());
        withdrawCreateParams.setDescription("");
        auditMap = this.checkAndCreateWithdrawOrder(withdrawCreateParams);

        return auditMap;
    }

    @Override
    public PageInfo<WithdrawAuditListVo> getAuditListByApp(String uid, String page, String size) {
        PageInfo<WithdrawAuditListVo> listVo = new PageInfo<>();
        PageParams pageParams = new PageParams();
        pageParams.setPage(page);
        pageParams.setSize(size);

        Long count = auditRecordRepository.countByUid(uid);
        // 记录为空，不执行查询操作
        listVo.setTotal(count);
        if (1 > count) {
            listVo.setList(new ArrayList<>());
            return listVo;
        }

        PageRequest pageRequest = PageUtils.getPageRequest(pageParams);
        List<WithdrawAuditRecord> recordList = auditRecordRepository.findByUid(uid, pageRequest);
        if (CollectionUtils.isNotEmpty(recordList)) {
            List<WithdrawAuditListVo> list = new ArrayList<>();
            for (WithdrawAuditRecord record : recordList) {
                WithdrawAuditListVo vo = new WithdrawAuditListVo();
                vo.setId(record.getId());
                vo.setUid(record.getUid());
                vo.setVerifyState(WithdrawVerifyEnum.lookup(record.getVerifyState()).getName());
                vo.setTotalAmount(new BigDecimal(record.getTotalFee()).divide(new BigDecimal(HUNDRED)));
                vo.setCreateDate(DateUtil.fromDate2Str(record.getCreateDate()));

                list.add(vo);
            }
            listVo.setList(list);
        }

        return listVo;
    }

    @Override
    public PageInfo<WithdrawAuditListVo> getAuditListByBackend(WithdrawAuditListVo queryVo, String page, String size) {
        PageInfo<WithdrawAuditListVo> listVo = new PageInfo<>();
        PageParams pageParams = new PageParams();
        pageParams.setPage(page);
        pageParams.setSize(size);

        PageRequest pageRequest = PageUtils.getPageRequest(pageParams);

        Specification<WithdrawAuditRecord> specification = this.getSpecipicationInfo(queryVo);

        //查询列表
        Page<WithdrawAuditRecord> respPage = auditRecordRepository.findAll(specification, pageRequest);
        long total = respPage.getTotalElements();
        listVo.setTotal(total);

        List<WithdrawAuditListVo> list = new ArrayList<>();
        if (total > 0) {
            List<WithdrawAuditRecord> recordList = respPage.getContent();
            for (WithdrawAuditRecord record : recordList) {
                WithdrawAuditListVo vo = new WithdrawAuditListVo();
                vo.setId(record.getId());
                vo.setUid(record.getUid());
                vo.setTotalFee(record.getTotalFee());
                vo.setVerifyState(WithdrawVerifyEnum.lookup(record.getVerifyState()).getName());
                vo.setRemark(record.getRemark());
                vo.setOrderId(record.getOrderId());
                vo.setChannel(record.getChannel());
                vo.setCreateDate(DateUtil.fromDate2Str(record.getCreateDate()));
                vo.setLastModifiedDate(record.getLastModifiedDate() == null ? "" : DateUtil.fromDate2Str(record.getLastModifiedDate()));
                vo.setFinishDate(record.getFinishDate() == null ? "" : DateUtil.fromDate2Str(record.getFinishDate()));
                vo.setBlockState(WithdrawBlockEnum.lookup(record.getBlockState()).getName());

                list.add(vo);
            }
        }
        listVo.setList(list);
        return listVo;
    }

    /**
     * 拼装查询条件
     * @param queryVo
     * @return
     */
    public Specification<WithdrawAuditRecord> getSpecipicationInfo(WithdrawAuditListVo queryVo) {

        String uid = queryVo.getUid();
        String verifyState = queryVo.getVerifyState();
        String orderId = queryVo.getOrderId();
        String blockState = queryVo.getBlockState();

        Specification<WithdrawAuditRecord> querySpecifiction = new Specification<WithdrawAuditRecord>() {
            @Override
            public Predicate toPredicate(Root<WithdrawAuditRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (ObjectTools.isNotEmpty(uid)) {
                    predicates.add(criteriaBuilder.equal(root.get("uid").as(String.class), uid));
                }

                if (ObjectTools.isNotEmpty(verifyState)) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("verifyState").as(String.class), verifyState));
                }

                if (ObjectTools.isNotEmpty(orderId)) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("orderId").as(String.class), orderId));
                }

                if (ObjectTools.isNotEmpty(blockState)) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("blockState").as(String.class), blockState));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return querySpecifiction;
    }

    @Override
    public void updateAuditState(WithdrawAuditListVo queryVo) {
        Long id = queryVo.getId();

        WithdrawAuditRecord record = auditRecordRepository.findOne(id);
        if(null == record) {
            logger.error("updateAuditState >> Exception : " + BizExceptionEnum.WITHDRAW_AUDIT_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_AUDIT_NOT_EXIST);
        }
        String verifyState = record.getVerifyState();
        if (!WithdrawVerifyEnum.WAIT.getValue().equals(verifyState)) {
            logger.error("updateAuditState >> Exception : " + BizExceptionEnum.WITHDRAW_AUDIT_STATE_NOT_WAIT.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_AUDIT_STATE_NOT_WAIT);
        }
        String blockState = record.getBlockState();
        if (!WithdrawBlockEnum.NOT_YET.getValue().equals(blockState)) {
            logger.error("updateAuditState >> Exception : " + BizExceptionEnum.WITHDRAW_BLOCK_STATE_NOT_YET.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_BLOCK_STATE_NOT_YET);
        }
        // 前端审批状态
        String verifyStatus = queryVo.getVerifyState();
        String orderId = record.getOrderId();
        WithdrawVerifyEnum verifyEnum = WithdrawVerifyEnum.lookup(verifyStatus);
        String remitStatus = "";
        WithdrawTaxRecord taxRecord = null;
        switch (verifyEnum) {
            // 审批拒绝-余额变更
            case VERIFY_FAIL:
                record.setVerifyState(WithdrawVerifyEnum.VERIFY_FAIL.getValue());
                auditRecordRepository.save(record);

                // 异步处理逻辑
                remitStatus = WithdrawResultType.FAIL.getCode();
                doWithdrawStatusUpdateAysnc(orderId, remitStatus);
            // 云账户提现失败
            case FAIL:
                record.setVerifyState(WithdrawVerifyEnum.FAIL.getValue());
                auditRecordRepository.save(record);

                // 扣税记录状态变更
                taxRecord = taxRecordRepository.findByOrderId(orderId);
                taxRecord.setState(ExternalWithdrawStateEnum.FAILED.getValue());
                taxRecordRepository.save(taxRecord);

                // 异步处理逻辑
                remitStatus = WithdrawResultType.FAIL.getCode();
                doWithdrawStatusUpdateAysnc(orderId, remitStatus);
            // 审批通过-生成扣税记录，调起云账户支付
            case VERIFY_SUCCESS:
                String uid = record.getUid();
                Integer totalFee = record.getTotalFee();

                record.setVerifyState(WithdrawVerifyEnum.VERIFY_SUCCESS.getValue());
                auditRecordRepository.save(record);

                DeductRatioSetting taxSetting = deductRatioService.queryWithdrawTaxRatio();
                Float tax = taxSetting.getRatio();
                Integer taxFee = (new BigDecimal(totalFee).multiply(new BigDecimal(String.valueOf(tax)))).intValue();
                Integer incomeFee = totalFee - taxFee; // 税后金额
                // 生成扣税记录
                taxRecord = new WithdrawTaxRecord();
                taxRecord.setUserId(uid);
                taxRecord.setOrderId(orderId);
                taxRecord.setTotalFee(totalFee);
                taxRecord.setTaxFee(taxFee);
                taxRecord.setIncomeFee(incomeFee);
                taxRecord.setState(ExternalWithdrawStateEnum.PROCESSING.getValue());
                taxRecordRepository.save(taxRecord);

                // 异步发起云账户提现
                doCrateCloudPayOrder(record, incomeFee);
            // 云账户提现成功
            case SUCCESS:
                Date completedTime = DateUtil.str2Date(queryVo.getFinishDate(), DateUtil.FORMATTER);
                record.setVerifyState(WithdrawVerifyEnum.SUCCESS.getValue());
                record.setFinishDate(completedTime);
                auditRecordRepository.save(record);

                // 扣税记录状态变更
                taxRecord = taxRecordRepository.findByOrderId(orderId);
                taxRecord.setState(ExternalWithdrawStateEnum.SUCCESS.getValue());
                taxRecordRepository.save(taxRecord);

                remitStatus = WithdrawResultType.SUCCESS.getCode();
                doWithdrawStatusUpdateAysnc(orderId, remitStatus);
            default:
                break;
        }

    }

    /**
     * 异步更新提现状态
     * @param orderId
     * @param remitStatus
     */
    @Async
    public void doWithdrawStatusUpdateAysnc(String orderId, String remitStatus) {
        WithdrawUpdateParams withdrawUpdateParams = new WithdrawUpdateParams();
        withdrawUpdateParams.setOutTradeNo(orderId);
        withdrawUpdateParams.setRemitStatus(remitStatus);
        this.updateWithdrawStatus(withdrawUpdateParams);
    }

    @Async
    public void doCrateCloudPayOrder(WithdrawAuditRecord record, Integer incomeFee) {
        CloudAccountOrderParams orderParams = new CloudAccountOrderParams();
        orderParams.setUserId(record.getUid());
        orderParams.setOutTradeNo(record.getOrderId());
        // 扣除税费之后的钱
        orderParams.setTotalFee(String.valueOf(incomeFee));
        String channel = record.getChannel();
        orderParams.setChannel(WithdrawChannelEnum.lookupByName(channel).getValue());

        // TODO 查询用户实名信息-获取用户的身份证号
        // TODO 根据渠道查询账户绑定关系，获得对应的openid或支付宝账号
        orderParams.setRealName("张三");
        orderParams.setIdCard("xxx");
        orderParams.setCardNo("xxx");

        String channelTag = PayChannelEnum.CLOUD_ACCOUNT_PAY.getValue();
        String ip = record.getClientIp();
        cloudAccountServiceImpl.createWithdrawOrder(orderParams, record.getSourceAppId(), channelTag, null, ip);
    }

    @Override
    public void updateBlockState(WithdrawAuditListVo queryVo) {
        Long id = queryVo.getId();

        WithdrawAuditRecord record = auditRecordRepository.findOne(id);
        if(null == record) {
            logger.error("updateBlockState >> Exception : " + BizExceptionEnum.WITHDRAW_AUDIT_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_AUDIT_NOT_EXIST);
        }
        String verifyState = record.getVerifyState();
        if (!WithdrawVerifyEnum.WAIT.getValue().equals(verifyState)) {
            logger.error("updateBlockState >> Exception : " + BizExceptionEnum.WITHDRAW_BLOCK_STATE_NOT_WAIT.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_BLOCK_STATE_NOT_WAIT);
        }
        // BLOCKING 截停
        //NOT_YET 撤销截停
        //TWICE 二次截停
        String blockState = record.getBlockState();
        String blockStateBrowser = queryVo.getBlockState();
        // 未截停只能截停
        if (WithdrawBlockEnum.NOT_YET.getValue().equals(blockState) &&
                !WithdrawBlockEnum.BLOCKING.getValue().equals(blockStateBrowser)) {
            logger.error("updateBlockState >> Exception : " + BizExceptionEnum.WITHDRAW_BLOCK_MIS_OPERATION.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_BLOCK_MIS_OPERATION);
        }
        // 截停中不能在截停
        if (WithdrawBlockEnum.BLOCKING.getValue().equals(blockState) &&
                WithdrawBlockEnum.BLOCKING.getValue().equals(blockStateBrowser)) {
            logger.error("updateBlockState >> Exception : " + BizExceptionEnum.WITHDRAW_BLOCK_MIS_OPERATION.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_BLOCK_MIS_OPERATION);
        }
        //二次截停只能撤销
        if (WithdrawBlockEnum.TWICE.getValue().equals(blockState) &&
                !WithdrawBlockEnum.NOT_YET.getValue().equals(blockStateBrowser)) {
            logger.error("updateBlockState >> Exception : " + BizExceptionEnum.WITHDRAW_BLOCK_MIS_OPERATION.getErrMsg());
            throw new BusinessException(BizExceptionEnum.WITHDRAW_BLOCK_MIS_OPERATION);
        }

        record.setBlockState(blockStateBrowser);
        auditRecordRepository.save(record);

    }

    /**
     * 根据订单号生成一个分布式事务 id
     */
    private String genTransactionId(String teamSequence) {
        String now = DateUtil.dateTime2Str(LocalDateTime.now(), DateUtil.SIMPLE_FORMATTER);
        return String.format(RocketMQConstant.PAYMENT_TOPIC, teamSequence, now);
    }


}