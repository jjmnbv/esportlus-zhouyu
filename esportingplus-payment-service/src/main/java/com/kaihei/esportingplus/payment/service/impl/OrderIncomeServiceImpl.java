package com.kaihei.esportingplus.payment.service.impl;

import com.google.gson.Gson;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.HttpUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.payment.api.enums.AccountStateType;
import com.kaihei.esportingplus.payment.api.enums.DeductRatioStatus;
import com.kaihei.esportingplus.payment.api.params.OrderIncomeParams;
import com.kaihei.esportingplus.payment.api.params.OrderQueryParam;
import com.kaihei.esportingplus.payment.api.params.WorkRoomQueryParams;
import com.kaihei.esportingplus.payment.api.vo.InComeBenefitJaVoJa;
import com.kaihei.esportingplus.payment.api.vo.InComeBenefitVo;
import com.kaihei.esportingplus.payment.data.jpa.repository.OrderIncomeRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.StarlightBalanceRepository;
import com.kaihei.esportingplus.payment.domain.entity.DeductRatioSetting;
import com.kaihei.esportingplus.payment.domain.entity.OrderIncome;
import com.kaihei.esportingplus.payment.domain.entity.StarlightBalance;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.DeductRatioService;
import com.kaihei.esportingplus.payment.service.OrderIncomeService;
import com.kaihei.esportingplus.payment.util.CommonUtils;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 订单收益分成计算
 *
 * @author zhouyu, haycco
 */
@Service
public class OrderIncomeServiceImpl implements OrderIncomeService {

    private static final Logger logger = LoggerFactory.getLogger(OrderIncomeServiceImpl.class);

    @Autowired
    private OrderIncomeRepository orderIncomeRepository;

    @Autowired
    private BillFlowService billFlowService;

    private String notifyUrl;

    @Autowired
    private StarlightBalanceRepository starlightBalanceRepository;

    @Autowired
    private DeductRatioService deductRatioService;

    @Autowired
    SnowFlake snowFlake;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponsePacket<List<InComeBenefitVo>> insertBatchRateOrderIncome(
            List<OrderIncomeParams> incomeParamsList,int type) {
        logger.info("结算分成上行参数(python-->java)：{}", incomeParamsList.toString());
        //加锁（可以改成AOP优化，先手动加锁解锁）
        long key = System.currentTimeMillis() / 1000;
        RLock redisLock = CommonUtils.getRedisLock("OrderIncomeServiceImpl" + key);
        //死锁超时时间200ms(具体需要看程序耗时)
        redisLock.lock(200, TimeUnit.MILLISECONDS);
        ResponsePacket<List<InComeBenefitVo>> responsePacket1 = null;
        try {
            //重构数据保存
            List<OrderIncome> orderIncomes = reConstructPojo(incomeParamsList);
            //暴鸡或爆娘入账
            orderIncomes.forEach(orderIncome -> {
                OrderIncome completedOrderIncome = orderIncomeRepository
                        .findByIncomeOrdernum(orderIncome.getIncomeOrdernum());
                if (completedOrderIncome != null) {
                    logger.warn("该订单【{}】收益已结算！", orderIncome.getIncomeOrdernum());
                    throw new BusinessException(BizExceptionEnum.ORDER_INCOME_REPEAT);
                }

                StarlightBalance starlightBalance = starlightBalanceRepository
                        .findOneByUserId(orderIncome.getUserId());
                //暴击值账户不可用
                if (AccountStateType.UNAVAILABLE.getCode().equals(starlightBalance.getState())) {
                    logger.warn("暴鸡/爆娘账户【{}】不可用导致订单收益结算异常：{}", orderIncome.getUserId(),
                            BizExceptionEnum.GCOIN_ACCOUNT_UNAVAILABLE.getErrMsg());
                    throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_UNAVAILABLE);
                }

                //暴击值账户被冻结
                if (AccountStateType.FROZEN.getCode().equals(starlightBalance.getState())) {
                    logger.warn("暴鸡/爆娘账户【{}】被冻结导致订单收益结算异常：{}", orderIncome.getUserId(),
                            BizExceptionEnum.GCOIN_ACCOUNT_FROZEN.getErrMsg());
                    throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_FROZEN);
                } else {
                    //如果沒有賬戶，那麽初始化賬戶
                    starlightBalance=new StarlightBalance();
                    starlightBalance.setState(AccountStateType.AVAILABLE.getCode());
                    starlightBalance.setUserId(orderIncome.getUserId());
                    starlightBalance.setFrozenAmount(new BigDecimal(0.00));
                }

                BigDecimal total = new BigDecimal(orderIncome.getTotalAmount()).divide(new BigDecimal(100));
                BigDecimal platIncome = total.multiply(orderIncome.getRatio()).setScale(2, BigDecimal.ROUND_DOWN);
                BigDecimal baojiIncome = total.subtract(platIncome);
                //最新余额（暴击收益存储单位为：分，余额单位为：元）
                BigDecimal newTotalStarlightBalance = starlightBalance.getBalance()
                        .add(baojiIncome);
                starlightBalance.setBalance(newTotalStarlightBalance);
                //增加可用余额
                BigDecimal newTotalStarlightUsableAmount = starlightBalance.getUsableAmount()
                        .add(baojiIncome);
                starlightBalance.setUsableAmount(newTotalStarlightUsableAmount);
                //逐个创建收益分成订单，并更新暴击值余额
                orderIncomeRepository.save(orderIncome);
                starlightBalanceRepository.save(starlightBalance);

                orderIncome.setBalance(
                        starlightBalance.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN)
                                .toString());
                billFlowService.saveRecord(orderIncome);
            });
//            orderIncomeRepository.save(orderIncomes);
            //重构数据返回pyhton
            List<InComeBenefitVo> inComeBenefitVos = reConstructVo(orderIncomes,type);
            responsePacket1 = ResponsePacket.onSuccess(inComeBenefitVos);

            //消息入列(mq使用异常，该方法暂时挂起，等我改天研究一下MQ非事务消息的框架再优化，现在使用线程池异步发送)
            //sendMessageToMq(responsePacket1);

            //异步线程推送
            if(!StringUtils.isEmpty(notifyUrl)){
                asyncNotify(responsePacket1);
            }


        }catch (BusinessException e){
            logger.error("收入分成保存异常：{}", e.getErrMsg());
            responsePacket1=ResponsePacket.onError(e.getErrCode(),e.getErrMsg());
        }finally {
            //解锁
            try {
                if (redisLock != null && redisLock.isLocked()) {
                    redisLock.unlock();
                }
            } catch (Exception e) {
                logger.error("分布式解锁异常：{}", e.getMessage());
                responsePacket1 = ResponsePacket.onError(2, e.getMessage());
            }
        }
        logger.info("结算分成下行信息(java-->python):{}", responsePacket1.toString());
        return responsePacket1;
    }

    @Override
    public ResponsePacket<Map<String, Object>> getOrderIncomDetail(
            List<OrderQueryParam> queryParamList) {
        ResponsePacket<Map<String, Object>> mapResponsePacket = null;
        logger.info("查询订单分成上行参数：{}", new Gson().toJson(queryParamList));
        //构造返回列表
        TreeMap<String, Object> treeMap2 = new TreeMap<>();
        try {
            queryParamList.forEach(orderQueryParam -> {
                TreeMap<String, Object> treeMap = new TreeMap<>();
                OrderIncome orderIncome = orderIncomeRepository
                        .findByIncomeOrdernumAndOrderType(orderQueryParam.getOrderId(),
                                orderQueryParam.getOrderType());
                if (ObjectTools.isNotNull(orderIncome)) {
                    treeMap.put("income_order_id", orderIncome.getIncomeOrdernum());
                    Integer baoji = new Integer(orderIncome.getBaojiIncome());
                    Integer total = new Integer(orderIncome.getTotalAmount());
                    treeMap.put("amount", total);
                    treeMap.put("baoji_amount", baoji);
                    treeMap.put("order_type", orderIncome.getOrderType());
                    treeMap2.put(orderIncome.getIncomeOrdernum() + "_" + orderIncome.getOrderType(),
                            treeMap);
                }

            });
            mapResponsePacket = ResponsePacket.onSuccess(treeMap2);
        } catch (Exception e) {
            logger.error("查询订单分成查询异常：{}", e.getMessage());
            mapResponsePacket = ResponsePacket.onError(2, e.getMessage());
        }
        logger.info("查询订单分成下行参数：{}", new Gson().toJson(mapResponsePacket));
        return mapResponsePacket;
    }

    /**
     * 查询工作室暴鸡的暴击值收益
     */
    @Override
    public ResponsePacket<Map<String, Object>> getWorkRoomIncome(
            List<WorkRoomQueryParams> workRoomQueryParams) {
        logger.info("查询工作室分成上行参数：{}", workRoomQueryParams);
        ResponsePacket responsePacket = null;
        TreeMap<String, Object> resmap = new TreeMap<>();
        try {
            for (WorkRoomQueryParams workRoomQueryParam : workRoomQueryParams) {
                TreeMap<Integer, Object> treeMap = new TreeMap<>();
                List<Integer> orderTypes = workRoomQueryParam.getOrderTypes();
                for (Integer orderType : orderTypes) {
                    Date beginTime = new Date(Long.parseLong(workRoomQueryParam.getBeginTime()));
                    Date endTime = new Date(Long.parseLong(workRoomQueryParam.getEndTime()));
                    Integer income = orderIncomeRepository
                            .queryWorkRoomIncome(workRoomQueryParam.getUserId(), orderType,
                                    beginTime,
                                    endTime);
                    //订单类型--订单收入
                    treeMap.put(orderType, Optional.ofNullable(income).orElse(0));
                }
                resmap.put(workRoomQueryParam.getUserId(), treeMap);
            }
        } catch (Exception e) {
            responsePacket = ResponsePacket.onError(2, e.getMessage());
            logger.error("查询工作室分成异常,message:{},result:{}", e.getMessage(), responsePacket);
            return responsePacket;
        }
        responsePacket = ResponsePacket.onSuccess(resmap);
        logger.info("查询工作室分成下行参数：{}", responsePacket);
        return responsePacket;
    }

    /**
     * 查询用户在36小时之内的分成的暴鸡值，返回用户在规定时间内的产生暴鸡值和用户id
     *
     * @param userId 用户ID
     * @param timeStramp 时间戳(秒)
     * @return userId amount
     */
    @Override
    public ResponsePacket<Map<String, Object>> getUserAmountInLimitTime(String userId,
            Long timeStramp, Integer timeLimit) {

        ResponsePacket<Map<String, Object>> responsePacket = null;
        try {
            int startTime = timeStramp.intValue() - timeLimit * 60 * 60;

            if (StringUtils.isEmpty(userId)) {
                return ResponsePacket.onError(2, "your userId is blank!!!");

            } else if (timeStramp == null && timeLimit == null) {
                return ResponsePacket.onError(2, "your timeStramp/timeLimit is null!!!");
            }

            Date end = new Date(timeStramp * 1000l);
            Date start = new Date(startTime * 1000l);
            Integer orderIncome = orderIncomeRepository.queryLimitTime(userId, start, end);

            TreeMap<String, Object> map = new TreeMap<>();
            map.put("userId", userId);
            map.put("amount", orderIncome);

            responsePacket = ResponsePacket.onSuccess(map);
        } catch (Exception e) {
            logger.error("查询用户时间内的暴鸡值失败，reason：{}", e.getMessage());
            responsePacket = ResponsePacket.onError(2, e.getMessage());
        }
        return responsePacket;
    }

    /**
     * 使用异步任务返回结果
     */
    public void asyncNotify(ResponsePacket<List<InComeBenefitVo>> inComeBenefitVos) {
        ThreadPoolManager.INSTANCE.getDefaultExecutor().execute(() -> {
            try {
                HttpEntity httpEntity = HttpUtils.buildParam(inComeBenefitVos);
                RestTemplate restTemplate = new RestTemplate();
                ResponsePacket pythonResponse = restTemplate
                        .postForObject(notifyUrl, httpEntity, ResponsePacket.class);
                logger.info("收入分成异步返回：url==>{},response==>{}", notifyUrl,
                        pythonResponse.toString());
            } catch (RestClientException e) {
                logger.error("收入分成异步异常：{}", e.getMessage());
            }
        });
    }

    /**
     * 收入分成
     */
    public OrderIncome doDispatchMoney(int mount) {
        OrderIncome orderIncome = new OrderIncome();
        DeductRatioSetting deductRatioSetting = deductRatioService.queryCalcOrderIncomeRatio();
        BigDecimal ratio;
        if(DeductRatioStatus.DISABLE == DeductRatioStatus.valueOf(deductRatioSetting.getState())){
            //禁用抽成后，默认为 0
            ratio = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            ratio = new BigDecimal(deductRatioSetting.getRatio()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        BigDecimal total = new BigDecimal(mount);
        //算出平台分成(默认0.00)
        int platIncome = total.multiply(ratio).intValue();
        orderIncome.setTotalAmount(mount);
        orderIncome.setRatio(ratio);
        orderIncome.setPlatformIncome(platIncome);
        orderIncome.setBaojiIncome(mount - platIncome);
        return orderIncome;
    }

    /**
     * 定义返回python数据
     */
    public List<InComeBenefitVo> reConstructVo(List<OrderIncome> orderIncomes,int type) {
        ArrayList<InComeBenefitVo> inComeBenefitVos = new ArrayList<>(3);
        Date date = new Date();
        if(type==0){
            for (OrderIncome orderIncome : orderIncomes) {
                InComeBenefitVo benefit = new InComeBenefitVo();
                benefit.setAmount(orderIncome.getTotalAmount());
                benefit.setAttach(orderIncome.getAttach());
                benefit.setIncomeFinishDate(DateUtil.fromDate2Str(date));
                benefit.setIncomeOrderId(orderIncome.getIncomeOrdernum());
                benefit.setOrderType(orderIncome.getOrderType());
                benefit.setState(0);
                inComeBenefitVos.add(benefit);
            }
        }else {
            for (OrderIncome orderIncome : orderIncomes) {
                InComeBenefitVo benefit = new InComeBenefitJaVoJa();
                benefit.setAmount(orderIncome.getTotalAmount());
                benefit.setAttach(orderIncome.getAttach());
                benefit.setIncomeFinishDate(DateUtil.fromDate2Str(date));
                benefit.setIncomeOrderId(orderIncome.getIncomeOrdernum());
                benefit.setOrderType(orderIncome.getOrderType());
                benefit.setState(0);
                ((InComeBenefitJaVoJa) benefit).setBaojiIncome(orderIncome.getBaojiIncome());
                inComeBenefitVos.add(benefit);
            }
        }
        return inComeBenefitVos;
    }

    /**
     * dto转po
     *
     * @param incomeParamsList python服务入参dto
     * @return po
     */
    public List<OrderIncome> reConstructPojo(List<OrderIncomeParams> incomeParamsList) {
        ArrayList<OrderIncome> orderIncomes = new ArrayList<>(3);
        //回调地址
        notifyUrl = incomeParamsList.get(0).getNotifyUrl();
        for (OrderIncomeParams orderIncomeParams : incomeParamsList) {
            //分账
            orderIncomeParams.setUserId(orderIncomeParams.getUserId());
            OrderIncome orderIncome = doDispatchMoney(orderIncomeParams.getAmount());
            orderIncome.setOrderType(orderIncomeParams.getOrderType());
            orderIncome.setAttach(orderIncomeParams.getAttach());
            orderIncome.setIncomeOrdernum(orderIncomeParams.getOrderId());
            orderIncome.setUserId(orderIncomeParams.getUserId());
            orderIncome.setFlowNo(snowFlake.nextId()+"");

            orderIncomes.add(orderIncome);
        }
        logger.info("收入分成信息：{}", orderIncomes.toString());
        return orderIncomes;
    }

}
