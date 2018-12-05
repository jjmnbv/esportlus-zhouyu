package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.payment.api.enums.OrderTypeEnum;
import com.kaihei.esportingplus.payment.api.enums.TradeType;
import com.kaihei.esportingplus.payment.api.params.ExternalTradeBillQueryParams;
import com.kaihei.esportingplus.payment.api.vo.ExternalTradeBillVo;
import com.kaihei.esportingplus.payment.data.jpa.repository.ExternalTradeBillRepository;
import com.kaihei.esportingplus.payment.domain.entity.*;
import com.kaihei.esportingplus.payment.service.ExternalTradeBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: tangtao
 **/
@Service
public class ExternalTradeBillServiceImpl implements ExternalTradeBillService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalTradeBillServiceImpl.class);

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private ExternalTradeBillRepository externalTradeBillRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> boolean saveTradeBill(T order) {
        ExternalTradeBill tradeBill;
        if (order instanceof ExternalPaymentOrder) {
            tradeBill = this.saveBill((ExternalPaymentOrder) order);
        } else if (order instanceof ExternalRefundOrder) {
            tradeBill = this.saveBill((ExternalRefundOrder) order);
        } else if (order instanceof ExternalWithdrawOrder) {
            tradeBill = this.saveBill((ExternalWithdrawOrder) order);
        } else {
            logger.warn("未定义的支付订单类型，支付流水保存失败！");
            throw new BusinessException(BizExceptionEnum.ORDER_ENTITY_TYPE_EMPTY);
        }
        return this.saveSuccess(tradeBill);
    }

    @Override
    public List<ExternalTradeBillVo> query(ExternalTradeBillQueryParams params) {

        Specification<ExternalTradeBill> specification = new ExternalTradeBillSpecification(params);

        List<ExternalTradeBill> tradeBills = externalTradeBillRepository.findAll(specification);

        List<ExternalTradeBillVo> ExternalTradeBillVos = tradeBills.parallelStream()
                .map(e -> toVo(e)).collect(Collectors.toList());

        return ExternalTradeBillVos;
    }

    private ExternalTradeBillVo toVo(ExternalTradeBill bill) {
        ExternalTradeBillVo vo = new ExternalTradeBillVo();
        vo.setFlowNo(bill.getFlowNo());
        vo.setOrderId(bill.getOrderId());
        vo.setChannel(bill.getChannel());
        vo.setTotalFee(bill.getTotalFee());
        vo.setTradeType(bill.getTradeType());
        vo.setTransactionId(bill.getTransactionId());
        vo.setOutTradeNo(bill.getOutTradeNo());
        vo.setOrderType(bill.getOrderType());
        vo.setCreateDate(bill.getCreateDate());
        return vo;
    }

    private class ExternalTradeBillSpecification implements Specification<ExternalTradeBill> {

        private ExternalTradeBillQueryParams params;

        public ExternalTradeBillSpecification(ExternalTradeBillQueryParams params) {
            this.params = params;
        }

        @Override
        public Predicate toPredicate(Root<ExternalTradeBill> root, CriteriaQuery<?> criteriaQuery,
                CriteriaBuilder criteriaBuilder) {

            Date begin = DateUtil
                    .str2Date(params.getCreateDateBegin() + " 00:00:00", DateUtil.FORMATTER);
            Date end = DateUtil
                    .str2Date(params.getCreateDateEnd() + " 23:59:59", DateUtil.FORMATTER);

            List<Predicate> predicates = new ArrayList<>();
            //查询时间范围
            predicates.add(criteriaBuilder
                    .between(root.get("createDate").as(Date.class), begin, end));

            if (StringUtils.isNotBlank(params.getFlowNo())) {
                predicates.add(criteriaBuilder
                        .equal(root.get("flowNo").as(String.class), params.getFlowNo()));
            }
            if (StringUtils.isNotBlank(params.getTransactionId())) {
                predicates.add(criteriaBuilder
                        .equal(root.get("transactionId").as(String.class),
                                params.getTransactionId()));
            }
            if (StringUtils.isNotBlank(params.getChannel())) {
                predicates.add(criteriaBuilder
                        .equal(root.get("channel").as(String.class), params.getChannel()));
            }
            if (StringUtils.isNotBlank(params.getOutTradeNo())) {
                predicates.add(criteriaBuilder
                        .equal(root.get("outTradeNo").as(String.class), params.getOutTradeNo()));
            }
            if (StringUtils.isNotBlank(params.getTradeType())) {
                predicates.add(criteriaBuilder
                        .equal(root.get("orderType").as(String.class), params.getTradeType()));
            }
            if (StringUtils.isNotBlank(params.getOrderId())) {
                predicates.add(criteriaBuilder
                        .equal(root.get("orderId").as(String.class), params.getOrderId()));
            }
            if (StringUtils.isNotBlank(params.getTradeType())) {
                predicates.add(criteriaBuilder
                        .equal(root.get("tradeType").as(String.class), params.getTradeType()));
            }

            Predicate[] pre = new Predicate[predicates.size()];
            return criteriaQuery.where(predicates.toArray(pre)).getRestriction();
        }
    }

    public ExternalTradeBill save(ExternalTradeBill externalTradeBill) {
        return externalTradeBillRepository.save(externalTradeBill);
    }


    private boolean saveSuccess(AbstractBillEntity entity) {
        return (entity != null && entity.getId() != null && entity.getId().intValue() != 0);
    }

    private ExternalTradeBill saveBill(ExternalPaymentOrder paymentOrder) {
        long id = snowFlake.nextId();
        ExternalTradeBill tradeBill = new ExternalTradeBill();
        tradeBill.setId(id);
        tradeBill.setChannel(paymentOrder.getChannelName());
        tradeBill.setOrderId(paymentOrder.getId().toString());
        tradeBill.setOrderType(paymentOrder.getOrderType());
        tradeBill.setOutTradeNo(paymentOrder.getOutTradeNo());
        tradeBill.setTransactionId(paymentOrder.getTransactionId());
        tradeBill.setFlowNo(String.valueOf(id));
        tradeBill.setTotalFee(paymentOrder.getTotalFee());
        tradeBill.setTradeType(TradeType.PAYMENT.getCode());

        return this.save(tradeBill);
    }

    private ExternalTradeBill saveBill(ExternalRefundOrder refundOrder) {
        long id = snowFlake.nextId();
        ExternalTradeBill tradeBill = new ExternalTradeBill();
        tradeBill.setId(id);
        tradeBill.setChannel(refundOrder.getChannelName());
        tradeBill.setOrderId(refundOrder.getId().toString());
        tradeBill.setOrderType(refundOrder.getOrderType());
        tradeBill.setOutTradeNo(refundOrder.getOutTradeNo());
        tradeBill.setTransactionId(refundOrder.getTransactionId());
        tradeBill.setFlowNo(String.valueOf(id));
        tradeBill.setTotalFee(refundOrder.getTotalFee());
        tradeBill.setTradeType(TradeType.REFUND.getCode());

        return this.save(tradeBill);
    }

    private ExternalTradeBill saveBill(ExternalWithdrawOrder withdrawOrder) {
        long id = snowFlake.nextId();
        ExternalTradeBill tradeBill = new ExternalTradeBill();
        tradeBill.setId(id);
        tradeBill.setChannel(withdrawOrder.getChannel());
        tradeBill.setOrderId(withdrawOrder.getId().toString());
        tradeBill.setOrderType(OrderTypeEnum.WITHDRAW_ORDER.getCode());
        tradeBill.setOutTradeNo(withdrawOrder.getOutTradeNo());
        tradeBill.setTransactionId(withdrawOrder.getRefNo());
        tradeBill.setFlowNo(String.valueOf(id));
        tradeBill.setTotalFee(withdrawOrder.getTotalFee());
        tradeBill.setTradeType(TradeType.WITHDRAW.getCode());

        return this.save(tradeBill);
    }
}
