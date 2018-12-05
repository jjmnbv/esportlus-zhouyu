package com.kaihei.esportingplus.payment.migrate.service;

import static com.kaihei.esportingplus.payment.migrate.constant.Constant.BILL_TYPE_RECHARGE_ALI;
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
public class MigrateRechargeAliService extends AbstractMigrateRechargeService {

    @Autowired
    private AliPayOrderRepository aliPayOrderRepository;

    @Override
    int getPayType() {
        return PAY_TYPE_ALI;
    }

    @Override
    int getRechargeBillType() {
        return BILL_TYPE_RECHARGE_ALI;
    }

    @Override
    synchronized <T extends PayOrder> List<T> getPayOrders() {
        List<AliPayOrder> payOrders = aliPayOrderRepository
                .findByAttachAndStatusGreaterThan(ATTACH_RECHARGE, Constant.PAY_ORDER_STATUS_PREPAY, startIndex, pageSize);
        if (!payOrders.isEmpty()) {
            startIndex = payOrders.get(payOrders.size() - 1).getOutTradeNo();
        }
        return (List<T>) payOrders;
    }

    @Override
    long getTotalCount() {
        return aliPayOrderRepository.countByAttachAndStatusGreaterThan(ATTACH_RECHARGE, Constant.PAY_ORDER_STATUS_PREPAY);
    }

}
