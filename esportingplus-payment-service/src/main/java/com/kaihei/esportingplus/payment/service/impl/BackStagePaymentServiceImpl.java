package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.payment.api.enums.*;
import com.kaihei.esportingplus.payment.api.params.DeductParams;
import com.kaihei.esportingplus.payment.api.params.ExchangeQueryParams;
import com.kaihei.esportingplus.payment.api.params.GCoinBackStageRechargeParam;
import com.kaihei.esportingplus.payment.api.vo.DeductOrderVo;
import com.kaihei.esportingplus.payment.api.vo.ExchangeRecordsVo;
import com.kaihei.esportingplus.payment.api.vo.FrontGCcoinRechargeVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo;
import com.kaihei.esportingplus.payment.data.jpa.repository.*;
import com.kaihei.esportingplus.payment.domain.entity.*;
import com.kaihei.esportingplus.payment.service.BackStagePaymentService;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.GCoinPaymentService;
import com.kaihei.esportingplus.payment.service.GCoinRechargeService;
import com.kaihei.esportingplus.payment.util.AccountUtil;
import com.kaihei.esportingplus.payment.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;

/**
 * 后台充值退款操作相关service
 *
 * @author xusisi
 * @create 2018-10-09 17:32
 **/
@Service
public class BackStagePaymentServiceImpl implements BackStagePaymentService {

    private static Logger logger = LoggerFactory.getLogger(BackStagePaymentServiceImpl.class);

    @Autowired
    private GCoinBalanceRepository gCoinBalanceRepository;

    @Autowired
    private StarlightBalanceRepository starlightBalanceRepository;

    @Autowired
    private GCoinRechargeRepository gCoinRechargeRepository;

    @Autowired
    private GCoinRechargeService gCoinRechargeService;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private GCoinPaymentService gCoinPaymentService;

    @Autowired
    private DeductOrderRepository deductOrderRepository;

    @Autowired
    private BillFlowService billFlowService;

    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeductOrderVo createDeductOrder(DeductParams deductParams) throws BusinessException {

        logger.debug("createDeductOrder >> deductParams : {}", deductParams);
        String currencyType = deductParams.getCurrencyType();
        DeductOrderVo vo = null;

        if (DeductCurrencyTypeEnum.GCOIN.getCode().equals(currencyType)) {
            vo = this.createGcoinDeduct(deductParams);
        } else if (DeductCurrencyTypeEnum.STARLIGHT.getCode().equals(currencyType)) {
            vo = this.createStarLightDeduct(deductParams);
        } else {
            logger.error("createDeductOrder >> exception : {}", BizExceptionEnum.DEDUCT_CURRENCY_TYPE_ERROR.getErrMsg());
            throw new BusinessException(BizExceptionEnum.DEDUCT_CURRENCY_TYPE_ERROR);
        }
        logger.debug("createDeductOrder >> vo : {}", vo);
        return vo;
    }

    @Override
    public FrontGCcoinRechargeVo getGCoinRechargeList(String userId, String channel, String sourceId, String beginDate, String endDate, String page
            , String size) throws BusinessException {
        FrontGCcoinRechargeVo vo = new FrontGCcoinRechargeVo();
        vo.setUserId(userId);
        vo.setBeginDate(beginDate);
        vo.setEndDate(endDate);
        vo.setChannel(channel);
        vo.setSourceId(sourceId);
        vo.setPage(Integer.valueOf(page));
        vo.setSize(Integer.valueOf(size));

        //查询充值的用户数量，暴鸡币数量，支付金额、查询的总记录数
        logger.debug("getInfo >> start ");
        Map<String, Integer> map = gCoinRechargeService.findAllSuccessSumInfoByCondition(userId, channel, sourceId, beginDate, endDate,
                GCoinRechargeTypeEnum.USER_RECHARGE.getCode());
        logger.debug("getInfo>> end :{}", map);
        vo.setTotalAmount(map.get("totalAmount"));
        vo.setTotalGcoinAmount(map.get("totalGcoinAmount"));
        vo.setTotalUser(map.get("totalUser"));

        Integer intPage = Integer.valueOf(page);
        Integer intSize = Integer.valueOf(size);

        Map<String, Object> pageInfo = gCoinRechargeService.findAllSuccessByCondition(userId, channel, sourceId, beginDate, endDate,
                GCoinRechargeTypeEnum.USER_RECHARGE.getCode(), intPage, intSize);

        Integer totalRecords = Integer.valueOf(pageInfo.get("counts").toString());
        List<GCoinRechargeVo> voList = null;
        if (totalRecords > 0) {
            List<GCoinRechargeOrder> orderList = (List<GCoinRechargeOrder>) pageInfo.get("list");
            logger.debug("page >> rawList ：{} ", orderList);

            voList = new ArrayList<GCoinRechargeVo>();

            for (GCoinRechargeOrder order : orderList) {
                GCoinRechargeVo gCoinRechargeVo = gCoinRechargeService.entityTransformVo(order);
                voList.add(gCoinRechargeVo);
            }
        }

        logger.debug("page >> totalRecords :{}", totalRecords);
        logger.debug("page >> content :{} ", voList);
        vo.setTotalRecords(totalRecords);
        vo.setgCoinRechargeVoList(voList);
        return vo;
    }

    /**
     * @Description: 暴击值扣款
     * @Param: [deductParams]
     * @return: com.kaihei.esportingplus.payment.api.vo.DeductOrderVo
     * @Author: xusisi
     * @Date: 2018/10/11
     */
    @Transactional(rollbackFor = Exception.class)
    public DeductOrderVo createStarLightDeduct(DeductParams deductParams) {
        /***
         * 判断用户暴击值扣款订单是否已经存在
         * 判断用户暴击值账户是否有足够金额扣款
         * 新增暴击值扣款信息
         * 更新用户暴击值账户信息
         * 新增操作流水
         */

        logger.debug("createStarLightDeduct >> deductParams :{} ", deductParams);
        String outTradeNo = deductParams.getOutTradeNo();
        String currencyType = deductParams.getCurrencyType();
        String userId = deductParams.getUserId();
        String deductType = deductParams.getDeductType();
        String message = deductParams.getMessage();
        String orderType = deductParams.getOrderType();
        String amount = deductParams.getAmount();
        Integer intAmount = CommonUtils.strToInteger(amount);

        BigDecimal starLightAmount = new BigDecimal(intAmount).divide(new BigDecimal(100));

        //判断用户暴击值扣款订单是否已经存在
        DeductOrder deductOrder = deductOrderRepository.findOneByCurrencyTypeAndOutTradeNoAndOrderType(currencyType, outTradeNo, orderType);
        if (deductOrder != null) {
            logger.error("createStarLightDeduct >> exception : {} ", BizExceptionEnum.STAR_DEDUCT_ORDER_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STAR_DEDUCT_ORDER_EXIST);
        }

        //判断用户暴击值账户是否有足够金额扣款
        StarlightBalance starlightBalance = this.checkStarLightAccountState(userId);
        BigDecimal usableAmount = starlightBalance.getUsableAmount();
        if (usableAmount.compareTo(starLightAmount) < 0) {
            logger.error("checkStarLightAccountState >> exception : {}", BizExceptionEnum.STAR_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STAR_ACCOUNT_BALANCE_NOT_ENOUGH);
        }

        deductOrder = new DeductOrder();
        String orderId = String.valueOf(snowFlake.nextId());
        deductOrder.setOrderId(orderId);
        deductOrder.setUserId(userId);
        deductOrder.setAmount(starLightAmount);
        deductOrder.setDeductType(deductType);
        deductOrder.setState(DeductStateEnum.DEDUCT_SUCCESS.getCode());
        deductOrder.setSubject(deductParams.getSubject());
        deductOrder.setBody(deductParams.getBody());
        deductOrder.setDescription(message);

        deductOrder.setCurrencyType(currencyType);
        deductOrder.setOutTradeNo(outTradeNo);
        deductOrder.setOrderType(orderType);

        //新增暴击值扣款信息
        deductOrderRepository.save(deductOrder);

        BigDecimal newUsableAmount = usableAmount.subtract(starLightAmount);
        BigDecimal newTotalAmount = starlightBalance.getBalance().subtract(starLightAmount);
        starlightBalance.setBalance(newTotalAmount);
        starlightBalance.setUsableAmount(newUsableAmount);
        //更新用户暴击值账户信息
        starlightBalanceRepository.save(starlightBalance);

        deductOrder.setBalance(
                starlightBalance.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
        //新增一条操作流水
        billFlowService.saveRecord(deductOrder);

        //返回信息给Python
        DeductOrderVo vo = new DeductOrderVo();
        vo.setOrderId(orderId);
        vo.setUserId(userId);
        vo.setAmount(amount);
        vo.setCurrencyType(currencyType);
        vo.setMessage(message);
        vo.setState(DeductStateEnum.DEDUCT_SUCCESS.getCode());

        vo.setOutTradeNo(outTradeNo);
        vo.setDeductType(deductType);
        vo.setOrderType(orderType);

        logger.debug("createStarLightDeduct >> vo : {} ", vo);
        return vo;
    }

    /**
     * @Description: 暴鸡币扣款
     * @Param: [gCoinBackStageDeductParam]
     * @return: com.kaihei.esportingplus.payment.api.vo.GCoinDeductVo
     * @Author: xusisi
     * @Date: 2018/10/11
     */
    @Transactional(rollbackFor = Exception.class)
    public DeductOrderVo createGcoinDeduct(DeductParams deductParams) {
        logger.debug("createGcoinDeduct >> deductParams : {} ", deductParams);

        String currencyType = deductParams.getCurrencyType();
        String outTradeNo = deductParams.getOutTradeNo();
        String orderType = deductParams.getOrderType();
        String message = deductParams.getMessage();
        String userId = deductParams.getUserId();
        String deductType = deductParams.getDeductType();
        String amount = deductParams.getAmount();
        //金额由String转换成Integer
        Integer intAmount = CommonUtils.strToInteger(amount);

        BigDecimal gcoinAmount = new BigDecimal(intAmount).divide(new BigDecimal(100));

        //检验跟进扣款订单ID判断是否已经扣款
        DeductOrder deductOrder = deductOrderRepository.findOneByCurrencyTypeAndOutTradeNoAndOrderType(currencyType, outTradeNo, orderType);
        if (deductOrder != null) {
            logger.error("createGcoinDeduct >> exception :{}", BizExceptionEnum.GCOIN_DEDUCT_ORDER_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_DEDUCT_ORDER_EXIST);
        }

        //校验用户暴鸡币账号信息
        GCoinBalance gCoinBalance = gCoinPaymentService.checkGcoinAccountState(userId);
        BigDecimal usableAmount = gCoinBalance.getUsableAmount();
        if (usableAmount.compareTo(gcoinAmount) < 0) {
            logger.error("createGcoinDeduct >> exception :{}", BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
            throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH);
        }

        /****
         * 新增一条暴鸡币扣款记录
         * 更新用户暴鸡币账户金额
         * 新增一条流水记录
         *
         */
        String orderId = String.valueOf(snowFlake.nextId());
        deductOrder = new DeductOrder();
        deductOrder.setOrderId(orderId);
        deductOrder.setUserId(userId);
        deductOrder.setAmount(gcoinAmount);

        deductOrder.setDeductType(deductType);
        deductOrder.setOutTradeNo(outTradeNo);
        deductOrder.setOrderType(orderType);

        deductOrder.setCurrencyType(currencyType);
        deductOrder.setState(DeductStateEnum.DEDUCT_SUCCESS.getCode());
        deductOrder.setDescription(message);
        deductOrder.setSubject(deductParams.getSubject());
        deductOrder.setBody(deductParams.getBody());

        //新增一条暴鸡币扣款记录
        deductOrderRepository.save(deductOrder);

        BigDecimal newUsableAmount = usableAmount.subtract(gcoinAmount);
        BigDecimal newTotalAmount = gCoinBalance.getGcoinBalance().subtract(gcoinAmount);
        gCoinBalance.setUsableAmount(newUsableAmount);
        gCoinBalance.setGcoinBalance(newTotalAmount);
        //更新用户暴鸡币账户金额
        gCoinBalanceRepository.save(gCoinBalance);

        //新增一条流水记录
        deductOrder.setBalance(
                gCoinBalance.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
        billFlowService.saveRecord(deductOrder);

        //返回数据给Python
        DeductOrderVo vo = new DeductOrderVo();

        vo.setOutTradeNo(outTradeNo);
        vo.setOrderType(orderType);
        vo.setDeductType(deductType);
        vo.setState(DeductStateEnum.DEDUCT_SUCCESS.getCode());
        vo.setUserId(userId);
        vo.setOrderId(orderId);
        vo.setAmount(amount);
        vo.setCurrencyType(currencyType);
        vo.setMessage(message);

        logger.debug("createGcoinDeduct >> vo : {} ", vo);

        return vo;

    }

    /**
     * @Description: 暴鸡币充值
     * @Param: [gCoinBackStageRechargeParam]
     * @return: com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo
     * @Author: xusisi
     * @Date: 2018/10/11
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GCoinRechargeVo createGcoinRecharge(GCoinBackStageRechargeParam gCoinBackStageRechargeParam) throws BusinessException {

        logger.debug("createGcoinRecharge >> gCoinBackStageRechargeParam : {} ", gCoinBackStageRechargeParam);
        String userId = gCoinBackStageRechargeParam.getUserId();
        String outTradeNo = gCoinBackStageRechargeParam.getOutTradeNo();
        String rechargeType = gCoinBackStageRechargeParam.getRechargeType();
        String orderType = gCoinBackStageRechargeParam.getOrderType();
        String amount = gCoinBackStageRechargeParam.getGcoinAmount();

        Integer intAmount = CommonUtils.strToInteger(amount);

        BigDecimal gcoinAmount = new BigDecimal(intAmount).divide(new BigDecimal(100));

        /***
         * 校验订单是否已经充值完成
         * 新增暴鸡币充值记录
         * 暴鸡账户充值
         * 新增操作流水记录
         */
        GCoinRechargeOrder gCoinRechargeOrder = gCoinRechargeRepository.findOneByPaymentOrderNoAndRechargeTypeAndOrderType(outTradeNo, rechargeType
                , orderType);
        if (gCoinRechargeOrder != null) {
            logger.error("createGcoinRecharge >> exception : {}", BizExceptionEnum.BACK_STAGE_GCOIN_PAYMENT_ORDER_EXIST.getErrMsg());
            throw new BusinessException(BizExceptionEnum.BACK_STAGE_GCOIN_PAYMENT_ORDER_EXIST);
        }

        GCoinBalance gCoinBalance = gCoinBalanceRepository.findOneByUserId(userId);
        if (gCoinBalance == null) {
            gCoinBalance = AccountUtil.generateGcoinBalance(userId);
        }

        gCoinRechargeOrder = new GCoinRechargeOrder();
        String orderId = String.valueOf(snowFlake.nextId());
        gCoinRechargeOrder.setOrderId(orderId);
        gCoinRechargeOrder.setUserId(userId);
        gCoinRechargeOrder.setPaymentOrderNo(outTradeNo);
        gCoinRechargeOrder.setGcoinAmount(gcoinAmount);
        gCoinRechargeOrder.setAmount(gcoinAmount);
        String paymentDate = DateUtil.fromDate2Str(new Date());
        gCoinRechargeOrder.setPaymentDate(paymentDate);

        gCoinRechargeOrder.setRechargeType(rechargeType);
        gCoinRechargeOrder.setOrderType(orderType);
        gCoinRechargeOrder.setOutTradeNo(outTradeNo);

        gCoinRechargeOrder.setBody(gCoinBackStageRechargeParam.getBody());
        gCoinRechargeOrder.setDescription(gCoinBackStageRechargeParam.getMessage());
        gCoinRechargeOrder.setSubject(gCoinBackStageRechargeParam.getSubject());
        gCoinRechargeOrder.setRemarks(gCoinBackStageRechargeParam.getRemarks());
        //充值状态：成功
        gCoinRechargeOrder.setState(GCoinPaymentStateType.WAIT_FOR_SETTLE.getCode());
        //操作来源：平台系统
        gCoinRechargeOrder.setSourceId(SourceType.PLATFORM.getCode());
        //支付渠道：平台系统支付
        gCoinRechargeOrder.setChannel(PayChannelEnum.PLATFORM_SYSTEM.getValue());

        //新增暴鸡币充值记录
        gCoinRechargeRepository.save(gCoinRechargeOrder);

        BigDecimal newUsableAmount = gCoinBalance.getUsableAmount().add(gcoinAmount);
        BigDecimal newTotalAmount = gCoinBalance.getGcoinBalance().add(gcoinAmount);
        gCoinBalance.setUsableAmount(newUsableAmount);
        gCoinBalance.setGcoinBalance(newTotalAmount);
        //更新暴鸡币账户金额
        gCoinBalanceRepository.save(gCoinBalance);

        //新增一条操作流水
        //gCoinRechargeOrder.setBalance(gCoinBalance.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
        billFlowService.saveRecord(gCoinRechargeOrder);

        //返回给Python的数据
        GCoinRechargeVo vo = gCoinRechargeService.getRechargeVo(orderId, userId);
        logger.debug("createGcoinRecharge >> vo : {} ", vo);
        return vo;
    }

    /**
     * @Description: 检查用户暴击值账户状态
     * @Param: [userId]
     * @return: com.kaihei.esportingplus.payment.domain.entity.StarlightBalance
     * @Author: xusisi
     * @Date: 2018/10/11
     */
    public StarlightBalance checkStarLightAccountState(String userId) throws BusinessException {

        logger.debug("checkStarLightAccountState >> userId : {} ", userId);

        StarlightBalance starlightBalance = starlightBalanceRepository.findOneByUserId(userId);

        logger.debug("checkStarLightAccountState >> starlightBalance : {} ", starlightBalance);

        if (starlightBalance == null) {
            logger.error("checkStarLightAccountState >> exception : {}", BizExceptionEnum.STAR_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STAR_ACCOUNT_BALANCE_NOT_ENOUGH);
        }

        String stateCode = starlightBalance.getState();
        if (AccountStateType.UNAVAILABLE.getCode().equals(stateCode)) {
            logger.error("checkStarLightAccountState >> exception : {}", BizExceptionEnum.STAR_ACCOUNT_UNAVAILABLE.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STAR_ACCOUNT_UNAVAILABLE);
        }

        if (AccountStateType.FROZEN.getCode().equals(stateCode)) {
            logger.error("checkStarLightAccountState >> exception : {}", BizExceptionEnum.STAR_ACCOUNT_FROZEN.getErrMsg());
            throw new BusinessException(BizExceptionEnum.STAR_ACCOUNT_FROZEN);
        }
        return starlightBalance;

    }

    /**
     * 查询暴击值兑换记录
     *
     * @param queryParams
     * @return
     * @throws BusinessException
     */
    @Override
    public Map<String, Object> getStarlightExchangeList(ExchangeQueryParams queryParams) throws BusinessException {
        Map<String, Object> map = new HashMap<>();

        String uid = queryParams.getUid();
        String beginDate = queryParams.getBeginDate();
        String endDate = queryParams.getEndDate();
        //排序参数
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "createDate"));
        //分页参数
        PageRequest pageRequest = null;
        if (StringUtils.isNotEmpty(queryParams.getPage()) && StringUtils
                .isNotEmpty(queryParams.getSize())) {
            int page = Integer.parseInt(queryParams.getPage());
            int size = Integer.parseInt(queryParams.getSize());
            if (size == -1) {
                pageRequest = new PageRequest(page - 1, 9999, sort);
            } else {
                pageRequest = new PageRequest(page - 1, size, sort);
            }
        } else {
            pageRequest = new PageRequest(0, 20, sort);

        }

        Specification<WithdrawOrder> specification = this.getSpecipicationInfo(uid, beginDate,
                endDate, OrderTypeEnum.STAR_EXCHANGE_ORDER.getCode(), WithdrawStatusType.SUCCESS.getCode());

        //查询列表
        Page<WithdrawOrder> respPage = withdrawOrderRepository.findAll(specification, pageRequest);
        long total = respPage.getTotalElements();
        map.put("total", total);
        Map<String, Object> sumMap = new HashMap<>();

        List<ExchangeRecordsVo> voList = new ArrayList<ExchangeRecordsVo>();
        ;
        if (total > 0) {
            List<WithdrawOrder> orderList = respPage.getContent();
            logger.debug("getStarlightExchangeList >> page ：{} ", orderList);

            for (WithdrawOrder order : orderList) {
                ExchangeRecordsVo vo = new ExchangeRecordsVo();
                vo.setId(order.getId());
                vo.setGcoin(order.getAmount());
                vo.setStarlight(order.getAmount());
                vo.setUid(order.getUserId());
                vo.setExchangeDate(DateUtil.fromDate2Str(order.getCreateDate()));

                voList.add(vo);

            }

            // 查询有记录时才汇总，否则会报错
            sumMap = this.sumExchangeDataByCondition(uid, beginDate,
                    endDate, OrderTypeEnum.STAR_EXCHANGE_ORDER.getCode(), WithdrawStatusType.SUCCESS.getCode());
        } else {
            sumMap.put("gcoin_count", 0);
            sumMap.put("starlight_count", 0);
            sumMap.put("user_count", 0);
        }
        map.put("sum_data", sumMap);
        map.put("list", voList);

        return map;
    }

    /**
     * 拼接查询条件
     *
     * @param userId
     * @param beginDate
     * @param endDate
     * @return
     */
    public Specification<WithdrawOrder> getSpecipicationInfo(String userId, String beginDate, String endDate, String orderType, String state) {

        Specification<WithdrawOrder> querySpecifiction = new Specification<WithdrawOrder>() {
            @Override
            public Predicate toPredicate(Root<WithdrawOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (ObjectTools.isNotEmpty(userId)) {
                    predicates.add(criteriaBuilder.equal(root.get("userId").as(String.class), userId));
                }

                if (ObjectTools.isNotEmpty(beginDate)) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate").as(String.class), beginDate));
                }

                if (ObjectTools.isNotEmpty(endDate)) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate").as(String.class), endDate));
                }

                predicates.add(criteriaBuilder.equal(root.get("orderType").as(String.class), orderType));
                predicates.add(criteriaBuilder.equal(root.get("state").as(String.class), state));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return querySpecifiction;
    }

    /**
     * 获取暴击值兑换——统计数据
     *
     * @param uid
     * @param beginDate
     * @param endDate
     * @param orderType
     * @param state
     * @return
     */
    public Map<String, Object> sumExchangeDataByCondition(String uid, String beginDate, String endDate, String orderType, String state) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();

        Root<WithdrawOrder> root = query.from(WithdrawOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        if (ObjectTools.isNotEmpty(uid)) {
            predicates.add(criteriaBuilder.equal(root.get("userId").as(String.class), uid));
        }

        if (ObjectTools.isNotEmpty(beginDate)) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate").as(String.class), beginDate));
        }

        if (ObjectTools.isNotEmpty(endDate)) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate").as(String.class), endDate));
        }

        predicates.add(criteriaBuilder.equal(root.get("orderType").as(String.class), orderType));
        predicates.add(criteriaBuilder.equal(root.get("state").as(String.class), state));

        Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);

        query.where(predicatesArray);

        // 用户兑换多次只算1个用户
        query.multiselect(criteriaBuilder.sum(root.get("amount")), criteriaBuilder.countDistinct(root.get("userId")));

        TypedQuery<Tuple> q1 = entityManager.createQuery(query);

        List<Tuple> result1 = q1.getResultList();

        logger.debug("result1 : {} ", result1);

        Iterator it = result1.iterator();
        while (it.hasNext()) {
            logger.debug("t :{}", it.next());
        }
        Tuple tuple = result1.get(0);

        String amount = tuple.get(0).toString();
        String totalUsers = tuple.get(1).toString();

        logger.debug("amount :{}, totalUsers:{}", amount, totalUsers);
        Map<String, Object> map = new HashMap<>();
        map.put("gcoin_count", new BigDecimal(amount));
        map.put("starlight_count", new BigDecimal(amount));
        map.put("user_count", Integer.valueOf(totalUsers));

        logger.debug("map >> {} ", map);
        return map;
    }

}
