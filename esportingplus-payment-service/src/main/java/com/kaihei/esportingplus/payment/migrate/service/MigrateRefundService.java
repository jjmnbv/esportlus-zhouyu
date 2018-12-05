package com.kaihei.esportingplus.payment.migrate.service;

import static com.kaihei.esportingplus.payment.migrate.constant.Constant.REFUND_WAY_ALI;
import static com.kaihei.esportingplus.payment.migrate.constant.Constant.REFUND_WAY_QQ;
import static com.kaihei.esportingplus.payment.migrate.constant.Constant.REFUND_WAY_WX;

import com.google.common.collect.Lists;
import com.kaihei.esportingplus.payment.api.enums.TradeType;
import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalTradeBill;
import com.kaihei.esportingplus.payment.migrate.constant.ThreadResult;
import com.kaihei.esportingplus.payment.migrate.constant.TypeMapping;
import com.kaihei.esportingplus.payment.migrate.entity.OrderBill;
import com.kaihei.esportingplus.payment.migrate.entity.RefundOrder;
import com.kaihei.esportingplus.payment.migrate.repository.OrderBillRepository;
import com.kaihei.esportingplus.payment.migrate.repository.RefundOrderRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author: tangtao
 **/
@ConditionalOnProperty(name = "migrate.enable", havingValue = "true")
@Service
public class MigrateRefundService extends AbstractMigrateService {

    private final Integer REFUND_VERIFY_STATUS_SUCCESS = 2;
    @Autowired
    private RefundOrderRepository refundOrderRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private OrderBillRepository orderBillRepository;

    @Override
    public long getTotalCount() {
        return refundOrderRepository
                .countByRefundWayInAndVerifyStatus(Lists.newArrayList(REFUND_WAY_WX, REFUND_WAY_ALI, REFUND_WAY_QQ), REFUND_VERIFY_STATUS_SUCCESS);
    }

    synchronized List<RefundOrder> getRefundOrder() {
        List<RefundOrder> orders = refundOrderRepository
                .findByRefundWayInAndVerifyStatus(Lists.newArrayList(REFUND_WAY_WX, REFUND_WAY_ALI, REFUND_WAY_QQ), REFUND_VERIFY_STATUS_SUCCESS,
                        startIndex.equals("") ? 0 : Integer.parseInt(startIndex), pageSize);
        if (!orders.isEmpty()) {
            startIndex = orders.get(orders.size() - 1).getId().toString();
        }
        return orders;
    }

    @Override
    public ThreadResult doMigrate(Semaphore semaphore) {
        try {
            long start = System.currentTimeMillis();

            List<String> warningData = Lists.newLinkedList();
            List<String> errData = Lists.newLinkedList();
            List<ExternalRefundOrder> externalRefundOrders = Lists.newLinkedList();
            List<ExternalTradeBill> externalTradeBills = Lists.newLinkedList();

            /**
             *  退款方式：微信、支付宝、QQ
             *  退款状态:退款成功
             *  普通支付退款 goods_type = 1,8,9,17,16
             *  保证金退款 goods_type = 2,18
             */
//            Page<RefundOrder> refundOrderPage = refundOrderRepository
//                    .findByRefundWayInAndVerifyStatus(Lists.newArrayList(REFUND_WAY_WX, REFUND_WAY_ALI, REFUND_WAY_QQ), REFUND_VERIFY_STATUS_SUCCESS,
//                            getPageable());
//            List<RefundOrder> orders = refundOrderPage.getContent();

            List<RefundOrder> orders = getRefundOrder();

            List<String> orderIds = orders.stream().map(RefundOrder::getOutTradeNo).collect(Collectors.toList());

            // 流水
            Map<String, List<OrderBill>> orderBillMap = orderBillRepository.findByOrderIdInOrderByCreateTimeDesc(orderIds)
                    .stream().collect(Collectors.groupingBy(OrderBill::getOrderId));

            long initEnd = System.currentTimeMillis();

            for (RefundOrder refundOrder : orders) {

                ExternalRefundOrder externalRefundOrder = commonService.createExternalRefundOrder(refundOrder);
                externalRefundOrders.add(externalRefundOrder);

                // 获取流水
                List<OrderBill> orderBills = orderBillMap.get(refundOrder.getOutTradeNo());
                // 退款成功的退款单必须有流水记录
                if (orderBills == null) {
                    errData.add(String.format("退款单缺失流水记录 outTradeNo=%s", refundOrder.getOutTradeNo()));
                    continue;
                }

                OrderBill orderBill = null;
                for (OrderBill bill : orderBills) {
                    // 订单类型相同且是退款
                    if (bill.getOrderType().equals(refundOrder.getGoodsType().toString()) && TypeMapping.isRefund(bill.getBillType())) {
                        if (orderBill == null) {
                            orderBill = bill;
                        } else {
                            warningData.add(String.format("退款单存在重复流水 outTradeNo=%s", refundOrder.getOutTradeNo()));
                            break;
                        }
                    }
                }

                if (orderBill == null) {
                    errData.add(String.format("退款单缺失流水记录 outTradeNo=%s", refundOrder.getOutTradeNo()));
                    continue;
                }

                externalTradeBills
                        .add(commonService
                                .createExternalTradeBill(orderBill, externalRefundOrder.getTransactionId(), externalRefundOrder.getOrderId(),
                                        refundOrder.getRefundWay(),
                                        TradeType.REFUND));

            }

            long handelEnd = System.currentTimeMillis();

            // save
            commonService.batchSaveRefundOrder(externalRefundOrders, externalTradeBills);

            long saveEnd = System.currentTimeMillis();

            ThreadResult result = new ThreadResult();
            result.setInitTime(initEnd - start);
            result.setHandleTime(handelEnd - initEnd);
            result.setSaveTime(saveEnd - handelEnd);
            result.setTotalTime(saveEnd - start);
            result.setHandleOrderSize(orders.size());
            result.setOrderSize(externalRefundOrders.size());
            result.setBillSize(externalTradeBills.size());
            result.setWarningSize(warningData.size());
            result.setWarningData(warningData);
            result.setErrSize(errData.size());
            result.setErrData(errData);

            logger.info(">>> 退款单迁移线程执行完成，{}", result.toString());

            return result;
        } finally {
            semaphore.release();
        }
    }
}
