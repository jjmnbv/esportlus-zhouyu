package com.kaihei.esportingplus.payment.util;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.payment.api.enums.ExternalRefundStateEnum;
import com.kaihei.esportingplus.payment.data.jpa.repository.ExternalPaymentOrderRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.ExternalRefundOrderRepository;
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ExternalRefundUtil
 * @Description 退款订单工具类
 * @Author xusisi
 * @Date 2018/11/22 上午11:05
 */
@Component
public class ExternalRefundUtil {

    private static Logger logger = LoggerFactory.getLogger(ExternalRefundOrder.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private ExternalRefundOrderRepository refundOrderRepository;

    @Autowired
    private ExternalPaymentOrderRepository externalPaymentOrderRepository;

    @Autowired
    private ExternalPaymentUtil externalPaymentUtil;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据业务方的退款订单号查找对应的退款信息
     *
     * @param : [outRefundNo]
     * @Author : xusisi
     **/
    public ExternalRefundOrder getRefundOrder(String outRefundNo) throws BusinessException {
        logger.debug("入参 >> outRefundNo :{} ", outRefundNo);
        String refundKey = String.format(RedisKey.EXTERNAL_REFUND_ORDER_KEY, outRefundNo);
        ExternalRefundOrder externalRefundOrder = cacheManager.get(refundKey, ExternalRefundOrder.class);
        if (externalRefundOrder == null) {
            externalRefundOrder = refundOrderRepository.findOneByOutRefundNo(outRefundNo);
        }

        logger.debug("出参 >> externalRefundOrder : {} ", externalRefundOrder);
        return externalRefundOrder;
    }

    public void checkRefundOrderWhenRefund(ExternalRefundOrder refundOrder) {
        String refundState = refundOrder.getState();

        //退款订单已经退款失败
        if (ExternalRefundStateEnum.FAILED.getCode().equals(refundState)) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_REFUND_ORDER_FAIL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_REFUND_ORDER_FAIL);
        }

        //退款订单已经取消
        if (ExternalRefundStateEnum.CANCEL.getCode().equals(refundState)) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_REFUND_ORDER_CANCEL.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_REFUND_ORDER_CANCEL);
        }

        //退款订单已经退款成功
        if (ExternalRefundStateEnum.SUCCESS.getCode().equals(refundState)) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_REFUND_ORDER_SUCCESS.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_REFUND_ORDER_SUCCESS);
        }

    }

    /**
     * 获取一笔支付订单所对应的所有已退金额和退款中的金额
     *
     * @param : [outTradeNo]
     * @Author : xusisi
     **/
    public Integer getRefundAmount(String outTradeNo) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();
        Root<ExternalRefundOrder> root = query.from(ExternalRefundOrder.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("payOrderId").as(String.class), outTradeNo));
        List<String> stateList = new ArrayList<String>();
        stateList.add(ExternalRefundStateEnum.SUCCESS.getCode());
        stateList.add(ExternalRefundStateEnum.REFUNDING.getCode());
        Expression<String> exp = root.get("state");
        Predicate statePredicate = exp.in(stateList);
        predicates.add(statePredicate);

        Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);

        query.where(predicatesArray);

        query.multiselect(criteriaBuilder.sum(root.get("totalFee")));
        TypedQuery<Tuple> q = entityManager.createQuery(query);
        List<Tuple> result = q.getResultList();
        logger.debug("result : {} ", result);
        Iterator it = result.iterator();
        while (it.hasNext()) {
            logger.debug("t :{}", it.next());
        }
        Tuple tuple = result.get(0);

        Integer totalFee = 0;

        if (ObjectTools.isNotEmpty(tuple.get(0))) {
            totalFee = Integer.valueOf(tuple.get(0).toString());
        }

        logger.debug("totalFee : {} ", totalFee);
        return totalFee;
    }

    /**
     * 更新MySQL中的退款订单信息，Redis中的退款订单信息
     *
     * @param : [refundOrder]
     * @Author : xusisi
     **/
    public void refreshRefundInfo(ExternalRefundOrder refundOrder) {
        String outRefundNo = refundOrder.getOutRefundNo();
        String refundKey = String.format(RedisKey.EXTERNAL_REFUND_ORDER_KEY, outRefundNo);
        cacheManager.del(refundKey);
        refundOrderRepository.save(refundOrder);
        cacheManager.set(refundKey, refundOrder, RedisKey.SAVE_DATA_TIME);
    }

    /***
     * 根据退款订单号获取对应的配置信息
     * @param outRefunNo
     * @return
     */
    public Map<String, String> getPaySetting(String outRefunNo) {
        ExternalRefundOrder refundOrder = this.getRefundOrder(outRefunNo);
        if (refundOrder == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_REFUND_ORDER_NOT_FOUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_REFUND_ORDER_NOT_FOUND);
        }
        //根据退款订单查询到对应的支付订单信息
        String payOrderId = refundOrder.getPayOrderId();
        ExternalPaymentOrder paymentOrder = externalPaymentOrderRepository.findOneByOrderId(payOrderId);
        if (paymentOrder == null) {
            logger.error("exception : {} ", BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND.getErrMsg());
            throw new BusinessException(BizExceptionEnum.EXTERNAL_PAY_ORDER_NOT_FOUND);
        }
        String outTradeNo = paymentOrder.getOutTradeNo();
        String orderType = paymentOrder.getOrderType();
        return externalPaymentUtil.getPaySetting(outTradeNo, orderType);

    }
}
