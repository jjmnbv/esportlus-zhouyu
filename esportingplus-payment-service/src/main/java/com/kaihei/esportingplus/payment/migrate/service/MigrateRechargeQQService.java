package com.kaihei.esportingplus.payment.migrate.service;

import static com.kaihei.esportingplus.payment.migrate.constant.Constant.BILL_TYPE_RECHARGE_QQ;
import static com.kaihei.esportingplus.payment.migrate.constant.Constant.PAY_TYPE_QQ;

import com.kaihei.esportingplus.payment.migrate.constant.Constant;
import com.kaihei.esportingplus.payment.migrate.entity.PayOrder;
import com.kaihei.esportingplus.payment.migrate.entity.QQPayOrder;
import com.kaihei.esportingplus.payment.migrate.repository.QQPayOrderRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author: tangtao
 **/
@ConditionalOnProperty(name = "migrate.enable", havingValue = "true")
@Service
public class MigrateRechargeQQService extends AbstractMigrateRechargeService {

    @Autowired
    private QQPayOrderRepository qqPayOrderRepository;

    @Override
    int getPayType() {
        return PAY_TYPE_QQ;
    }

    @Override
    int getRechargeBillType() {
        return BILL_TYPE_RECHARGE_QQ;
    }

    @Override
    synchronized <T extends PayOrder> List<T> getPayOrders() {
        List<QQPayOrder> payOrders = qqPayOrderRepository
                .findByAttachAndStatusGreaterThan(ATTACH_RECHARGE, Constant.PAY_ORDER_STATUS_PREPAY, startIndex, pageSize);
        if (!payOrders.isEmpty()) {
            startIndex = payOrders.get(payOrders.size() - 1).getOutTradeNo();
        }
        return (List<T>) payOrders;
    }

    @Override
    long getTotalCount() {
        return qqPayOrderRepository.countByAttachAndStatusGreaterThan(ATTACH_RECHARGE, Constant.PAY_ORDER_STATUS_PREPAY);
    }

}
