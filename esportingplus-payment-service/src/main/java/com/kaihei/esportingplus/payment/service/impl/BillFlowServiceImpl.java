package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.enums.TransactionTypeEnum;
import com.kaihei.esportingplus.payment.api.params.BillQueryParams;
import com.kaihei.esportingplus.payment.api.vo.WalletBillsVO;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinBillRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.StarlightBillRepository;
import com.kaihei.esportingplus.payment.domain.entity.*;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.WalletService;
import com.kaihei.esportingplus.payment.util.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流水保存接口实现类
 *
 * @author xiaolijun, haycco
 */
@Service
public class BillFlowServiceImpl implements BillFlowService {

    private static final Logger logger = LoggerFactory.getLogger(BillFlowServiceImpl.class);
    @Autowired
    private GCoinBillRepository gCoinBillRepository;
    @Autowired
    private StarlightBillRepository starlightBillRepository;
    @Autowired
    private WalletService walletHistoryService;


    private static final BigDecimal NUM_100 = new BigDecimal(100);


    @Async
    @Override
    public <T> void asyncSaveRecord(T order) {
        this.saveRecord(order);
    }

    @Override
    public <T> boolean saveRecord(T order) {
        logger.debug("开始流水明细记录，订单信息【{}】", order.toString());
        boolean flag = false;
        try {
            if (order instanceof GCoinRewardOrder) {
                //暴鸡币打赏订单
                flag = walletHistoryService.saveHistoryDetails((GCoinRewardOrder) order);
            } else if (order instanceof GCoinPaymentOrder) {
                //暴鸡币支付订单
                flag = walletHistoryService.saveHistoryDetails((GCoinPaymentOrder) order);
            } else if (order instanceof GCoinRefundOrder) {
                //退款订单
                flag = walletHistoryService.saveHistoryDetails((GCoinRefundOrder) order);
            } else if (order instanceof GCoinRechargeOrder) {
                //充值暴鸡币订单
                flag = walletHistoryService.saveHistoryDetails((GCoinRechargeOrder) order);
            } else if (order instanceof OrderIncome) {
                //结算订单
                flag = walletHistoryService.saveHistoryDetails((OrderIncome) order);
            } else if (order instanceof WithdrawOrder) {
                //提现订单
                flag = walletHistoryService.saveHistoryDetails((WithdrawOrder) order);
            } else if (order instanceof DeductOrder) {
                //暴鸡币和暴击值扣款订单
                flag = walletHistoryService.saveHistoryDetails((DeductOrder) order);
            } else {
                logger.warn("未定义的订单类型，流水保存失败！");
                throw new BusinessException(BizExceptionEnum.ORDER_ENTITY_TYPE_EMPTY);
            }
        } catch (Exception e) {
            logger.debug("流水保存异常：{}", e.getMessage());
            throw new BusinessException(BizExceptionEnum.BILL_SAVE);
        }
        logger.debug("=======================完成流水明细记录=======================");

        return flag;
    }

    @Override
    public List<WalletBillsVO> getStarlightBill(BillQueryParams params) {
        try {
            String userId = params.getUserId();

            //分页参数
            PageRequest pageRequest = PageUtils.getPageRequest(params);
            //查询列表
            Page<StarlightBill> respPage = starlightBillRepository
                    .findByUserId(userId, pageRequest);

            List<WalletBillsVO> voList = new ArrayList<>();
            if (respPage.hasContent()) {
                List<StarlightBill> billList = respPage.getContent();
                for (StarlightBill starlightBill : billList) {
                    WalletBillsVO vo = new WalletBillsVO();
                    BeanUtils.copyProperties(starlightBill, vo);
                    vo.setAmount(vo.getAmount().multiply(NUM_100));
                    if (TransactionTypeEnum.INCOME.getCode().equals(vo.getTradeType())
                            || TransactionTypeEnum.REFUND.getCode()
                            .equals(vo.getTradeType())) {
                        //打赏场景、提现失败退款收入
                        vo.setPositive(true);
                    } else if (TransactionTypeEnum.EXPENDITURE.getCode()
                            .equals(vo.getTradeType())) {
                        //兑现场景
                        vo.setPositive(false);
                    }
                    voList.add(vo);
                }
            }
            return voList;
        } catch (Exception e) {
            logger.error("打印暴击值流水查询服务接口异常：" + e.getMessage(), e);
            throw new BusinessException(BizExceptionEnum.STARLIGHT_BILL_SELECT);
        }

    }

    @Override
    public List<WalletBillsVO> getGCoinBill(BillQueryParams params) {
        try {
            String userId = params.getUserId();

            //分页参数
            PageRequest pageRequest = PageUtils.getPageRequest(params);
            //查询列表
            Page<GCoinBill> respPage = gCoinBillRepository.findByUserId(userId, pageRequest);

            List<WalletBillsVO> voList = new ArrayList<>();
            if (respPage.hasContent()) {
                List<GCoinBill> billList = respPage.getContent();
                for (GCoinBill gCoinBill : billList) {
                    WalletBillsVO vo = new WalletBillsVO();
                    BeanUtils.copyProperties(gCoinBill, vo);
                    vo.setAmount(vo.getAmount().multiply(NUM_100));
                    if (TransactionTypeEnum.INCOME.getCode().equals(vo.getTradeType())
                            || TransactionTypeEnum.REFUND.getCode().equals(vo.getTradeType())) {
                        //打赏场景、退款收入
                        vo.setPositive(true);
                    } else if (TransactionTypeEnum.EXPENDITURE.getCode()
                            .equals(vo.getTradeType())) {
                        //充值场景
                        vo.setPositive(false);
                    }
                    voList.add(vo);
                }
            }
            return voList;
        } catch (Exception e) {
            logger.error("打印暴鸡币流水查询服务接口异常：" + e.getMessage(), e);
            throw new BusinessException(BizExceptionEnum.GCOIN_BILL_SELECT);
        }
    }

    @Override
    public Integer getAllStarlight(String userId, String tradeType) {
        return this.getAllStarlight(userId, tradeType, null, null);
    }

    @Override
    public Integer getAllStarlight(String userId, String tradeType, Date beginDate, Date endDate) {
        try {
            List<StarlightBill> starlightBills;
            if (beginDate == null || endDate == null) {
                starlightBills = starlightBillRepository.findAllByUserIdAndTradeType(userId, tradeType);
            } else {
                starlightBills = starlightBillRepository.findAllByUserIdAndTradeTypeAndCreateDateBetween(userId, tradeType, beginDate, endDate);
            }
            BigDecimal usableStarlight = starlightBills.parallelStream().map(StarlightBill::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            return usableStarlight.multiply(NUM_100).intValue();
        } catch (Exception e) {
            logger.error("用户累计暴击值查询服务接口异常：" + e.getMessage(), e);
            throw new BusinessException(BizExceptionEnum.USER_STARLIGHT_SELECT);
        }
    }
}
