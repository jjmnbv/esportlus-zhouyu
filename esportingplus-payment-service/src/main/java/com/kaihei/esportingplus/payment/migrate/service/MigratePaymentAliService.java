package com.kaihei.esportingplus.payment.migrate.service;

import static com.kaihei.esportingplus.payment.migrate.constant.Constant.PAY_TYPE_ALI;

import com.kaihei.esportingplus.payment.migrate.constant.Constant;
import com.kaihei.esportingplus.payment.migrate.entity.AliPayOrder;
import com.kaihei.esportingplus.payment.migrate.entity.PayOrder;
import com.kaihei.esportingplus.payment.migrate.repository.AliPayOrderRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author: tangtao
 **/
@ConditionalOnProperty(name = "migrate.enable", havingValue = "true")
@Service
public class MigratePaymentAliService extends AbstractMigratePaymentService {

    @Autowired
    private AliPayOrderRepository aliPayOrderRepository;

    @Override
    synchronized <T extends PayOrder> List<T> getPayOrders() {
        List<AliPayOrder> payOrders = aliPayOrderRepository
                .findByAttachInAndStatusGreaterThan(ATTACH_PAYMENT, Constant.PAY_ORDER_STATUS_PREPAY, startIndex, pageSize);
        if (!payOrders.isEmpty()) {
            startIndex = payOrders.get(payOrders.size() - 1).getOutTradeNo();
        }
        return (List<T>) payOrders;
    }

//    @Override
//    <T extends PayOrder> List<T> getPayOrders() {
//        Page<AliPayOrder> payOrderPage = aliPayOrderRepository
//                .findByAttachInAndStatusGreaterThan(ATTACH_PAYMENT, Constant.PAY_ORDER_STATUS_PREPAY, getPageable());
//
//
//        return (List<T>) payOrderPage.getContent();
//    }

    @Override
    int getPayType() {
        return PAY_TYPE_ALI;
    }

    @Override
    long getTotalCount() {
        return aliPayOrderRepository.countByAttachInAndStatusGreaterThan(ATTACH_PAYMENT, Constant.PAY_ORDER_STATUS_PREPAY);
    }
}
