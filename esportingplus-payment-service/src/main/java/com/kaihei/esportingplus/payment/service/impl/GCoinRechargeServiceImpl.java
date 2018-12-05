package com.kaihei.esportingplus.payment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.exception.ApplePayException;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.payment.api.enums.*;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeCreateParams;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargePreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinBalanceRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinRechargeRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinBalance;
import com.kaihei.esportingplus.payment.domain.entity.GCoinRechargeOrder;
import com.kaihei.esportingplus.payment.event.ApplePayRiskInfoEvent;
import com.kaihei.esportingplus.payment.mq.message.MQSimpleOrder;
import com.kaihei.esportingplus.payment.mq.producer.GCoinRechargeTransactionProducer;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.GCoinRechargeService;
import com.kaihei.esportingplus.payment.util.AccountUtil;
import com.kaihei.esportingplus.payment.util.CommonUtils;
import com.kaihei.esportingplus.payment.util.ExternalPaymentUtil;
import com.kaihei.esportingplus.payment.util.IosVerifyUtils;
import com.maihaoche.starter.mq.base.MessageBuilder;
import org.apache.rocketmq.common.message.Message;
import org.hibernate.jpa.criteria.OrderImpl;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 暴鸡币充值相关Service
 *
 * @author xusisi
 * @create 2018-08-17 11:29
 **/
@Service
public class GCoinRechargeServiceImpl implements GCoinRechargeService {

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    private static final Logger logger = LoggerFactory.getLogger(GCoinRechargeServiceImpl.class);

    @Autowired
    private GCoinRechargeRepository rechargeRepository;

    @Autowired
    private GCoinBalanceRepository balanceRepository;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private GCoinRechargeTransactionProducer rechargeTransactionProducer;

    @Autowired
    private BillFlowService billFlowService;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${app.product.query}")
    private String productSearchUrl;

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplate;

    @Autowired
    private ExternalPaymentUtil externalPaymentUtil;

    /**
     * @Description: 创建充值订单
     * @Param: [paymentCreateParams]
     * @return: java.lang.String
     * @Author: xusisi
     * @Date: 2018/8/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GCoinRechargePreVo preCreatePayment(GCoinRechargeCreateParams rechargeCreateParams) throws BusinessException {

        logger.debug("入参 >> GCoinRechargeCreateParams : {}" + rechargeCreateParams);

        String flowNo = String.valueOf(snowFlake.nextId());
        String userId = rechargeCreateParams.getUserId();
        String sourceId = rechargeCreateParams.getSourceId();

        String tempAmount = rechargeCreateParams.getAmount();
        Integer intTempAmount = CommonUtils.strToInteger(tempAmount);

        //获取的金额单位是分，要转换成元为单位的数据
        BigDecimal amount = new BigDecimal(intTempAmount).divide(new BigDecimal(100));
        GCoinRechargeOrder recharge = new GCoinRechargeOrder();
        recharge.setOrderId(flowNo);
        recharge.setUserId(userId);
        recharge.setAmount(amount);
        //暴鸡币数量处理
        String tempGcoinAmount = rechargeCreateParams.getGcoinAmount();

        Integer intTempGcoinAmount = CommonUtils.strToInteger(tempGcoinAmount);

        BigDecimal gcoinAmount = new BigDecimal(intTempGcoinAmount).divide(new BigDecimal(100));

        recharge.setGcoinAmount(gcoinAmount);
        recharge.setSourceId(sourceId);
        //001（交易创建，等待买家付款）、002（未付款交易超时关闭，或支付完成后全额退款）、003（交易支付成功）、004（交易结束，不可退款）
        recharge.setState(GCoinRechargeStatusType.CREATE.getCode());
        recharge.setBody(rechargeCreateParams.getBody());
        recharge.setDescription(rechargeCreateParams.getDescription());
        recharge.setSubject(rechargeCreateParams.getSubject());
        //设置为前台用户主动充值类型
        recharge.setRechargeType(GCoinRechargeTypeEnum.USER_RECHARGE.getCode());
        //前台用户主动充值，该字段信息不需要记录
        recharge.setOrderType("----");
        recharge.setOutTradeNo("----");

        logger.info("recharge >> {}", recharge);
        //将订单信息保存至数据库
        rechargeRepository.save(recharge);

        String key = String.format(RedisKey.GCOIN_RECHARGE_KEY, userId, flowNo);
        logger.debug("saveToRedis >> key : {} ", key);

        //将订单信息存于redis中，过期时间为24小时
        cacheManager.set(key, recharge, 24 * 60 * 60);

        GCoinRechargePreVo vo = new GCoinRechargePreVo();
        vo.setOrderId(flowNo);

        logger.debug("出参 >> GCoinRechargePreVo : {}", vo);
        return vo;

    }

    /**
     * @Description: 将充值成功的订单信息存入消息队列
     * @Param: [orderId, paymentUpdateParams]
     * @return: java.lang.String
     * @Author: xusisi
     * @Date: 2018/8/18
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GCoinRechargeVo updateOrderInfo(String orderId, GCoinRechargeUpdateParams rechargeUpdateParams) throws BusinessException {
        logger.debug("updateOrderInfo >> start >> rechargeUpdateParams : {} ", rechargeUpdateParams);

        String channel = rechargeUpdateParams.getChannel();
        String userId = rechargeUpdateParams.getUserId();

        //校验订单状态
        GCoinRechargeOrder recharge = this.checkOrderState(orderId, userId);
        logger.debug(" recharge :{} ", recharge);

        //如果是apple支付需要查看是否有票据信息,是否有设备ID，是否有货币类型
        if (PayChannelEnum.APPLE_PAY.getValue().equals(channel)) {
            String receiptData = rechargeUpdateParams.getReceiptData();

            if (receiptData == null || "".equals(String.valueOf(receiptData).trim())) {
                logger.error("updateOrderInfo >> Exception : {} ", BizExceptionEnum.GCOIN_RECHARGE_APPLEPAY_RECIPT_NOT_EXIST.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_APPLEPAY_RECIPT_NOT_EXIST);
            }

            String deviceId = rechargeUpdateParams.getDeviceId();
            if (deviceId == null || "".equals(deviceId.trim())) {
                logger.error("updateOrderInfo >> Exception : {} ", BizExceptionEnum.GCOIN_RECHARGE_APPLEPAY_DEVICEID_NOT_EXIST.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_APPLEPAY_DEVICEID_NOT_EXIST);
            }

            String currencyType = rechargeUpdateParams.getCurrencyType();
            if (currencyType == null || "".equals(currencyType.trim())) {
                logger.error("updateOrderInfo >> Exception : {} ", BizExceptionEnum.GCOIN_RECHARGE_APPLEPAY_CURRENCYTYPE_NOT_EXIST.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_APPLEPAY_CURRENCYTYPE_NOT_EXIST);
            }

        }

        //将订单ID-notifyUrl发送至MQ
        try {

            MQSimpleOrder mqSimpleOrder = new MQSimpleOrder();
            String notifyUrl = rechargeUpdateParams.getNotifyUrl();
            mqSimpleOrder.setNotifyUrl(notifyUrl);
            mqSimpleOrder.setOrderId(orderId);
            mqSimpleOrder.setUserId(userId);

            Message message = MessageBuilder.of(mqSimpleOrder).topic(RocketMQConstant.PAYMENT_TOPIC).tag(RocketMQConstant.GCOIN_RECHARGE_TAG).build();

            message.setTransactionId(CommonUtils.genTransactionId(RocketMQConstant.PAYMENT_TRANSACTION_ID, orderId));

            logger.debug("sendMessage >> start >> topic :{} , tag :{} , message : {} ,rechargeUpdateParams :{}",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.GCOIN_RECHARGE_TAG,
                    message,
                    rechargeUpdateParams);

            rechargeTransactionProducer.sendMessageInTransaction(message, rechargeUpdateParams);

            logger.debug("sendMessage >> end >> topic :{} , tag :{} , message : {} , rechargeUpdateParams : {} ",
                    RocketMQConstant.PAYMENT_TOPIC,
                    RocketMQConstant.GCOIN_RECHARGE_TAG,
                    message,
                    rechargeUpdateParams);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception :{} ", e.getMessage());
            throw new BusinessException(BizExceptionEnum.ROCKETMQ_PRODUCER_ERROR);
        }
        GCoinRechargeVo vo = this.getRechargeVo(orderId, userId);
        logger.debug("出参 >> GcoinRechargeVo : {} ", vo);
        return vo;
    }

    /***
     * 从redis获取充值订单详情
     * @param orderId
     * @param userId
     * @return
     */
    @Override
    public GCoinRechargeOrder getOrderInfoFromRedis(String orderId, String userId) {
        logger.debug("入参 >> order : {} ,userId : {}", orderId, userId);

        String key = String.format(RedisKey.GCOIN_RECHARGE_KEY, userId, orderId);

        logger.debug("key : {}", key);

        GCoinRechargeOrder recharge = cacheManager.get(key, GCoinRechargeOrder.class);

        logger.debug("出参 >> recharge : {}", recharge);

        return recharge;
    }

    /**
     * 获取充值订单信息详情
     *
     * @param orderId
     * @param userId
     * @return
     */
    @Override
    public GCoinRechargeOrder getRechargeOrder(String orderId, String userId) {
        logger.debug("入参 >> orderId : {} ,userId : {} ", orderId, userId);
        GCoinRechargeOrder recharge = this.getOrderInfoFromRedis(orderId, userId);
        if (recharge == null) {
            recharge = rechargeRepository.findOneByOrderIdAndUserId(orderId, userId);
        }

        logger.debug("出参 >> recharge : {} ", recharge);
        return recharge;
    }

    /***
     * 用于返回给PYTHON的VO数据
     * @param orderId
     * @param userId
     * @return
     */
    @Override
    public GCoinRechargeVo getRechargeVo(String orderId, String userId) {
        logger.debug("getOrderInfo >> start >> orderId :{} , userId :{} ", orderId, userId);

        GCoinRechargeOrder recharge = this.getRechargeOrder(orderId, userId);

        logger.debug("getOrderInfo >> end >> recharge : {} ", recharge);

        if (recharge == null) {
            return null;
        }
        GCoinRechargeVo vo = this.entityTransformVo(recharge);
        logger.debug("getRechargeVo >> vo : {}", vo);
        return vo;
    }

    /**
     * @Description: 封装一个VO对象用于返回给Python
     * @Param: [recharge]
     * @return: com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo
     * @Author: xusisi
     * @Date: 2018/10/19
     */
    @Override
    public GCoinRechargeVo entityTransformVo(GCoinRechargeOrder recharge) {

        GCoinRechargeVo vo = new GCoinRechargeVo();
        vo.setOrderId(recharge.getOrderId());
        vo.setSubject(recharge.getSubject());
        vo.setBody(recharge.getBody());
        //vo.setDescription(recharge.getDescription());
        vo.setUserId(recharge.getUserId());
        vo.setAmount(recharge.getAmount().multiply(new BigDecimal(100)));
        vo.setGcoinAmount(recharge.getGcoinAmount().multiply(new BigDecimal(100)));
        vo.setChannel(recharge.getChannel());
        vo.setSourceId(recharge.getSourceId());
        vo.setPaymentOrderNo(recharge.getPaymentOrderNo());
        vo.setPaymentDate(recharge.getPaymentDate());
        vo.setState(recharge.getState());
        vo.setDeviceId(recharge.getDeviceId());
        vo.setCurrencyType(recharge.getCurrencyType());

        vo.setRechargeType(recharge.getRechargeType());
        vo.setOutTradeNo(recharge.getOutTradeNo());
        vo.setOrderType(recharge.getOrderType());

        vo.setRemarks(recharge.getRemarks());

        return vo;
    }

    /**
     * @Description: 因为超时未支付，所以充值订单状态被更新为关闭状态。
     * @Param: [orderId, userId]
     * @return: void
     * @Author: xusisi
     * @Date: 2018/8/28
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshOrderInfo(String orderId, String userId) {
        logger.debug("refreshOrderInfo >> start >> orderId : {}, userId : {} ", orderId, userId);

        GCoinRechargeOrder recharge = rechargeRepository.findOneByOrderIdAndUserId(orderId, userId);

        logger.debug("refreshOrderInfo >> payment {} :", recharge);

        if (recharge == null) {
            logger.warn("refreshOrderInfo >> error : {} ", BizExceptionEnum.GCOIN_RECHARGE_ORDER_NOT_EXIST.getErrMsg());
            //throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_ORDER_NOT_EXIST);
            return;
        }

        String state = recharge.getState();
        //充值订单状态为除了创建以外的其他状态时都不做处理
        if (!GCoinRechargeStatusType.CREATE.getCode().equals(state)) {
            logger.debug("refreshOrderInfo >> end >> orderId : {} ,该订单已经处理完毕，无需再刷新状态", orderId);
            return;
        }
        //充值订单状态为创建，则重置为关闭状态
        recharge.setState(GCoinRechargeStatusType.CLOSE.getCode());

        rechargeRepository.save(recharge);

        logger.debug("refreshOrderInfo >> end >> orderId : {} 该订单因超时未处理，状态变更为充值失败。", orderId);
    }

    /***
     * 将充值订单状态更新为成功，更新用户暴鸡币余额，增加一条流水记录
     *
     * @param orderId
     * @throws BusinessException
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRechargeInfo(String orderId) throws BusinessException {
        GCoinRechargeOrder gcoinRecharge = rechargeRepository.findOneByOrderId(orderId);
        String channel = gcoinRecharge.getChannel();

        gcoinRecharge.setState(GCoinRechargeStatusType.SUCCESS.getCode());
        gcoinRecharge.setLastModifiedDate(new Date());

        String userId = gcoinRecharge.getUserId();

        //先清缓存，然后保存数据库，数据库保存成功再添加到缓存中
        String key = String.format(RedisKey.GCOIN_RECHARGE_KEY, userId, orderId);
        cacheManager.del(key);

        //更新提现订单
        rechargeRepository.save(gcoinRecharge);

        //将最新的payment信息保存进redis
        //订单成功以后保存30分钟
        cacheManager.set(key, gcoinRecharge, 30 * 60);

        //更新用暴鸡币余额表
        GCoinBalance gCoinBalance = balanceRepository.findOneByUserId(userId);

        if (gCoinBalance == null) {
            gCoinBalance = AccountUtil.generateGcoinBalance(userId);
        }

        //暴鸡币账户不可用
        if (AccountStateType.UNAVAILABLE.getCode().equals(gCoinBalance.getState())) {
            logger.error("executePayment >> Exception " + BizExceptionEnum.GCOIN_ACCOUNT_UNAVAILABLE.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_UNAVAILABLE);
        }

        //暴鸡币账户被冻结
        if (AccountStateType.FROZEN.getCode().equals(gCoinBalance.getState())) {
            logger.error("executePayment >> Exception " + BizExceptionEnum.GCOIN_ACCOUNT_FROZEN.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_FROZEN);
        }

        //先获取用户原来的总暴鸡币余额
        BigDecimal oldTotalGcoinBalance = gCoinBalance.getGcoinBalance();
        //原来的可用暴鸡币金额
        BigDecimal oldUsableGcoinBalance = gCoinBalance.getUsableAmount();
        //本次充值的暴鸡币数量
        BigDecimal paymentGcoin = gcoinRecharge.getGcoinAmount();
        //最新余额
        BigDecimal newTotalGcoinBalance = paymentGcoin.add(oldTotalGcoinBalance);
        BigDecimal newUsableGcoinBalance = paymentGcoin.add(oldUsableGcoinBalance);
        gCoinBalance.setGcoinBalance(newTotalGcoinBalance);
        gCoinBalance.setUsableAmount(newUsableGcoinBalance);

        //更新用户暴鸡币余额信息
        balanceRepository.save(gCoinBalance);

        gcoinRecharge.setBalance(gCoinBalance.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
        //增加一条暴鸡币充值流水
        billFlowService.saveRecord(gcoinRecharge);

        //如果支付方式是apple支付，则需要将信息发送至RISK的相关MQ
        if (PayChannelEnum.APPLE_PAY.getValue().equals(channel)) {
            ApplePayRiskInfoEvent event = new ApplePayRiskInfoEvent();
            event.setCurrencyType(gcoinRecharge.getCurrencyType());
            event.setDeviceNo(gcoinRecharge.getDeviceId());
            event.setUid(gcoinRecharge.getUserId());
            event.setSourceId(gcoinRecharge.getSourceId());
            event.setAmount(gcoinRecharge.getAmount().multiply(new BigDecimal(100)).intValue());
            try {
                logger.debug("sendEvent >> start >> event :{} ", event);
                EventBus.post(event);
                logger.debug("sendEvent >> end >> event :{} ", event);

            } catch (Exception e) {
                logger.error("用户apple支付，发送给风控时出现异常");
            }

        }

    }

    @Override
    public GCoinRechargeOrder checkOrderState(String orderId, String userId) throws BusinessException {

        GCoinRechargeOrder gcoinRecharge = this.getRechargeOrder(orderId, userId);

        if (gcoinRecharge == null) {
            logger.error("exception : {} ", BizExceptionEnum.GCOIN_RECHARGE_ORDER_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_ORDER_NOT_EXIST);
        }

        if (GCoinRechargeStatusType.FINISH.getCode().equals(gcoinRecharge.getState())) {
            logger.error("exception >> {} ", BizExceptionEnum.GCOIN_RECHARGE_ORDER_CLOSED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_ORDER_CLOSED);
        }

        if (!GCoinRechargeStatusType.CREATE.getCode().equals(gcoinRecharge.getState())) {
            logger.error("exception >> {} ", BizExceptionEnum.GCOIN_RECHARGE_ORDER_FINISHED.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_ORDER_FINISHED);
        }

        return gcoinRecharge;

    }

    @Override
    public GCoinRechargeOrder checkOrderState(String orderId) throws BusinessException {
        GCoinRechargeOrder gcoinRecharge = rechargeRepository.findOneByOrderId(orderId);

        if (gcoinRecharge == null) {
            logger.error("exception : {} ", BizExceptionEnum.GCOIN_RECHARGE_ORDER_NOT_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_ORDER_NOT_EXIST);
        }

        String userId = gcoinRecharge.getUserId();
        gcoinRecharge = this.checkOrderState(orderId, userId);

        return gcoinRecharge;
    }

    /**
     * @Description: 动态拼接查询条件
     * @Param: [userId, channel, sourceId, beginDate, endDate, rechargeType, state]
     * @return: org.springframework.data.jpa.domain.Specification<com.kaihei.esportingplus.payment.domain.entity.GCoinRechargeOrder>
     * @Author: xusisi
     * @Date: 2018/10/22
     */
    public Specification<GCoinRechargeOrder> getSpecipicationInfo(String userId, String channel, String sourceId, String beginDate, String endDate,
                                                                  String rechargeType) {

        Specification<GCoinRechargeOrder> querySpecifiction = new Specification<GCoinRechargeOrder>() {
            @Override
            public Predicate toPredicate(Root<GCoinRechargeOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (ObjectTools.isNotEmpty(userId)) {
                    predicates.add(criteriaBuilder.equal(root.get("userId").as(String.class), userId));
                }

                if (ObjectTools.isNotEmpty(channel)) {
                    predicates.add(criteriaBuilder.equal(root.get("channel").as(String.class), channel));
                }

                if (ObjectTools.isNotEmpty(sourceId)) {
                    predicates.add(criteriaBuilder.equal(root.get("sourceId").as(String.class), sourceId));
                }

                //展示全部成功的订单
                List<String> stateList = new ArrayList<String>();

                stateList.add(GCoinRechargeStatusType.SUCCESS.getCode());
                stateList.add(GCoinRechargeStatusType.REFUND_ALL.getCode());
                stateList.add(GCoinRechargeStatusType.FINISH.getCode());

                Expression<String> exp = root.get("state");
                Predicate statePredicate = exp.in(stateList);
                predicates.add(statePredicate);

                if (ObjectTools.isNotEmpty(beginDate) && ObjectTools.isNotEmpty(endDate)) {
                    predicates.add(criteriaBuilder.between(root.get("paymentDate").as(String.class), beginDate, endDate));
                }

                predicates.add(criteriaBuilder.equal(root.get("rechargeType").as(String.class), rechargeType));

                //查询条件，支付时间倒序排列
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("paymentDate").as(String.class)));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return querySpecifiction;
    }

    @Override
    public Map<String, Object> findAllSuccessByCondition(String userId, String channel, String sourceId, String beginDate, String endDate,
                                                         String rechargeType, int page, int size) {
        Map<String, Object> result = new HashMap<>();
        List<GCoinRechargeOrder> resultList = null;

        Specification<GCoinRechargeOrder> querySpecifi = this.getSpecipicationInfo(userId, channel, sourceId, beginDate, endDate, rechargeType);
        //查询总记录数
        long totalRecords = rechargeRepository.count(querySpecifi);

        //有一个等于-1，则查询全部，否则使用默认的page=1,size=20。
        if (page != -1 && size != -1) {
            Pageable pageable = new PageRequest(page - 1, size);
            Page<GCoinRechargeOrder> pageInfo = rechargeRepository.findAll(querySpecifi, pageable);
            resultList = pageInfo.getContent();
        } else {
            resultList = rechargeRepository.findAll(querySpecifi);
        }
        result.put("list", resultList);
        result.put("counts", totalRecords);

        logger.debug("page : {}", resultList);
        logger.debug("counts : {}", totalRecords);
        return result;
    }

    @Override
    public Map<String, Integer> findAllSuccessSumInfoByCondition(String userId, String channel, String sourceId, String beginDate, String endDate,
                                                                 String rechargeType) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();

        Root<GCoinRechargeOrder> root = query.from(GCoinRechargeOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        if (ObjectTools.isNotEmpty(userId)) {
            predicates.add(criteriaBuilder.equal(root.get("userId").as(String.class), userId));
        }

        if (ObjectTools.isNotEmpty(channel)) {
            predicates.add(criteriaBuilder.equal(root.get("channel").as(String.class), channel));
        }

        if (ObjectTools.isNotEmpty(sourceId)) {
            predicates.add(criteriaBuilder.equal(root.get("sourceId").as(String.class), sourceId));
        }

        if (ObjectTools.isNotEmpty(beginDate) && ObjectTools.isNotEmpty(endDate)) {
            predicates.add(criteriaBuilder.between(root.get("paymentDate").as(String.class), beginDate, endDate));
        }

        //展示全部成功的订单
        List<String> stateList = new ArrayList<String>();

        stateList.add(GCoinRechargeStatusType.SUCCESS.getCode());
        stateList.add(GCoinRechargeStatusType.REFUND_ALL.getCode());
        stateList.add(GCoinRechargeStatusType.FINISH.getCode());

        Expression<String> exp = root.get("state");
        Predicate statePredicate = exp.in(stateList);
        predicates.add(statePredicate);

        predicates.add(criteriaBuilder.equal(root.get("rechargeType").as(String.class), rechargeType));

        Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);
        //查询条件，支付时间倒序排列
        Order paymentDateOrder = new OrderImpl(root.get("paymentDate"), false);

        query.where(predicatesArray);

        query.orderBy(paymentDateOrder);

        query.multiselect(criteriaBuilder.sum(root.get("amount")), criteriaBuilder.sum(root.get("gcoinAmount")),
                criteriaBuilder.countDistinct(root.get("userId")));

        TypedQuery<Tuple> q1 = entityManager.createQuery(query);

        List<Tuple> result1 = q1.getResultList();

        logger.debug("result1 : {} ", result1);

        Iterator it = result1.iterator();
        while (it.hasNext()) {
            logger.debug("t :{}", it.next());
        }
        Tuple tuple = result1.get(0);

        String amount = "0";

        String gcoinAmount = "0";

        String totalUsers = "0";

        if (ObjectTools.isNotEmpty(tuple.get(0))) {
            amount = tuple.get(0).toString();
        }

        if (ObjectTools.isNotEmpty(tuple.get(1))) {
            gcoinAmount = tuple.get(1).toString();
        }

        if (ObjectTools.isNotEmpty(tuple.get(2))) {
            totalUsers = tuple.get(2).toString();
        }

        logger.debug("amount : {} ,gcoinAmount :{} ,totalUsers:{}", amount, gcoinAmount, totalUsers);
        Map<String, Integer> map = new HashMap<>();
        map.put("totalAmount", new BigDecimal(amount).multiply(new BigDecimal(100)).intValue());
        map.put("totalGcoinAmount", new BigDecimal(gcoinAmount).multiply(new BigDecimal(100)).intValue());
        map.put("totalUser", Integer.valueOf(totalUsers));

        logger.debug("map >> {} ", map);
        return map;
    }

    /**
     * @Description: 消费暴鸡币充值消息
     * @Param: [mqSimpleOrder]
     * @Return java.lang.Boolean
     * @Author: xusisi
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean consumeRechargeMQ(MQSimpleOrder mqSimpleOrder) {

        //订单ID
        String orderId = mqSimpleOrder.getOrderId();
        //回调URL
        String notifyUrl = mqSimpleOrder.getNotifyUrl();
        ResponsePacket result = null;
        GCoinRechargeVo vo = new GCoinRechargeVo();
        GCoinRechargeOrder gcoinRecharge = null;

        //消息是否消费锁标识
        String consumerLock = String.format(RedisKey.GCOIN_RECHARGE_LOCK_KEY, orderId);
        //判断这个订单是否正在被消费
        RLock lock = cacheManager.redissonClient().getLock(consumerLock);
        if (lock.isLocked()) {
            logger.debug("GCoinRechargeConsumer >> mq 已经被消费 orderId : {} ", orderId);
            return true;
        }

        //mq标记
        Boolean flag = true;
        try {
            //设置锁失效时间为3秒
            lock.lock(3, TimeUnit.SECONDS);
            //校验订单状态
            gcoinRecharge = this.checkOrderState(orderId);

            //如果支付方式是apple支付需要去查对应的收据信息
            String userId = gcoinRecharge.getUserId();
            if (PayChannelEnum.APPLE_PAY.getValue().equals(gcoinRecharge.getChannel())) {
                String reciptData = gcoinRecharge.getDescription();

                Map<String, Object> objectMap = this.doIosRequest(reciptData, orderId, userId);
                Boolean appleFlag = Boolean.parseBoolean(objectMap.get("success").toString());
                if (!appleFlag) {
                    logger.error("GCoinRechargeConsumer >> Exception " + BizExceptionEnum.APPLE_PAY_FAILED.getErrMsg());
                    throw new BusinessException(BizExceptionEnum.APPLE_PAY_FAILED);
                }
                gcoinRecharge = (GCoinRechargeOrder) objectMap.get("message");
            }

            //更新订单状态、暴鸡币余额、增加流水记录
            this.updateRechargeInfo(orderId);
            vo = this.getRechargeVo(orderId, userId);
            result = ResponsePacket.onSuccess(vo);

        } catch (BusinessException e) {
            logger.error("GCoinRechargeConsumer >> BusinessException : " + e.getErrMsg());
            result = ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
            vo.setOrderId(orderId);
            result.setData(vo);
            flag = true;
        } catch (ApplePayException e) {
            logger.error("GCoinRechargeConsumer >> ApplePayException : " + e.getErrMsg());
            result = ResponsePacket.onError(e.hashCode(), e.getErrMsg());
            vo.setOrderId(orderId);
            result.setData(vo);
            flag = false;
        } catch (Exception e) {
            logger.error("GCoinRechargeConsumer >> exception : {}", e);
            result = ResponsePacket.onError(e.hashCode(), e.getMessage());
            vo.setOrderId(orderId);
            result.setData(vo);
            flag = false;
        } finally {

            flag = externalPaymentUtil.callBack(notifyUrl, result);

            //处理完解锁
            try {
                if (lock != null && lock.isLocked()) {
                    lock.unlock();
                }
            } catch (Exception e) {
                logger.error("分布式锁系统：{} ", e);
            }

            return flag;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> doIosRequest(String payLoad, String orderId, String userId) throws BusinessException,
            ApplePayException, ParseException {
        Map<String, Object> map = new HashMap<String, Object>();
        logger.debug("客户端传过来的值2：" + payLoad);
        //1.先线上测试发送平台验证
        String verifyResult = IosVerifyUtils.buyAppVerify(payLoad, 1);
        if (verifyResult == null) {
            // 苹果服务器没有返回验证结果
            logger.debug("无订单信息!");
        } else {
            // 苹果验证有返回结果
            logger.debug("线上，苹果平台返回JSON:" + verifyResult);
            JSONObject job = JSONObject.parseObject(verifyResult);
            String states = job.getString("status");
            if ("21007".equals(states)) {
                //是沙盒环境，应沙盒测试，否则执行下面
                verifyResult = IosVerifyUtils.buyAppVerify(payLoad, 0);
                //2.再沙盒测试  发送平台验证
                logger.debug("沙盒环境，苹果平台返回JSON:" + verifyResult);
                job = JSONObject.parseObject(verifyResult);
                states = job.getString("status");
                logger.debug("states : " + states);
            }

            logger.debug("苹果平台返回值 >>> " + job);
            if ("0".equals(states.trim())) {
                // 前端所提供的收据是有效的验证成功
                logger.debug("states = 0  >>> " + job);
                String r_receipt = job.getString("receipt");
                JSONObject returnJson = JSONObject.parseObject(r_receipt);
                String in_app = returnJson.getString("in_app");

                JSONObject in_appJson = JSONObject.parseObject(in_app.substring(1, in_app.length() - 1));

                //产品标识符
                String product_id = in_appJson.getString("product_id");
                // 订单号
                String transaction_id = in_appJson.getString("transaction_id");
                //订单创建日期
                String purchase_date = in_appJson.getString("purchase_date");

                //先判断票据是否使用过,再判断订单状态是否成功，如果成功则返回订单已经处理完；如果未处理，则去查看订单是否存在，存在则判断是否成功，不存在直接抛异常
                GCoinRechargeOrder recharge = rechargeRepository.findOneByPaymentOrderNoAndUserId(transaction_id, userId);
                if (recharge != null) {
                    if (GCoinRechargeStatusType.SUCCESS.getCode().equals(recharge.getState())) {
                        throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_APPLEPAY_RECIPT_IS_USED);
                    }
                } else {
                    recharge = rechargeRepository.findOneByOrderIdAndUserId(orderId, userId);

                    if (recharge == null) {
                        throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_ORDER_NOT_EXIST);
                    }
                    if (GCoinRechargeStatusType.SUCCESS.equals(recharge.getState())) {
                        throw new BusinessException(BizExceptionEnum.GCOIN_RECHARGE_ORDER_FINISHED);
                    }
                }
                recharge.setUserId(userId);
                recharge.setState(PaymentStatusType.SUCCESS.getCode());
                recharge.setPaymentOrderNo(transaction_id);
                recharge.setChannel(PayChannelEnum.APPLE_PAY.getValue());
                recharge.setSourceId(SourceType.IOS.getCode());

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = format.parse(purchase_date);
                logger.debug("purchase_date >> " + purchase_date);
                logger.debug("date >> " + date);
                recharge.setLastModifiedDate(date);
                recharge.setPaymentDate(purchase_date);
                recharge.setPaymentOrderNo(transaction_id);

                //调用Python接口查询对应的商品信息
                String url = productSearchUrl + "?product_id=" + product_id;

                Map<String, String> param = new HashMap<>();
                param.put("product_id", product_id);

                logger.debug("python productUrl >> 入参 " + param);
                ResponsePacket responsePacket = restTemplate.getForObject(url, ResponsePacket.class);
                logger.debug("python productUrl return >> 出参 " + responsePacket);

                int code = responsePacket.getCode();
                if (code != 0) {
                    logger.error("python productUrl >> Exception : " + BizExceptionEnum.GCOIN_RECHARGE_APPLEPAY_PRODUCT_SEARCH_ERROR.getErrMsg());
                    throw new ApplePayException(BizExceptionEnum.GCOIN_RECHARGE_APPLEPAY_PRODUCT_SEARCH_ERROR);
                }

                LinkedHashMap<String, Object> resultMap = (LinkedHashMap<String, Object>) responsePacket.getData();
                String gcoinAmountStr = String.valueOf(resultMap.get("coin_value"));
                String amountStr = String.valueOf(resultMap.get("price"));
                //人民币金额除以100
                recharge.setAmount(new BigDecimal(amountStr).divide(new BigDecimal(100)));

                //暴鸡币数量除以100
                recharge.setGcoinAmount(new BigDecimal(gcoinAmountStr).divide(new BigDecimal(100)));
                //苹果支付的收据信息
                recharge.setDescription(payLoad);

                //将订信息单保存到数据库
                rechargeRepository.save(recharge);
                String key = "payment:" + userId + ":" + transaction_id;
                //将订单信息存于redis中
                cacheManager.set(key, recharge, 30 * 60);

                map.put("success", true);
                map.put("message", recharge);
                map.put("status", states);

            } else {
                map.put("success", false);
                map.put("message", "receipt数据有问题");
                map.put("status", states);
            }
        }
        logger.debug("doIosRequest >> 出参 " + map);
        return map;
    }
}
