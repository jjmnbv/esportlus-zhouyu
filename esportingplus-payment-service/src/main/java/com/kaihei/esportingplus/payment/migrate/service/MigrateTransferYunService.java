package com.kaihei.esportingplus.payment.migrate.service;

import com.google.common.collect.Lists;
import com.kaihei.esportingplus.payment.api.enums.TradeType;
import com.kaihei.esportingplus.payment.domain.entity.ExternalTradeBill;
import com.kaihei.esportingplus.payment.domain.entity.ExternalWithdrawOrder;
import com.kaihei.esportingplus.payment.migrate.constant.Constant;
import com.kaihei.esportingplus.payment.migrate.constant.ThreadResult;
import com.kaihei.esportingplus.payment.migrate.constant.TypeMapping;
import com.kaihei.esportingplus.payment.migrate.entity.OrderBill;
import com.kaihei.esportingplus.payment.migrate.entity.TransferOrder;
import com.kaihei.esportingplus.payment.migrate.entity.YunOrder;
import com.kaihei.esportingplus.payment.migrate.repository.OrderBillRepository;
import com.kaihei.esportingplus.payment.migrate.repository.TransferOrderRepository;
import com.kaihei.esportingplus.payment.migrate.repository.YunOrderRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 提现记录迁移
 *
 * @author: tangtao
 **/
@ConditionalOnProperty(name = "migrate.enable", havingValue = "true")
@Service
public class MigrateTransferYunService extends AbstractMigrateService {

    //提现订单类型
    private final String ORDER_TYPE_TRANSFER = "11";
    private final int TRANSFER_VERIFY_STATUS_SUCCESS = 2;
    private final int YUN_ORDER_STATUS_SUCCESS = 1;
    @Autowired
    private TransferOrderRepository transferOrderRepository;
    @Autowired
    private YunOrderRepository yunOrderRepository;
    @Autowired
    private OrderBillRepository orderBillRepository;
    @Autowired
    private CommonService commonService;

    @Override
    public long getTotalCount() {
        return yunOrderRepository.count();
    }

    synchronized List<YunOrder> getYunOrder() {
        List<YunOrder> orders = yunOrderRepository.findLimit(startIndex, pageSize);
        if (!orders.isEmpty()) {
            startIndex = orders.get(orders.size() - 1).getOutTradeNo();
        }
        return orders;
    }

    @Override
    public ThreadResult doMigrate(Semaphore semaphore) {
        try {
            long start = System.currentTimeMillis();

            List<String> warningData = Lists.newLinkedList();
            List<String> errData = Lists.newLinkedList();
            List<ExternalWithdrawOrder> externalWithdrawOrders = Lists.newLinkedList();
            List<ExternalTradeBill> externalTradeBills = Lists.newLinkedList();

            // 云账户提现（微信提现数据不足无法写入）
//            Page<YunOrder> yunOrderPage = yunOrderRepository.findAll(getPageable());
//            List<YunOrder> orders = yunOrderPage.getContent();

            List<YunOrder> orders = getYunOrder();

            List<String> orderIds = orders.stream().map(YunOrder::getOutTradeNo).collect(Collectors.toList());

            // 已经确认从云账户表的out_trade_no查询不会有重复的值
            Map<String, String> outTradeNoPackageNameMap = transferOrderRepository.findByOutTradeNoIn(orderIds).stream()
                    .collect(Collectors.toMap(TransferOrder::getOutTradeNo, TransferOrder::getPackageName));

            // 获取提现流水 (bill_type = 65,66 order_type=11)
            Map<String, List<OrderBill>> orderBillMap = orderBillRepository
                    .findByOrderIdInAndBillTypeInAndOrderTypeOrderByCreateTimeDesc(orderIds, TypeMapping.withdraw_yun_bill_type, ORDER_TYPE_TRANSFER)
                    .stream().collect(Collectors.groupingBy(OrderBill::getOrderId));

            long initEnd = System.currentTimeMillis();

            for (YunOrder yunOrder : orders) {

                String outTradeNo = yunOrder.getOutTradeNo();

                String packageName = outTradeNoPackageNameMap.get(outTradeNo);

                if (packageName == null) {
                    errData.add(String.format("云账户单缺失提现记录 outTradeNo=%s", outTradeNo));
                    continue;
                }

                //写external_payment_order
                ExternalWithdrawOrder withdrawOrder = commonService.createExternalWithdrawOrder(yunOrder, packageName);
                externalWithdrawOrders.add(withdrawOrder);

                // 不是成功状态不写流水 (基本验证过）
                if (yunOrder.getStatus() != YUN_ORDER_STATUS_SUCCESS) {
                    continue;
                }

                // 获取流水
                List<OrderBill> orderBills = orderBillMap.get(outTradeNo);
                // 没有流水记录且不是预支付状态的支付订单缺失流水记录
                if (orderBills == null) {
                    errData.add(String.format("云账户单缺失流水记录 outTradeNo=%s", outTradeNo));
                    continue;
                }

                if (orderBills.size() > 1) {
                    warningData.add(String.format("云账户单存在重复流水 outTradeNo=%s", outTradeNo));
                }

                //写external_trade_bill 获取第一个OrderBill
                externalTradeBills
                        .add(commonService
                                .createExternalTradeBill(orderBills.get(0), withdrawOrder.getRefNo(), withdrawOrder.getOrderId(),
                                        Constant.PAY_TYPE_YUN,
                                        TradeType.WITHDRAW));
            }

            long handelEnd = System.currentTimeMillis();

            // save
            commonService.batchSaveWithdrawOrder(externalWithdrawOrders, externalTradeBills);

            long saveEnd = System.currentTimeMillis();

            ThreadResult result = new ThreadResult();
            result.setInitTime(initEnd - start);
            result.setHandleTime(handelEnd - initEnd);
            result.setSaveTime(saveEnd - handelEnd);
            result.setTotalTime(saveEnd - start);
            result.setHandleOrderSize(orders.size());
            result.setOrderSize(externalWithdrawOrders.size());
            result.setBillSize(externalTradeBills.size());
            result.setWarningSize(warningData.size());
            result.setWarningData(warningData);
            result.setErrSize(errData.size());
            result.setErrData(errData);

            logger.info(">>> 云账户单迁移线程执行完成，{}", result.toString());

            return result;
        } finally {
            semaphore.release();
        }
    }

}
