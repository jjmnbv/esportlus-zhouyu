package com.kaihei.esportingplus.payment.migrate.service;

import com.google.common.collect.Lists;
import com.kaihei.esportingplus.payment.api.enums.TradeType;
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalTradeBill;
import com.kaihei.esportingplus.payment.migrate.constant.ThreadResult;
import com.kaihei.esportingplus.payment.migrate.entity.OrderBill;
import com.kaihei.esportingplus.payment.migrate.entity.PayOrder;
import com.kaihei.esportingplus.payment.migrate.repository.OrderBillRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: tangtao
 **/
public abstract class AbstractMigrateRechargeService extends AbstractMigrateService {

    static final String ATTACH_RECHARGE = "{\"order_type\": 3}";
    //充值订单类型
    static final String ORDER_TYPE_RECHARGE = "3";

    @Autowired
    private OrderBillRepository orderBillRepository;
    @Autowired
    private CommonService commonService;

    abstract int getPayType();

    abstract int getRechargeBillType();

    abstract <T extends PayOrder> List<T> getPayOrders();

    @Override
    public ThreadResult doMigrate(Semaphore semaphore) {
        try {
            long start = System.currentTimeMillis();

            // 获取status>0的充值单
            List<PayOrder> payOrders = getPayOrders();

            List<String> warningData = Lists.newLinkedList();
            List<String> errData = Lists.newLinkedList();
            List<ExternalPaymentOrder> externalPaymentOrders = Lists.newLinkedList();
            List<ExternalTradeBill> externalTradeBills = Lists.newLinkedList();

            List<String> orderIds = payOrders.stream().map(PayOrder::getOutTradeNo).collect(Collectors.toList());

            Map<String, List<OrderBill>> orderBillMap = orderBillRepository
                    .findByOrderIdInAndOrderTypeOrderByCreateTimeDesc(orderIds, ORDER_TYPE_RECHARGE)
                    .stream().collect(Collectors.groupingBy(OrderBill::getOrderId));

            long initEnd = System.currentTimeMillis();

            for (PayOrder payOrder : payOrders) {

                //写external_payment_order
                ExternalPaymentOrder externalPaymentOrder = commonService.createPaymentOder(payOrder, ORDER_TYPE_RECHARGE, getPayType());
                externalPaymentOrders.add(externalPaymentOrder);

                // 没有流水记录且不是预支付状态的支付订单缺失流水记录
                List<OrderBill> orderBills = orderBillMap.get(payOrder.getOutTradeNo());
                if (orderBills == null) {
                    errData.add(String.format("1-充值单缺失流水记录 payType=%s outTradeNo=%s orderType=%s status=%s",
                            getPayType(), payOrder.getOutTradeNo(), ORDER_TYPE_RECHARGE, payOrder.getStatus()));
                    continue;
                }

                OrderBill orderBill = null;
                for (OrderBill bill : orderBills) {
                    // bill_type是支付类型
                    if (getRechargeBillType() == bill.getBillType()) {
                        if (orderBill == null) {
                            orderBill = bill;
                        } else {
                            warningData.add(String.format("充值单存在重复流水 payType=%s outTradeNo=%s", getPayType(), payOrder.getOutTradeNo()));
                            break;
                        }
                    }
                }
                if (orderBill == null) {
                    errData.add(String.format("2-充值单缺失流水记录 payType=%s outTradeNo=%s attach=%s status=%s", getPayType(), payOrder.getOutTradeNo(),
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

            logger.info(">>> 充值单迁移线程执行完成，{}", result.toString());

            return result;
        } finally {
            semaphore.release();
        }
    }

}
