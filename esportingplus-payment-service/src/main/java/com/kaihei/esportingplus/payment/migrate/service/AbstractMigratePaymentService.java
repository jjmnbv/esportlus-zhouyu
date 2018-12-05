package com.kaihei.esportingplus.payment.migrate.service;

import com.google.common.collect.Lists;
import com.kaihei.esportingplus.payment.api.enums.TradeType;
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalTradeBill;
import com.kaihei.esportingplus.payment.migrate.constant.ThreadResult;
import com.kaihei.esportingplus.payment.migrate.constant.TypeMapping;
import com.kaihei.esportingplus.payment.migrate.entity.OrderBill;
import com.kaihei.esportingplus.payment.migrate.entity.PayOrder;
import com.kaihei.esportingplus.payment.migrate.repository.OrderBillRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: tangtao
 **/
public abstract class AbstractMigratePaymentService extends AbstractMigrateService {

    /**
     * 支付单中的order_type,通过枚举数据库得出
     *
     * 普通支付 1,8,9,16,17
     *
     * 保证金支付 2,18
     *
     * 过滤掉  3 充值
     */
    static List<String> ATTACH_PAYMENT = Lists
            .newArrayList("{\"order_type\": 1}", "{\"order_type\": 8}", "{\"order_type\": 9}", "{\"order_type\": 16}", "{\"order_type\": 17}"
                    , "{\"order_type\": 2}", "{\"order_type\": 18}");
    static Map<String, String> ATTACH_ORDER_TYPE_MAP = new HashMap<>(7);

    static {
        ATTACH_ORDER_TYPE_MAP.put("{\"order_type\": 1}", "1");
        ATTACH_ORDER_TYPE_MAP.put("{\"order_type\": 8}", "8");
        ATTACH_ORDER_TYPE_MAP.put("{\"order_type\": 9}", "9");
        ATTACH_ORDER_TYPE_MAP.put("{\"order_type\": 16}", "16");
        ATTACH_ORDER_TYPE_MAP.put("{\"order_type\": 17}", "17");
        ATTACH_ORDER_TYPE_MAP.put("{\"order_type\": 2}", "2");
        ATTACH_ORDER_TYPE_MAP.put("{\"order_type\": 18}", "18");
    }

    @Autowired
    private OrderBillRepository orderBillRepository;
    @Autowired
    private CommonService commonService;

    abstract int getPayType();

    abstract <T extends PayOrder> List<T> getPayOrders();

    @Override
    public ThreadResult doMigrate(Semaphore semaphore) {
        try {
            long start = System.currentTimeMillis();

            // 获取status>0的支付单
            List<PayOrder> payOrders = getPayOrders();

            List<String> warningData = Lists.newLinkedList();
            List<String> errData = Lists.newLinkedList();
            List<ExternalPaymentOrder> externalPaymentOrders = Lists.newLinkedList();
            List<ExternalTradeBill> externalTradeBills = Lists.newLinkedList();

            List<String> orderIds = payOrders.stream().map(PayOrder::getOutTradeNo).collect(Collectors.toList());
            Map<String, List<OrderBill>> orderBillMap = orderBillRepository.findByOrderIdInOrderByCreateTimeDesc(orderIds).stream()
                    .collect(Collectors.groupingBy(OrderBill::getOrderId));

            long initEnd = System.currentTimeMillis();

            for (PayOrder payOrder : payOrders) {

                // 待支付订单没有流水
//                if (payOrder.getStatus() == Constant.PAY_ORDER_STATUS_PREPAY) {
//                    continue;
//                }

                String orderType = ATTACH_ORDER_TYPE_MAP.get(payOrder.getAttach());

                //写external_payment_order
                ExternalPaymentOrder externalPaymentOrder = commonService.createPaymentOder(payOrder, orderType, getPayType());
                externalPaymentOrders.add(externalPaymentOrder);

                // 没有流水记录且不是预支付状态的支付订单缺失流水记录
                List<OrderBill> orderBills = orderBillMap.get(payOrder.getOutTradeNo());
                if (orderBills == null) {
                    errData.add(String.format("1-支付单缺失流水记录 payType=%s outTradeNo=%s orderType=%s status=%s",
                            getPayType(), payOrder.getOutTradeNo(), orderType, payOrder.getStatus()));
                    continue;
                }

                OrderBill orderBill = null;
                for (OrderBill bill : orderBills) {
                    // bill_type是支付类型
                    if (bill.getOrderType().equals(orderType) && TypeMapping.isPayment(bill.getBillType())) {
                        if (orderBill == null) {
                            orderBill = bill;
                        } else {
                            warningData.add(String.format("充值单存在重复流水 payType=%s outTradeNo=%s", getPayType(), payOrder.getOutTradeNo()));
                            break;
                        }
                    }
                }
                if (orderBill == null) {
                    errData.add(String.format("2-支付单缺失流水记录 payType=%s outTradeNo=%s attach=%s status=%s", getPayType(), payOrder.getOutTradeNo(),
                            payOrder.getAttach(), payOrder.getStatus()));
                    continue;
                }
                externalTradeBills.add(commonService.createExternalTradeBill(orderBill, externalPaymentOrder.getTransactionId(),
                        externalPaymentOrder.getOrderId(),
                        getPayType(), TradeType.PAYMENT));
            }

            long handelEnd = System.currentTimeMillis();

            // save
            commonService.batchSavePaymentOrder(externalPaymentOrders, externalTradeBills);

            long saveEnd = System.currentTimeMillis();

            ThreadResult result = new ThreadResult();
            result.setInitTime(initEnd - start);
            result.setHandleTime(handelEnd - initEnd);
            result.setSaveTime(saveEnd - handelEnd);
            result.setTotalTime(saveEnd - start);
            result.setHandleOrderSize(payOrders.size());
            result.setOrderSize(externalPaymentOrders.size());
            result.setBillSize(externalTradeBills.size());
            result.setWarningSize(warningData.size());
            result.setWarningData(warningData);
            result.setErrSize(errData.size());
            result.setErrData(errData);

            logger.info(">>> 支付订单迁移线程执行完成，{}", result.toString());

            return result;
        } finally {
            semaphore.release();
        }
    }

}
