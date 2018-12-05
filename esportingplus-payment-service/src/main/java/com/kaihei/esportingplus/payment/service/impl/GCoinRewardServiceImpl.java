package com.kaihei.esportingplus.payment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.enums.GCoinOrderEnum;
import com.kaihei.esportingplus.payment.api.enums.WalletStateEnum;
import com.kaihei.esportingplus.payment.api.params.ConsumeGCoinParams;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinBalanceRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinRewardOrderRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.StarlightBalanceRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinBalance;
import com.kaihei.esportingplus.payment.domain.entity.GCoinRewardOrder;
import com.kaihei.esportingplus.payment.domain.entity.StarlightBalance;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.GCoinRewardService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 暴鸡币打赏接口实现类
 *
 * @author xiaolijun, tangtao
 */
@Service
public class GCoinRewardServiceImpl implements GCoinRewardService {

    private static final Logger logger = LoggerFactory.getLogger(GCoinRewardServiceImpl.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    private static final String redisCreateOrderKey = "gcoin:create_order:user_wallet:";

    @Autowired
    BillFlowService billFlowService;
    @Autowired
    private GCoinBalanceRepository gCoinBalanceRepository;
    @Autowired
    private GCoinRewardOrderRepository gCoinRewardOrderRepository;
    @Autowired
    private StarlightBalanceRepository starlightBalanceRepository;
    @Autowired
    private SnowFlake snowFlake;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void consumeGCoin(String orderId) {
        BusinessException businessException = null;
        try {
            String orderState = cacheManager.get("gcoin:order:state:" + orderId, String.class);
            if (StringUtils.isEmpty(orderState)) {
                //订单不存在
                logger.debug(BizExceptionEnum.CONSUME_ORDER_NON_EXIST.getErrMsg());
                businessException = new BusinessException(BizExceptionEnum.CONSUME_ORDER_NON_EXIST);
                throw businessException;
            } else if (GCoinOrderEnum.TRANSACTION_STATE_PAYSUCCESS.getCode().equals(orderState)) {
                //订单已处理
                logger.debug(BizExceptionEnum.CONSUME_ORDER_STATE_PAYSUCCESS.getErrMsg());
                businessException = new BusinessException(
                        BizExceptionEnum.CONSUME_ORDER_STATE_PAYSUCCESS);
                throw businessException;
            }
            //获取流水号ID
      /*SnowFlake snowFlake = new SnowFlake(snowFlakeConfig.getDataCenter(),
          snowFlakeConfig.getMachine());
      String flowNo = String.valueOf(snowFlake.nextId());*/
            ConsumeGCoinParams consumeGCoinParams = cacheManager
                    .get("gcoin:" + orderId, ConsumeGCoinParams.class);
//      ConsumeGCoinParams consumeGCoinParams = (ConsumeGCoinParams) redisTemplate.opsForValue()
//          .get("gcoin:" + orderId);
            //校验暴鸡币信息
            List<GCoinBalance> gCoinBalanceList = gCoinBalanceRepository
                    .findByUserId(consumeGCoinParams.getSourceUserId());
            GCoinBalance gCoinBalance = gCoinBalanceList.get(0);
            logger.debug("打印消费暴鸡币兑换暴击值服务-用户暴鸡币余额信息：" + gCoinBalance.toString());
            BigDecimal gcoinAmount = consumeGCoinParams.getGcoinAmount()
                    .divide(new BigDecimal(100));
            if (gcoinAmount.compareTo(gCoinBalance.getGcoinBalance()) == 1) {
                logger.debug(BizExceptionEnum.USER_GCOIN_NOT_ENOUGH.getErrMsg());
                businessException = new BusinessException(BizExceptionEnum.USER_GCOIN_NOT_ENOUGH);
                throw businessException;
            }
            BigDecimal starlighAmount = consumeGCoinParams.getStarlightAmount()
                    .divide(new BigDecimal(100));
            //新增暴鸡币订单表
            GCoinRewardOrder gcoinRewardOrder = new GCoinRewardOrder();
            gcoinRewardOrder.setOrderId(orderId);
            gcoinRewardOrder.setUserId(consumeGCoinParams.getSourceUserId());//打赏支出用户ID
            gcoinRewardOrder.setReceivedUserId(consumeGCoinParams.getReceiveUserId());//被打赏收入用户ID
            gcoinRewardOrder.setState(GCoinOrderEnum.TRANSACTION_STATE_PAYSUCCESS.getCode());//打赏成功
            gcoinRewardOrder.setGcoinAmount(gcoinAmount);
            gcoinRewardOrder.setStarlightAmount(starlighAmount);
            gcoinRewardOrder.setSourceId(consumeGCoinParams.getSourceId());
            gcoinRewardOrder.setBody(consumeGCoinParams.getBody());
            gcoinRewardOrder.setSubject(consumeGCoinParams.getSubject());
            gCoinRewardOrderRepository.save(gcoinRewardOrder);
            //更新暴鸡币表（减除暴鸡币）
            gCoinBalance.setGcoinBalance(gCoinBalance.getGcoinBalance().subtract(gcoinAmount));
            gCoinBalance.setUsableAmount(gCoinBalance.getUsableAmount().subtract(gcoinAmount));
            gCoinBalanceRepository.save(gCoinBalance);
            //更新“暴击值”表（增加“暴击值”）
            StarlightBalance starlightBalance = null;
            //查询暴击值余额表
            List<StarlightBalance> starlightBalanceList = starlightBalanceRepository
                    .findByUserId(consumeGCoinParams.getReceiveUserId());
            if (starlightBalanceList.isEmpty()) {
                starlightBalance = new StarlightBalance();
                starlightBalance.setBalance(starlighAmount);
                starlightBalance.setFrozenAmount(new BigDecimal(0.00));
                starlightBalance.setUsableAmount(starlighAmount);
                starlightBalance.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
                starlightBalance.setUserId(consumeGCoinParams.getReceiveUserId());
            } else {
                starlightBalance = starlightBalanceList.get(0);
                starlightBalance.setBalance(starlightBalance.getBalance().add(starlighAmount));
                starlightBalance
                        .setUsableAmount(starlightBalance.getUsableAmount().add(starlighAmount));
            }
            starlightBalanceRepository.save(starlightBalance);

            //打赏订单对应流水新增暴鸡币和暴击值流水表
            gcoinRewardOrder.setBalance(gCoinBalance.getUsableAmount().toString());
            gcoinRewardOrder.setStartLightBalance(
                    starlightBalance.getUsableAmount().setScale(2, BigDecimal.ROUND_DOWN)
                            .toString());
            billFlowService.saveRecord(gcoinRewardOrder);
            //确认时间单位
            cacheManager.set("gcoin:order:state:" + orderId,
                    GCoinOrderEnum.TRANSACTION_STATE_PAYSUCCESS.getCode(), 2 * 60 * 60);
        } catch (Exception e) {
            if (businessException != null) {
                throw businessException;
            } else {
                logger.debug("打印消费暴鸡币兑换暴击值服务接口异常：" + e.getMessage(), e);
                throw new BusinessException(BizExceptionEnum.GCOIN_CONSUME);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String gCoinCreateOrder(ConsumeGCoinParams consumeGCoinParams) {
        String sourceUserId = consumeGCoinParams.getSourceUserId();
        String receiveUserId = consumeGCoinParams.getReceiveUserId();
        if (sourceUserId.equals(receiveUserId)) {
            logger.warn("打赏用户和被打赏用户ID不能为同一个用户，暴鸡币订单创建失败！");
            throw new BusinessException(BizExceptionEnum.CREATE_GCOIN_ORDER_FAILURE);
        }
        try {
            BigDecimal initAmount = new BigDecimal("0.00");
            /***
             * 校验打赏用户和被打赏用户是否存在钱包信息，如果不存在就创建一个
             */
            String sourceUserWallet = cacheManager
                    .get(redisCreateOrderKey + sourceUserId, String.class);
            String receiveUserWallet = cacheManager
                    .get(redisCreateOrderKey + receiveUserId, String.class);

            if ("exist".equals(sourceUserWallet) && "exist".equals(receiveUserWallet)) {
                //此处无逻辑代码
            } else {
                //查询
                int sourceIdGCoinCount = gCoinBalanceRepository.searchCount(sourceUserId);
                int receiveIdGCoinCount = gCoinBalanceRepository.searchCount(receiveUserId);
                //判断是否新增暴鸡币表
                if (sourceIdGCoinCount < 1 && receiveIdGCoinCount < 1) {
                    List<GCoinBalance> gCoinBalances = new ArrayList<GCoinBalance>();
                    GCoinBalance sourceG = new GCoinBalance();
                    sourceG.setGcoinBalance(initAmount);
                    sourceG.setFrozenAmount(initAmount);
                    sourceG.setUsableAmount(initAmount);
                    sourceG.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
                    sourceG.setUserId(sourceUserId);
                    gCoinBalances.add(sourceG);

                    GCoinBalance receiveG = new GCoinBalance();
                    receiveG.setGcoinBalance(initAmount);
                    receiveG.setFrozenAmount(initAmount);
                    receiveG.setUsableAmount(initAmount);
                    receiveG.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
                    receiveG.setUserId(receiveUserId);
                    gCoinBalances.add(receiveG);

                    gCoinBalanceRepository.save(gCoinBalances);
                } else if (sourceIdGCoinCount < 1) {
                    GCoinBalance sourceG = new GCoinBalance();
                    sourceG.setGcoinBalance(initAmount);
                    sourceG.setFrozenAmount(initAmount);
                    sourceG.setUsableAmount(initAmount);
                    sourceG.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
                    sourceG.setUserId(sourceUserId);
                    gCoinBalanceRepository.save(sourceG);
                } else if (receiveIdGCoinCount < 1) {
                    GCoinBalance receiveG = new GCoinBalance();
                    receiveG.setGcoinBalance(initAmount);
                    receiveG.setFrozenAmount(initAmount);
                    receiveG.setUsableAmount(initAmount);
                    receiveG.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
                    receiveG.setUserId(receiveUserId);
                    gCoinBalanceRepository.save(receiveG);
                }
                //查询
                int sourceIdStarlightCount = starlightBalanceRepository.searchCount(sourceUserId);
                int receiveIdStarlightCount = starlightBalanceRepository.searchCount(receiveUserId);
                //判断是否新增“暴击值”表
                if (sourceIdStarlightCount < 1 && receiveIdStarlightCount < 1) {
                    List<StarlightBalance> starlightBalances = new ArrayList<StarlightBalance>();
                    StarlightBalance sourceS = new StarlightBalance();
                    sourceS.setBalance(initAmount);
                    sourceS.setFrozenAmount(initAmount);
                    sourceS.setUsableAmount(initAmount);
//          sourceS.setCreateUserId(sourceUserId);
                    sourceS.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
                    sourceS.setUserId(sourceUserId);
                    starlightBalances.add(sourceS);
                    StarlightBalance receiveS = new StarlightBalance();
                    receiveS.setBalance(initAmount);
                    receiveS.setFrozenAmount(initAmount);
                    receiveS.setUsableAmount(initAmount);
//          receiveS.setCreateUserId(receiveUserId);
                    receiveS.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
                    receiveS.setUserId(receiveUserId);
                    starlightBalances.add(receiveS);
                    starlightBalanceRepository.save(starlightBalances);
                } else if (sourceIdStarlightCount < 1) {
                    StarlightBalance sourceS = new StarlightBalance();
                    sourceS.setBalance(initAmount);
                    sourceS.setFrozenAmount(initAmount);
                    sourceS.setUsableAmount(initAmount);
//          sourceS.setCreateUserId(sourceUserId);
                    sourceS.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
                    sourceS.setUserId(sourceUserId);
                    starlightBalanceRepository.save(sourceS);
                } else if (receiveIdStarlightCount < 1) {
                    StarlightBalance receiveS = new StarlightBalance();
                    receiveS.setBalance(initAmount);
                    receiveS.setFrozenAmount(initAmount);
                    receiveS.setUsableAmount(initAmount);
//          receiveS.setCreateUserId(receiveUserId);
                    receiveS.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
                    receiveS.setUserId(receiveUserId);
                    starlightBalanceRepository.save(receiveS);
                }
                //redis里面设置用户钱包是否存在
                cacheManager.set(redisCreateOrderKey + sourceUserId, "exist");
                cacheManager.set(redisCreateOrderKey + receiveUserId, "exist");
                //如果是新增的暴鸡币钱包就直接返回，提示用户去充值
                if (sourceIdGCoinCount < 1) {
                    logger.debug(BizExceptionEnum.USER_GCOIN_NOT_ENOUGH.getErrMsg());
                    return "";
                }
            }
            //校验暴鸡币信息
            List<GCoinBalance> gCoinBalanceList = gCoinBalanceRepository.findByUserId(sourceUserId);
            GCoinBalance gCoinBalance = gCoinBalanceList.get(0);
            logger.debug("打印暴鸡币订单创建服务接口-用户暴鸡币余额信息：" + JSONObject.toJSONString(gCoinBalance));
            BigDecimal gcoinAmount = consumeGCoinParams.getGcoinAmount()
                    .divide(new BigDecimal(100));
            if (gcoinAmount.compareTo(gCoinBalance.getGcoinBalance()) == 1) {
                logger.debug(BizExceptionEnum.USER_GCOIN_NOT_ENOUGH.getErrMsg());
                return "";
            }
            //获取流水号ID
            String flowNo = String.valueOf(snowFlake.nextId());
            cacheManager.set("gcoin:" + flowNo, consumeGCoinParams, 2 * 60 * 60);
            cacheManager.set("gcoin:order:state:" + flowNo,
                    GCoinOrderEnum.TRANSACTION_STATE_CREATE.getCode(), 2 * 60 * 60);

            return flowNo;
      /*respJsonObject.put("order_id", flowNo);
      return respJsonObject;*/
        } catch (Exception e) {
            cacheManager.set(redisCreateOrderKey + sourceUserId, "");
            cacheManager.set(redisCreateOrderKey + receiveUserId, "");
//      redisTemplate.opsForValue().set(redisCreateOrderKey + sourceUserId, "");
//      redisTemplate.opsForValue().set(redisCreateOrderKey + receiveUserId, "");
            logger.debug("打印暴鸡币订单创建服务接口异常：" + e.getMessage(), e);
            throw new BusinessException(BizExceptionEnum.CREATE_GCOIN_ORDER_FAILURE);
        }

    }

}
